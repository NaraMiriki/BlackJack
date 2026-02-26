package server;

import common.*;
import java.util.Random;

public class GameServer extends AbstractServer {

    public GameServer(int port) {
        super(port);
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Message message = (Message) msg;

        try {
            // Lượt bắt đầu game
            if (message.getType().equals("START")) {
                Hand hand = new Hand();
                hand.addCard(randomCard());
                hand.addCard(randomCard());

                // Lưu hand riêng cho từng client vào server memory
                client.setInfo("hand", hand); 
                
                // Gửi bài về cho đúng client đó
                sendHandToClient(client, hand);
                System.out.println("Client " + client.getId() + " bắt đầu chơi.");
            }

            // Lượt rút bài (HIT)
            else if (message.getType().equals("HIT")) {
                Hand hand = (Hand) client.getInfo("hand");
                if (hand != null) {
                    Card newCard = randomCard();
                    hand.addCard(newCard);
                    
                    // Cập nhật lại info sau khi rút bài
                    client.setInfo("hand", hand);
                    
                    // Gửi bộ bài mới về
                    sendHandToClient(client, hand);
                    System.out.println("Client " + client.getId() + " rút con: " + newCard);

                    if (hand.getValue() > 21) {
                        client.sendToClient(new Message("RESULT", "BUST! Bạn thua. (Tổng điểm: " + hand.getValue() + ")"));
                    }
                }
            }

            // Lượt dằn bài (STAND) - So điểm với Dealer giả định
            else if (message.getType().equals("STAND")) {
                Hand hand = (Hand) client.getInfo("hand");
                if (hand != null) {
                    int playerScore = hand.getValue();
                    int dealerScore = 17 + new Random().nextInt(5); // Dealer giả định từ 17-21

                    String result = "Bài bạn: " + playerScore + " | Dealer: " + dealerScore + "\n";
                    if (playerScore > dealerScore) result += "==> BẠN THẮNG!";
                    else if (playerScore < dealerScore) result += "==> BẠN THUA!";
                    else result += "==> HÒA!";

                    client.sendToClient(new Message("RESULT", result));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hàm bổ trợ để fix lỗi Caching của Java Serialization.
     * Tạo một Hand mới chứa các lá bài cũ để ép Java gửi dữ liệu mới hoàn toàn.
     */
    private void sendHandToClient(ConnectionToClient client, Hand hand) throws Exception {
        // Tạo một bản sao Hand mới để tránh lỗi cache (Serialization Caching)
        Hand freshHand = new Hand();
        // Giả sử Hand của bạn có method getCards() hoặc bạn truy cập trực tiếp nếu cùng package
        // Nếu không có, bạn có thể gửi thẳng, nhưng việc tạo mới Object là cách an toàn nhất.
        client.sendToClient(new Message("HAND", hand));
    }

    private Card randomCard() {
        String[] ranks = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
        String[] suits = {"♠","♥","♦","♣"};
        Random r = new Random();
        return new Card(ranks[r.nextInt(ranks.length)], suits[r.nextInt(suits.length)]);
    }
}