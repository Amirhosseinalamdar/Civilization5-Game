package Model;

public class User {
    private String username;
    private String password;
    private String nickname;
    private Civilization civilization;
    private boolean isLoggedIn;
    private int score;

    public User(String username, String password, String nickname, boolean isLoggedIn, int score) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.isLoggedIn = isLoggedIn;
        this.score = score;
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

    public int getScore() {
        return score;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
