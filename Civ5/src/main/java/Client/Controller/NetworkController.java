package Client.Controller;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkController {
    private static Socket socket;
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;

    public static boolean connect() {
        try {
            socket = new Socket("localhost", 8000);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            return true;
        } catch (IOException e) {
            System.out.println("Connection to server is refused");
            return false;
        }
    }
    public static String send(ArrayList<String> args) {
        try {
            dataOutputStream.writeUTF(new Gson().toJson(args));
            dataOutputStream.flush();
            return dataInputStream.readUTF();
        } catch (IOException e) {
            System.out.println("connection reset");
        }
        return "no response from server";
    }

}
