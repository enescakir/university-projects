package server;

import model.Bid;
import model.User;
import util.Logger;
import server.Packet.Type;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Simulates a client for testing server
 */
class Client {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private static final String HOST = "18.224.60.222";
    //    private static final String HOST = "localhost";
    private static final int PORT = 5001;

    public Client(Socket socket_) {
        socket = socket_;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            Logger.error("Client construct error");
        }
    }

    public static void main(String argv[]) throws Exception {
        Client client = new Client(new Socket(HOST, PORT));
        (new Thread(() -> {
            try {
                client.listen();
            } catch (IOException e) {
                Logger.client("Listen thread error");
                Logger.error(e.getMessage());
            }
        })).start();

        Scanner scanner = new Scanner(System.in);
        label:
        while (true) {
            String command = scanner.nextLine();
            String[] parts = command.split(" ");
            switch (parts[0]) {
                case "auctions":
                    client.sendMessage(new Packet(Type.AUCTIONS, "").toJson());
                    TimeUnit.SECONDS.sleep(5);
                    client.socket.close();
                    break label;
                case "bid_listen":
                    client.sendMessage(new Packet(Type.BID_LISTEN, parts[1]).toJson());
                    break;
                case "bid_store":
                    client.sendMessage(new Packet(Type.BID_STORE, new Bid(parts[1], Integer.parseInt(parts[2]), new Date())).toJson());
                    break;
                case "auth":
                    client.sendMessage(new Packet(Type.USER, new User(parts[1], parts[2])).toJson());
                    break;
                case "exit":
                    break label;
            }
        }
    }

    private void sendMessage(String message) {
        try {
            Logger.client("Message sent to server: " + message);
            out.write(message);
            out.newLine();
            out.flush();
        } catch (Exception e) {
            Logger.client("Message sent error: " + message);
            Logger.error(e.getMessage());
        }
    }

    private void listen() throws IOException {
        String message;
        while ((message = in.readLine()) != null) {
            Logger.server("Message from server: " + message);
        }
    }
}