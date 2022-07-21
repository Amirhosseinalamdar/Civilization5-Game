package Server;

import Client.Model.User;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    public static String run(ArrayList<String> args, Socket socket) {
        String request = args.get(1);
        if (request.equals(Request.LOGIN.getString())) return logUserIn(args, socket);
        else if (request.equals(Request.REGISTER.getString())) return registerUser(args);
        return "typo in register or login";
    }

    private static String logUserIn(ArrayList<String> args, Socket socket) {// 2 username 3 password
        String username = args.get(2);
        String password = args.get(3);
        for (User allUser : GameDatabase.getAllUsers()) {
            if (allUser.getUsername().equals(username) && allUser.getPassword().equals(password)) {
                if (allUser.isLoggedIn()) return "error: already logged in!";
                allUser.setLoggedIn(true);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                allUser.setTime(dtf.format(now));
                GameDatabase.getPlayerSockets().put(allUser.getUsername(), socket);
                return "user logged in successfully!";
            }
        }
        return "error: username and password didn't match!";
    }

    private static String registerUser(ArrayList<String> args) {// 2 username 3 nickname 4 password
        String username = args.get(2);
        String nickname = args.get(3);
        String password = args.get(4);
        for (User allUser : GameDatabase.getAllUsers()) {
            if (allUser.getUsername().equals(username)) {
                return "error username: user with username " + username + " already exists";

            } else if (allUser.getNickname().equals(nickname)) {
                return "error nickname: user with nickname " + nickname + " already exists";
            }
        }
        User user = new User(username, password, nickname, false, 0);
        GameDatabase.getAllUsers().add(user);
//        writeDataToJson();
        return "user created successfully!";

    }

    public static void readDataFromJson() {
        try {
            String json = new String(Files.readAllBytes(Paths.get("json.json")));
            GameDatabase.setAllUsers(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(json, new TypeToken<List<User>>() {
            }.getType()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeDataToJson() {
        if (GameDatabase.getAllUsers() != null) {
            for (User allUser : GameDatabase.getAllUsers()) {
                allUser.setCivilization(null);
                allUser.setLoggedIn(false);
            }
        }
        try {
            String json = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(GameDatabase.getAllUsers());
            FileWriter fileWriter = new FileWriter("json.json");
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String logUserOut(ArrayList<String> args) {//2 username
        String username = args.get(2);
        User user;
        if((user = getUserByUsername(username)) != null) {
            user.setLoggedIn(false);
            GameDatabase.getPlayerSockets().remove(username);
            return "user logged out successfully!";
        }
        return "couldn't find the user";
    }
    public static User getUserByUsername(String username){
        for(User user: GameDatabase.getAllUsers()){
            if(user.getUsername().equals(username))
                return user;
        }
        return null;
    }
}
