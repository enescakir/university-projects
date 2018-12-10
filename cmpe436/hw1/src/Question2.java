// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 1
// Question 2

/**
 * Solutions for Question 2
 */
public class Question2 {

    public static void main(String[] args) {
        // Check arguments
        if (args.length < 3) {
            System.out.println("\nYou have to give at least 3 arguments but given " + args.length + " arguments.");
            System.out.println("Example usage:");
            System.out.println("\tjava Question2 [NUMBER_OF_ROWS] [NUMBER_OF_COLUMNS] [MAX_GENERATIONS] [INITIAL_POPULATION]?");
            System.exit(0);
        }

        try {
            // Parse arguments
            int numberOfRows = Integer.parseInt(args[0]);
            int numberOfColumns = Integer.parseInt(args[1]);
            int maxGeneration = Integer.parseInt(args[2]);

            // Construct population
            Population population = new Population(numberOfRows, numberOfColumns, maxGeneration);

            if (args.length == 4) {
                // Import initial population
                population.importPopulation(args[3]);
            } else {
                // Generate random inital population
                population.generateRandom();
            }

            // Run population
            population.run();

        } catch (Exception e) {
            System.out.println("Matrix dimensions and max generation must be positive numbers.");
        }
    }
}