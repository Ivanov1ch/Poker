package GUI;

import GameComponents.*;
import javax.swing.*;

/**
 * File:        PokerPanel.java
 * Description: The canvas on which Poker is run.
 * Created:     12/21/2018
 *
 * @author danIv
 * @version 1.0
 */

public class PokerPanel extends JPanel {
    private Deck deck;
    private PokerPlayer player;
    private ComputerPlayer computer;
    private GameRules gameRules;
    private Pot pot;
}
