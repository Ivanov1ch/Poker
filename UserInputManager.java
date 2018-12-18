import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * File:        UserInputManager.java
 * Description: Description goes here.
 * Created:     12/17/2018
 *
 * @author danIv
 * @version 1.0
 */

public class UserInputManager {
    public static String getName(Scanner scanner) {
        while (true) {
            System.out.println("Welcome to the Java Casino!\nWhat is your name?");
            String input = scanner.nextLine();

            if (input.trim().length() < 2) {
                System.out.println("Sorry, but valid names have at least two characters, excluding whitespace!");
            } else {
                return input;
            }
        }
    }

    public static double getBalance(Scanner scanner) {
        while (true) {
            System.out.println("How much money do you have?");
            try {
                double input = scanner.nextDouble();
                scanner.nextLine(); // Clear the newline character

                if (input < 10 || input >= Double.MAX_VALUE || Double.toString(input).substring(Double.toString(input).indexOf(".") + 1).length() > 2) {
                    System.out.println("Sorry, but you must enter a valid balance!\n\nValid balances have at most 2 digits after the decimal point and are more than $10");
                } else {
                    return input;
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Please enter a number!");
            }
        }
    }

    public static boolean isContinuing(Scanner scanner, double balance) {
        while (true) {
            System.out.println("Would you like to play another hand? You have $" + balance + " left\nY/N");
            String input = scanner.nextLine();

            if (input.toUpperCase().startsWith("Y")) {
                return true;
            } else if (input.toUpperCase().startsWith("N"))
                return false;
            else
                System.out.println("Please enter Y or N");
        }
    }

    public static int[] getDiscardedCards(Scanner scanner, PokerPlayer player) {
        ArrayList<Integer> chosenIndexes = new ArrayList<>();

        System.out.println(player.getName() + ", your hand currently contains the following cards:");
        System.out.println(player.showHand());
        for (int i = 1; i <= player.getCurrentSize(); i++) {
            System.out.print("(" + i + ")\t");
        }
        System.out.println("\n");

        while (true) {
            if (chosenIndexes.size() == player.getCurrentSize())
                break;

            System.out.println("What card would you like to discard? (Please enter the number underneath the card you would like to discard, or -1 to quit):");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == -1) {
                    break;
                } else if (choice <= 0 || choice > player.getCurrentSize()) {
                    System.out.println("Please enter a valid card index!");
                } else {
                    if (chosenIndexes.contains(choice)) {
                        System.out.println("You've already chosen to discard that card!");
                    } else {
                        chosenIndexes.add(choice);
                    }
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Please enter a number!");
            }
        }

        int[] ret = new int[chosenIndexes.size()];
        if (chosenIndexes.size() != 0) {
            String output = "Discarding cards ";

            for (int i = 0; i < ret.length; i++) {
                ret[i] = chosenIndexes.get(i);
                output += chosenIndexes.get(i);
                if (i != ret.length - 1) {
                    output += ", ";
                }
            }

            System.out.println("\n" + output + "\n");
        }
        return ret;
    }
}
