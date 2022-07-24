package Client.Controller;

import Client.App.Main;
import Client.View.GameMenu;
import Server.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

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
                Platform.runLater(() -> {
                    setInvitationPopup(args.get(2));
                });
//                sendFromListener(new Gson().toJson(new ArrayList<>(Arrays.asList("game menu", "answer",
//                        args.get(2),Main.username, "yes"))),dataOutputStream);//TODO
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
    private static void setInvitationPopup (String inv) {
        Label inviter = new Label(inv + " has invited you"), yes = new Label("Accept"), no = new Label("Refuse");
        inviter.getStylesheets().add("css/GamePageStyle.css");
        inviter.getStyleClass().add("startGameText");
        inviter.setLayoutX(100); inviter.setLayoutY(50);
        yes.getStylesheets().add("css/GamePageStyle.css");
        yes.getStyleClass().add("startGameButton");
        HBox box = new HBox(30);
        box.setLayoutX(110); box.setLayoutY(170);
        no.getStylesheets().add("css/GamePageStyle.css");
        no.getStyleClass().add("startGameButton");
        inviter.setAlignment(Pos.CENTER);
        inviter.setTextAlignment(TextAlignment.CENTER);
        yes.setAlignment(Pos.CENTER);
        yes.setTextAlignment(TextAlignment.CENTER);
        no.setAlignment(Pos.CENTER);
        no.setTextAlignment(TextAlignment.CENTER);
        box.getChildren().addAll(yes, no);
        Rectangle rectangle = new Rectangle(); rectangle.setFill(Color.rgb(0,0,0,0.5));
        rectangle.setArcWidth(20); rectangle.setArcHeight(20); rectangle.setWidth(Integer.max((inv.length() + 1) * 50, 400));
        rectangle.setHeight(300);
        Pane pane = (Pane)Main.scene.getRoot();
        yes.setOnMouseClicked(event -> {
            sendFromListener(new Gson().toJson(new ArrayList<>(Arrays.asList("game menu", "answer",
                    inv,Main.username, "yes"))),dataOutputStream);
            pane.getChildren().removeAll(box, inviter, rectangle);
        });
        no.setOnMouseClicked(event -> {
            sendFromListener(new Gson().toJson(new ArrayList<>(Arrays.asList("game menu", "answer",
                    inv,Main.username, "no"))),dataOutputStream);
            pane.getChildren().removeAll(box, inviter, rectangle);
        });
        pane.getChildren().addAll(rectangle, inviter, box);
    }
}
