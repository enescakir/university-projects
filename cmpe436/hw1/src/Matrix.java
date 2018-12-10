// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 1
// Question 1

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Encapsulation for matrix data
 */
public class Matrix {
    /**
     * Keeps matrix data
     */
    private int[][] data;

    /**
     * Constructs empty matrix object
     */
    public Matrix() {
        this.data = null;
    }

    /**
     * Constructs matrix object from 2 dimensional array
     *
     * @param data two dimensional int array for matrix data
     */
    public Matrix(int[][] data) {
        this.data = data;
    }

    /**
     * Constructs empty matrix object with given sizes
     *
     * @param rows    number of the rows
     * @param columns number of the columns
     */
    public Matrix(int rows, int columns) {
        if (rows <= 0 || columns <= 0) {
            abort("Dimensions of the matrix have to be positive");
        }
        this.data = new int[rows][columns];
    }

    /**
     * Read given file and constructs new matrix from it
     *
     * @param filename data file for matrix
     * @return new matrix from file data
     */
    public void read(String filename) {
        Scanner scanner = this.openFile(filename);

        int rows = scanner.nextInt();
        int columns = scanner.nextInt();

        if (rows <= 0 || columns <= 0) {
            abort("Row and column numbers have to be positive");
        }
        this.data = new int[rows][columns];

        this.readData(scanner);

        scanner.close();
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
     * Writes this matrix to file with given name
     *
     * @param filename file for writing matrix
     */
    public void write(String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            writer.print(this.toString());
            writer.close();
        } catch (Exception e) {
            abort("Couldn't write result to file");
        }

    }

    /**
     * Multiplies this matrix with given matrix
     *
     * @param second matrix for multiplications
     * @return result matrix
     */
    public Matrix multiplyBy(Matrix second) {
        if (this.getNumberOfColumns() != second.getNumberOfRows()) {
            abort("Matrix size error. \n Column number of the first matrix and row number of the second matrix have to be equal.");
        }

        int rows = this.getNumberOfRows();
        int columns = second.getNumberOfColumns();

        Matrix result = new Matrix(rows, columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int sum = 0;
                for (int k = 0; k < this.getNumberOfColumns(); k++) {
                    sum += this.getElement(i, k) * second.getElement(k, j);
                }
                result.setElement(i, j, sum);
            }
        }
        return result;
    }

    /**
     * Prints matrix to console
     */
    public void print() {
        System.out.println(this);
    }

    /**
     * Converts matrix to string
     *
     * @return string representation of matrix
     */
    @Override // Inherited from the Object class.
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getNumberOfRows() + " " + this.getNumberOfColumns() + "\n");

        for (int i = 0; i < this.getNumberOfRows(); i++) {
            for (int j = 0; j < this.getNumberOfColumns(); j++) {
                result.append(this.getElement(i, j) + " ");
            }
            result.append("\n");
        }

        return result.toString();
    }

    /**
     * Returns number of the rows
     *
     * @return number of the rows
     */
    public int getNumberOfRows() {
        return this.data.length;
    }

    /**
     * Returns number of the columns
     *
     * @return number of the columns
     */
    public int getNumberOfColumns() {
        return this.data[0].length;
    }

    /**
     * Returns element at given coordinates
     *
     * @param i row number of position
     * @param j column number of position
     * @return element at given coordinates
     */
    public int getElement(int i, int j) {
        return this.data[i][j];
    }

    /**
     * Changes element at given coordinates
     *
     * @param i       row number of position
     * @param j       column number of position
     * @param element data to put position
     */
    public void setElement(int i, int j, int element) {
        this.data[i][j] = element;
    }

    /**
     * Returns data array of matrix
     *
     * @return two dimension array version of matrix
     */
    public int[][] getData() {
        return this.data;
    }

    /**
     * Changes whole data of matrix
     *
     * @param data new data for matrix
     */
    public void setData(int[][] data) {
        this.data = data;
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