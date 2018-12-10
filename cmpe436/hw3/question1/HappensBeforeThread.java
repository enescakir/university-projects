// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 3

public class HappensBeforeThread extends Thread {
    static int[] sharedVariable = {0};
    static CountingSemaphore semaphore = new CountingSemaphore(2);

    int newValue;
    // If it's true, it not use lock
    // If it's false, it use lock
    boolean isDetectable;

    public HappensBeforeThread(int value, boolean isDetectable) {
        this.newValue = value;
        this.isDetectable = isDetectable;
    }

    @Override
    public void run() {
        threadWait();
        if (isDetectable) {
            sharedVariable[0] = newValue;
        } else {
            semaphore.P();
            sharedVariable[0] = newValue;
            semaphore.V();
        }
        System.out.println("New value: " + newValue);
    }

    public void threadWait() {
        try {
            Thread.sleep(10 / (newValue + 1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}