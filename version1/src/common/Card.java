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
        // Dùng switch-case sẽ an toàn và sạch sẽ hơn
        switch (rank) {
            case "A": return 11;
            case "K": case "Q": case "J": return 10;
            default: 
                try {
                    return Integer.parseInt(rank);
                } catch (NumberFormatException e) {
                    return 0; // Hoặc xử lý lỗi tùy bạn
                }
        }
    }
    
    public boolean isAce() {
        return "A".equals(rank);
    }

    public String toString() {
        return rank + suit;
    }
}