package Controller;

import Model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class UserController {
    private static ArrayList<User> allUsers;
    private static User loggedInUser;

    public static void setAllUsers(ArrayList<User> allUsers) {
        UserController.allUsers = allUsers;
    }

    public static void setLoggedInUser(User loggedInUser) {
        UserController.loggedInUser = loggedInUser;
    }

    public static String registerUser(Matcher matcher) {
        String username = matcher.group("username");
        String nickname = matcher.group("nickname");
        String password = matcher.group("password");
        String output = "";
        if (allUsers == null) allUsers = new ArrayList<>();
        else {
            for (User allUser : allUsers) {
                if (allUser.getUsername().equals(username)) {
                    output = "user with username " + username + " already exists";
                    break;
                } else if (allUser.getNickname().equals(nickname)) {
                    output = "user with nickname " + nickname + " already exists";
                    break;
                }
            }
        }
        if (output.isEmpty()) {
            output = "user created successfully!";
            User user = new User(username, password, nickname, false, 0);
            allUsers.add(user);
        }
        return output;
    }

    public static String logUserIn(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        String output = "";
        for (User allUser : allUsers) {
            if (allUser.getUsername().equals(username) && allUser.getPassword().equals(password)) {
                output = "user logged in successfully!";
                loggedInUser = allUser;
                break;
            }
        }
        if (output.isEmpty()) output = "username and password didn't match!";
        return output;
    }

    public static String logUserOut() {
        loggedInUser = null;
        return "user logged out successfully!";
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

    public static String changeNickname(Matcher matcher) {
        String newNickname = matcher.group("nickname");
        String output;
        if (isNicknameExist(newNickname))
            output = "user with nickname " + newNickname + " already exists";
        else {
            output = "nickname changed successfully!";
            loggedInUser.setNickname(newNickname);
        }
        return output;
    }

    public static String changePassword(Matcher matcher) {
        String currentPassword = matcher.group("currentPassword");
        String newPassword = matcher.group("newPassword");
        String output;
        if (!loggedInUser.getPassword().equals(currentPassword)) output = "current password is invalid";
        else if (loggedInUser.getPassword().equals(newPassword)) output = "please enter a new password";
        else {
            output = "password changed successfully!";
            loggedInUser.setPassword(newPassword);
        }
        return output;
    }

    public static void readDataFromJson(String fileName) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(fileName)));
            allUsers = new Gson().fromJson(json, new TypeToken<List<User>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeDataToJson(String fileName) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(new Gson().toJson(allUsers));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
