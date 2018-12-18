import java.util.Arrays;

/**
 * File:        GameRules.java
 * Description: Scores hands and keeps track of the ante.
 * Created:     12/14/2018
 *
 * @author danIv
 * @version 1.0
 */

public class GameRules {
    private double ante;

    public GameRules(double ante) {
        this.ante = ante;
    }

    public void raiseAnte(double amount) {
        ante += amount;
    }

    // Returns 1 if hand1 is better or 2 if hand2 is better, or 0 if its a tie
    public int getBetterHand(Card[] hand1, Card[] hand2) {
        int[][] hand1Score = scoreHand(hand1), hand2Score = scoreHand(hand2);

        if (hand1Score[0][0] > hand2Score[0][0])
            return 1;
        else if (hand1Score[0][0] < hand2Score[0][0])
            return 2;
        else {
            // They are both cards of the same hand ranking, check total face value of involved cards and high card as failsafe

            if (hand1Score[0][0] == 0) {
                // Both have junk hands, check high cards
                if (hand1Score[0][1] > hand2Score[0][1])
                    return 1;
                else if (hand1Score[0][1] < hand2Score[0][1])
                    return 2;
                else
                    return 0;
            } else {
                // They both have "real" hands, compare the sum of the involved cards
                int hand1Sum = sumInvolvedCards(hand1Score), hand2Sum = sumInvolvedCards(hand2Score);

                if (hand1Sum > hand2Sum) {
                    return 1;
                } else if (hand1Sum < hand2Sum)
                    return 2;
                else {
                    // Check high cards as a last resort
                    if (hand1Score[0][1] > hand2Score[0][1])
                        return 1;
                    else if (hand1Score[0][1] < hand2Score[0][1])
                        return 2;
                    else
                        return 0;
                }
            }
        }
    }

    private int sumInvolvedCards(int[][] handScore) {
        int sum = 0;

        // Sum up the face values of all involved cards, ignoring the default -1s
        for (int faceValue : handScore[1]) {
            if (faceValue != -1)
                sum += faceValue;
        }

        return sum;
    }

    /* Scores the cards using the following format:
       [[Hand Ranking, High Card Face Value], [Face value of cards involved in hand]]
       Where Hand Ranking is an integer from 0 to 9, 0 being a hand with only a high card, and 9 being a royal flush

       FULL HAND LISTING:
        Royal Flush: 9
        Straight Flush: 8
        Four of a kind: 7
        Full house: 6
        Flush: 5
        Straight: 4
        Three of a kind: 3
        Two pair: 2
        One pair: 1
        High card: 0
     */
    public static int[][] scoreHand(Card[] cards) {
        int[][] retArray = new int[2][5];

        for (int i = 0; i < retArray.length; i++) {
            for (int j = 0; j < retArray[i].length; j++) {
                retArray[i][j] = -1;
            }
        }

        retArray[0][1] = getHighCard(cards);

        // FORMAT FOR is{hand type}(): [isPair (boolean), cards involved]
        if ((boolean) isRoyalFlush(cards)[0]) {
            retArray[0][0] = 9;
            setFaceValueArray(isRoyalFlush(cards), retArray);
        } else if ((boolean) isStraightFlush(cards)[0]) {
            retArray[0][0] = 8;
            setFaceValueArray(isStraightFlush(cards), retArray);
        } else if ((boolean) isFourOfAKind(cards)[0]) {
            retArray[0][0] = 7;
            setFaceValueArray(isFourOfAKind(cards), retArray);
        } else if ((boolean) isFullHouse(cards)[0]) {
            retArray[0][0] = 6;
            setFaceValueArray(isFullHouse(cards), retArray);
        } else if ((boolean) isFlush(cards)[0]) {
            retArray[0][0] = 5;
            setFaceValueArray(isFlush(cards), retArray);
        } else if ((boolean) isStraight(cards)[0]) {
            retArray[0][0] = 4;
            setFaceValueArray(isStraight(cards), retArray);
        } else if ((boolean) isThreeOfAKind(cards)[0]) {
            retArray[0][0] = 3;
            setFaceValueArray(isThreeOfAKind(cards), retArray);
        } else if ((boolean) isTwoPair(cards)[0]) {
            retArray[0][0] = 2;
            setFaceValueArray(isTwoPair(cards), retArray);
        } else if ((boolean) isPair(cards)[0]) {
            retArray[0][0] = 1;
            setFaceValueArray(isPair(cards), retArray);
        } else {
            retArray[0][0] = 0;
        }

        return retArray;
    }

