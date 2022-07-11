package Model.Chat;

import Model.User;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class PublicChat {
    //private ArrayList<Message> messages = new ArrayList<>();
    private VBox chatBox;
    private static PublicChat publicChat;

    public VBox getChatBox() {
        return chatBox;
    }

    private PublicChat(){
    }
    public static PublicChat getInstance(){
        if(publicChat == null) publicChat = new PublicChat();
        return publicChat;
    }

    public void setChatBox(VBox chatBox) {
        this.chatBox = chatBox;
    }
}
