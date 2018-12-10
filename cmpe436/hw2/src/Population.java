// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 1
// Question 2

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

/**
 * Encapsulation for game of life's population
 * Data matrix has 2 more rows and columns.
 * Outer line is always zero. It's for neighbor calculation of borders.
 */
public class Population {
    /**
     * Keeps threads in matrix
     */
    private CellThread[][] data;

    /**
     * Maximum number of last generation
     */
    private int maxGeneration;

    /**
     * Semaphores for synchronization barriers
     */
    int calculatedCellCount = 0;
    /**
     * Semaphore for critical section
     */
    CountingSemaphore mutex;
    /**
     * Semaphore for waiting all threads to calculate
     */
    CountingSemaphore barrier;
    /**
     * Semaphore for waiting all threads to finish generation
     */
    CountingSemaphore generationThreads;

    /**
     * Constructs empty matrix object thread with given sizes
     *
     * @param rows          number of the rows
     * @param columns       number of the columns
     * @param maxGeneration number of the max generations
     */
    public Population(int rows, int columns, int maxGeneration) {
        if (rows <= 0 || columns <= 0) {
            abort("Dimensions of the matrix have to be positive");
        }
        this.data = new CellThread[rows + 2][columns + 2];
        for (int i = 0; i < this.getNumberOfRows() + 2; i++) {
            for (int j = 0; j < this.getNumberOfColumns() + 2; j++) {
                // Threads don't know about border empty cells. So we subtract 1.
                data[i][j] = new CellThread(i - 1, j - 1, this);
            }
        }
        this.setMaxGeneration(maxGeneration);
    }

    /**
     * Imports population from file
     *
     * @param filename data file for initial population
     */
    public void importPopulation(String filename) {
        Scanner scanner = this.openFile(filename);
        this.readData(scanner);
        scanner.close();
    }

    /**
     * Converts file to scanner
     *
     * @param filename Name of the file that opened
     * @return opened file
     */
    public Scanner openFile(String filename) {
        File file = new File(filename);
        Scanner scanner = null;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            abort("File " + filename + " not found.");
        }
        return scanner;
    }

    /**
     * Read data part of the matrix
     *
     * @param scanner opened file
     */
    public void readData(Scanner scanner) {
        for (int i = 0; i < getNumberOfRows(); i++) {
            for (int j = 0; j < getNumberOfColumns(); j++) {
                try {
                    this.setElement(i, j, scanner.nextInt());
                } catch (Exception e) {
                    abort("Error at element [" + i + ", " + j + "]. \n Please make sure all elements are integers.");
                }
            }
        }
    }

    /**
     * Generates random population
     */
    public void generateRandom() {
        Random random = new Random();
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            for (int j = 0; j < this.getNumberOfColumns(); j++) {
                this.setElement(i, j, random.nextInt(2));
            }
        }
    }


    /**
     * Runs population until max generation
     */
    public void run() {
        System.out.println("Initial Population");
        this.print();

        // Initialize  common resource for synchronization
        calculatedCellCount = 0;
        mutex = new CountingSemaphore(1);
        barrier = new CountingSemaphore(0);
        generationThreads = new CountingSemaphore(this.getSize());

        // Start all threads
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            for (int j = 0; j < this.getNumberOfColumns(); j++) {
                this.data[i + 1][j + 1].start();
            }
        }
    }

    /**
     * Converts matrix to string
     * It deletes first line, because it has dimension information
     *
     * @return string representation of matrix
     */
    @Override // Inherited from the Object class.
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            for (int j = 0; j < this.getNumberOfColumns(); j++) {
                result.append(this.getElement(i, j) + " ");
            }
            result.append("\n");
        }

        return result.toString();
    }

    /**
     * Prints matrix to console
     */
    public void print() {
        System.out.println(this);
    }

    /**
     * Calculates next value of given coordinates
     *
     * @param i row number of position
     * @param j column number of position
     * @return next of element
     */
    public int getNextValue(int i, int j, int value) {
        int neighborCount = getNeighborCount(i, j);
        if (value == 1) {
            return (neighborCount == 2 || neighborCount == 3) ? 1 : 0;
        }

        return (neighborCount == 3) ? 1 : 0;
    }

    /**
     * Counts neighbors of given coordinates
     *
     * @param i row number of position
     * @param j column number of position
     * @return count of neighbors
     */
    public int getNeighborCount(int i, int j) {
        return this.getElement(i - 1, j - 1) + this.getElement(i - 1, j) + this.getElement(i - 1, j + 1) +
                this.getElement(i, j - 1) + this.getElement(i, j + 1) +
                this.getElement(i + 1, j - 1) + this.getElement(i + 1, j) + this.getElement(i + 1, j + 1);
    }

    /**
     * Returns number of the rows. Subtracts 2 because of empty outer area.
     *
     * @return number of the rows
     */
    public int getNumberOfRows() {
        return this.data.length - 2;
    }

    /**
     * Returns number of the columns. Subtracts 2 because of empty outer area.
     *
     * @return number of the columns
     */
    public int getNumberOfColumns() {
        return this.data[0].length - 2;
    }

    /**
     * Returns element at given coordinates. Adds 1 because of empty outer area
     *
     * @param i row number of position
     * @param j column number of position
     * @return element at given coordinates
     */
    public int getElement(int i, int j) {
        return this.data[i + 1][j + 1].getValue();
    }

    /**
     * Changes element at given coordinates
     *
     * @param i       row number of position
     * @param j       column number of position
     * @param element data to put position
     */
    public void setElement(int i, int j, int element) {
        if (element != 0 && element != 1) {
            abort("Cell number have to be 1 or 0.");
        }

        this.data[i + 1][j + 1].setValue(element);

    }

    /**
     * Changes max generation of population
     *
     * @param value new value of max generation
     */
    public void setMaxGeneration(int value) {
        if (value <= 0) {
            abort("Max generation number must be an positive integer.");
        }
        this.maxGeneration = value;
    }

    public int getMaxGeneration() {
        return this.maxGeneration;
    }

    /**
     * Returns data array of matrix
     *
     * @return two dimension array version of matrix
     */
    public CellThread[][] getData() {
        return this.data;
    }

    /**
     * Changes whole data of matrix
     *
     * @param data new data for matrix
     */
    public void setData(CellThread[][] data) {
        this.data = data;
    }

    /**
     * Returns number of cells
     *
     * @return number of cells
     */
    public int getSize() {
        return this.getNumberOfRows() * this.getNumberOfColumns();
    }

    /**
     * Aborts application with messages
     *
     * @param message error message for aborting
     */
    public void abort(String message) {
        System.out.println(message);
        System.exit(0);
    }

}