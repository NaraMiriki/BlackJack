package server;

public class RunServer {

    public static void main(String[] args) throws Exception {

        GameServer server = new GameServer(5555);
        server.listen();

        System.out.println("Server đang chạy...");
    }
}