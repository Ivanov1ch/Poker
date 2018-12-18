/**
 * File:        ComputerPlayer.java
 * Description: The computer's PokerPlayer.
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

    // Returns -1 if folding, 0 if matching, and the amount raised if raising
    public double decideWager(double amountOwed) {
        Object[] strategicDecision = strategy.decideBet(hand, amountOwed, getBalance());

        switch ((Strategy.Action) strategicDecision[0]) {
            case FOLD:
                return -1;
            case ALL_IN:
                return getBalance() - amountOwed;
            case RAISE:
                return (double) strategicDecision[1];
            default:
                return 0;
        }
    }
}
