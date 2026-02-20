package client;

import common.Message;
import java.util.Scanner;

public class RunClient {

    public static void main(String[] args) throws Exception {

        GameClient client = new GameClient("localhost", 5555);
        Scanner sc = new Scanner(System.in);

        client.sendMessage(new Message("START", null));

        while (true) {

            System.out.println("1. HIT");
            System.out.println("2. STAND");

            String input = sc.nextLine();

            if (input.equals("1")) {
                client.sendMessage(new Message("HIT", null));
            }
            else if (input.equals("2")) {
                client.sendMessage(new Message("STAND", null));
                break;
            }
        }

        sc.close();
    }
}