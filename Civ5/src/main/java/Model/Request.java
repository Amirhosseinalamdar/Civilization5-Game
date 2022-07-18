package Model;

import java.util.HashMap;

public class Request {
    private User sender;
    private String action;
    private HashMap<String, Object> params = new HashMap<>();

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public HashMap<String, Object> getParams() {
        return params;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
