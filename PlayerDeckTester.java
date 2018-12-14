/**
 * File:        PlayerDeckTester.java
 * Description: Tests Player and Deck.
 * Created:     12/14/2018
 *
 * @author danIv
 * @version 1.0
 */

import java.util.Scanner;

public class PlayerDeckTester {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Deck deck = new Deck();
        CardPlayer player1 = new CardPlayer("Player 1", 5), player2 = new CardPlayer("Player 2", 5);

        for (int i = 0; i < 5; i++) player1.setCard(deck.deal());
        for (int i = 0; i < 5; i++) player2.setCard(deck.deal());

        System.out.println("Player 1, you have the following cards in your hand: ");
        System.out.println(player1.showHand());
        System.out.println("How many would you like to discard?");

        int numToDiscard = scanner.nextInt();
        scanner.nextLine();

        int[] discardIndexes = new int[numToDiscard];

        for (int i = 0; i < discardIndexes.length; i++) {
            System.out.println("Which card index would you like to discard?");
            discardIndexes[i] = scanner.nextInt();
            scanner.nextLine();
        }

        System.out.println("Discarding " + numToDiscard + " cards....");

        System.out.println("The deck contains: \n" + deck);

        for (int i = 0; i < discardIndexes.length; i++) {
            Card card = player1.discard(discardIndexes[i]);
            deck.returnToDeck(card);
        }

        deck.shuffle();
        player1.fixHand();

        for (int i = 0; i < numToDiscard; i++) player1.setCard(deck.deal());

        System.out.println("Player 1, you now have the following cards in your hand: ");
        System.out.println(player1.showHand() + "\n");
        System.out.println("The deck contains: \n" + deck);
    }
}
