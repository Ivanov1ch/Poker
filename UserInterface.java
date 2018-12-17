import java.util.Scanner;

/**
 * File:        UserInterface.java
 * Description: Drives a console-based game of poker.
 * Created:     12/17/2018
 *
 * @author danIv
 * @version 1.0
 */
public class UserInterface {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = getName(scanner);
        double balance = getBalance(scanner);

        PokerPlayer player = new PokerPlayer(name, 5, balance);

        boolean isPlaying = true;

        while (isPlaying) {
            isPlaying = isContinuing(scanner, player.getBalance());
        }
    }

    private static String getName(Scanner scanner) {
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

    private static double getBalance(Scanner scanner) {
        while (true) {
            System.out.println("How much money do you have?");
            double input = scanner.nextDouble();
            scanner.nextLine(); // Clear the newline character

            if (input < 10 || input >= Double.MAX_VALUE || Double.toString(input).substring(Double.toString(input).indexOf(".") + 1).length() > 2) {
                System.out.println("Sorry, but you must enter a valid balance!\n\nValid balances have at most 2 digits after the decimal point and are more than $10");
            } else {
                return input;
            }
        }
    }

    private static boolean isContinuing(Scanner scanner, double balance) {
        while (true) {
            System.out.println("Would you like to play another hand? You have $" + balance + " left\nY/N");
            String input = scanner.nextLine();

            if (input.toUpperCase().startsWith("Y")) {
                return true;
            } else if(input.toUpperCase().startsWith("N"))
                return false;
            else
                System.out.println("Please enter Y or N");
        }
    }
}
