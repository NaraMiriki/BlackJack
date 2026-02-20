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

            if (message.getType().equals("START")) {

                Hand hand = new Hand();
                hand.addCard(randomCard());
                hand.addCard(randomCard());

                client.setInfo("hand", hand);
                client.sendToClient(new Message("HAND", hand));
            }

            else if (message.getType().equals("HIT")) {

                Hand hand = (Hand) client.getInfo("hand");
                hand.addCard(randomCard());

                client.sendToClient(new Message("HAND", hand));

                if (hand.getValue() > 21) {
                    client.sendToClient(new Message("RESULT", "BUST! Bạn thua."));
                }
            }

            else if (message.getType().equals("STAND")) {

                Hand hand = (Hand) client.getInfo("hand");

                if (hand.getValue() <= 21) {
                    client.sendToClient(new Message("RESULT", "Bạn dừng với điểm: " + hand.getValue()));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Card randomCard() {
        String[] ranks = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
        String[] suits = {"♠","♥","♦","♣"};

        Random r = new Random();
        return new Card(
                ranks[r.nextInt(ranks.length)],
                suits[r.nextInt(suits.length)]
        );
    }
}