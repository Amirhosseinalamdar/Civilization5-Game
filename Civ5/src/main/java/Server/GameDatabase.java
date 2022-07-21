package Server;

import Client.App.Main;
import Client.Model.Civilization;
import Client.Model.Game;
import Client.Model.User;
import Client.View.GameMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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

    public static String run(ArrayList<String> args){
        if(args.get(1).equals(Request.GET_PLAYERS.getString())) return getGamePlayers();
        else if(args.get(1).equals(Request.START_GAME.getString())) return startNewGame();
        return "listo nadadam";
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
            GameMenu.startGame(users, new Scanner(System.in), -1);
            return "game started";
        }
    }


    private static String getGamePlayers() {
        ArrayList<User> users = new ArrayList<>();
        for (String s : playerSockets.keySet()) {
            UserController.getUserByUsername(s).setCivilization(null);
            users.add(UserController.getUserByUsername(s));
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

}