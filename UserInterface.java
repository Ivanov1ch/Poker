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

        boolean isPlaying = true;

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

            if (!player.hasFolded() && !computer.hasFolded()) {
                discardCards();
                System.out.println("Your new hand is: ");
                showHand(player);

                // TODO: computer discarding cards

                System.out.println("Starting second round of wagers...");
                manageBets();

                if (!player.hasFolded() && !computer.hasFolded()) {
                    System.out.println("\nRevealing hands...\n");
                    System.out.print("You have: ");
                    showHand(player);

                    System.out.print(computer.getName() + " has: ");
                    showHand(computer);

                    // TODO: display name of both hands' card combinations

                    int betterHand = gameRules.getBetterHand(player.getHand(), computer.getHand());
                    if (betterHand == 1) {
                        System.out.println("\nYou have the better hand!\n");
                        declareWinner(player, computer);
                    } else {
                        System.out.println("\n" + computer.getName() + " has the better hand!\n");
                        declareWinner(computer, player);
                    }
                } else {
                    declareWinner((player.hasFolded()) ? computer : player, (player.hasFolded()) ? player : computer);
                }
            } else
                declareWinner((player.hasFolded()) ? computer : player, (player.hasFolded()) ? player : computer);

            reset();

            if (player.getBalance() >= gameRules.getAnte())
                isPlaying = UserInputManager.isContinuing(scanner, player.getBalance());
            else
                System.out.println("Sorry, but you don't have enough money to play again!");
        }

        System.out.println("You left the casino with $" + player.getBalance() + " in your pocket...\n\nWe hope to see you again soon!");
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

    private static void reset() {
        player.setFolded(false);
        player.setAllIn(false);

        computer.setFolded(false);
        computer.setAllIn(false);

        deck.returnToDeck(player.discard());
        deck.returnToDeck(computer.discard());

        for (int i = 0; i < 5; i++)
            deck.shuffle();
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
            if (!player.isAllIn() && !computer.hasFolded() && !player.hasFolded()) {
                System.out.println("Your hand is: ");
                showHand(player);

                while (true) {
                    System.out.println("The current wager is $" + currentBet + ".\nThere is $" + pot.getTotal() + " in the pot.\nWould you like to match, fold, or raise?");
                    String choice = scanner.nextLine().toUpperCase();

                    boolean retry = false;

                    if (!choice.startsWith("F") && !choice.startsWith("R") && !choice.startsWith("M")) {
                        System.out.println("Please choose to either 'fold', 'match', or to 'raise'");
                    } else {
                        if (choice.startsWith("F")) {
                            System.out.println("\nYou folded!\n");
                            player.setFolded(true);
                            break;
                        } else if (choice.startsWith("R")) {
                            while (true) {
                                if (player.getBalance() - (currentBet - playerPayed) >= 1) {
                                    player.removeFromBalance(currentBet - playerPayed);
                                    pot.update(currentBet - playerPayed);
                                    System.out.println("Matching the current wager of $" + currentBet + " before raising... You have $" + player.getBalance() + " left");
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
                                            pot.update(raise);
                                            break;
                                        }
                                    } catch (InputMismatchException e) {
                                        scanner.nextLine();
                                        System.out.println("Please enter a number!");
                                    }
                                } else {
                                    System.out.println("Sorry, but you don't have enough money to raise! When you'd match, you'd have $" + (currentBet - playerPayed) + " left.");
                                    retry = true;
                                    break;
                                }
                            }

                            if (retry) {
                                retry = false;
                                continue;
                            }

                            System.out.println("The wager has been raised to $" + currentBet + "\nYou have $" + player.getBalance() + " left");
                            break;
                        } else {
                            player.removeFromBalance(currentBet - playerPayed);
                            pot.update(currentBet - playerPayed);
                            System.out.println("You matched the current wager of $" + currentBet + "\nYou have $" + player.getBalance() + " left");
                            playerMatch = true;
                            break;
                        }
                    }
                }

                if (player.getBalance() == 0) {
                    System.out.println("\nYou have gone all-in!\nYou will no longer be asked if you would like to raise, fold, or match!\n");
                    player.setAllIn(true);
                }

                System.out.println();
            }

            if (!computer.isAllIn() && !player.hasFolded() && !computer.hasFolded()) {
                double computerChoice = computer.decideWager(currentBet - (pot.getTotal() - playerPayed - gameRules.getAnte() * 2 - computerPayed));

                if (computerChoice == -1) {
                    computer.setFolded(true);
                    System.out.println("The computer folded...");
                    break;
                } else if (computerChoice == 0) {
                    computerMatch = true;
                    computer.removeFromBalance(currentBet - (pot.getTotal() - playerPayed - gameRules.getAnte() * 2 - computerPayed));
                    computerPayed = currentBet;
                    System.out.println("The computer matched the $" + currentBet + " wager... It has $" + computer.getBalance() + " left.");
                } else {
                    computer.removeFromBalance(currentBet - (pot.getTotal() - playerPayed - gameRules.getAnte() * 2 - computerPayed));
                    computer.removeFromBalance(computerChoice);

                    currentBet += computerChoice;

                    pot.update(computerChoice + currentBet - (pot.getTotal() - playerPayed - gameRules.getAnte() * 2 - computerPayed));

                    computerPayed = currentBet;

                    System.out.println("The computer raised $" + computerChoice + "... It has $" + computer.getBalance() + " left.");
                }

                if (computer.getBalance() == 0) {
                    System.out.println("\nThe computer has gone all-in!\n");
                    computer.setAllIn(true);
                }

                System.out.println();
            }

            if (((playerMatch || player.isAllIn()) && (computerMatch || computer.isAllIn())) || (computer.hasFolded() || player.hasFolded())) {
                System.out.println("\nNeither party raised; this round of betting is now closed.\n");
                break; // They both didn't raise, and are therefore fine with the current wager
            }
        }

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

    private static void declareWinner(PokerPlayer winner, PokerPlayer loser) {
        System.out.println(winner.getName() + " wins $" + pot.getTotal() + "!");
        loser.removeFromBalance(pot.getTotal());
        winner.addToBalance(pot.getTotal());// Call payout the second time to empty it out
        System.out.println("They have $" + winner.getBalance() + " left!");
    }
}
