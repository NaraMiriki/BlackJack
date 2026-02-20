package common;

import java.io.Serializable;
import java.util.ArrayList;

public class Hand implements Serializable {

    private ArrayList<Card> cards = new ArrayList<>();

    public void addCard(Card c) {
        cards.add(c);
    }

    public int getValue() {
        int total = 0;
        int aceCount = 0;

        for (Card c : cards) {
            total += c.getValue();
            if (c.toString().startsWith("A")) aceCount++;
        }

        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }

        return total;
    }

    public String toString() {
        return cards.toString();
    }
}