/**
 * File:        CardPlayer.java
 * Description: Represents a player in a Card game.
 * Created:     12/14/2018
 *
 * @author danIv
 * @version 1.0
 */

public class CardPlayer {
    private Card[] hand;
    private final int MAX_SIZE; // The max size of the hand
    private int currentSize; // The number of cards currently in hand
    private String name;

    public CardPlayer(String name, int max_size) {
        this.name = name;
        this.MAX_SIZE = max_size;
        this.hand = new Card[max_size];
        this.currentSize = 0;
    }

    public void setCard(Card c) {
        if (currentSize < MAX_SIZE) {
            hand[getFirstNullIndex(hand)] = c;
            currentSize++;
        }
    }

    public Card discard(int i) {
        if (i >= 0 && i < currentSize) {
            Card discardedCard = hand[i];
            hand[i] = null;
            currentSize--;
            return discardedCard;
        } else {
            return null;
        }
    }

    public Card[] discard() {
        currentSize = 0;
        Card[] retHand = new Card[hand.length];

        for (int i = 0; i < hand.length; i++) {
            retHand[i] = hand[i];
            hand[i] = null;
        }

        return retHand;
    }

    public void fixHand() {
        Deck.fixCards(hand);
    }

    private int getFirstNullIndex(Object[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null)
                return i;
        }
        return -1;
    }

    public String showHand() {
        String retString = "";

        for (int i = 0; i < hand.length; i++) {
            Card card = hand[i];
            if (card != null) {
                retString += card.getShortName();
                retString += "\t";
            }
        }

        return retString;
    }

    public String getName() {
        return name;
    }

    public int getCurrentSize() {
        return currentSize;
    }
}
