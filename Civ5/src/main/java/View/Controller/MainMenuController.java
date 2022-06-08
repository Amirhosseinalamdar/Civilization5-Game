package View.Controller;

import App.Main;
import Controller.UserController;
import Transiton.NavigationTransition;
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
        NavigationTransition.fadeTransition(gameMenu, "GamePage"); //TODO set name of game menu fxml file
    }

    public void publicChat(MouseEvent mouseEvent) {
        Main.clickSound();
        NavigationTransition.fadeTransition(publicChat, ""); //TODO set name of publicChat fxml file
    }

    public void privateChat(MouseEvent mouseEvent) {
        Main.clickSound();
        NavigationTransition.fadeTransition(privateChat, ""); //TODO set name of privateChat fxml file
    }

    public void room(MouseEvent mouseEvent) {
        Main.clickSound();
        NavigationTransition.fadeTransition(room, ""); //TODO set name of room fxml file
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
