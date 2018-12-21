package GameComponents;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * File:        GameComponents.ComputerPlayer.java
 * Description: The computer's GameComponents.PokerPlayer.
 * Created:     12/17/2018
 *
 * @author danIv
 * @version 1.0
 */

public class ComputerPlayer extends PokerPlayer {
    private Strategy strategy;

    public ComputerPlayer(int max_size, double balance) {
        super("The Computer", max_size, balance);
        this.strategy = new Strategy();
    }

    // Returns -1 if folding, -2 if all in, 0 if matching, and the amount to raise to if raising
    // balance should be the balance before the wagers started, and shouldn't be updated
    public double decideWager(double currentBet, double balance) {
        Object[] strategicDecision = strategy.decideBet(hand, currentBet, balance);

        switch ((Strategy.Action) strategicDecision[0]) {
            case FOLD:
                return -1;
            case ALL_IN:
                return -2;
            case RAISE:
                return round((double) strategicDecision[1], 2); // Round to 2 decimal places because we are doing division in GameComponents.Strategy
            default:
                return 0;
        }
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void discardCards(Deck deck) {
        int[] indexesToDiscard = strategy.discardCards(getHand());
        for (int i = 0; i < indexesToDiscard.length; i++) {
            deck.returnToDeck(discard(indexesToDiscard[i]));
            setCard(deck.deal());
        }
    }
}
