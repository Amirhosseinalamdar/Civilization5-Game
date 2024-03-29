package Client.View.Controller;

import Client.App.Main;
import Client.Transiton.NavigationTransition;
import Client.Controller.UserController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class MainMenuController {

    @FXML
    private Label gameMenu;
    @FXML
    private Label publicChat;
    @FXML
    private Label privateChat;
    @FXML
    private Label room;
    @FXML
    private Label scoreboard;
    @FXML
    private Label profile;
    @FXML
    private Label logout;

    public void gameMenu(MouseEvent mouseEvent) {
        Main.clickSound();
        NavigationTransition.fadeTransition(gameMenu, "GamePage");
    }

    public void publicChat(MouseEvent mouseEvent) {
        Main.clickSound();
        NavigationTransition.fadeTransition(publicChat, "PublicChat");
    }

    public void privateChat(MouseEvent mouseEvent) {
        Main.clickSound();
        NavigationTransition.fadeTransition(privateChat, "ChooseChat");

    }

    public void room(MouseEvent mouseEvent) {
        Main.clickSound();
        NavigationTransition.fadeTransition(room, "");
    }

    public void scoreboardMenu(MouseEvent mouseEvent) {
        Main.clickSound();
        NavigationTransition.fadeTransition(scoreboard, "ScoreboardMenu");
    }

    public void profileMenu(MouseEvent mouseEvent) {
        Main.clickSound();
        NavigationTransition.fadeTransition(profile, "ProfileMenu");
    }

    public void logout(MouseEvent mouseEvent) {
        Main.clickSound();
        UserController.logUserOut();
        NavigationTransition.fadeTransition(logout, "LoginMenu");
    }

    public void exitGame(MouseEvent mouseEvent) {
        Main.clickSound();
        UserController.writeDataToJson();
        System.exit(0);
    }
}
