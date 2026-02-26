package client;

import common.*;

public class GameClient extends AbstractClient {
    // Chỉ cần 2 biến này là đủ
    public boolean isWaiting = false; 
    private boolean gameOver = false; 

    public GameClient(String host, int port) throws Exception {
        super(host, port);
        openConnection(); // Mở kết nối ngay khi khởi tạo
    }
    
    // Accessor để bên ngoài (RunClient) kiểm tra
    public boolean isGameOver() {
        return gameOver;
    }
    
    public void setGameOver(boolean status) {
        this.gameOver = status;
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        Message message = (Message) msg;

        if (message.getType().equals("HAND")) {
            Hand hand = (Hand) message.getData();
            System.out.println("Bài của bạn: " + hand + " = " + hand.getValue());
            this.isWaiting = false; // Nhận được bài rồi, không phải đợi nữa
        } 
        else if (message.getType().equals("RESULT")) {
            System.out.println(message.getData());
            this.gameOver = true;   // Chốt hạ ván đấu
            this.isWaiting = false; // Ngừng đợi
        }
    }

    public void sendMessage(Message msg) throws Exception {
        this.isWaiting = true; // Đánh dấu bắt đầu đợi phản hồi từ Server
        sendToServer(msg);     // Gửi dữ liệu đi thông qua OCSF
    }
}