import os
import math
import hashlib
from config import FILE_PATH, CHUNK_SIZE
import asyncio
import random
from _thread import start_new_thread
from threading import Lock

class File:
    def __init__(self, name):
        self.name = name
        self.chunk_size = math.ceil(os.path.getsize(self.get_path()) / CHUNK_SIZE)
        self.reader = open(self.get_path(), 'rb')
        self.checksum = self.calculate_md5()

    def get_dict(self):
        dict = vars(self)
        return {x: dict[x] for x in dict if x != "reader"}

    def get_path(self):
        return FILE_PATH + self.name

    def calculate_md5(self):
        checksum = hashlib.md5(self.reader.read()).hexdigest()
        self.reader.seek(0)
        return checksum

    def get_chunk(self, offset):
        self.reader.seek(CHUNK_SIZE * offset)
        return self.reader.read(CHUNK_SIZE)

class Chunk:
    def __init__(self, offset):
        self.offset = offset
        self.data = None
        self.status = 'new'
        self.lock = Lock()

class AvailableFile():
    def __init__(self, name, checksum, chunk_size, first_peer):
        self.name = name
        self.checksum = checksum
        self.chunk_size = chunk_size
        self.peers = {first_peer}
        self.status = 'discovered'
        self.chunks = None

    def save_to_shared(self):
        self.status = "finished"
        with open(FILE_PATH + self.name, 'wb') as writer:
            for chunk in self.chunks:
                # print("Writing chunk with offset:" + str(chunk.offset))
                writer.write(chunk.data)
                # print("successfully wrote")
            writer.close()

    def add_peer(self, ip):
        self.peers.add(ip)

    def start_download(self):
        self.chunks = [Chunk(i) for i in range(self.chunk_size)]
        self.status = 'downloading'

    def count_in_flight(self):
        return len([1 for chunk in self.chunks if chunk.status == 'in_flight'])

    def check_if_finished(self):
        return all(chunk.status == 'finished' for chunk in self.chunks)

    async def check_chunks(self, chunks):
        await asyncio.sleep(1)
        for chunk in chunks:
            chunk.lock.acquire()
            if chunk.status == 'in_flight':
                chunk.status = "new"
            chunk.lock.release()

    def get_batch_new_chunks(self, count=10):
        pending = [chunk for chunk in self.chunks if chunk.status == "new"]
        chunks = random.sample(pending, min(count, len(pending)))
        start_new_thread(asyncio.run,(self.check_chunks(chunks),))
        return chunks
