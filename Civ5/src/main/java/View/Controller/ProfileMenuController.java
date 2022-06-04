package View.Controller;

import App.Main;
import Controller.UserController;
import Transiton.NavigationTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ProfileMenuController {
    @FXML
    private TextField currentPassword;
    @FXML
    private TextField newPassword;
    @FXML
    private Label invalidPassword;
    @FXML
    private TextField newNickname;
    @FXML
    private Label invalidNickname;
    @FXML
    private Label back;

    public void changePassword(MouseEvent mouseEvent) {
        Main.clickSound();
        invalidPassword.setVisible(false);
        currentPassword.setStyle("-fx-border-color: #382603");
        newPassword.setStyle("-fx-border-color: #382603");
        if (UserController.changePassword(currentPassword.getText(), newPassword.getText()).startsWith("error current")) {
            invalidPassword.setText("current password is invalid");
            invalidPassword.setVisible(true);
            currentPassword.setStyle("-fx-border-color: red");
        } else if (UserController.changePassword(currentPassword.getText(), newPassword.getText()).startsWith("error new")){
            invalidPassword.setText("please enter a new password");
            invalidPassword.setVisible(true);
            newPassword.setStyle("-fx-border-color: red");
        } else {
            invalidPassword.setText("password changed successfully!");
            invalidPassword.setStyle("-fx-text-fill: #017301");
            invalidPassword.setVisible(true);
        }
    }

    public void changeNickname(MouseEvent mouseEvent) {
        Main.clickSound();
        invalidNickname.setVisible(false);
        newNickname.setStyle("-fx-border-color: #382603");
        if (UserController.changeNickname(newNickname.getText()).startsWith("user")) {
            invalidNickname.setText("user with nickname " + newNickname.getText() + " already exists");
            invalidNickname.setVisible(true);
            newNickname.setStyle("-fx-border-color: red");
        } else {
            invalidNickname.setText("nickname changed successfully!");
            invalidNickname.setStyle("-fx-text-fill: #017301");
            invalidNickname.setVisible(true);
        }
    }

    public void changeVisibility1(KeyEvent keyEvent) {
        invalidPassword.setVisible(false);
        currentPassword.setStyle("-fx-border-color: #382603");
        newPassword.setStyle("-fx-border-color: #382603");
    }

    public void changeVisibility2(KeyEvent keyEvent) {
        invalidNickname.setVisible(false);
        newNickname.setStyle("-fx-border-color: #382603");
    }

    public void back(MouseEvent mouseEvent) {
        Main.clickSound();
        NavigationTransition.fadeTransition(back, "MainMenu");
    }
}
