package Client.Controller;

import Client.App.Main;
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
import java.util.*;
import java.util.stream.Collectors;

public class UserController {
    private static ArrayList<User> allUsers;
    private static User loggedInUser;


    public static void setAllUsers(ArrayList<User> allUsers) {
        UserController.allUsers = allUsers;
    }

    public static void setLoggedInUser(User loggedInUser) {
        UserController.loggedInUser = loggedInUser;
    }

    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static String registerUser(String username, String nickname, String password) {
        ArrayList<String> strings = new ArrayList<>(Arrays.asList("login menu","register",username,nickname,password));
        return NetworkController.send(strings);
    }

    public static String logUserIn(String username, String password) {
        ArrayList<String> strings = new ArrayList<>(Arrays.asList("login menu","login",username,password));
        Main.username = username;
        return NetworkController.send(strings);
    }

    public static String logUserOut() {
        ArrayList<String> strings = new ArrayList<>(Arrays.asList("main menu","logout",Main.username));
        return NetworkController.send(strings);
//        loggedInUser.setLoggedIn(false);
//        loggedInUser = null;
//        return "user logged out successfully!";
    }

    public static ArrayList<User> startGame(ArrayList<String> usernames) {
        ArrayList<User> players = new ArrayList<>();
        for (String username : usernames) {
            if (getUserByUsername(username.trim()) == null || getUserByUsername(username.trim()).equals(loggedInUser))
                return null;
            else players.add(getUserByUsername(username.trim()));
        }
        players.add(0, loggedInUser);
        return players;
    }

    private static User getUserByUsername(String username) {
        for (User allUser : allUsers) {
            if (allUser.getUsername().equals(username)) return allUser;
        }
        return null;
    }

    private static boolean isNicknameExist(String nickname) {
        for (User allUser : allUsers) {
            if (allUser.getNickname().equals(nickname)) return true;
        }
        return false;
    }

    public static String changeNickname(String newNickname) {
        String output;
        if (isNicknameExist(newNickname))
            output = "user with nickname " + newNickname + " already exists";
        else {
            output = "nickname changed successfully!";
            loggedInUser.setNickname(newNickname);
        }
        return output;
    }

    public static String changePassword(String currentPassword, String newPassword) {
        String output;
        if (!loggedInUser.getPassword().equals(currentPassword)) output = "error current: current password is invalid";
        else if (loggedInUser.getPassword().equals(newPassword)) output = "error new: please enter a new password";
        else {
            output = "password changed successfully!";
            loggedInUser.setPassword(newPassword);
        }
        return output;
    }

    public static ArrayList<User> getBestUsers() {
        ArrayList<User> sorted = new ArrayList<>();
        for (User allUser : allUsers) {
            if (allUser.getTime() != null) sorted.add(allUser);
        }
        Comparator<User> comparator = Comparator.comparing(User::getScore).reversed().thenComparing(User::getTime);
        if (!sorted.isEmpty()) {
            sorted = (ArrayList<User>) sorted.stream().sorted(comparator).collect(Collectors.toList());
            sorted.sort(comparator);
        }
        return sorted;
    }

    public static void readDataFromJson() {
        try {
            String json = new String(Files.readAllBytes(Paths.get("json.json")));
            allUsers = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(json, new TypeToken<List<User>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeDataToJson() {
        if (allUsers != null) {
            for (User allUser : allUsers) {
                allUser.setCivilization(null);
                allUser.setLoggedIn(false);
            }
        }
        try {
            String json = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(allUsers);
            FileWriter fileWriter = new FileWriter("json.json");
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
