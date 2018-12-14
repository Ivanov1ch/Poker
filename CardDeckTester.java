import java.util.Arrays;

/**
 * File:        CardDeckTester.java
 * Description: Tests the Card and Deck classes.
 * Created:     12/12/2018
 *
 * @author danIv
 * @version 1.0
 */

public class CardDeckTester {
    public static void main(String[] args) {
        Deck deck = new Deck();
        System.out.println(deck);
        Card[] hand1 = new Card[7], hand2 = new Card[7];

        for(int i = 0; i < 7; i++) hand1[i] = deck.deal();
        for(int i = 0; i < 7; i++) hand2[i] = deck.deal();

        // Prettify and print the two hands
        System.out.println(String.join(", ", Arrays.toString(hand1)));
        System.out.println(String.join(", ", Arrays.toString(hand2)));
        System.out.println(deck);

        hand1[0] = null;
        System.out.println(String.join(", ", Arrays.toString(hand1)));
        Deck.sortCards(hand1);
        System.out.println(String.join(", ", Arrays.toString(hand1)));
    }
}
