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
        MATCH, FOLD, RAISE, DISCARD, ALL_IN;
    }

    public Object[] decideBet(Card[] hand, double amountOwed, double balance) {
        int[][] handScore = GameRules.scoreHand(hand);
        int handType = handScore[0][0];

        Object[] ret = new Object[2]; // FORMAT: [ACTION, RAISE AMMOUNT]l
        ret[1] = -1;

        if (handType >= 6) {
            ret[0] = Action.ALL_IN;
        } else if (handType >= 3) {
            // Pretty good hands are worth raising, unless the opponent is very confident
            if (amountOwed <= 2 * balance / 3) {
                ret[0] = Action.RAISE;
                ret[1] = (2 * balance / 3 - amountOwed);
            } else {
                if(amountOwed < 5 * balance / 6)
                    ret[0] = Action.MATCH;
                else
                    ret[0] = Action.FOLD;
            }
        } else if (handType >= 1) {
            // Only risk up to a third of balance on a relatively weak hand
            if(amountOwed <= balance / 3) {
                ret[0] = Action.RAISE;
                ret[1] = (balance / 3 - amountOwed);
            } else {
                if(amountOwed < 3 * balance / 8)
                    ret[0] = Action.MATCH;
                else
                    ret[0] = Action.FOLD;
            }
        } else {
            if(amountOwed <= balance / 5)
                ret[0] = Action.MATCH;
            else
                ret[0] = Action.FOLD;
        }

        return ret;
    }

//    public Action playHand() {
//
//    }
}
