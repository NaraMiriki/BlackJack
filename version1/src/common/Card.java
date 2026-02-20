package common;

import java.io.Serializable;

public class Card implements Serializable {

    private String rank;
    private String suit;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public int getValue() {
        if (rank.equals("A")) return 11;
        if (rank.equals("K") || rank.equals("Q") || rank.equals("J")) return 10;
        return Integer.parseInt(rank);
    }

    public String toString() {
        return rank + suit;
    }
}