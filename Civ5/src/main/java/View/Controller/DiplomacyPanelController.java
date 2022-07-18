package View.Controller;

import App.Main;
import Controller.UserController;
import Model.Game;
import Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class DiplomacyPanelController {
    @FXML
    private VBox list;
    @FXML
    private Button exit;
    @FXML
    private Label choose;
    private static User player;

    public void initialize() {
//        ArrayList<User> allPlayers = Game.getInstance().getPlayers();
        ArrayList<User> allPlayers = UserController.getAllUsers();
//        allPlayers.remove(Game.getInstance().getPlayers().get(Game.getInstance().getTurn()));
        for (User user : allPlayers) {
            Label label = new Label();
            label.setStyle("-fx-font-family: 'Tw Cen MT';" +
                    "-fx-text-fill: #dbdbb1;" +
                    "-fx-font-size: 30;" +
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 10;" +
                    "-fx-arc-height: 5;" +
                    "-fx-arc-width: 5;" +
                    "-fx-text-alignment: center;");
            label.setAlignment(Pos.CENTER);
            label.setPrefWidth(370);
            label.setText(user.getUsername());
            label.setOnMouseClicked(event -> changeUserPresence(user));
            list.getChildren().add(label);
        }
        exit.setOnMouseClicked(event -> {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
    }

    private void changeUserPresence(User user) {
        choose.setTextFill(Paint.valueOf("ffffff"));
        player = user;
        for (Node node : list.getChildren())
            if (((Label) node).getText().equals(user.getUsername())) {
                node.setStyle("-fx-font-family: 'Tw Cen MT';" +
                        "-fx-text-fill: #508e96;" +
                        "-fx-font-size: 30;" +
                        "-fx-background-color: #f2f2ef;" +
                        "-fx-background-radius: 10;" +
                        "-fx-arc-height: 5;" +
                        "-fx-arc-width: 5;");
            }else {node.setStyle("-fx-font-family: 'Tw Cen MT';" +
                        "-fx-text-fill: #dbdbb1;" +
                        "-fx-font-size: 30;" +
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 10;" +
                        "-fx-arc-height: 5;" +
                        "-fx-arc-width: 5;");
            }
        changeWarState();
    }

    private void changeWarState() {
        //TODO handle the visibility of war or peace button
    }

    public static User getPlayer() {
        return player;
    }

    public void startDemand(MouseEvent mouseEvent) throws IOException {
        if (player == null) choose.setTextFill(Paint.valueOf("ff0000"));
        else {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/DemandPanel.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 450);
            stage.setTitle("Civilization V");
            stage.getIcons().add(new Image("/Pictures/steamworkshop_guide_1393684511_guide_branding.jpg"));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
    }

    public void startTrading(MouseEvent mouseEvent) throws IOException {
        if (player == null) choose.setTextFill(Paint.valueOf("ff0000"));
        else {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/TradePanel.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 450);
            stage.setTitle("Civilization V");
            stage.getIcons().add(new Image("/Pictures/steamworkshop_guide_1393684511_guide_branding.jpg"));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
    }

    public void discussing(MouseEvent mouseEvent) throws IOException {
        if (player == null) choose.setTextFill(Paint.valueOf("ff0000"));
        else {
            //TODO Next Phase
//            Stage stage = new Stage();
//            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/DiplomacyPanel.fxml"));
//            Scene scene = new Scene(fxmlLoader.load(), 800, 450);
//            stage.setTitle("Civilization V");
//            stage.getIcons().add(new Image("/Pictures/steamworkshop_guide_1393684511_guide_branding.jpg"));
//            stage.setScene(scene);
//            stage.setResizable(false);
//            stage.show();
        }
    }
}
