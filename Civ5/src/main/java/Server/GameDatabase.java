package Server;

import Client.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class GameDatabase {
    private static ArrayList<User> allUsers = new ArrayList<>();
    private static HashMap<String, Socket> playerSockets = new HashMap<>();

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
        //TODO pas bede bazi kone baadi
        return "";
    }
    public static String allUsersToJson(){
        return new GsonBuilder().create().toJson(allUsers);
    }
    public static String runGlobal(ArrayList<String> args){
        if(args.get(1).equals("get all users")) return allUsersToJson();
        return "global typo";
    }
}
