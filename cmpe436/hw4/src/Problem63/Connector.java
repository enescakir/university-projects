// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 4

package Problem63;

import Problem61.*;

import java.util.*;
import java.net.*;
import java.io.*;

public class Connector {
    ServerSocket listener;
    Socket[] link;

    public void Connect(String basename, int myId, int numProc, BufferedReader[] dataIn, PrintWriter[] dataOut) throws Exception {
        NameClient myNameclient = new NameClient();
        link = new Socket[numProc];
        int localport = getLocalPort(myId);
        listener = new ServerSocket(localport);

        /* register in the name server */
        myNameclient.insertName(basename + myId, (InetAddress.getLocalHost()).getHostName(), localport);

        /* accept connections from all the smaller processes */
        for (int i = 0; i < myId; i++) {
            Socket s = listener.accept();
            BufferedReader dIn = new BufferedReader(
                    new InputStreamReader(s.getInputStream()));
            String getline = dIn.readLine();
            StringTokenizer st = new StringTokenizer(getline);
            int hisId = Integer.parseInt(st.nextToken());
            int destId = Integer.parseInt(st.nextToken());
            String tag = st.nextToken();
            if (tag.equals("hello")) {
                link[hisId] = s;
                dataIn[hisId] = dIn;
                dataOut[hisId] = new PrintWriter(s.getOutputStream());
            }
        }
        /* contact all the bigger processes */
        for (int i = myId + 1; i < numProc; i++) {
            InetSocketAddress addr;
            do {
                addr = myNameclient.searchName(basename + i, false);
                Thread.sleep(100);
            } while (addr.getPort() == -1);
            link[i] = new Socket(addr.getHostName(), addr.getPort());
            dataOut[i] = new PrintWriter(link[i].getOutputStream());
            dataIn[i] = new BufferedReader(new
                    InputStreamReader(link[i].getInputStream()));
            /* send a hello message to P_i */
            dataOut[i].println(myId + " " + i + " " + "hello" + " " + "null");
            dataOut[i].flush();
        }
    }

    int getLocalPort(int id) {
        return Symbols.ServerPort + 10 + id;
    }

    public void closeSockets() {
        try {
            listener.close();
            for (int i = 0; i < link.length; i++)
                link[i].close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}