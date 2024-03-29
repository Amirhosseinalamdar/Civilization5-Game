package Client.View.Controller;

import Client.App.Main;
import Client.Controller.NetworkController;
import Client.Controller.UserController;
import Client.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SendInvitationPageController {
    public Pane pane;
    public VBox list;

    private Label send;
    private Label clickedLabel;

    private User chosenUser;

    public void initialize() {
        clickedLabel = null;
        send = new Label("Send");
        send.setLayoutX(165);
        send.setLayoutY(540);
        send.getStylesheets().add("css/GamePageStyle.css");
        send.getStyleClass().add("startGameButton");

        String json = NetworkController.send(new ArrayList<>(Arrays.asList("global","get all users")));
        ArrayList<String> allUsers = new Gson().fromJson(json, new TypeToken<List<String>>() {
        }.getType());
        allUsers.removeIf(u -> u.equals(Main.username));
        for (String username : allUsers) {
            Label label = new Label();
            label.setStyle("-fx-font-family: 'Tw Cen MT';" +
                    "-fx-text-fill: #dbdbb1;" +
                    "-fx-font-size: 30;" +
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 10;" +
                    "-fx-arc-height: 5;" +
                    "-fx-arc-width: 5;" +
                    "-fx-text-alignment: center;");
            label.setPrefWidth(370);
            label.setText(username);
            label.setOnMouseClicked(event -> {
                if (clickedLabel != null) {
                    clickedLabel.setStyle("-fx-font-family: 'Tw Cen MT';" +
                            "-fx-text-fill: #dbdbb1;" +
                            "-fx-font-size: 30;" +
                            "-fx-background-color: transparent;" +
                            "-fx-background-radius: 10;" +
                            "-fx-arc-height: 5;" +
                            "-fx-arc-width: 5;" +
                            "-fx-text-alignment: center;");
                }
                clickedLabel = label;
                Main.clickSound();
                changeUserLabel(label);
                if (!pane.getChildren().contains(send)) pane.getChildren().add(send);
            });
            list.getChildren().add(label);
        }
        send.setOnMouseClicked(event -> {
            for (Node child : list.getChildren()) {
                if (!(child instanceof Label)) continue;
                Label label = (Label) child;
                label.setStyle("-fx-font-family: 'Tw Cen MT';" +
                        "-fx-text-fill: #dbdbb1;" +
                        "-fx-font-size: 30;" +
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 10;" +
                        "-fx-arc-height: 5;" +
                        "-fx-arc-width: 5;");
                if (clickedLabel == null) continue;
                for (String username : allUsers)
                    if (clickedLabel.getText().equals(username))
                        NetworkController.send(new ArrayList<>(Arrays.asList("game menu", "invite", Main.username, username)));
            }
            pane.getChildren().remove(send);
        });
    }

    private void changeUserLabel(Label label) {
        label.setStyle("-fx-font-family: 'Tw Cen MT';" +
                "-fx-text-fill: #508e96;" +
                "-fx-font-size: 30;" +
                "-fx-background-color: #f2f2ef;" +
                "-fx-background-radius: 10;" +
                "-fx-arc-height: 5;" +
                "-fx-arc-width: 5;");
    }
}