    private static void setFaceValueArray(Object[] handCheckResult, int[][] retArray) {
        for (int i = 0; i < retArray[1].length; i++) {
            if (i < handCheckResult.length - 1)
                retArray[1][i] = (int) handCheckResult[i + 1];
        }
    }

    private static int getHighCard(Card[] cards) {
        int currentHigh = Integer.MIN_VALUE;

        for (Card card : cards) {
            if (card != null) {
                if (card.getFaceValue() > currentHigh) {
                    currentHigh = card.getFaceValue();
                }
            }
        }

        return currentHigh;
    }

    private static Object[] isRoyalFlush(Card[] cards) {
        Object[] straightFlush = isStraightFlush(cards);
        Object[] ret = {false, 0, 0, 0, 0, 0};

        if ((boolean) (straightFlush[0])) {
            Card[] temp = new Card[cards.length];

            System.arraycopy(cards, 0, temp, 0, cards.length);

            sortByFaceValue(temp);

            ret[0] = temp[0].getFaceValue() == 10 && temp[temp.length - 1].getFaceValue() == 14;
            for (int i = 1; i <= temp.length; i++) {
                ret[i] = temp[i - 1].getFaceValue();
            }
        }

        return ret;
    }

    private static Object[] isStraightFlush(Card[] cards) {
        Object[] isFlush = isFlush(cards), isStraight = isStraight(cards);
        Object[] ret = {false, 0, 0, 0, 0, 0};

        ret[0] = (boolean) (isFlush[0]) && (boolean) (isStraight[0]);
        for (int i = 1; i <= cards.length; i++) {
            ret[i] = cards[i - 1].getFaceValue();
        }

        return ret;
    }

