package Client.Controller;

import Client.App.Main;
import Client.View.GameMenu;
import Server.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
            byte[] data = (new Gson().toJson(args)).getBytes(StandardCharsets.UTF_8);
            dataOutputStream.writeInt(data.length);
            dataOutputStream.write(data);
            dataOutputStream.flush();
            int length = dataInputStream.readInt();
            byte[] data2 = new byte[length];
            dataInputStream.readFully(data2);
            return new String(data2,StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("connection reset");
            e.printStackTrace();
        }
        return "no response from server";
    }
//    public static String decode(String coded) {
//        try{
//            byte[] bytes = Base64.getDecoder().decode(coded);
//            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bytes));
//            Scanner scanner = new Scanner(gis, "UTF-8");
//            String json = scanner.next();
//            scanner.close();
//            return json;
//        }catch(Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }

    public static String getResponse(DataInputStream dataInputStream){
        try {
            byte[] data = new byte[dataInputStream.readInt()];
            dataInputStream.readFully(data);
            return new String(data,StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "javab nadad";
    }
    public static void listenForUpdates() throws IOException {
        readerSocket = new Socket("localhost", 8000);
        DataInputStream inputStream = new DataInputStream(readerSocket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(readerSocket.getOutputStream());
        ArrayList <String> args = new ArrayList<>(Arrays.asList("global", "listener", Main.username));
        sendFromListener(new Gson().toJson(args),outputStream);
        new Thread(() -> {
            while (true) {
                try {
                    System.out.println("waiting for updates...");
                    String response = getResponse(inputStream);
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
    public static void sendFromListener(String input,DataOutputStream dataOutputStream){
        try {
            byte[] data = input.getBytes(StandardCharsets.UTF_8);
            dataOutputStream.writeInt(data.length);
            dataOutputStream.write(data);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void handleOptions (String response) throws IOException {
        try {
            ArrayList<String> args = new Gson().fromJson(response, new TypeToken<List<String>>() {
            }.getType());
            if (args.get(1).equals(Request.INVITE.getString())) {
                sendFromListener(new Gson().toJson(new ArrayList<>(Arrays.asList("game menu", "answer",
                        args.get(2),Main.username, "yes"))),dataOutputStream);//TODO
            }else if(args.get(1).equals(Request.INIT_GAME.getString())){
                String json = args.get(2);
                GameMenu.receiveGame(json);
            }else if (args.get(1).equals(Request.YOUR_TURN.getString())) {
                String json = args.get(2);
                GameMenu.receiveGame(json);
            }
        }
        catch (Exception e) {
            System.out.println("response is not arrayList");
            System.out.println(response);
            e.printStackTrace();
        }
    }
}
