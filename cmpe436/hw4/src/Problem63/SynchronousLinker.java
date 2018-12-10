// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 4

package Problem63;

import java.io.*;

public class SynchronousLinker extends Linker {
    public CountingSemaphore mutex = new CountingSemaphore(1);

    public SynchronousLinker(String basename, int id, int numProc) throws Exception {
        super(basename, id, numProc);
    }

    public synchronized void sendMsg(int destId, String tag, String msg) {
        mutex.P();
        dataOut[destId].println(myId + " " + destId + " " + tag + " " + msg + "#");
        dataOut[destId].flush();
        boolean done = false;
        // Wait until done message is received
        while (!done) {
            try {
                Msg message = receiveMsg(destId);
                if (message.tag.equals("done")) {
                    // Unblock
                    mutex.V();
                    done = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public synchronized Msg receiveMsg(int fromId) throws IOException {
        Msg message = super.receiveMsg(fromId);
        // Send done message, if it is normal message
        if (!message.tag.equals("done")) {
            sendMsg(fromId, "done");
        }
        return message;
    }

}
