package Server;

public enum Request {
    REGISTER("register"),
    LOGIN("login"),
    START("start"),
    LOGOUT("logout"),
    INVITE("invite");

    public String getString() {
        return request;
    }

    private String request;

    Request(String request) {
        this.request = request;
    }
}
