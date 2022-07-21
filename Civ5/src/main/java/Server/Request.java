package Server;

public enum Request {
    REGISTER("register"),
    LOGIN("login"),
    START("start"),
    LOGOUT("logout"),
    INVITE("invite"),
    ANSWER("answer"),
    LISTENER("listener");

    public String getString() {
        return request;
    }

    private final String request;

    Request(String request) {
        this.request = request;
    }
}
