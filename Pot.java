/**
 * File:        Pot.java
 * Description: The poker game's pot.
 * Created:     12/14/2018
 *
 * @author danIv
 * @version 1.0
 */

public class Pot {
    private double total;

    public void update(double amount) {
        total += amount;
    }

    public double payOut() {
        double temp = total;
        total = 0;
        return temp;
    }

    public double getTotal() {
        return total;
    }
}
