import threading
import os
import json
from fileUtils import File
from _thread import *
from config import *
from shutil import copyfile
import asyncio
import random
from utils import print_notification
import base64


class FileServer:

    def __init__(self):
        self.shared_files = {}
        self.active_connections = {}
        self.initialize_files()
        self.loop = asyncio.get_event_loop()

    def start(self):
        self.broadcast_shared_files()
        self.listen_chunk_request()

    def initialize_files(self):
        for filename in os.listdir('shared_files/'):
            file = File(filename)
            self.shared_files[file.checksum] = file

    def handle_chunk_request(self, message):
        source, file_hash, raw_chunks = message.split('|')
        print_notification(source + " has requested the file with hash: " + file_hash)
        chunk_list = json.loads(raw_chunks)

        is_new = source not in self.active_connections
        if not is_new and self.active_connections[source].ended:
            temp = self.active_connections[source]
            del self.active_connections[source]
            del temp
        self.active_connections[source] = FileServerConnection(self.loop)

        file_connection = self.active_connections[source]

        for chunk in chunk_list:
            file_connection.add_chunk(file_hash, int(chunk), self.shared_files[file_hash].get_chunk(int(chunk)))
        # if is_new:
        #     start_new_thread(asyncio.run, (self.start_connection(file_connection, source, ),))
        start_new_thread(asyncio.run, (self.start_connection(file_connection, source, ),))

    async def start_connection(self, connection, source):
        loop = asyncio.get_event_loop()
        transport, protocol = await loop.create_datagram_endpoint(
            lambda: connection,
            remote_addr=(source, FILE_PORT))
        try:
            await asyncio.sleep(3600)
        finally:
            del transport
            del connection
            transport.close()

    def receive_chunk_request(self):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
            s.bind((SELF_IP, CHUNK_PORT))
            s.listen()
            while True:
                conn, addr = s.accept()
                with conn:
                    message = ""
                    while True:
                        data = conn.recv(1024)
                        if not data:
                            # print(message)
                            self.handle_chunk_request(message)
                            # conn.send(b"OK")
                            conn.close()
                            break
                        message = message + data.decode('utf_8')

    def listen_chunk_request(self):
        chunk_thread = threading.Thread(target=self.receive_chunk_request)
        chunk_thread.setDaemon(True)
        chunk_thread.start()

    def new_file_downloaded(self, filename):
        file = File(filename)
        self.shared_files[file.checksum] = file
        self.broadcast_shared_files()

    def add_file(self, filepath):
        if os.path.isfile(filepath):
            filename = os.path.basename(filepath)
            copyfile(filepath, 'shared_files/' + filename)
            file = File(filename)
            self.shared_files[file.checksum] = file
            self.broadcast_shared_files()
            return True
        else:
            return False

    def broadcast_shared_files(self):
        for i in range(1, 255):
            target_ip = SUBNET + "." + str(i)
            if target_ip != SELF_IP:
                self.send_shared_files(target_ip, MESSAGE_TYPES['request'])

    def send_shared_files(self, target_ip, type):
        message = SELF_IP + "|" + str(type) + "|" + json.dumps([data.get_dict() for data in self.shared_files.values()])
        start_new_thread(self.send_packet, (target_ip, DISCOVERY_PORT, message,))

    def send_packet(self, host, port, message):
        try:
            with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
                s.settimeout(5)
                s.connect((host, port))
                s.send(message.encode('utf-8'))
                s.close()
        except:
            pass


class SChunk:
    def __init__(self, file_hash, offset, data):
        self.offset = offset
        self.file_hash = file_hash
        self.data = data
        self.status = 'new'

    def get_key(self):
        return self.file_hash + "|" + str(self.offset)

    def get_bytes(self):
        return self.get_key().encode() + "|".encode() + base64.b64encode(self.data)

class FileServerConnection:
    def __init__(self, loop):
        self.loop = loop
        self.transport = None
        self.window_size = TOLERANCE + 1
        self.in_flight = 0
        self.chunks = {}
        self.window_lock = threading.Lock()
        self.flight_lock = threading.Lock()
        self.on_con_lost = loop.create_future()
        self.started = False
        self.ended = False

    def set_window_size(self, value):
        self.window_lock.acquire()
        self.window_size = value
        self.window_lock.release()

    def inc_flight(self):
        self.flight_lock.acquire()
        self.in_flight += 1
        self.flight_lock.release()

    def dec_flight(self):
        self.flight_lock.acquire()
        self.in_flight -= 1
        self.flight_lock.release()

    def add_chunk(self, file_hash, offset, data):
        chunk = SChunk(file_hash, offset, data)
        self.chunks[chunk.get_key()] = chunk
        if self.started:
            try:
                asyncio.ensure_future(self.try_send(chunk, TRY_COUNT))
            except:
                return
            # if asyncio.get_running_loop():
            #     asyncio.ensure_future(self.try_send(chunk, TRY_COUNT))
            # else:
            #     return

    def start(self):
        self.started = True
        for chunk in list(self.chunks.values()):
            asyncio.ensure_future(self.try_send(chunk, TRY_COUNT))

    async def probe(self):
        if self.ended:
            return
        try:
            self.window_lock.acquire()
            if self.window_size <= TOLERANCE:
                self.transport.sendto("None|-1|probe".encode())
            self.window_lock.release()
            await asyncio.sleep(1)
            await self.probe()
        except:
            pass

    async def try_send(self, chunk, count):
        if self.ended:
            return
        if count == 0 or chunk.status == "done":
            self.chunks.pop(chunk.get_key(), None)
            self.check_if_complete()
            return

        if self.in_flight >= self.window_size - TOLERANCE:
            await asyncio.sleep(random.random())
            await self.try_send(chunk, count)
        else:
            # print("Try chunk #" + str(chunk) + " for " + str(self.try_count + 1 - count) + " times")
            chunk.status = "flight"
            self.inc_flight()
            # print("sending: " + str(chunk.offset))
            try:
                self.transport.sendto(chunk.get_bytes())
            except:
                pass
                # print("error: " + str(chunk.offset))
            await asyncio.sleep(1)
            if chunk.status != "done":
                chunk.status = "new"
                self.dec_flight()
                await self.try_send(chunk, count - 1)

    def connection_made(self, transport):
        self.transport = transport
        self.start()
        asyncio.create_task(self.probe())

    def error_received(self, exc):
        pass
        # print('Error received:', exc)

    def connection_lost(self, exc):
        self.on_con_lost.set_result(True)
        self.ended = True

    def check_if_complete(self):
        if not bool(self.chunks):
            self.transport.close()

    def datagram_received(self, data, addr):
        message = data.decode()

        hash, chunk_num, window_size = message.split('|')
        self.set_window_size(int(window_size))
        if chunk_num == "-1":
            return
        self.chunks[hash + "|" + chunk_num].status = "done"
        self.chunks.pop(hash + "|" + chunk_num, None)
        self.dec_flight()
        self.check_if_complete()
