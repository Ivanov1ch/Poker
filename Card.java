/**
 * File:        Card.java
 * Description: An immutable class representing a card in a normal 52 card deck.
 * Created:     12/12/2018
 *
 * @author danIv
 * @version 1.0
 */

public class Card {
    public enum Suit {
        HEARTS, SPADES, CLUBS, DIAMONDS;

        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }

    private final Suit suit;
    private final String name; // ”Ace”, “Queen”, “Ten”, etc
    private final int faceValue; // The face value of the card, from 1 to 13

    public Card(int faceValue, Suit suit) {
        this.suit = suit;
        this.faceValue = faceValue;
        this.name = nameFromFaceValue();
    }

    private String nameFromFaceValue() {
        String[] cardNames = {"Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King", "Ace"};
        return cardNames[faceValue - 1];
    }

    public Suit getSuit() {
        return suit;
    }

    public String getName() {
        return name;
    }

    public int getFaceValue() {
        return faceValue;
    }

    @Override
    public String toString() {
        return "The " + name + " of " + suit + ", with a face value of " + faceValue;
    }
}
