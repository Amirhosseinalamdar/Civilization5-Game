package Client.View.Controller;

import Client.Model.User;
import Client.Controller.UserController;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class PlayerListPageController {
    public VBox list;
    public Label ok;

    private static ArrayList<User> players;

    public void initialize() {
        players = new ArrayList<>();
        ArrayList<User> allUsers = new ArrayList<>(UserController.getAllUsers());
        allUsers.remove(UserController.getLoggedInUser());
        for (User user : allUsers) {
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
            label.setText(user.getUsername());
            label.setOnMouseClicked(event -> changeUserPresence(user));
            list.getChildren().add(label);
        }
        ok.setOnMouseClicked(event -> {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
    }

    private void changeUserPresence(User user) {
        if (players.contains(user)) players.remove(user);
        else players.add(user);
        for (Node node : list.getChildren())
            if (((Label) node).getText().equals(user.getUsername())) {
                if (!players.contains(user)) node.setStyle("-fx-font-family: 'Tw Cen MT';" +
                        "-fx-text-fill: #dbdbb1;" +
                        "-fx-font-size: 30;" +
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 10;" +
                        "-fx-arc-height: 5;" +
                        "-fx-arc-width: 5;");
                else node.setStyle("-fx-font-family: 'Tw Cen MT';" +
                        "-fx-text-fill: #508e96;" +
                        "-fx-font-size: 30;" +
                        "-fx-background-color: #f2f2ef;" +
                        "-fx-background-radius: 10;" +
                        "-fx-arc-height: 5;" +
                        "-fx-arc-width: 5;");
                return;
            }
    }

    public static ArrayList<User> getPlayers() {
        if (players == null || players.size() == 0) return null;
        players.add(0, UserController.getLoggedInUser());
        return players;
    }
}
