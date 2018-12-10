package server;

import model.Bid;
import util.Logger;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Accepts socket connection requests and put them in a thread
 */
public class AuctionServer {
    private static LinkedList<ServerThread> threads = new LinkedList<>();
    private static final int DEFAULT_PORT = 5001;

    public static void main(String[] args) {
        AuctionServer server = new AuctionServer();

        int port = DEFAULT_PORT;

        if (args.length > 1)
            port = Integer.parseInt(args[0]);

        server.start(port);
    }

    private void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                ServerThread thread = new ServerThread(socket);
                threads.add(thread);
                thread.start();
            }
        } catch (Exception e) {
            Logger.server("Socket construct error");
            Logger.error(e.getMessage());
        }
    }

    /**
     * Sends new bid on active thread
     */
    public static void broadcastBid(int auctionID, Bid bid) {
        Logger.server("Broadcasting " + Integer.toString(bid.getPoint()) + " point bid by " + bid.getUsername() + " on auction #" + Integer.toString(auctionID) + " ");
        for (ServerThread thread : threads) {
            if (thread.isAlive() && thread.getAuctionID() == auctionID) {
                thread.sendMessage(new Packet(Packet.Type.BID_NEW, bid).toJson());
            }
        }
    }
}