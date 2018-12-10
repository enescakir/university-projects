// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 4

package Problem61;

import java.util.Arrays;

public class NameTable {
    final int maxSize = 100;
    public String[] names = new String[maxSize];
    public String[] hosts = new String[maxSize];
    public int[] ports = new int[maxSize];
    public int dirsize = 0;

    NameTable() {

    }

    NameTable(NameTable table) {
        names = Arrays.copyOf(table.names, table.names.length);
        hosts = Arrays.copyOf(table.hosts, table.hosts.length);
        ports = Arrays.copyOf(table.ports, table.ports.length);
        dirsize = table.dirsize;
    }

    int search(String s) {
        for (int i = 0; i < dirsize; i++)
            if (names[i].equals(s))
                return i;
        return -1;
    }

    int insert(String s, String hostName, int portNumber) {
        int oldIndex = search(s); // is it already there
        if (oldIndex == -1) {
            names[dirsize] = s;
            hosts[dirsize] = hostName;
            ports[dirsize] = portNumber;
            dirsize++;
            return 1;
        } else
            return 0;
    }

    int getPort(int index) {
        return ports[index];
    }

    String getHostName(int index) {
        return hosts[index];
    }
}