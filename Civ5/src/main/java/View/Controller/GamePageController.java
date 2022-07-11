package View.Controller;

import View.GameMenu;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Scanner;

public class GamePageController {
    public VBox background;
    public Label autoSaveDuration;
    public Label newGame;

    private int autoSave;
    private final String[] labels = {"Each Turn", "Each 5 Turns", "Each 10 Turns"};
    public void initialize() {
        autoSave = 0;
        autoSaveDuration.setText(labels[0]);
        newGame.setOnMouseClicked(event -> {
//            Node source = (Node) event.getSource();
//            Stage stage = (Stage) source.getScene().getWindow();
//            stage.close();
            GameMenu.startGame(PlayerListPageController.getPlayers(), new Scanner(System.in), -1);
        });
    }
    public void next() {
        autoSave++;
        autoSaveDuration.setText(labels[autoSave % 3]);
    }
    public void choosePlayers() {
        try {
            Scene scene = new Scene(FXMLLoader.load(this.getClass().getResource("/fxml/PlayerListPage.fxml")));
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void loadGame() {
        try {
            Scene scene = new Scene(FXMLLoader.load(this.getClass().getResource("/fxml/LoadPage.fxml")));
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception ignored){}
    }
}
