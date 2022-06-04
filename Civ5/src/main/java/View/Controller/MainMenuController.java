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
    private Label chatroom;
    @FXML
    private Label scoreboard;
    @FXML
    private Label profile;
    @FXML
    private Label logout;

    public void gameMenu(MouseEvent mouseEvent) {
        Main.clickSound();
        NavigationTransition.fadeTransition(gameMenu, ""); //TODO set name of game menu fxml file
    }

    public void Chatroom(MouseEvent mouseEvent) {
        Main.clickSound();
        NavigationTransition.fadeTransition(chatroom, ""); //TODO set name of chatroom fxml file
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
