package Model;

import com.google.gson.annotations.Expose;

public class User {
    @Expose(serialize = true, deserialize = true)
    private String username;
    @Expose(serialize = true, deserialize = true)
    private String password;
    @Expose(serialize = true, deserialize = true)
    private String nickname;
    @Expose(serialize = true, deserialize = true)
    private Civilization civilization;
    @Expose(serialize = true, deserialize = true)
    private boolean isLoggedIn;
    @Expose(serialize = true, deserialize = true)
    private int score;

    public User(String username, String password, String nickname, boolean isLoggedIn, int score) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.isLoggedIn = isLoggedIn;
        this.score = score;
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
}
