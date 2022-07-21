package Client.View.Controller;

import Client.App.Main;
import Client.Controller.UserController;
import Client.Model.Game;
import Client.Model.User;
import Client.View.GameMenu;
import com.google.gson.GsonBuilder;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class LoadPageController {
    public VBox box;

    public void initialize() {
        for (int i = 0; i < 5; i++)
            if (new File("Game" + i + ".json").exists()) {
                try {
                    String json = new String(Files.readAllBytes(Paths.get("Game" + i + ".json")));
                    ArrayList<User> players = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(json, Game.class).getPlayers();
                    if (!iAmAmong(players))
                        continue;
                    Label label = new Label();
                    label.setStyle("-fx-font-family: 'Tw Cen MT';" +
                            "-fx-text-fill: #dbdbb1;" +
                            "-fx-font-size: 30;" +
                            "-fx-background-color: transparent;" +
                            "-fx-background-radius: 10;" +
                            "-fx-arc-height: 5;" +
                            "-fx-arc-width: 5;" +
                            "-fx-text-alignment: center;");
                    label.setText("Load no." + (i + 1));
                    label.setPrefWidth(370);
                    int finalI = i;
                    label.setOnMouseEntered(event -> {
                        label.setStyle("-fx-font-family: 'Tw Cen MT';" +
                                "-fx-text-fill: #dbdbb1;" +
                                "-fx-font-size: 30;" +
                                "-fx-background-color: #508e96;" +
                                "-fx-background-radius: 10;" +
                                "-fx-arc-height: 5;" +
                                "-fx-arc-width: 5;" +
                                "-fx-cursor: hand;");
                    });
                    label.setOnMouseExited(event -> {
                        label.setStyle("-fx-font-family: 'Tw Cen MT';" +
                                "-fx-text-fill: #dbdbb1;" +
                                "-fx-font-size: 30;" +
                                "-fx-background-color: transparent;" +
                                "-fx-background-radius: 10;" +
                                "-fx-arc-height: 5;" +
                                "-fx-arc-width: 5;" +
                                "-fx-text-alignment: center;" +
                                "-fx-cursor: default;");
                    });
                    label.setOnMouseClicked(event -> {
                        Node source = (Node) event.getSource();
                        Stage stage = (Stage) source.getScene().getWindow();
                        stage.close();
                        if (Main.music.isPlaying()) Main.playSound("Game.mp3");
                        GameMenu.startGame(null, new Scanner(System.in), finalI);
                    });
                    box.getChildren().add(label);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    private boolean iAmAmong(ArrayList<User> users) {
        for (User user : users)
            if (user.getUsername().equals(UserController.getLoggedInUser().getUsername()) &&
                    user.getNickname().equals(UserController.getLoggedInUser().getNickname()) &&
                    user.getPassword().equals(UserController.getLoggedInUser().getPassword()))
                return true;
        return false;
    }
}
