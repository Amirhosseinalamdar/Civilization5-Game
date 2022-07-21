package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int turn;
    public int getTurn() {
        return turn;
    }
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(SocketHandler.PORT);
        while (true){
            Socket socket = serverSocket.accept();
            SocketHandler socketHandler  = new SocketHandler(socket);
            socketHandler.start();
        }
    }
    public void increaseTurn(){
        turn++;
    }
}
