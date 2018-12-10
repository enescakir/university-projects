// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 2
// Question 3

import java.util.Random;

/**
 * Threads that are racing
 */
public class RaceThread extends Thread {

    /**
     * Index of thread
     */
    int id;

    /**
     * Shared resources between threads
     */
    int[] commonValue;

    /**
     * Constructs new racer thread
     *
     * @param id          Position of thread
     * @param commonValue Shared resources between threads
     */
    RaceThread(int id, int[] commonValue) {
        this.id = id;
        this.commonValue = commonValue;
    }

    @Override
    public void run() {
        // Wait to get resource
        threadWait();
        // Get shared resource to temporary variable and wait for writing back common resource
        int tmp = this.commonValue[0];
        threadWait();
        tmp += 10;
        this.commonValue[0] = tmp;
    }

    /**
     * Calculates waiting time for thread based on their id
     */
    public void threadWait() {
        try {
            Thread.sleep(10 / (id + 1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}