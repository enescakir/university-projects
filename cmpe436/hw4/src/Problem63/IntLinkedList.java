// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 4

package Problem63;

import java.util.LinkedList;

public class IntLinkedList extends LinkedList {
    public void add(int i) {
        super.add(new Integer(i));
    }

    public boolean contains(int i) {
        return super.contains(new Integer(i));
    }

    public int removeHead() {
        Integer j = (Integer) super.removeFirst();
        return j.intValue();
    }

    public boolean removeObject(int i) {
        return super.remove(new Integer(i));
    }

    public int getEntry(int index) {
        Integer j = (Integer) super.get(index);
        return j.intValue();
    }
}