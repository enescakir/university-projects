// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 4

package Problem63;

import java.util.*;
import java.io.*;

public class Linker {
    PrintWriter[] dataOut;
    BufferedReader[] dataIn;
    BufferedReader dIn;
    int myId, N;
    Connector connector;
    public IntLinkedList neighbors = new IntLinkedList();

    public Linker(String basename, int id, int numProc) throws Exception {
        myId = id;
        N = numProc;
        dataIn = new BufferedReader[numProc];
        dataOut = new PrintWriter[numProc];
        Topology.readNeighbors(myId, N, neighbors);
        connector = new Connector();
        connector.Connect(basename, myId, numProc, dataIn, dataOut);
    }

    public void sendMsg(int destId, String tag, String msg) {
        dataOut[destId].println(myId + " " + destId + " " +
                tag + " " + msg + "#");
        dataOut[destId].flush();
    }

    public void sendMsg(int destId, String tag) {
        sendMsg(destId, tag, " 0 ");
    }

    public void multicast(IntLinkedList destIds, String tag, String msg) {
        for (int i = 0; i < destIds.size(); i++) {
            sendMsg(destIds.getEntry(i), tag, msg);
        }
    }

    public Msg receiveMsg(int fromId) throws IOException {
        String getline = dataIn[fromId].readLine();
        System.out.println(" received message " + getline);
        StringTokenizer st = new StringTokenizer(getline);
        int srcId = Integer.parseInt(st.nextToken());
        int destId = Integer.parseInt(st.nextToken());
        String tag = st.nextToken();
        String msg = st.nextToken("#");
        return new Msg(srcId, destId, tag, msg);
    }

    public int getMyId() {
        return myId;
    }

    public int getNumProc() {
        return N;
    }

    public void close() {
        connector.closeSockets();
    }
}