## Real-time Auction App
`server.AuctionServer` class is responsible for backend.
It listens coming packets and handle connections.
It's based on TCP sockets and use 5001 port.

You can start server with:
`$ java -cp gson.jar:./bin server.AuctionServer`

You can simulate a client with `server.Client` class.
`$ java -cp gson.jar:./bin server.Client`

It has following methods;
* `auctions`
* `bid_listen [BID_ID]`
* `bid_store [USERNAME] [POINTS]`
* `auth [USERNAME] [PASSWORD]`

Server and clients use `server.Packet` class for communication protocol.