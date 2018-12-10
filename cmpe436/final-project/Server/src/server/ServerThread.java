package server;

import model.*;
import persistence.Database;
import server.Packet.Type;
import util.Logger;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Handles TCP connection and listen new packets
 */
public class ServerThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private int auctionID;

    ServerThread(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            Logger.thread("Thread construct error");
            Logger.error(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            this.listen();
            Logger.client(getHostAddress() + " connection is closed");
        } catch (Exception e) {
            Logger.thread("Thread listen error");
            Logger.error(e.getMessage());
        }
    }

    private void listen() throws IOException {
        String message;

        while ((message = in.readLine()) != null) {
            Packet packet;

            try {
                packet = Packet.fromJson(message);
            } catch (Exception e) {
                packet = new Packet(Type.UNKNOWN, "");
            }

            switch (packet.getType()) {
                case AUCTIONS:
                    Logger.server("Sending auctions to " + getHostAddress());
                    ArrayList<Auction> auctions = Database.getAuctions();
                    this.sendMessage(new Packet(Type.AUCTIONS, auctions).toJson());
                    break;
                case BID_LISTEN:
                    this.auctionID = packet.toObject(Integer.class);
                    Logger.client(getHostAddress() + " connected to auction #" + Integer.toString(this.auctionID));
                    ArrayList<Bid> bids = Database.getBids(new Auction(this.auctionID));
                    this.sendMessage(new Packet(Type.BID_LISTEN, bids).toJson());
                    break;
                case BID_STORE:
                    Bid bid = packet.toObject(Bid.class);
                    int result = Database.saveBid(new Auction(this.auctionID), bid);
                    if (result == 1) {
                        Logger.client(bid.getUsername() + " bid " + bid.getPoint() + " on auction #" + Integer.toString(auctionID));
                        this.sendMessage(new Packet(Type.BID_STORE, Database.getUserPoint(bid.getUsername())).toJson());
                        AuctionServer.broadcastBid(auctionID, bid);
                    } else {
                        Logger.client(bid.getUsername() + " could't bid on " + bid.getPoint() + " on auction #" + Integer.toString(auctionID));
                        if (result == -1)
                            Logger.client(bid.getUsername() + " hasn't enough points");
                        else if (result == -2)
                            Logger.client("New bid is smaller than last bid.");
                        this.sendMessage(new Packet(Type.BID_STORE, Integer.toString(result)).toJson());
                    }
                    break;
                case USER:
                    User user = packet.toObject(User.class);

                    if (Database.authUser(user.getName(), user.getPassword())) {
                        Logger.client(user.getName() + " login");
                        this.sendMessage(new Packet(Type.USER, Database.getUserPoint(user.getName())).toJson());
                    } else {
                        Logger.client(user.getName() + " couldn't login");
                        this.sendMessage(new Packet(Type.USER, "-1").toJson());
                    }
                    break;
                default:
                    Logger.client("Unknown packet from " + getHostAddress() + ": " + message);
            }
        }
    }

    private void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendMessage(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (Exception e) {
            Logger.client("Message send error to " + getHostAddress() + ": " + message);
            Logger.error(e.getMessage());
        }
    }

    private String getHostAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    int getAuctionID() {
        return this.auctionID;
    }
}
