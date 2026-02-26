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
            if (c.isAce()) aceCount++; // Dùng method mới thêm ở trên
        }

        // Tối ưu logic giảm điểm Ace
        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }

        return total;
    }

 // Trong file Hand.java
    @Override
    public String toString() {
        // Phải trả về danh sách các cards hiện có
        return cards.toString(); 
    }
}