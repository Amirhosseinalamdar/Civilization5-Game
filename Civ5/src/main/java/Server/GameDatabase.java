package Server;

import Client.Model.ImageBase;
import Client.Model.Map.TerrainType;
import Client.Model.Technology;
import Client.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.xml.crypto.Data;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GameDatabase {
    private static ArrayList<User> allUsers = new ArrayList<>();
    private static HashMap<String, Socket> playerSockets = new HashMap<>();
    private static HashMap<String, Socket> playerReaderSockets = new HashMap<>();

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
        //TODO pas bede bazi kone baadi
        return "";
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