package Client.View.Controller;

import Client.App.Main;
import Client.Controller.NetworkController;
import Client.Transiton.NavigationTransition;
import Client.Controller.UserController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;

public class LoginMenuController {

    @FXML
    private Label login;
    @FXML
    private Label invalidRegister;
    @FXML
    private Label invalidLogin;
    @FXML
    private TextField usernameLogin;
    @FXML
    private TextField passwordLogin;
    @FXML
    private TextField usernameRegister;
    @FXML
    private TextField nicknameRegister;
    @FXML
    private TextField passwordRegister;

    public void registerUser(MouseEvent mouseEvent) {
        Main.clickSound();
        invalidRegister.setVisible(false);
        invalidLogin.setVisible(false);
        usernameRegister.setStyle("-fx-border-color: #382603");
        nicknameRegister.setStyle("-fx-border-color: #382603");
        passwordRegister.setStyle("-fx-border-color: #382603");
        String err = UserController.registerUser(usernameRegister.getText(), nicknameRegister.getText(), passwordRegister.getText());
        if (err.startsWith("error username")) {
            invalidRegister.setText("user with username " + usernameRegister.getText() + " already exists");
            invalidRegister.setVisible(true);
            usernameRegister.setStyle("-fx-border-color: red");
            invalidRegister.setTextFill(Color.RED);
        } else if (err.startsWith("error nickname")) {
            invalidRegister.setText("user with nickname " + nicknameRegister.getText() + " already exists");
            invalidRegister.setVisible(true);
            nicknameRegister.setStyle("-fx-border-color: red");
            invalidRegister.setTextFill(Color.RED);
        } else {
            invalidRegister.setText("user created successfully!");
            invalidRegister.setStyle("-fx-text-fill: #017301");
            invalidRegister.setVisible(true);
        }
    }

    public void loginUser(MouseEvent mouseEvent) {
        Main.clickSound();
        invalidRegister.setVisible(false);
        invalidLogin.setVisible(false);
        usernameLogin.setStyle("-fx-border-color: #382603");
        passwordLogin.setStyle("-fx-border-color: #382603");
        if (UserController.logUserIn(usernameLogin.getText(), passwordLogin.getText()).startsWith("error")) {
            invalidLogin.setText("username and password didn't match!");
            invalidLogin.setVisible(true);
            usernameLogin.setStyle("-fx-border-color: red");
            passwordLogin.setStyle("-fx-border-color: red");
        } else {
            try {
                NetworkController.listenForUpdates();
            }
            catch (IOException io) {
                io.printStackTrace();
            }
            NavigationTransition.fadeTransition(login, "MainMenu");
        }
    }

    public void changeVisibility1(KeyEvent keyEvent) {
        invalidRegister.setVisible(false);
        usernameRegister.setStyle("-fx-border-color: #382603");
        nicknameRegister.setStyle("-fx-border-color: #382603");
        passwordRegister.setStyle("-fx-border-color: #382603");
    }

    public void changeVisibility2(KeyEvent keyEvent) {
        invalidLogin.setVisible(false);
        usernameLogin.setStyle("-fx-border-color: #382603");
        passwordLogin.setStyle("-fx-border-color: #382603");
    }
}