    private static Object[] isFourOfAKind(Card[] cards) {
        Deck.fixCards(cards);
        Object[] hasPair = isPair(cards), hasThreeOfAKind = isThreeOfAKind(cards);
        Object[] ret = {false, 0};

        if ((boolean) hasPair[0] && (boolean) hasThreeOfAKind[0]) {
            int pairValue = (int) hasPair[1];

            for (int i = 0; i < cards.length; i++) {
                if (cards[i] != null) {
                    if (cards[i].getFaceValue() == pairValue) {
                        for (int j = 0; j < cards.length; j++) {
                            if (cards[j] != null && i != j && cards[j].getFaceValue() == pairValue) {
                                for (int k = 0; k < cards.length; k++) {
                                    if (cards[k] != null && k != i && k != j && cards[k].getFaceValue() == pairValue) {
                                        for (int l = 0; l < cards.length; l++) {
                                            if (cards[l] != null && l != k && l != j && l != i && cards[l].getFaceValue() == pairValue) {
                                                ret[0] = true;
                                                ret[1] = pairValue;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return ret;
    }

    private static Object[] isFullHouse(Card[] cards) {
        Deck.fixCards(cards);
        Object[] hasPair = isPair(cards), hasThreeOfAKind = isThreeOfAKind(cards);
        Object[] ret = {false, 0, 0};

        if ((boolean) hasPair[0] && (boolean) hasThreeOfAKind[0]) {
            // A three of a kind would still trigger this, so we must check that it is really a full house
            for (int i = 0; i < cards.length; i++) {
                for (int j = 0; j < cards.length; j++) {
                    if (i != j) {
                        // Check if removing the two cards at i and j still keeps a three of a kind
                        if (cards[i].getFaceValue() == cards[j].getFaceValue()) {
                            Card[] temp = new Card[cards.length - 2];

                            for (int k = 0; k < cards.length; k++) {
                                if (k != i && k != j) {
                                    temp[firstEmptyIndex(temp)] = cards[k];
                                }
                            }

                            if ((boolean) isThreeOfAKind(temp)[0]) {
                                // It's a full house
                                ret[0] = true;
                                ret[1] = temp[0].getFaceValue();
                                ret[2] = cards[i].getFaceValue();
                                return ret;
                            }
                        }
                    }
                }
            }
        }

        return ret;
    }

    private static Object[] isFlush(Card[] cards) {
        Deck.fixCards(cards);
        Object[] ret = {false, 0, 0, 0, 0, 0};
        if (cards[0] == null) return ret;

        Card.Suit firstSuit = cards[0].getSuit();

        for (int i = 1; i < cards.length; i++) {
            if (cards[i] != null && cards[i].getSuit() != firstSuit)
                return ret;
        }

        ret[0] = true;
        for (int i = 1; i <= cards.length; i++) {
            ret[i] = cards[i - 1].getFaceValue();
        }

        return ret;
    }

    private static Object[] isStraight(Card[] cards) {
        Deck.fixCards(cards);
        Card[] sortedCards = new Card[cards.length];
        System.arraycopy(cards, 0, sortedCards, 0, cards.length);

        sortByFaceValue(sortedCards);
        Object[] ret = {false, 0, 0, 0, 0, 0};

        int lastValue = sortedCards[0].getFaceValue();

        for (int i = 1; i < sortedCards.length; i++) {
            if (sortedCards[i].getFaceValue() != lastValue + 1)
                return ret;

            lastValue++;
        }

        ret[0] = true;

        for (int i = 1; i <= sortedCards.length; i++) {
            ret[i] = sortedCards[i - 1].getFaceValue();
        }

        return ret;
    }

    public static Object[] isThreeOfAKind(Card[] cards) {
        Deck.fixCards(cards);
        Object[] ret = {false, 0};

        for (int i = 0; i < cards.length; i++) {
            if (cards[i] != null) {
                int currentFaceValue = cards[i].getFaceValue();

                for (int j = 0; j < cards.length; j++) {
                    if (j != i) {
                        if (cards[j] != null) {
                            if (cards[j].getFaceValue() == currentFaceValue) {
                                for (int k = 0; k < cards.length; k++) {
                                    if (k != i && k != j) {
                                        if (cards[k] != null) {
                                            if (cards[k].getFaceValue() == currentFaceValue && cards[k].getFaceValue() == cards[j].getFaceValue()) {
                                                ret[0] = true;
                                                ret[1] = currentFaceValue;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return ret;
    }

    public static Object[] isTwoPair(Card[] cards) {
        Deck.fixCards(cards);
        Object[] ret = {false, 0, 0};

        Object[] hasPair = isPair(cards);

        if ((boolean) hasPair[0]) {
            int firstPair = (int) hasPair[1];

            Card[] temp = new Card[cards.length - 2];

            for (int i = 0; i < cards.length; i++) {
                if (cards[i].getFaceValue() != firstPair)
                    temp[firstEmptyIndex(temp)] = cards[i];
            }

            Object[] hasOtherPair = isPair(temp);
            if ((boolean) hasOtherPair[0]) {
                ret[0] = true;
                ret[1] = firstPair;
                ret[2] = hasOtherPair[1];
            }
        }

        return ret;
    }

    public static Object[] isPair(Card[] cards) {
        Deck.fixCards(cards);
        Object[] ret = {false, 0};

        for (int i = 0; i < cards.length; i++) {
            if (cards[i] != null) {
                int currentFaceValue = cards[i].getFaceValue();

                for (int j = 0; j < cards.length; j++) {
                    if (j != i) {
                        if (cards[j] != null) {
                            if (currentFaceValue == cards[j].getFaceValue()) {
                                ret[0] = true;
                                ret[1] = currentFaceValue;
                            }
                        }
                    }
                }
            }
        }

        return ret;
    }

    private static void sortByFaceValue(Card[] cards) {
        Deck.fixCards(cards);

        for (int i = 0; i < cards.length; i++) {
            for (int j = cards.length - 1; j > 0; j--) {
                if (cards[j] != null) {
                    if (cards[j - 1].getFaceValue() > cards[j].getFaceValue()) {
                        Card temp = cards[j - 1];
                        cards[j - 1] = cards[j];
                        cards[j] = temp;
                    }
                }
            }
        }
    }

    private static int firstEmptyIndex(Object[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                return i;
            }
        }

        return -1;
    }

    private static int firstEmptyIndex(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0) {
                return i;
            }
        }

        return -1;
    }

    public double getAnte() {
        return ante;
    }
}
