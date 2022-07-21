package Client.Model.Chat;

import javafx.scene.layout.VBox;

public class PrivateChat {
    private VBox chatBox;
    private String username1;
    private String username2;

    public VBox getChatBox() {
        return chatBox;
    }

    public void setChatBox(VBox chatBox) {
        this.chatBox = chatBox;
    }


    public PrivateChat(String username1, String username2) {
        this.username1 = username1;
        this.username2 = username2;
    }

    public String getUsername1() {
        return username1;
    }

    public String getUsername2() {
        return username2;
    }
}
