package com.enescakir.auctioner.Model;

import android.util.Log;

import java.io.*;
import java.net.*;

public class SocketThread extends Thread {
    public Socket socket;
    public BufferedReader in;
    public BufferedWriter out;

    @Override
    public void run() {
        super.run();
        try {
//            socket = new Socket("10.0.2.2", 5001);
            socket = new Socket("18.224.60.222", 5001);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            Log.e("SOCKET_THREAD", "Error");
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        Log.e("SEND_MESSAGE", message);

        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (Exception e) {
            Log.e("SEND_MESSAGE", "Error");
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
            this.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
