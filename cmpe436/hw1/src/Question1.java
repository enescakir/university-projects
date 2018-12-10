// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 1
// Question 1

/**
 * Solutions for Question 1
 */
public class Question1 {

    public static void main(String[] args) {
        // Check arguments
        if (args.length != 3) {
            System.out.println("\nYou have to give 3 arguments but given " + args.length + " arguments.");
            System.out.println("Example usage:");
            System.out.println("\tjava Question1 [FIRST_MATRIX] [SECOND_MATRIX] [RESULT_MATRIX]");
            System.exit(0);
        }

        // Parse arguments
        String firstFile = args[0];
        String secondFile = args[1];
        String resultFile = args[2];

        // Read files and construct matrices
        Matrix first = new Matrix();
        first.read(firstFile);
        Matrix second = new Matrix();
        second.read(secondFile);

        // Multiply two matrices
        Matrix result = first.multiplyBy(second);

        // Write result to file
        result.write(resultFile);

        // Print result to console
        result.print();
    }
}