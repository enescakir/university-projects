// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 2
// Question 1

/**
 * Represents thread of cell
 */
public class CellThread extends Thread {
    /**
     * The population that cell is belongs
     */
    Population population;

    /**
     * Coordinates and value of cell
     */
    private int i, j, value;


    /**
     * Constructs cell thread object with given coordinates and population
     *
     * @param i          row number of position
     * @param j          column number of position
     * @param population that cell is belongs
     */
    public CellThread(int i, int j, Population population) {
        this.i = i;
        this.j = j;
        this.population = population;
    }


    /**
     * Operates cell thread with barrier synchronization
     */
    @Override
    public void run() {
        // Run thread until the last generation
        for (int generation = 0; generation < population.getMaxGeneration(); generation++) {
            // Thread reserve ticket for this generation
            population.generationThreads.P();

            // Just one thread calculate next value at a time
            // Enter critical session for calculating next value
            population.mutex.P();
            population.calculatedCellCount++;
            int next = getNextValue();

            // Release barrier if all threads calculated next
            if (population.calculatedCellCount == population.getSize()) {
                population.barrier.V();
            }

            // Exit critical session
            population.mutex.V();

            // If all threads are not calculate next value, put barrier to thread
            population.barrier.P();

            // Enter critical session for changing next value
            population.mutex.P();

            population.calculatedCellCount--;
            this.value = next;

            // Exit critical session
            population.mutex.V();

            // If all threads are not change next value, put barrier to thread
            population.barrier.V();

            // If all cell are calculated, jump to next generation
            // Release barrier if all threads changes next value
            if (population.calculatedCellCount == 0) {
                // Print current generation
                System.out.printf("%d. Generation\n", generation + 1);
                population.print();

                // Release barrier for threads to jump next generation
                population.barrier.P();

                // Release all tickets for next cycle.
                for (int j = 0; j < population.getSize(); j++) {
                    population.generationThreads.V();
                }
            }
        }
    }

    /**
     * Changes value of cell
     *
     * @param value new value of cell
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Return value of cell
     *
     * @return value of cell
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Calculates next value of current cell
     *
     * @return next value of thread
     */

    private int getNextValue() {
        return population.getNextValue(i, j, value);
    }
}
