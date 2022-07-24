package Client.View.Controller;

import Client.App.Main;
import Client.Controller.NetworkController;
import Client.View.GameMenu;
import Client.Model.User;
import Server.Menu;
import Server.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
//            String json = NetworkController.send(new ArrayList<>(Arrays.asList
//                    (Menu.GAME.getMenuName(),Request.GET_PLAYERS.getString())));
//            ArrayList<User> players = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson
//                    (json, new TypeToken<List<User>>(){}.getType());
//            ArrayList<User> players = PlayerListPageController.getPlayers();
            String response = NetworkController.send(new ArrayList<>(Arrays.asList
                    (Menu.GAME.getMenuName(),Request.GET_PLAYERS.getString())));
            ArrayList<String> data = new Gson().fromJson(response,new TypeToken<List<String>>(){}.getType());
            ArrayList<User> players = new ArrayList<>();
            for(int i=0;i<data.size();i+=4){
                User user = new User(data.get(i),data.get(i+1),data.get(i+2),true,Integer.parseInt(data.get(i+3)));
                players.add(user);
            }
            if (players.size()<=1) {
                Popup popup = new Popup();
                Label label = new Label("Choose at least One Opponent to Start the Game!");
                label.setStyle("-fx-font-family: 'Tw Cen MT'; -fx-font-size: 27; -fx-font-weight: bold;" +
                        "-fx-text-fill: red; -fx-background-color: black; -fx-background-radius: 2");
                popup.getContent().add(label);
                popup.setAutoHide(true);
                popup.show(background.getScene().getWindow());
            } else {
//                if (Main.music.isPlaying()) Main.playSound("Game.mp3");
                GameMenu.setMapSize(mapSize);
                GameMenu.setAutoSaveDuration(autoSave);
                GameMenu.startGame(players, new Scanner(System.in), -1,"");
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
            Scene scene = new Scene(FXMLLoader.load(this.getClass().getResource("/fxml/SendInvitationPage.fxml")));
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
