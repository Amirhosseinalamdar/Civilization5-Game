package View.Controller;

import App.Main;
import Controller.GameController;
import Controller.UserController;
import Model.Game;
import Model.Request;
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
    private Label message;
    @FXML
    private VBox list;
    @FXML
    private Button exit;
    @FXML
    private Button peaceButton;
    @FXML
    private Button warButton;
    @FXML
    private Label choose;
    private static User player;

    public void initialize() {
        System.out.println(Game.getInstance().getPlayers());
        ArrayList<User> allPlayers = new ArrayList<>(Game.getInstance().getPlayers());
        System.out.println(allPlayers);
        allPlayers.remove(Game.getInstance().getPlayers().get(Game.getInstance().getTurn()));
        System.out.println(allPlayers);
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
            } else {
                node.setStyle("-fx-font-family: 'Tw Cen MT';" +
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
        if (GameController.getCivilization().getInWarCivilizations().contains(player.getUsername())) {
            peaceButton.setVisible(true);
            peaceButton.setDisable(false);
        } else {
            warButton.setVisible(true);
            warButton.setDisable(false);
        }
    }


    public static User getPlayer() {
        return player;
    }

    public void startDemand(MouseEvent mouseEvent) throws IOException {
        if (player == null) choose.setTextFill(Paint.valueOf("ff0000"));
        else if (GameController.getCivilization().getInWarCivilizations().contains(player.getUsername())) {
            message.setTextFill(Paint.valueOf("ff0000"));
            message.setText("You are in war with this civilization");
            message.setVisible(true);
        } else {
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
        else if (GameController.getCivilization().getInWarCivilizations().contains(player.getUsername())) {
            message.setTextFill(Paint.valueOf("ff0000"));
            message.setText("You are in war with this civilization");
            message.setVisible(true);
        } else {
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

    public void declareWar(MouseEvent mouseEvent) {
        if (player == null) choose.setTextFill(Paint.valueOf("ff0000"));
        else if (GameController.getCivilization().getInWarCivilizations().contains(player.getUsername())) {
            message.setTextFill(Paint.valueOf("ff0000"));
            message.setText("You are already in war with this civilization");
            message.setVisible(true);
        } else {
            GameController.getCivilization().getInWarCivilizations().add(player.getUsername());
            player.getCivilization().getInWarCivilizations().add(GameController.getCivilization().getUsername());
            Request request = new Request();
            request.setSender(Game.getInstance().getPlayers().get(Game.getInstance().getTurn()).getUsername());
            request.setAction("War");
            player.getCivilization().getRequests().add(request);
            message.setTextFill(Paint.valueOf("ffffff"));
            message.setText("War message sent successfully");
            message.setVisible(true);
        }
    }

    public void declarePeace(MouseEvent mouseEvent) {
        if (player == null) choose.setTextFill(Paint.valueOf("ff0000"));
        else if (isPeaceSent()) {
            message.setTextFill(Paint.valueOf("ff0000"));
            message.setText("You are already sent peace message to this civilization");
            message.setVisible(true);
        } else {
            Request request = new Request();
            request.setSender(Game.getInstance().getPlayers().get(Game.getInstance().getTurn()).getUsername());
            request.setAction("Peace");
            player.getCivilization().getRequests().add(request);
            message.setText("Peace message sent successfully");
            message.setTextFill(Paint.valueOf("ffffff"));
            message.setVisible(true);
        }
    }

    private boolean isPeaceSent() {
        for (Request request : player.getCivilization().getRequests()) {
            if (request.getAction().equals("Peace") && request.getSender().equals(Game.getInstance().getPlayers().get(Game.getInstance().getTurn())))
                return true;
        }
        return false;
    }
}
