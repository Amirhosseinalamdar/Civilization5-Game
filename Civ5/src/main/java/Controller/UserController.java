package Controller;

import Model.Game;
import Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class UserController {
    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    private static ArrayList<User> allUsers;
    private static User loggedInUser;

    public static void setAllUsers(ArrayList<User> allUsers) {
        UserController.allUsers = allUsers;
    }

    public static void setLoggedInUser(User loggedInUser) {
        UserController.loggedInUser = loggedInUser;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static String registerUser(String username, String nickname, String password) {
        String output = "";
        if (allUsers == null) allUsers = new ArrayList<>();
        else {
            for (User allUser : allUsers) {
                if (allUser.getUsername().equals(username)) {
                    output = "error username: user with username " + username + " already exists";
                    break;
                } else if (allUser.getNickname().equals(nickname)) {
                    output = "error nickname: user with nickname " + nickname + " already exists";
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

    public static String logUserIn(String username, String password) {
        String output = "";
        for (User allUser : allUsers) {
            if (allUser.getUsername().equals(username) && allUser.getPassword().equals(password)) {
                output = "user logged in successfully!";
                loggedInUser = allUser;
                allUser.setLoggedIn(true);
                break;
            }
        }
        if (output.isEmpty()) output = "error: username and password didn't match!";
        return output;
    }

    public static String logUserOut() {
        loggedInUser.setLoggedIn(false);
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
        ArrayList<User> sorted = new ArrayList<>(allUsers);
        if (loggedInUser.getUsername().equals("guest")) sorted.add(loggedInUser);
        Comparator<User> comparator = Comparator.comparing(User::getScore).reversed().thenComparing(User::getTime);
        sorted = (ArrayList<User>) sorted.stream().sorted(comparator).collect(Collectors.toList());
        sorted.sort(comparator);
        return sorted;
    }

    public static void readDataFromJson() {
        try {
            String json = new String(Files.readAllBytes(Paths.get("json.json")));
            allUsers = new Gson().fromJson(json, new TypeToken<List<User>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeDataToJson() {
        for (User allUser : allUsers) {
            allUser.setCivilization(null);
            allUser.setLoggedIn(false);
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
