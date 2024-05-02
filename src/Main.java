import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static double[][] matrix;
    private static Scanner userInput = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Matrix Operations Program!");
        collectMatrix();
        menu();
    }

    /*
    * Collects matrix data from the user.
    * This method prompts the user to enter the dimension of a square matrix (n x n) where n is less than or equal to 4.
    * It then collects user input for each element of the matrix.
    */
    public static void collectMatrix() {
        int dimension = 0; // Variable to store the matrix dimension.
        boolean validInput = false; // Flag to check if the input is valid.

        // Loop until a valid dimension is entered.
        while (!validInput) {
            try {
                System.out.print("Enter the dimension of the matrix (n x n, where n <= 4): "); // Prompt for matrix dimension.
                dimension = userInput.nextInt(); // Read user input for dimension.

                // Check if the dimension is within the valid range.
                if (dimension >= 1 && dimension <= 4) {
                    validInput = true; // Set flag to true if dimension is valid.
                } else {
                    System.out.println("Invalid dimension. Please enter a value for n where 1 <= n <= 4."); // Error message for invalid dimension.
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer."); // Error message for non-integer input.
                userInput.next(); // Consume the invalid input to avoid infinite loop.
            }
        }

        matrix = new double[dimension][dimension]; // Initialize the matrix with the specified dimension.
        System.out.println("Input Values for the matrix:"); // Prompt for matrix values.

        // Nested loops to collect matrix elements from the user.
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                System.out.printf("Enter value for Row %d, Column %d: ", i + 1, j + 1); // Prompt for each matrix element.
                while (!userInput.hasNextDouble()) {
                    System.out.println("Invalid input. Please enter a numeric value."); // Error message for non-numeric input.
                    userInput.next(); // Consume the invalid input.
                }
                matrix[i][j] = userInput.nextDouble(); // Store the user input in the matrix.
            }
        }
    }


    public static void menu() {
        boolean repeatLoop = true;
        while (repeatLoop) {
            System.out.print("\n================================================\n" +
                    "What would you like to do?\n" +
                    "1. Print Matrix\n" +
                    "2. Show Step-by-Step Solutions for Determinant\n" +
                    "3. Calculate Determinant using Cofactor Expansion\n" +
                    "4. Input another values\n" +
                    "5. Exit Program\n" +
                    "Enter the number of your choice: ");

            int option = getIntInput();
            switch (option) {
                case 1 -> {
                    System.out.println("\nHere is the Matrix:");
                    printMatrixForm(matrix);
                }
                case 2 -> {
                    System.out.println("\nStep-by-Step Solution for Determinant:");
                    showDeterminantSolutions(matrix);
                }
                case 3 -> {
                    System.out.println("\nDeterminant of the matrix:");
                    double determinant = calculateDeterminant(matrix);
                    System.out.println("The determinant is: " + determinant);
                }
                case 4 -> collectMatrix();
                case 5 -> {
                    System.out.println("Exiting the program.");
                    repeatLoop = false;
                }
                default -> System.out.println("Invalid option. Please enter a valid choice.");
            }
        }
    }
    /*
    * Displays the step-by-step solutions for calculating the determinant of a matrix.
    * If the matrix has a dimension of 1, the determinant is the single element itself.
    * For larger matrices, the method demonstrates the cofactor expansion method for determinant calculation.
    * @param matrix The input matrix for which the determinant solutions are displayed.
    */
    public static void showDeterminantSolutions(double[][] matrix) {
        int n = matrix.length; // Get the dimension of the matrix.

        if (n == 1) {
            System.out.println("Single element matrix, determinant is the element itself: " + matrix[0][0]);
            return;
        }

        System.out.println("Determinant is calculated as follows:");
        System.out.println("Starting matrix:");
        printMatrixForm(matrix); // Display the original matrix.

        System.out.println("\nExpanding along the first row:");
        for (int j = 0; j < n; j++) {
            double cofactor = Math.pow(-1, j) * matrix[0][j]; // Calculate the cofactor for the element.
            double[][] minor = getMinor(matrix, 0, j); // Get the minor matrix for the element.
            System.out.println("Cofactor for element at (1," + (j + 1) + ") = " + matrix[0][j] + " * " + ((int) Math.pow(-1, j)) + " = " + cofactor);
            System.out.println("Minor for element at (1," + (j + 1) + "):");
            printMatrixForm(minor);
            System.out.println(" "); // Display the minor matrix.
            System.out.println("Determinant of this minor: " + calculateDeterminant(minor)); // Calculate and display the determinant of the minor.
        }
    }

    /*
    * Calculates the determinant of a square matrix using the cofactor expansion method.
    *
    * @param matrix The input square matrix for which the determinant is calculated.
    * @return The determinant of the input matrix.
    */
    public static double calculateDeterminant(double[][] matrix) {
        int n = matrix.length; // Get the dimension of the matrix.
        double determinant = 0; // Initialize the determinant.
        // Base cases for 1x1 and 2x2 matrices
        switch (n) {
            case 1 -> determinant = matrix[0][0]; // For a 1x1 matrix, the determinant is the single element.
            case 2 -> determinant = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]; // For a 2x2 matrix, use the standard formula.
            default -> {
                // For larger matrices, use cofactor expansion to calculate the determinant.
                for (int j = 0; j < n; j++) {
                    determinant += Math.pow(-1, j) * matrix[0][j] * calculateDeterminant(getMinor(matrix, 0, j));
                }
            }
        }
        return determinant; // Return the calculated determinant.
    }

    private static double[][] getMinor(double[][] matrix, int row, int column) {
        int n = matrix.length;
        double[][] minor = new double[n - 1][n - 1];
        for (int i = 0, minorRow = 0; i < n; i++) {
            if (i == row) continue;
            for (int j = 0, minorCol = 0; j < n; j++) {
                if (j == column) continue;
                minor[minorRow][minorCol] = matrix[i][j];
                minorCol++;
            }
            minorRow++;
        }
        return minor;
    }

    private static int getIntInput() {
        while (!userInput.hasNextInt()) {
            System.out.println("Invalid input. Please enter an integer.");
            userInput.next();
        }
        return userInput.nextInt();
    }

    public static void printMatrixForm(double[][] matrix) {
        for (double[] row : matrix) {
            System.out.print("|");
            for (double value : row) {
                if (Math.floor(value) == value) {
                    System.out.printf("%5d ", (int) value);  // Print as integer with width of 5
                } else {
                    System.out.printf("%8.2f ", value);  // Print with two decimal places with width of 8
                }
            }
            System.out.println("|");
        }
    }
}
