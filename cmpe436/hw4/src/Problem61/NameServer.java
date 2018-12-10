// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 4

package Problem61;

import java.net.*;
import java.io.*;
import java.util.*;

public class NameServer {
    NameTable table;

    public NameServer() {
        table = new NameTable();
    }

    /**
     * Starts new name server with current table
     *
     * @param table Synchronized table
     */
    public NameServer(NameTable table) {
        this.table = table;
    }

    /**
     * Handles client connection
     * It throws exception with %10 percentage for simulating name server downfall
     *
     * @param theClient        socket that connection come from
     * @param secondNameServer insert new names to other name server
     * @param downable         we assume at most one server goes down, second server is not downable
     * @throws Exception server is down
     */
    private void handleClient(Socket theClient, NameServer secondNameServer, boolean downable) throws Exception {
        // Simulate name server downfall
        if (downable && new Random().nextInt(10) == 0) {
            throw new Exception("Name server is down");
        }

        try {
            BufferedReader din = new BufferedReader
                    (new InputStreamReader(theClient.getInputStream()));
            PrintWriter pout = new PrintWriter(theClient.getOutputStream());
            String getline = din.readLine();
            System.out.println(getline);
            StringTokenizer st = new StringTokenizer(getline);
            String tag = st.nextToken();
            if (tag.equals("search")) {
                int index = table.search(st.nextToken());
                if (index == -1) // not found
                    pout.println(-1 + " " + "nullhost");
                else
                    pout.println(table.getPort(index) + " " + table.getHostName(index));
            } else if (tag.equals("insert")) {
                String name = st.nextToken();
                String hostName = st.nextToken();
                int port = Integer.parseInt(st.nextToken());
                int retValue = table.insert(name, hostName, port);
                // Add new hostname to other name server
                secondNameServer.table.insert(name, hostName, port);
                pout.println(retValue);
            }
            pout.flush();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) throws Exception {
        // Starts two new name server
        NameServer[] nameServers = new NameServer[2];
        nameServers[0] = new NameServer();
        nameServers[1] = new NameServer();

        System.out.println("Name servers listening on " + Symbols.nameServer + ":" + Symbols.ServerPort);

        try {
            ServerSocket listener = new ServerSocket(Symbols.ServerPort);
            while (true) {
                Socket socket = listener.accept();
                // Choose name server randomly
                int selected = new Random().nextInt(2);
                int other = 1 - selected;
                System.out.println("[SELECT]  NS " + (selected + 1));
                try {
                    // Try handle connection with selected name server
                    nameServers[selected].handleClient(socket, nameServers[other], true);
                } catch (Exception e) {
                    System.out.println("[DOWN]  NS " + (selected + 1));
                    System.out.println("[CONNECT]  NS " + (other + 1));

                    nameServers[other].handleClient(socket, nameServers[selected], false);

                    // Restart failed name server 1
                    System.out.println("[RESTART]  NS " + (selected + 1));
                    nameServers[selected] = new NameServer(new NameTable(nameServers[other].table));
                }
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Server aborted: " + e);
        }
    }
}