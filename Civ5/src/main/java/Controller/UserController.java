package Controller;

import Model.User;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class UserController {
    private static ArrayList<User> allUsers = new ArrayList<>();
    private static User loggedInUser;

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static String registerUser(Matcher matcher){
        String username = matcher.group("username");
        String nickname = matcher.group("nickname");
        String password = matcher.group("password");
        String output = "";
        for (User allUser : allUsers) {
            if (allUser.getUsername().equals(username)) {
                output = "user with username " + username + " already exists";
                break;
            }
            else if (allUser.getNickname().equals(nickname)) {
                output = "user with nickname " + nickname + " already exists";
                break;
            }
        }
        if (output.isEmpty()){
            output = "user created successfully!";
            User user = new User(username, password, nickname, false, 0);
            allUsers.add(user);
        }
        return output;
    }

    public static String logUserIn(Matcher matcher){
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
        if (output.isEmpty()) output = "Username and password didn't match!";
        return output;
    }

    public static String logUserOut(){
        loggedInUser = null;
        return "user logged out successfully!";
    }


}
