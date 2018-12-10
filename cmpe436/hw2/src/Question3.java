// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 2
// Question 3

/**
 * Solutions for Question 3
 */
public class Question3 {
    public static final int NUMBER_OF_THREADS= 3;

    public static void main(String[] args) {

        // Create thread to race
        RaceThread[] threads = new RaceThread[NUMBER_OF_THREADS];

        // It's type is array, because it passed by reference not by value.
        int[] commonValue = new int[1];

        // Constructs threads
        for (int i = 0; i < NUMBER_OF_THREADS; i++)
            threads[i] = new RaceThread(i, commonValue);

        // Start all threads
        for (int i = 0; i < NUMBER_OF_THREADS; i++)
            threads[i].start();

        // All threads to be done
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Expected result is NUMBER_OF_THREADS * 10
        // But 20 is printed
        System.out.println(commonValue[0]);
    }
}