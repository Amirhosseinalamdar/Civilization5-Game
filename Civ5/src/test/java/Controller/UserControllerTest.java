package Controller;

import Model.User;
import View.Commands;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class UserControllerTest {

    @Test
    void registerUser1() {
        String input = "user create -u ehsan -n esi -p 1087";
        Matcher matcher = Pattern.compile(Commands.REGISTER1.getRegex(), Pattern.CASE_INSENSITIVE).matcher(input);
        matcher.matches();
        ArrayList<User> allUsers = null;
        UserController.setAllUsers(allUsers);
        String output = "user created successfully!";
        Assertions.assertEquals(output, UserController.registerUser(matcher));
    }

    @Test
    void registerUser2() {
        String input = "user create -u ehsan -n esi -p 1087";
        Matcher matcher = Pattern.compile(Commands.REGISTER1.getRegex(), Pattern.CASE_INSENSITIVE).matcher(input);
        matcher.matches();
        User user = new User("ehsan", "1087", "esi", false, 0);
        ArrayList<User> allUsers = new ArrayList<>();
        allUsers.add(user);
        UserController.setAllUsers(allUsers);
        String output = "user with username " + user.getUsername() + " already exists";
        Assertions.assertEquals(output, UserController.registerUser(matcher));
    }

    @Test
    void registerUser3() {
        String input = "user create -u ali -n alino -p 1234";
        Matcher matcher = Pattern.compile(Commands.REGISTER1.getRegex(), Pattern.CASE_INSENSITIVE).matcher(input);
        matcher.matches();
        User user = new User("ehsan", "1087", "alino", false, 0);
        ArrayList<User> allUsers = new ArrayList<>();
        allUsers.add(user);
        UserController.setAllUsers(allUsers);
        String output = "user with nickname " + user.getNickname() + " already exists";
        Assertions.assertEquals(output, UserController.registerUser(matcher));
    }

    @Test
    void registerUser4() {
        String input = "user create -u ali -n alino -p 1234";
        Matcher matcher = Pattern.compile(Commands.REGISTER1.getRegex(), Pattern.CASE_INSENSITIVE).matcher(input);
        matcher.matches();
        User user = new User("ehsan", "1087", "esi", false, 0);
        ArrayList<User> allUsers = new ArrayList<>();
        allUsers.add(user);
        UserController.setAllUsers(allUsers);
        String output = "user created successfully!";
        Assertions.assertEquals(output, UserController.registerUser(matcher));
    }

    @Test
    void logUserIn1() {
        String input = "user login -u ehsan -p 1087";
        Matcher matcher = Pattern.compile(Commands.LOGIN1.getRegex(), Pattern.CASE_INSENSITIVE).matcher(input);
        matcher.matches();
        User user1 = new User("hesi", "1234", "hesi", false, 0);
        User user2 = new User("ehsan", "1087", "esi", false, 0);
        ArrayList<User> allUsers = new ArrayList<>();
        allUsers.add(user1);
        allUsers.add(user2);
        UserController.setAllUsers(allUsers);
        String output = "user logged in successfully!";
        Assertions.assertEquals(output, UserController.logUserIn(matcher));
    }

    @Test
    void logUserIn2() {
        String input = "user login -u ehsan -p 108";
        Matcher matcher = Pattern.compile(Commands.LOGIN1.getRegex(), Pattern.CASE_INSENSITIVE).matcher(input);
        matcher.matches();
        User user = new User("ehsan", "1087", "esi", false, 0);
        ArrayList<User> allUsers = new ArrayList<>();
        allUsers.add(user);
        UserController.setAllUsers(allUsers);
        String output = "username and password didn't match!";
        Assertions.assertEquals(output, UserController.logUserIn(matcher));
    }

    @Test
    void logUserOut() {
        String output = "user logged out successfully!";
        Assertions.assertEquals(output, UserController.logUserOut());
    }

    @Test
    void startGame1() {
        User user1 = new User("ehsan", "1087", "esi", false, 0);
        ArrayList<User> allUsers = new ArrayList<>();
        allUsers.add(user1);
        UserController.setAllUsers(allUsers);
        UserController.setLoggedInUser(user1);
        ArrayList<String> usernames = new ArrayList<>();
        usernames.add("ehsan");
        Assertions.assertTrue(UserController.startGame(usernames) == null);
    }

    @Test
    void startGame2() {
        User user1 = new User("ehsan", "1087", "esi", false, 0);
        User user2 = new User("hesi", "1234", "hesi", false, 0);
        ArrayList<User> allUsers = new ArrayList<>();
        allUsers.add(user1);
        allUsers.add(user2);
        UserController.setAllUsers(allUsers);
        UserController.setLoggedInUser(user1);
        ArrayList<String> usernames = new ArrayList<>();
        usernames.add("hesi");
        Assertions.assertTrue(UserController.startGame(usernames).size() == 2 && UserController.startGame(usernames).get(0).equals(user1) && UserController.startGame(usernames).get(1).equals(user2));
    }

    @Test
    void startGame3() {
        User user1 = new User("ehsan", "1087", "esi", false, 0);
        ArrayList<User> allUsers = new ArrayList<>();
        allUsers.add(user1);
        UserController.setAllUsers(allUsers);
        UserController.setLoggedInUser(user1);
        ArrayList<String> usernames = new ArrayList<>();
        usernames.add("hossein");
        Assertions.assertTrue(UserController.startGame(usernames) == null);
    }

    @Test
    void changeNickname1() {
        String input = "profile change -n ehsun";
        Matcher matcher = Pattern.compile(Commands.CHANGE_NICKNAME.getRegex(), Pattern.CASE_INSENSITIVE).matcher(input);
        matcher.matches();
        User user = new User("ehsan", "1087", "esi", false, 0);
        ArrayList<User> allUsers = new ArrayList<>();
        allUsers.add(user);
        UserController.setAllUsers(allUsers);
        UserController.setLoggedInUser(user);
        String output = "nickname changed successfully!";
        Assertions.assertEquals(output, UserController.changeNickname(matcher));
    }

    @Test
    void changeNickname2() {
        String input = "profile change -n hesi";
        Matcher matcher = Pattern.compile(Commands.CHANGE_NICKNAME.getRegex(), Pattern.CASE_INSENSITIVE).matcher(input);
        matcher.matches();
        User user1 = new User("ehsan", "1087", "esi", false, 0);
        User user2 = new User("hesi", "1087", "hesi", false, 0);
        ArrayList<User> allUsers = new ArrayList<>();
        allUsers.add(user1);
        allUsers.add(user2);
        UserController.setAllUsers(allUsers);
        UserController.setLoggedInUser(user1);
        String output = "user with nickname " + user2.getNickname() + " already exists";
        Assertions.assertEquals(output, UserController.changeNickname(matcher));
    }

    @Test
    void changePassword1() {
        String input = "profile change -p -c 1234 -n 1234";
        Matcher matcher = Pattern.compile(Commands.CHANGE_PASSWORD1.getRegex(), Pattern.CASE_INSENSITIVE).matcher(input);
        matcher.matches();
        User user = new User("ehsan", "1087", "esi", false, 0);
        UserController.setLoggedInUser(user);
        String output = "current password is invalid";
        Assertions.assertEquals(output, UserController.changePassword(matcher));
    }

    @Test
    void changePassword2() {
        String input = "profile change -p -c 1087 -n 1087";
        Matcher matcher = Pattern.compile(Commands.CHANGE_PASSWORD1.getRegex(), Pattern.CASE_INSENSITIVE).matcher(input);
        matcher.matches();
        User user = new User("ehsan", "1087", "esi", false, 0);
        UserController.setLoggedInUser(user);
        String output = "please enter a new password";
        Assertions.assertEquals(output, UserController.changePassword(matcher));
    }

    @Test
    void changePassword3() {
        String input = "profile change -p -c 1087 -n 1234";
        Matcher matcher = Pattern.compile(Commands.CHANGE_PASSWORD1.getRegex(), Pattern.CASE_INSENSITIVE).matcher(input);
        matcher.matches();
        User user = new User("ehsan", "1087", "esi", false, 0);
        UserController.setLoggedInUser(user);
        String output = "password changed successfully!";
        Assertions.assertEquals(output, UserController.changePassword(matcher));
    }

    @Test
    void readDataFromJson1() {
        String fileName = "json.json";
        UserController.readDataFromJson(fileName);
    }

    @Test
    void readDataFromJson2() {
        String fileName = "xxx.json";
        UserController.readDataFromJson(fileName);
    }

    @Test
    void writeDataToJson1() {
        String fileName = "json.json";
        UserController.writeDataToJson(fileName);
    }

    @Test
    void writeDataToJson2() {
        String fileName = "xxx.json";
        UserController.writeDataToJson(fileName);
    }
}