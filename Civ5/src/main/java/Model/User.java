package Model;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String nickname;
    private Civilization civilization;
    private boolean isLoggedIn;
    private int score;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public Civilization getCivilization() {
        return civilization;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public int getScore() {
        return score;
    }
}
