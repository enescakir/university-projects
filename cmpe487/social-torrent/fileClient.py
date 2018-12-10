import queue
import socket
import json
from fileUtils import AvailableFile, File, Chunk
import threading
from threading import Lock
from time import sleep
import asyncio
from config import *
from utils import send_packet, print_notification
from _thread import start_new_thread
import base64


class FileClient():
    def __init__(self, send_file_callback, download_finish_callback):
        self.available_files = {}
        self.active_peers = 1
        self.send_file_callback = send_file_callback
        self.download_finish_callback = download_finish_callback
        self.lock = Lock()

    def start(self):
        self.listen_discovery()
        self.start_client()

    def handle_file_definition(self, message):
        source, type, dict = message.split('|')
        file_list = json.loads(dict)
        print_notification("Received " + str(len(file_list)) + " new files from " + source)
        for file in file_list:
            if file['checksum'] in self.available_files:
                self.available_files[file['checksum']].add_peer(source)
            else:
                self.available_files[file['checksum']] = AvailableFile(file['name'], file['checksum'],
                                                                       file['chunk_size'], source)
        if int(type) == MESSAGE_TYPES["request"]:
            self.send_file_callback(source, MESSAGE_TYPES["response"])

    def receive_discovery(self):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
            s.bind((SELF_IP, DISCOVERY_PORT))
            s.listen()

            while True:
                conn, addr = s.accept()
                with conn:
                    message = ""
                    while True:
                        data = conn.recv(1024)
                        if not data:
                            self.handle_file_definition(message)
                            conn.close()
                            break
                        message = message + data.decode('utf_8')

    def send_chunk_requests(self):
        while True:
            sleep(0.2)
            for file in self.available_files.values():
                if file.status == "downloading":
                    for peer in file.peers:
                        requested_chunks = file.get_batch_new_chunks()
                        if len(requested_chunks) == 0:
                            continue
                        self.send_chunk_request(peer, file.checksum, requested_chunks)

    def send_chunk_request(self, target_ip, checksum, chunks):
        message = SELF_IP + "|" + checksum + "|" + json.dumps([chunk.offset for chunk in chunks])
        for chunk in chunks:
            chunk.status = "in_flight"
        start_new_thread(send_packet, (target_ip, CHUNK_PORT, message,))

    def start_client(self):
        chunk_request_thread = threading.Thread(target=self.send_chunk_requests)
        chunk_request_thread.setDaemon(True)
        chunk_request_thread.start()

    def listen_discovery(self):
        discovery_thread = threading.Thread(target=self.receive_discovery)
        discovery_thread.setDaemon(True)
        discovery_thread.start()

    def start_download(self, checksum):
        file = self.available_files[checksum]
        file.status = "downloading"
        file.start_download()
        self.lock.acquire()
        self.active_peers += len(file.peers)
        self.lock.release()

    def end_download(self, checksum):
        file = self.available_files[checksum]
        if file.status == "finished":
            return
        file.status = "finished"
        self.lock.acquire()
        self.active_peers -= len(file.peers)
        if self.active_peers < 0:
            self.active_peers = 0
        self.lock.release()
        file.save_to_shared()
        print_notification("Download Finished for: " + self.available_files[checksum].name)
        self.download_finish_callback(self.available_files[checksum].name)


class FileClientConnection:
    def __init__(self, client):
        self.buffer = queue.Queue(maxsize=DEFAULT_WINDOW_SIZE)
        self.window_size = DEFAULT_WINDOW_SIZE
        self.transport = None
        self.client = client

    def connection_made(self, transport):
        self.transport = transport

    def start(self):
        for peer in self.file.peers:
            chunks = self.file.get_batch_new_chunks(self.file.chunk_size // len(self.file.peers))
            self.send_chunk_request(peer, chunks)

    async def queue_handler(self):
        while True:
            try:
                item = self.buffer.get(block=False)
                file = self.client.available_files[item[0]]
                chunk = file.chunks[int(item[1])]
                chunk.data = item[2]
                chunk.lock.acquire()
                chunk.status = "done"
                chunk.lock.release()
                if len([1 for chnk in file.chunks if chnk.status != "done"]) == 0:
                    self.client.end_download(file.checksum)
            except:
                pass
                # queue is empty here
            await asyncio.sleep(DRAINAGE)

    async def check_packets(self):
        pass

    def datagram_received(self, data, addr):
        message = data.decode()
        checksum, offset, *payload = message.split("|")
        payload = "".join(payload)
        # print(checksum)
        # print(offset)
        self.client.lock.acquire()
        if self.client.active_peers == 0:
            rwindow = str((self.window_size - self.buffer.qsize()) // 1)
        else:
            rwindow = str((self.window_size - self.buffer.qsize()) // self.client.active_peers)
        return_msg = checksum + "|" + offset + "|" + rwindow
        self.client.lock.release()
        if offset != "-1":
            data = base64.b64decode(payload)
            self.buffer.put((checksum, offset, data))
        self.transport.sendto(return_msg.encode(), addr)

    def send_chunk_request(self, target_ip, chunks):
        message = SELF_IP + "|" + self.file.checksum + "|" + json.dumps([chunk.offset for chunk in chunks])
        for chunk in chunks:
            chunk.status = "pending"
        start_new_thread(send_packet, (target_ip, CHUNK_PORT, message,))


async def start_listener(client):
    loop = asyncio.get_running_loop()
    listener = FileClientConnection(client)
    transport, protocol = await loop.create_datagram_endpoint(
        lambda: listener,
        local_addr=(SELF_IP, ACK_PORT))
    asyncio.ensure_future(listener.queue_handler())
    try:
        await asyncio.sleep(3600)  # Serve for 1 hour.
    finally:
        transport.close()


def start_download_queue(client):
    start_new_thread(asyncio.run, (start_listener(client),))
