// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 4

package Problem61;

import java.util.*;
import java.net.*;
import java.io.*;

public class NameClient {
    Scanner din;
    PrintStream pout;
    Socket server;

    public void getSocket() throws IOException {
        server = new Socket(Symbols.nameServer, Symbols.ServerPort);
        din = new Scanner(server.getInputStream());
        pout = new PrintStream(server.getOutputStream());
    }

    public int insertName(String name, String hname, int portnum)
            throws IOException {
        getSocket();
        pout.println("insert " + name + " " + hname + " " + portnum);
        pout.flush();
        int retValue = din.nextInt();
        server.close();
        return retValue;
    }

    public InetSocketAddress searchName(String name, boolean isBlocking) throws IOException {
        getSocket();
        if (isBlocking)
            pout.println("blockingFind " + name);
        else
            pout.println("search " + name);
        pout.flush();
        String result = din.nextLine();
        System.out.println("NameServer returned: " + result);
        Scanner sc = new Scanner(result);
        server.close();
        int portnum = sc.nextInt();
        String hname = sc.next();
        if (portnum == 0)
            return null;
        else
            return new InetSocketAddress(hname, portnum);
    }

    public void clear() throws IOException {
        getSocket();
        pout.println("clear ");
        pout.flush();
        server.close();
    }

    public static void main(String[] args) {
        if (args.length < 2 || (args[0].equals("insert") && args.length < 4)) {
            System.out.println("Example usage: ");
            System.out.println("java NameClient insert [NAME] [HOSTNAME] [PORT]");
            System.out.println("java NameClient search [NAME]");
            return;
        }
        NameClient client = new NameClient();

        try {
            if (args[0].equals("insert")) {
                client.insertName(args[1], args[2], Integer.parseInt(args[3]));
            } else if (args[0].equals("search")) {
                InetSocketAddress pa = client.searchName(args[1], false);
            }
        } catch (Exception e) {
            System.err.println("Server aborted: " + e);
        }
    }
}