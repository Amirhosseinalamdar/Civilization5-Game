package Client.Model.Chat;

import javafx.scene.layout.VBox;

public class PublicChat {
    private VBox chatBox;
    private static PublicChat publicChat;

    public VBox getChatBox() {
        return chatBox;
    }

    private PublicChat() {
    }

    public static PublicChat getInstance() {
        if (publicChat == null) publicChat = new PublicChat();
        return publicChat;
    }

    public void setChatBox(VBox chatBox) {
        this.chatBox = chatBox;
    }
}
