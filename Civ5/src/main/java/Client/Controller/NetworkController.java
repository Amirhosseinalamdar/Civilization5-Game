package Client.Controller;

import Client.App.Main;
import Server.Menu;
import Server.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetworkController {
    private static Socket socket;
    private static Socket readerSocket;
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
    public static void listenForUpdates() throws IOException {
        readerSocket = new Socket("localhost", 8000);
        DataInputStream inputStream = new DataInputStream(readerSocket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(readerSocket.getOutputStream());
        ArrayList <String> args = new ArrayList<>(Arrays.asList("global", "listener", Main.username));
        outputStream.writeUTF(new Gson().toJson(args));
        outputStream.flush();
        new Thread(() -> {
            while (true) {
                try {
                    System.out.println("waiting for updates...");
                    String response = inputStream.readUTF();
                    System.out.println("update received, response = " + response);
                    if (response.startsWith("{") || response.startsWith("["))
                        handleOptions(response);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private static void handleOptions (String response) throws IOException {
        try {
            ArrayList<String> args = new Gson().fromJson(response, new TypeToken<List<String>>() {
            }.getType());
            if (args.get(1).equals(Request.INVITE.getString())) {
                dataOutputStream.writeUTF(new Gson().toJson(new ArrayList<>(Arrays.asList("game menu", "answer",
                        args.get(2),Main.username, "yes")))); //TODO
                dataOutputStream.flush();
                System.out.println(dataInputStream.readUTF());
            }
        }
        catch (Exception e) {
            System.out.println("response is not arrayList");
            e.printStackTrace();
        }
    }
}
