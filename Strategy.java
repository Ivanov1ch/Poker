import java.util.ArrayList;

/**
 * File:        Strategy.java
 * Description: The ComputerPlayer's strategy.
 * Created:     12/17/2018
 *
 * @author danIv
 * @version 1.0
 */

public class Strategy {
    enum Action {
        MATCH, FOLD, RAISE, ALL_IN
    }

    // Balance should be the balance at the start of the turn and should not be updated
    public Object[] decideBet(Card[] hand, double currentBet, double balance) {
        int[][] handScore = GameRules.scoreHand(hand);
        int handType = handScore[0][0];

        Object[] ret = new Object[2]; // FORMAT: [ACTION, RAISE AMOUNT]l
        ret[1] = -1;

        if (handType >= 6) {
            ret[0] = Action.ALL_IN;
        } else if (handType >= 3) {
            // Pretty good hands are worth raising, unless the opponent is very confident
            if (currentBet < 2 * balance / 3) {
                ret[0] = Action.RAISE;
                ret[1] = 2 * balance / 3;
            } else if (currentBet >= 2 * balance / 3 && currentBet <= 3 * balance / 4) {
                ret[0] = Action.MATCH;
            } else {
                ret[0] = Action.FOLD;
            }
        } else if (handType >= 1) {
            // Only risk up to a third of balance on a relatively weak hand
            if (currentBet < balance / 3) {
                ret[0] = Action.RAISE;
                ret[1] = balance / 3;
            } else if (currentBet >= balance / 3 && currentBet <= balance / 2) {
                ret[0] = Action.MATCH;
            } else {
                ret[0] = Action.FOLD;
            }
        } else {
            // Only risk raising if high card is at least a 9
            if (handScore[0][1] >= 9) {
                if (currentBet < balance / 10) {
                    ret[0] = Action.RAISE;
                    ret[1] = balance / 10;
                } else if (currentBet >= balance / 10 && currentBet <= balance / 9) {
                    ret[0] = Action.MATCH;
                } else {
                    ret[0] = Action.FOLD;
                }
            } else {
                if(currentBet <= balance / 15)
                    ret[0] = Action.MATCH;
                else
                    ret[0] = Action.FOLD;
            }
        }

        return ret;
    }

    public int[] discardCards(Card[] cards) {
        int[][] handScore = GameRules.scoreHand(cards);
        int[] involvedFaceValues = handScore[1];
        ArrayList<Integer> chosenIndexes = new ArrayList<>();

        for (int i = 0; i < cards.length; i++) {
            if (handScore[0][0] <= 1 && cards[i].getFaceValue() == handScore[0][1])
                continue;

            boolean isInvolved = false;

            for (int j = 0; j < involvedFaceValues.length; j++) {
                if (involvedFaceValues[j] == cards[i].getFaceValue()) {
                    isInvolved = true;
                    break;
                }
            }

            if (!isInvolved)
                chosenIndexes.add(i);
        }

        int[] ret = new int[chosenIndexes.size()];
        if (chosenIndexes.size() != 0) {
            for (int i = 0; i < ret.length; i++) {
                ret[i] = chosenIndexes.get(i);
            }
        }
        return ret;
    }
}
