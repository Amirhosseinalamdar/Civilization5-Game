package Server;

import Client.App.Main;
import Client.Model.Civilization;
import Client.Model.Game;
import Client.Model.User;
import Client.View.GameMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GameDatabase {
    private static ArrayList<User> allUsers = new ArrayList<>();
    private static HashMap<String, Socket> playerReaderSockets = new HashMap<>();
    private static HashMap<String, Socket> playerSockets = new HashMap<>();

    public static HashMap<String, Socket> getPlayerReaderSockets() {
        return playerReaderSockets;
    }

    public static void setAllUsers(ArrayList<User> allUsers) {
        GameDatabase.allUsers = allUsers;
    }

    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public static HashMap<String, Socket> getPlayerSockets() {
        return playerSockets;
    }

    public static String run(ArrayList<String> args) throws IOException {
        if (args.get(1).equals(Request.GET_PLAYERS.getString())) return getGamePlayers();
        else if (args.get(1).equals(Request.START_GAME.getString())) return startNewGame();
        else if (args.get(1).equals(Request.INIT_GAME.getString())) return initGame(args);
        else if (args.get(1).equals(Request.NEXT_TURN.getString())) return nextTurn(args);
        return "listo nadadam";
    }


    private static String initGame(ArrayList<String> args) {
        try{
            for (String s : playerSockets.keySet()) {
                if(s.equals(args.get(2))) continue;
                DataOutputStream dataOutputStream = new DataOutputStream(playerSockets.get(s).getOutputStream());
                ArrayList<String> response = new ArrayList<>(Arrays.asList(Menu.GAME.getMenuName()
                        ,Request.INIT_GAME.getString(),args.get(3)));
                String json = new Gson().toJson(response);
                SocketHandler.send(json,dataOutputStream);
            }
            return "init sent";
        }catch (IOException e){
            e.printStackTrace();
        }
        return "nafrestadam";
    }

    private static String startNewGame() {
        ArrayList<User> users = new ArrayList<>();
        for (String s : playerSockets.keySet()) {
            users.add(UserController.getUserByUsername(s));
        }
        if(users.size() <= 1){
            return "no selected players";
        }else{
//            if (Main.music.isPlaying()) Main.playSound("Game.mp3");//todo
            GameMenu.setMapSize(20);//todo
            GameMenu.setAutoSaveDuration(1);//todo
            GameMenu.startGame(users, new Scanner(System.in), -1,"");
            return "game started";
        }
    }


    private static String getGamePlayers() {//username //password //nickname //score
        ArrayList <User> realUsers = new ArrayList<>();
        for (String s : playerSockets.keySet()) realUsers.add(UserController.getUserByUsername(s));
        realUsers.sort(Comparator.comparing(User::getUsername).thenComparing(User::getNickname));
        ArrayList<String> users = new ArrayList<>();
        for (User u : realUsers) {
            users.add(u.getUsername());
            users.add(u.getPassword());
            users.add(u.getNickname());
            users.add(u.getScore()+"");
        }
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(users);
    }

    public static String allUsersToJson(){
        System.out.println("json-ing...");
        ArrayList <String> usernames = new ArrayList<>();
        for (User user : allUsers)
            if (user.isLoggedIn()) usernames.add(user.getUsername());
        return new Gson().toJson(usernames);
    }
    public static String runGlobal(ArrayList<String> args, Socket readerSocket){
        if(args.get(1).equals("get all users")) return allUsersToJson();
        else if (args.get(1).equals("listener")) {
            playerReaderSockets.put(args.get(2), readerSocket);
            return "listened";
        }
        return "global typo";
    }

    private static String nextTurn (ArrayList <String> args) throws IOException {
        System.out.println(args.get(2) + " has nexted turn");
        String lastPlayer = args.get(2), gameJson = args.get(3), firstUsername = "null";
        boolean haveFound = false;
        for (String s : playerSockets.keySet()) {
            System.out.println("checking if next is " + s);
            if (firstUsername.equals("null")) firstUsername = s;
            if (haveFound) {
                System.out.println("giving turn to " + s);
                DataOutputStream outputStream = new DataOutputStream(playerSockets.get(s).getOutputStream());
                SocketHandler.send(new Gson().toJson(new ArrayList<>(Arrays.asList("game", "your turn", gameJson))),outputStream);
                return "";
            }
            if (s.equals(lastPlayer)) haveFound = true;
        }
        if (haveFound) {
            System.out.println("couldn't find easily lol, sending to " + firstUsername);
            DataOutputStream outputStream = new DataOutputStream(playerSockets.get(firstUsername).getOutputStream());
            SocketHandler.send(new Gson().toJson(new ArrayList<>(Arrays.asList("game", "your turn", gameJson))), outputStream);
            return "";
        }
        System.out.println("returning next turn invalid...");
        return "next turn invalid";
    }
}