// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 2

/**
 * Implementation of counting semaphore
 */
public class CountingSemaphore {
    int value;

    public CountingSemaphore(int initValue) {
        this.value = initValue;
    }

    // Takes resource
    public synchronized void P() {
        while (this.value == 0)
            try {
                wait();
            } catch (InterruptedException e) {
            }
        this.value--;
    }

    // Releases resource
    public synchronized void V() {
        this.value++;
        notify();
    }

    /**
     * Returns available resource count
     *
     * @return Available resource count
     */
    public synchronized int getValue() {
        return value;
    }
}

