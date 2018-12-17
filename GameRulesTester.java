import java.util.Arrays;

/**
 * File:        GameRulesTester.java
 * Description: Tests the GameRules class.
 * Created:     12/17/2018
 *
 * @author danIv
 * @version 1.0
 */

public class GameRulesTester {
    public static void main(String[] args) {
        GameRules gameRules = new GameRules(1.0);
        System.out.println(gameRules.getAnte());
        gameRules.raiseAnte(1);
        System.out.println(gameRules.getAnte());

        Card[] royalFlush = {new Card(14, Card.Suit.DIAMONDS), new Card(13, Card.Suit.DIAMONDS), new Card(12, Card.Suit.DIAMONDS), new Card(11, Card.Suit.DIAMONDS), new Card(10, Card.Suit.DIAMONDS)};
        Card[] straightFlush = {new Card(2, Card.Suit.DIAMONDS), new Card(3, Card.Suit.DIAMONDS), new Card(4, Card.Suit.DIAMONDS), new Card(5, Card.Suit.DIAMONDS), new Card(6, Card.Suit.DIAMONDS)};
        Card[] fourOfAKind = {new Card(2, Card.Suit.SPADES), new Card(2, Card.Suit.DIAMONDS), new Card(2, Card.Suit.HEARTS), new Card(2, Card.Suit.CLUBS), new Card(3, Card.Suit.SPADES)};
        Card[] fullHouse = {new Card(3, Card.Suit.DIAMONDS), new Card(3, Card.Suit.HEARTS), new Card(4, Card.Suit.DIAMONDS), new Card(4, Card.Suit.HEARTS), new Card(4, Card.Suit.CLUBS)};
        Card[] flush = {new Card(2, Card.Suit.DIAMONDS), new Card(3, Card.Suit.DIAMONDS), new Card(5, Card.Suit.DIAMONDS), new Card(7, Card.Suit.DIAMONDS), new Card(6, Card.Suit.DIAMONDS)};;
        Card[] straight = {new Card(2, Card.Suit.DIAMONDS), new Card(3, Card.Suit.HEARTS), new Card(4, Card.Suit.SPADES), new Card(5, Card.Suit.CLUBS), new Card(6, Card.Suit.DIAMONDS)};
        Card[] threeOfAKind = {new Card(3, Card.Suit.DIAMONDS), new Card(5, Card.Suit.HEARTS), new Card(4, Card.Suit.DIAMONDS), new Card(4, Card.Suit.HEARTS), new Card(4, Card.Suit.CLUBS)};
        Card[] twoPair = {new Card(3, Card.Suit.DIAMONDS), new Card(3, Card.Suit.HEARTS), new Card(5, Card.Suit.DIAMONDS), new Card(5, Card.Suit.HEARTS), new Card(4, Card.Suit.CLUBS)};
        Card[] onePair = {new Card(3, Card.Suit.DIAMONDS), new Card(3, Card.Suit.HEARTS), new Card(5, Card.Suit.DIAMONDS), new Card(6, Card.Suit.HEARTS), new Card(4, Card.Suit.CLUBS)};
        Card[] highCard = {new Card(3, Card.Suit.DIAMONDS), new Card(10, Card.Suit.HEARTS), new Card(5, Card.Suit.DIAMONDS), new Card(6, Card.Suit.HEARTS), new Card(4, Card.Suit.CLUBS)};;

        outputScore(gameRules, royalFlush);
        outputScore(gameRules, straightFlush);
        outputScore(gameRules, fourOfAKind);
        outputScore(gameRules, fullHouse);
        outputScore(gameRules, flush);
        outputScore(gameRules, straight);
        outputScore(gameRules, threeOfAKind);
        outputScore(gameRules, twoPair);
        outputScore(gameRules, onePair);
        outputScore(gameRules, highCard);
    }

    private static void outputScore(GameRules gameRules, Card[] hand) {
        System.out.println(Arrays.toString(gameRules.scoreHand(hand)[0]) + ", " + Arrays.toString(gameRules.scoreHand(hand)[1]));
    }
}
