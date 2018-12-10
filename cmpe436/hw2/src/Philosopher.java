// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 2
// Question 2

/**
 * Represents philosopher for deadlock example
 */
public class Philosopher extends Thread {

    /**
     * Shared forks between philosophers
     */
    static BinarySemaphore[] forks;

    /**
     * Position of philosopher
     */
    int id;

    /**
     * Name of philosopher
     */
    String name;

    /**
     * Constructs new philosopher
     *
     * @param id   Position of philosopher
     * @param name Name of philosopher
     */
    public Philosopher(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public void run() {
        // Start process
        System.out.println(name + " is thinking.");
        System.out.println(name + " is hungry now.");

        // Take one resourse
        Philosopher.forks[id].P();
        System.out.println(name + " took left fork.");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        // Try to take other resource. It already took by other philosopher
        // DEADLOCK
        Philosopher.forks[(id + 1) % forks.length].P();
        System.out.println(name + " took right fork.");

        System.out.println(name + " is eating.");

        Philosopher.forks[id].V();
        System.out.println(name + " put left fork.");
        Philosopher.forks[(id + 1) % forks.length].V();
        System.out.println(name + " put right fork.");
    }
}
