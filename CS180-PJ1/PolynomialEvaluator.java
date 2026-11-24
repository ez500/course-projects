import java.util.Scanner;

/**
 * A polynomial calculator.
 *
 * @author Eugene Zhang, zhan4713
 * @version Sep. 22, 2025
 */

public class PolynomialEvaluator {

    public static final String WELCOME_MESSAGE = "Welcome to the Polynomial Evaluator!";
    public static final String MAIN_MENU = "Please Select an Operation\n" +
        "1 - Calculate Polynomial\n" +
        "2 - Exit";
    
    public static final String DEGREE_PROMPT = "Enter the degree of the polynomial:";
    public static final String X_VALUE_PROMPT = "Enter the value of x:";
    public static final String COEFFICIENT_PROMPT = "Enter the coefficient for x^";
    public static final String CONFIRMATION = "Are you sure you want to exit?";
    public static final String INVALID_INPUT = "Invalid Input! Try again";
    public static final String OVERFLOW_PROMPT = "Overflow detected! Returning to main menu";
    public static final String THANK_YOU = "Thank You For Using the Polynomial Evaluator!";


    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        
        String inputString = "";
        
        System.out.println(WELCOME_MESSAGE);
        
        returnToMainMenu:
        while (true) {
            int option;
            System.out.println(MAIN_MENU);
            inputString = in.nextLine();

            try {
                option = Integer.parseInt(inputString);
                if (option != 1 && option != 2) {
                    System.out.println(INVALID_INPUT);
                    continue returnToMainMenu;
                }
            } catch (NumberFormatException e) {
                System.out.println(INVALID_INPUT);
                continue returnToMainMenu;
            }
            
            if (option == 2) {
                boolean inOptionTwo = true;
                while (inOptionTwo) {
                    System.out.println(CONFIRMATION);
                    inputString = in.nextLine().toLowerCase();

                    if (inputString.equals("yes") || inputString.equals("y")) {
                        System.out.println(THANK_YOU);
                        return;
                    } else if (inputString.equals("no") || inputString.equals("n")) {
                        inOptionTwo = false;
                        continue returnToMainMenu;
                    } else {
                        System.out.println(INVALID_INPUT);
                    }
                }
            }
            
            int sum = 0;
            String printedExpression = "";
            int degree;
            System.out.println(DEGREE_PROMPT);
            inputString = in.nextLine();
            
            try {
                degree = Integer.parseInt(inputString);
                if ((inputString.charAt(0) != '-' && degree < 0)
                    || (inputString.charAt(0) == '-' && degree > 0)) {
                    System.out.println(OVERFLOW_PROMPT);
                    continue returnToMainMenu;
                }
            } catch (NumberFormatException e) {
                System.out.println(INVALID_INPUT);
                continue returnToMainMenu;
            }
            
            int x;
            System.out.println(X_VALUE_PROMPT);
            inputString = in.nextLine();
            
            try {
                x = Integer.parseInt(inputString);
                if ((inputString.charAt(0) != '-' && x < 0)
                    || (inputString.charAt(0) == '-' && x > 0)) {
                    System.out.println(OVERFLOW_PROMPT);
                    continue returnToMainMenu;
                }
            } catch (NumberFormatException e) {
                System.out.println(INVALID_INPUT);
                continue returnToMainMenu;
            }
            
            printedExpression += "P(" + x + ") = ";
            
            while (degree >= 0) {
                int coeff;
                int term;
                System.out.println(COEFFICIENT_PROMPT + degree);
                inputString = in.nextLine();
                
                try {
                    coeff = Integer.parseInt(inputString);
                    if ((inputString.charAt(0) != '-' && coeff < 0)
                        || (inputString.charAt(0) == '-' && coeff > 0)) {
                        System.out.println(OVERFLOW_PROMPT);
                        continue returnToMainMenu;
                    }
                } catch (NumberFormatException e) {
                    System.out.println(INVALID_INPUT);
                    continue returnToMainMenu;
                }
                
                term = coeff;
                for (int i = 1; i <= degree; i++) {
                    term *= x;
                    // starting number is negative & numbers alternate in sign
                    if (coeff < 0 && x < 0) {
                        // should be negative if power (i) is even, otherwise positive
                        if ((i % 2 == 0 && term > 0) || (i % 2 == 1 && term < 0)) {
                            System.out.println("i: " + i + ", coeff:" + coeff + ", x: " + x + ", term: " + term);
                            System.out.println(OVERFLOW_PROMPT);
                            continue returnToMainMenu;
                        }
                    } else if (x < 0) { // numbers alternate in sign
                        // should be positive if power (i) is even, otherwise negative
                        if ((i % 2 == 0 && term < 0) || (i % 2 == 1 && term > 0)) {
                            System.out.println(OVERFLOW_PROMPT);
                            continue returnToMainMenu;
                        }
                    } else if (term < 0) {
                        System.out.println(OVERFLOW_PROMPT);
                        continue returnToMainMenu;
                    }
                }
                
                sum += term;
                if (coeff < 0) {
                    printedExpression += "(" + coeff + ")" + "(" + x + "^" + degree + ")";
                } else {
                    printedExpression += coeff + "(" + x + "^" + degree + ")";
                }
                
                degree--;
                if (degree >= 0) {
                    printedExpression += " + ";
                }
            }
            
            printedExpression += " = " + sum;
            
            System.out.println(printedExpression);
        }
    } // End main
} // End class



