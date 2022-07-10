package Model;

import com.google.gson.annotations.Expose;

import java.util.Random;

public class User {
    @Expose(serialize = true, deserialize = true)
    private String username;
    @Expose(serialize = true, deserialize = true)
    private String password;
    @Expose(serialize = true, deserialize = true)
    private String nickname;
    @Expose(serialize = true, deserialize = true)
    private String iconAddress;
    @Expose(serialize = true, deserialize = true)
    private Civilization civilization;
    @Expose(serialize = true, deserialize = true)
    private boolean isLoggedIn;
    @Expose(serialize = true, deserialize = true)
    private int score;
    @Expose(serialize = true, deserialize = true)
    private String time;

    public User(String username, String password, String nickname, boolean isLoggedIn, int score) {
        Random random = new Random();
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.iconAddress = this.getClass().getResource("/pictures/Icons/" + random.nextInt(5) + ".png").toExternalForm();
        this.isLoggedIn = isLoggedIn;
        this.score = score;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void newCivilization() {
        this.civilization = new Civilization();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setIconAddress(String iconAddress) {
        this.iconAddress = iconAddress;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getIconAddress() {
        return iconAddress;
    }

    public int getScore() {
        return score;
    }

    public String getTime() {
        return time;
    }

    public Civilization getCivilization() {
        return civilization;
    }
}
