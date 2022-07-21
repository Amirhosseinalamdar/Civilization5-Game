package Client.View.Controller;

import Client.App.Main;
import Client.View.GameMenu;
import Client.Model.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Scanner;

public class GamePageController {
    public VBox background;
    public Label autoSaveDuration;
    public Label newGame;
    public Label mapSizeLabel;

    private int mapSize = 20;

    private int autoSave;
    private final String[] labels = {"Off", "Each Turn", "Each 5 Turns", "Each 10 Turns"};

    public void initialize() {
        autoSave = 0;
        autoSaveDuration.setText(labels[0]);
        newGame.setOnMouseClicked(event -> {
            ArrayList<User> players = PlayerListPageController.getPlayers();
            if (players == null) {
                Popup popup = new Popup();
                Label label = new Label("Choose at least One Opponent to Start the Game!");
                label.setStyle("-fx-font-family: 'Tw Cen MT'; -fx-font-size: 27; -fx-font-weight: bold;" +
                        "-fx-text-fill: red; -fx-background-color: black; -fx-background-radius: 2");
                popup.getContent().add(label);
                popup.setAutoHide(true);
                popup.show(background.getScene().getWindow());
            } else {
                if (Main.music.isPlaying()) Main.playSound("Game.mp3");
                GameMenu.setMapSize(mapSize);
                GameMenu.setAutoSaveDuration(autoSave);
                GameMenu.startGame(players, new Scanner(System.in), -1);
            }
        });
        mapSizeLabel.setText(Integer.toString(mapSize));
        mapSizeLabel.setOnMouseClicked(mouseEvent -> changeMapSize());
    }

    public void next() {
        Main.clickSound();
        autoSave++;
        autoSave %= 4;
        autoSaveDuration.setText(labels[autoSave]);
    }

    public void choosePlayers() {
        Main.clickSound();
        try {
            Scene scene = new Scene(FXMLLoader.load(this.getClass().getResource("/fxml/PlayerListPage.fxml")));
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGame() {
        Main.clickSound();
        try {
            Scene scene = new Scene(FXMLLoader.load(this.getClass().getResource("/fxml/LoadPage.fxml")));
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            System.out.println("failed to load LoadPage");
            e.printStackTrace();
        }
    }

    public void back() {
        Main.clickSound();
        Main.changeScene("MainMenu");
    }

    public void sendInvitation() {
        Main.clickSound();
        try {
            Scene scene = new Scene(FXMLLoader.load(this.getClass().getResource("/fxml/SendInvitaionPage.fxml")));
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            System.out.println("failed to load inv page");
            e.printStackTrace();
        }
    }

    public void changeMapSize() {
        Main.clickSound();
        mapSize++;
        if (mapSize > 25) mapSize = 20;
        mapSizeLabel.setText(Integer.toString(mapSize));
    }
}
