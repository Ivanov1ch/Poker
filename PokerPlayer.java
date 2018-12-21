import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * File:        PokerPlayer.java
 * Description: A class representing a Poker Player.
 * Created:     12/14/2018
 *
 * @author danIv
 * @version 1.0
 */

public class PokerPlayer extends CardPlayer {
    private double balance;
    private boolean folded, allIn;

    public PokerPlayer(String name, int max_size, double balance) {
        super(name, max_size);
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void addToBalance(double amount) {
        balance += amount;
        balance = round(balance, 2);
    }

    public void removeFromBalance(double amount) {
        balance -= amount;
        balance = round(balance, 2);
    }

    public boolean hasFolded() {
        return folded;
    }

    public void setFolded(boolean folded) {
        this.folded = folded;
    }

    public boolean isAllIn() {
        return allIn;
    }

    public void setAllIn(boolean allIn) {
        this.allIn = allIn;
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
