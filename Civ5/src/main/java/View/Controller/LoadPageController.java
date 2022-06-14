package View.Controller;

import View.GameMenu;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.Scanner;

public class LoadPageController {
    public VBox box;

    public void initialize() {
        for (int i = 0; i < 5; i++)
            if (new File("Game" + i + ".json").exists()) {
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
                    GameMenu.startGame(null, new Scanner(System.in), finalI);
                });
                box.getChildren().add(label);
            }
    }
}
