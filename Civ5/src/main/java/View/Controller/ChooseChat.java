package View.Controller;

import Controller.UserController;
import Model.Chat.DataBase;
import Model.Chat.PrivateChat;
import Model.Game;
import Model.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ChooseChat {
    public TextField textField;
    public Pane pane;

    public void startChat(MouseEvent mouseEvent) throws IOException {
        for (User user : UserController.getAllUsers()) {
            if (user.getUsername().equals(textField.getText())) {
                if (DataBase.getInstance().getPrivateChatByUsers(user.getUsername(),
                        Game.getInstance().getLoggedInUser().getUsername()) != null) {
                    DataBase.getInstance().setCurrentPrivateChat(
                            (DataBase.getInstance().getPrivateChatByUsers
                                    (textField.getText(), Game.getInstance().getLoggedInUser().getUsername())));
                } else {
                    PrivateChat privateChat = new PrivateChat(textField.getText(),
                            Game.getInstance().getLoggedInUser().getUsername());
                    DataBase.getInstance().getPrivateChats().add(privateChat);
                    DataBase.getInstance().setCurrentPrivateChat(privateChat);
                }
                pane.getScene().setRoot(FXMLLoader.load(new URL(getClass().
                        getResource("/fxml/PrivateChat.fxml").toExternalForm())));
                return;
            }
        }
        textField.setStyle("-fx-border-color: red;");
    }
}
