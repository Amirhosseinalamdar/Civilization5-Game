package View.Controller;

import Controller.GameController;
import Controller.UserController;
import Model.Game;
import Model.Map.Resource;
import Model.Request;
import Model.User;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class TradePanelController {
    @FXML
    private VBox list1;
    @FXML
    private VBox list2;
    @FXML
    private VBox list3;
    @FXML
    private VBox list4;
    @FXML
    private Button exit;
    @FXML
    private Label error;
    @FXML
    private Label choose;

    public void initialize() {
        HashMap<Resource, Integer> resources1 = new HashMap<>(GameController.getCivilization().getStrategicResources());
        resources1.putAll(GameController.getCivilization().getLuxuryResources());
        HashMap<Resource, Integer> resources2 = new HashMap<>(DiplomacyPanelController.getPlayer().getCivilization().getStrategicResources());
        resources2.putAll(DiplomacyPanelController.getPlayer().getCivilization().getLuxuryResources());
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
        label.setText("Gold");
        label.setOnMouseClicked(event -> changeProductPresence(label));
        list1.getChildren().add(label);
        TextField textField = new TextField();
        textField.setStyle("-fx-font-family: 'Tw Cen MT';" +
                "-fx-text-fill: #dbdbb1;" +
                "-fx-font-size: 20;" +
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 10;" +
                "-fx-arc-height: 5;" +
                "-fx-arc-width: 5;" +
                "-fx-text-alignment: center;");
        textField.setAlignment(Pos.CENTER);
        textField.setPrefWidth(370);
        textField.setPrefHeight(25);
        textField.setText("1");
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                error.setVisible(false);
            }
        });
        list2.getChildren().add(textField);
        Label label1 = new Label();
        label1.setStyle("-fx-font-family: 'Tw Cen MT';" +
                "-fx-text-fill: #dbdbb1;" +
                "-fx-font-size: 30;" +
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 10;" +
                "-fx-arc-height: 5;" +
                "-fx-arc-width: 5;" +
                "-fx-text-alignment: center;");
        label1.setAlignment(Pos.CENTER);
        label1.setPrefWidth(370);
        label1.setText("Gold");
        label1.setOnMouseClicked(event -> changeProductPresence(label1));
        list3.getChildren().add(label1);
        TextField textField1 = new TextField();
        textField1.setStyle("-fx-font-family: 'Tw Cen MT';" +
                "-fx-text-fill: #dbdbb1;" +
                "-fx-font-size: 20;" +
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 10;" +
                "-fx-arc-height: 5;" +
                "-fx-arc-width: 5;" +
                "-fx-text-alignment: center;");
        textField1.setAlignment(Pos.CENTER);
        textField1.setPrefWidth(370);
        textField1.setPrefHeight(25);
        textField1.setText("1");
        textField1.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                error.setVisible(false);
            }
        });
        list4.getChildren().add(textField1);
        for (Map.Entry<Resource, Integer> set :
                resources1.entrySet()) {
            Label label2 = new Label();
            label2.setStyle("-fx-font-family: 'Tw Cen MT';" +
                    "-fx-text-fill: #dbdbb1;" +
                    "-fx-font-size: 30;" +
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 10;" +
                    "-fx-arc-height: 5;" +
                    "-fx-arc-width: 5;" +
                    "-fx-text-alignment: center;");
            label2.setAlignment(Pos.CENTER);
            label2.setPrefWidth(370);
            label2.setText(set.getKey().name());
            label2.setOnMouseClicked(event -> changeProductPresence(label2));
            list1.getChildren().add(label2);
            TextField textField2 = new TextField();
            textField2.setStyle("-fx-font-family: 'Tw Cen MT';" +
                    "-fx-text-fill: #dbdbb1;" +
                    "-fx-font-size: 20;" +
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 10;" +
                    "-fx-arc-height: 5;" +
                    "-fx-arc-width: 5;" +
                    "-fx-text-alignment: center;");
            textField2.setAlignment(Pos.CENTER);
            textField2.setPrefWidth(370);
            textField2.setPrefHeight(25);
            textField2.setText("1");
            textField2.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    error.setVisible(false);
                }
            });
            list2.getChildren().add(textField2);
        }
        for (Map.Entry<Resource, Integer> set :
                resources2.entrySet()) {
            Label label2 = new Label();
            label2.setStyle("-fx-font-family: 'Tw Cen MT';" +
                    "-fx-text-fill: #dbdbb1;" +
                    "-fx-font-size: 30;" +
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 10;" +
                    "-fx-arc-height: 5;" +
                    "-fx-arc-width: 5;" +
                    "-fx-text-alignment: center;");
            label2.setAlignment(Pos.CENTER);
            label2.setPrefWidth(370);
            label2.setText(set.getKey().name());
            label2.setOnMouseClicked(event -> changeProductPresence(label2));
            list3.getChildren().add(label2);
            TextField textField2 = new TextField();
            textField2.setStyle("-fx-font-family: 'Tw Cen MT';" +
                    "-fx-text-fill: #dbdbb1;" +
                    "-fx-font-size: 20;" +
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 10;" +
                    "-fx-arc-height: 5;" +
                    "-fx-arc-width: 5;" +
                    "-fx-text-alignment: center;");
            textField2.setAlignment(Pos.CENTER);
            textField2.setPrefWidth(370);
            textField2.setPrefHeight(25);
            textField2.setText("1");
            textField2.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    error.setVisible(false);
                }
            });
            list4.getChildren().add(textField2);
        }
        exit.setOnMouseClicked(event -> {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
    }

    private void changeProductPresence(Label label) {
        choose.setTextFill(Paint.valueOf("ffffff"));
        error.setVisible(false);
        if (label.getStyle().contains("f2f2ef")) {
            label.setStyle("-fx-font-family: 'Tw Cen MT';" +
                    "-fx-text-fill: #dbdbb1;" +
                    "-fx-font-size: 30;" +
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 10;" +
                    "-fx-arc-height: 5;" +
                    "-fx-arc-width: 5;");
        } else {
            label.setStyle("-fx-font-family: 'Tw Cen MT';" +
                    "-fx-text-fill: #508e96;" +
                    "-fx-font-size: 30;" +
                    "-fx-background-color: #f2f2ef;" +
                    "-fx-background-radius: 10;" +
                    "-fx-arc-height: 5;" +
                    "-fx-arc-width: 5;");
        }
    }

    public void trade(MouseEvent mouseEvent) {
        boolean isGet = false;
        boolean isGive = false;
        try {
            Request request = new Request();
            request.setSender(Game.getInstance().getPlayers().get(Game.getInstance().getTurn()).getUsername());
            request.setAction("Trade");
            for (int i = 0; i < list1.getChildren().size(); i++) {
                if (list1.getChildren().get(i).getStyle().contains("508e96")) {
                    if (Integer.parseInt(((TextField)list2.getChildren().get(i)).getText()) <= 0) throw new Exception();
                    request.getParams().put("Give: " + ((Label)list1.getChildren().get(i)).getText(), Integer.parseInt(((TextField)list2.getChildren().get(i)).getText()));
                    isGive = true;
                }
            }
            for (int i = 0; i < list3.getChildren().size(); i++) {
                if (list3.getChildren().get(i).getStyle().contains("508e96")) {
                    if (Integer.parseInt(((TextField)list4.getChildren().get(i)).getText()) <= 0) throw new Exception();
                    request.getParams().put("Get: " + ((Label)list3.getChildren().get(i)).getText(), Integer.parseInt(((TextField)list4.getChildren().get(i)).getText()));
                    isGet = true;
                }
            }
            if (isGet && isGive) {
                error.setText("Trade request sent successfully");
                error.setTextFill(Paint.valueOf("ffffff"));
                error.setVisible(true);
                DiplomacyPanelController.getPlayer().getCivilization().getRequests().add(request);
            } else choose.setTextFill(Paint.valueOf("ff0000"));
        } catch (Exception e) {
            error.setText("Enter valid numbers");
            error.setTextFill(Paint.valueOf("ff0000"));
            error.setVisible(true);
        }

    }
}
