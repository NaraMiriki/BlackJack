package client;

import common.*;
import ocsf.client.AbstractClient;

public class GameClient extends AbstractClient {

    public GameClient(String host, int port) throws Exception {
        super(host, port);
        openConnection();
    }

    @Override
    protected void handleMessageFromServer(Object msg) {

        Message message = (Message) msg;

        if (message.getType().equals("HAND")) {
            Hand hand = (Hand) message.getData();
            System.out.println("Bài của bạn: " + hand + " = " + hand.getValue());
        }

        else if (message.getType().equals("RESULT")) {
            System.out.println(message.getData());
        }
    }

    public void sendMessage(Message msg) throws Exception {
        sendToServer(msg);
    }
}