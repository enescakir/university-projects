// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 2
// Question 2

/**
 * Solutions for Question 2
 */

public class Question2 {
    public static final int NUMBER_OF_PHILOSOPHERS = 3;
    public static final String[] PHILOSOPHERS_NAMES = {"Aristotle", "Plato", "Sun Tzu", "Confucius", "Descartes", "Descartes"};

    public static void main(String[] args) {

        Philosopher.forks = new BinarySemaphore[NUMBER_OF_PHILOSOPHERS];

        for (int i = 0; i < NUMBER_OF_PHILOSOPHERS; i++)
            Philosopher.forks[i] = new BinarySemaphore();

        Philosopher[] philosophers = new Philosopher[NUMBER_OF_PHILOSOPHERS];

        for (int i = 0; i < NUMBER_OF_PHILOSOPHERS; i++)
            philosophers[i] = new Philosopher(i, PHILOSOPHERS_NAMES[i]);

        for (int i = 0; i < NUMBER_OF_PHILOSOPHERS; i++)
            philosophers[i].start();

        // All philosophers are locked
    }
}
