package GameComponents;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * File:        GameComponents.Card.java
 * Description: An immutable class representing a card in a normal 52 card deck.
 * Created:     12/12/2018
 *
 * @author danIv
 * @version 1.0
 */

public class Card {
    public enum Suit {
        HEARTS, SPADES, CLUBS, DIAMONDS;

        public String getShortName() {return name().substring(0, 1);}
        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }

    private final Suit suit;
    private final String name; // ”Ace”, “Queen”, “Ten”, etc
    private final int faceValue; // The face value of the card, from 2 to 14

    private BufferedImage cardImage; // Will be used in GUI

    public Card(int faceValue, Suit suit) {
        this.suit = suit;
        this.faceValue = faceValue;
        this.name = nameFromFaceValue();

        try {
            this.cardImage = ImageIO.read(new File("/img/" + getShortName() + suit.getShortName()));
        } catch (IOException e) {
            // TODO: popup explaining that not all images were found
        }
    }

    public Card(Card other) {
        this(other.getFaceValue(), other.getSuit());
    }

    private String nameFromFaceValue() {
        String[] cardNames = {"Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King", "Ace"};
        return cardNames[faceValue - 2];
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

    public char getSuitSymbol() {
        switch (this.suit) {
            case HEARTS:
                return '\u2660';
            case DIAMONDS:
                return '\u2666';
            case CLUBS:
                return '\u2663';
            case SPADES:
                return '\u2660';
            default:
                return ' ';
        }
    }

    public String getShortName() {
        String ret = "" + getSuitSymbol();

        if (faceValue <= 10) {
            ret += faceValue;
        } else {
            ret += name.charAt(0);
        }

        return ret;
    }

    @Override
    public String toString() {
        return "The " + name + " of " + suit + ", with a face value of " + faceValue;
    }
}
