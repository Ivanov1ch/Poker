/**
 * File:        Deck.java
 * Description: A class representing a deck of Cards.
 * Created:     12/12/2018
 *
 * @author danIv
 * @version 1.0
 */

public class Deck {
    private Card[] cards;
    public static final int MAX_SIZE = 52;
    private int currentSize;

    public Deck() {
        this.currentSize = MAX_SIZE;
        initializeDeck();
    }

    private void initializeDeck() {
        Card.Suit[] suits = {Card.Suit.HEARTS, Card.Suit.SPADES, Card.Suit.CLUBS, Card.Suit.DIAMONDS};
        cards = new Card[currentSize];

        // Generate all possible cards
        for (int suitIndex = 0; suitIndex < 4; suitIndex++) {
            for (int faceValue = 1; faceValue <= currentSize / 4.0; faceValue++) {
                cards[suitIndex * 13 + faceValue - 1] = new Card(faceValue, suits[suitIndex]);
            }
        }

        shuffle();
    }

    public void shuffle() {
        for (int i = 0; i < 2 * cards.length; i++) {
            int indexOne = (int) (Math.random() * currentSize), indexTwo = (int) (Math.random() * currentSize);

            Card temp = cards[indexOne];
            cards[indexOne] = cards[indexTwo];
            cards[indexTwo] = temp;
        }
    }

    // Returns the top Card on the deck (the last Card in the array), and replaces it with null
    // Returns null if all elements in cards are null
    public Card deal() {
        for (int index = currentSize - 1; index >= 0; index--) {
            Card currentCard = cards[index];
            if (currentCard != null) {
                cards[index] = null;
                currentSize--;
                return currentCard;
            }
        }

        return null;
    }

    // Adds the Card to the bottom of the deck (index 0)
    // Returns true if successful, false otherwise
    public boolean returnToDeck(Card card) {
        if (currentSize >= MAX_SIZE)
            return false;

        for (int index = cards.length - 1; index >= 1; index--) {
            cards[index] = cards[index - 1];
        }
        cards[0] = card;
        currentSize++;

        return true;
    }

    // Adds all the Cards in the array to the bottom of the deck (index 0)
    // Returns true if successful, false otherwise
    public boolean returnToDeck(Card[] cards) {
        for (Card card : cards) {
            if (!returnToDeck(card))
                return false;
        }

        return true;
    }

    @Override
    public String toString() {
        String returnString = "A deck with " + currentSize + " Cards in it:\n";
        for (int i = 1; i <= currentSize; i++) {
            Card currentCard = cards[i - 1];
            returnString += "Card " + i + ": ";
            returnString += currentCard + "\n";
        }

        return returnString;
    }
}
