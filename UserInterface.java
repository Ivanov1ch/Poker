import java.util.InputMismatchException;
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
    private static Deck deck;
    private static Scanner scanner;
    private static PokerPlayer player;
    private static ComputerPlayer computer;
    private static GameRules gameRules;
    private static Pot pot;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        String name = UserInputManager.getName(scanner);
        double balance = UserInputManager.getBalance(scanner);

        deck = new Deck();
        player = new PokerPlayer(name, 5, balance);
        computer = new ComputerPlayer(5, balance);
        gameRules = new GameRules(1.0);
        pot = new Pot();

        boolean isPlaying = true, computerTurn = false;

        while (isPlaying) {
            payAnte();

            // Check for any people unable to pay the ante
            if (player.getBalance() < 0) {
                player.addToBalance(-2 * player.getBalance()); // Now that we know they have lost, set the amount of money they have back to what they will leave with
                break;
            } else if (computer.getBalance() < 0) {
                System.out.println("The computer is out of money and is unable to pay the ante! You win!");
                break;
            }

            System.out.println();
            dealStartingHands();

            manageBets();
            discardCards();

            isPlaying = UserInputManager.isContinuing(scanner, player.getBalance());
        }

        System.out.println("You left the casino with $" + balance + " in your pocket...\n\nWe hope to see you again soon!");
    }

    private static void payAnte() {
        if (player.getBalance() < gameRules.getAnte()) {
            System.out.println("Sorry, you don't have enough money to pay the $" + gameRules.getAnte() + " ante! You have to leave the casino now...");
            player.removeFromBalance(2 * player.getBalance()); // Set their balance to - their current balance
        } else {
            System.out.println("Paying the $" + gameRules.getAnte() + " ante...");
            player.removeFromBalance(gameRules.getAnte());
            pot.update(gameRules.getAnte());
            System.out.println("You have $" + player.getBalance() + " left\n");
        }

        if (computer.getBalance() < gameRules.getAnte()) {
            computer.removeFromBalance(2 * computer.getBalance()); // Set their balance to - their current balance
        } else {
            System.out.println("The computer is paying the $" + gameRules.getAnte() + " ante...");
            computer.removeFromBalance(gameRules.getAnte());
            pot.update(gameRules.getAnte());
        }
    }

    private static void dealStartingHands() {
        for (int i = 0; i < 5; i++) {
            player.setCard(deck.deal());
            computer.setCard(deck.deal());
        }
    }

    private static void manageBets() {
        double currentBet = 0;
        double playerPayed = 0, computerPayed = 0;

        boolean playerMatch = false, computerMatch = false;
        while (true) {
            System.out.println("Your hand is: ");
            showHand(player);

            while (true) {
                System.out.println("The current wager is $" + currentBet + ".\nThere is $" + pot.getTotal() + " in the pot.\nWould you like to match, fold, or raise?");
                String choice = scanner.nextLine().toUpperCase();
                if (!choice.startsWith("F") && !choice.startsWith("R") && !choice.startsWith("M")) {
                    System.out.println("Please choose to either 'fold', 'match', or to 'raise'");
                } else {
                    if (choice.startsWith("F")) {
                        player.setFolded(false);
                        break;
                    } else if (choice.startsWith("R")) {
                        while (true) {
                            System.out.println("Matching the current wager of $" + currentBet + " before raising... You have $" + player.getBalance() + " left");
                            player.removeFromBalance(currentBet - playerPayed);
                            playerPayed = currentBet;

                            System.out.println("How much would you like to raise (minimum $1.00)");
                            try {
                                double raise = scanner.nextDouble();
                                scanner.nextLine();

                                if (raise < 1 || Double.toString(raise).substring(Double.toString(raise).indexOf(".") + 1).length() > 2) {
                                    System.out.println("Please enter an amount of at least $1.00 with at most 2 digits after the decimal point");
                                } else if (raise > player.getBalance()) {
                                    System.out.println("You only have $" + player.getBalance() + " to wager!");
                                } else {
                                    currentBet += raise;
                                    playerPayed = currentBet;
                                    player.removeFromBalance(raise);
                                    break;
                                }
                            } catch (InputMismatchException e) {
                                scanner.nextLine();
                                System.out.println("Please enter a number!");
                            }
                        }

                        System.out.println("The wager has been raised to $" + currentBet + "\nYou have $" + player.getBalance() + " left");
                        break;
                    } else {
                        player.removeFromBalance(currentBet - playerPayed);
                        System.out.println("You matched the current wager of $" + currentBet + "\nYou have $" + player.getBalance() + " left");
                        playerMatch = true;
                        break;
                    }
                }
            }

            double computerChoice = computer.decideWager(currentBet - computerPayed);

            if (computerChoice == -1) {
                computer.setFolded(true);
                System.out.println("The computer folded...");
                break;
            } else if (computerChoice == 0) {
                computerMatch = true;
                computer.removeFromBalance(currentBet - computerPayed);
                computerPayed = currentBet;
                System.out.println("The computer matched the $" + currentBet + " wager... It has $" + computer.getBalance() + " left.");
            } else {
                computer.removeFromBalance(currentBet - computerPayed);
                computer.removeFromBalance(computerChoice);

                currentBet += computerChoice;

                computerPayed = currentBet + computerChoice;

                System.out.println("The computer raised $" + computerChoice + "... It has $" + computer.getBalance() + " left.");
            }

            if (playerMatch && computerMatch) {
                System.out.println("Neither party raised; this round of betting is now closed.");
                break; // They both didn't raise, and are therefore fine with the current wager
            }
        }

        pot.update(currentBet);
    }

    private static void discardCards() {
        int[] cardsToDiscard = UserInputManager.getDiscardedCards(scanner, player);
        for (int i = 0; i < cardsToDiscard.length; i++) {
            deck.returnToDeck(player.discard(i));
            player.setCard(deck.deal());
        }
    }

    private static void showHand(CardPlayer cardPlayer) {
        System.out.println(cardPlayer.showHand());
    }
}
