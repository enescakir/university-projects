package util;

/**
 * Binary Semaphore implementation from book. It solves mutex issues.
 */
public class BinarySemaphore {
    boolean value;

    public BinarySemaphore() {
        this.value = true;
    }

    public BinarySemaphore(boolean initValue) {
        this.value = initValue;
    }

    // Takes resource
    public synchronized void P() {
        while (!this.value)
            try {
                wait();
            } catch (InterruptedException e) {
            }
        this.value = false;
    }

    // Releases resource
    public synchronized void V() {
        this.value = true;
        notify();
    }
}

