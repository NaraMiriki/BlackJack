package client;

import common.Message;
import java.util.Scanner;

public class RunClient {

    public static void main(String[] args) throws Exception {

        GameClient client = new GameClient("localhost", 5555);
        Scanner sc = new Scanner(System.in);

        // Bắt đầu game
        client.sendMessage(new Message("START", null));

        while (!client.isGameOver()) {

            // 1. Chốt chặn đầu tiên: Đợi Server phản hồi (HAND hoặc RESULT)
            while (client.isWaiting && !client.isGameOver()) {
                Thread.sleep(100); 
            }

            // 2. Chốt chặn quan trọng nhất: 
            // Nếu tin nhắn trả về là RESULT (thắng/thua), gameOver đã thành true
            // thì thoát ngay, KHÔNG in menu nữa.
            if (client.isGameOver()) break;

            System.out.println("----------------");
            System.out.println("Lựa chọn của bạn:");
            System.out.println("1. HIT");
            System.out.println("2. STAND");

            // 3. Đọc dữ liệu: Dùng hasNextLine để an toàn
            if (sc.hasNextLine()) {
                String input = sc.nextLine();

                if (input.equals("1")) {
                    client.sendMessage(new Message("HIT", null));
                } else if (input.equals("2")) {
                    client.sendMessage(new Message("STAND", null));
                    // Lưu ý: Đừng break ở đây, để vòng lặp quay lên bước 1 
                    // đợi Server so bài rồi gửi RESULT về.
                }
            }
        }

        System.out.println("Cảm ơn bạn đã chơi Blackjack!");
        sc.close();
        client.closeConnection(); // Đóng kết nối OCSF
    }
}