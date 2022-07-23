package Server;

public enum Request {
    REGISTER("register"),
    LOGIN("login"),
    START("start"),
    LOGOUT("logout"),
    INVITE("invite"),
    ANSWER("answer"),
    YES("yes"),
    START_GAME("start"),
    INIT_GAME("init game"),
    NO("no"),
    GET_PLAYERS("get players"),
    LISTENER("listener"),
    NEXT_TURN("next turn"),
    YOUR_TURN("your turn");

    public String getString() {
        return request;
    }

    private final String request;

    Request(String request) {
        this.request = request;
    }
}
