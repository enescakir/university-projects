// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 1
// Question 2

import java.util.Scanner;
import java.util.Random;

/**
 * Encapsulation for game of life's population
 * Data matrix has 2 more rows and columns.
 * Outer line is always zero. It's for neighbor calculation of borders.
 */
public class Population extends Matrix {
    /**
     * Maximum number of last generation
     */
    private int maxGeneration;

    /**
     * Constructs empty matrix object with given sizes
     *
     * @param rows          number of the rows
     * @param columns       number of the columns
     * @param maxGeneration number of the max generations
     */
    public Population(int rows, int columns, int maxGeneration) {
        super(rows + 2, columns + 2);
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
     * Calculates next generation for current state
     */
    public void nextGeneration() {
        Population next = new Population(getNumberOfRows(), getNumberOfColumns(), this.maxGeneration);

        for (int i = 0; i < this.getNumberOfRows(); i++) {
            for (int j = 0; j < this.getNumberOfColumns(); j++) {
                next.setElement(i, j, this.getNextValue(i, j));
            }
        }
        this.setData(next.getData());
    }

    /**
     * Runs population until max generation
     */
    public void run() {
        System.out.println("Initial Population");
        this.print();

        for (int i = 0; i < this.maxGeneration; i++) {
            this.nextGeneration();
            System.out.printf("%d. Generation\n", i + 1);
            this.print();
        }
    }

    /**
     * Converts matrix to string
     * It deletes first line, because it has dimension information
     *
     * @return string representation of matrix
     */
    @Override
    public String toString() {
        String result = super.toString();
        return result.substring(result.indexOf("\n") + 1);
    }

    /**
     * Calculates next value of given coordinates
     *
     * @param i row number of position
     * @param j column number of position
     * @return next of element
     */
    public int getNextValue(int i, int j) {
        int neighborCount = getNeighborCount(i, j);
        if (this.getElement(i, j) == 1) {
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
        return super.getNumberOfRows() - 2;
    }

    /**
     * Returns number of the columns. Subtracts 2 because of empty outer area.
     *
     * @return number of the columns
     */
    public int getNumberOfColumns() {
        return super.getNumberOfColumns() - 2;
    }

    /**
     * Returns element at given coordinates. Adds 1 because of empty outer area
     *
     * @param i row number of position
     * @param j column number of position
     * @return element at given coordinates
     */
    public int getElement(int i, int j) {
        return super.getElement(i + 1, j + 1);
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
        super.setElement(i + 1, j + 1, element);
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
}