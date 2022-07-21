package Client.View.Controller;

import Client.Controller.GameController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class CheatController {
    @FXML
    public TextField textField;
    @FXML
    public VBox cheatBox;

    public void cheat(MouseEvent mouseEvent) {
        if (textField.getText().length() == 0) return;
        if (textField.getText().equals("clear")) {
            cheatBox.getChildren().clear();
            textField.clear();
            return;
        }
        Text text = new Text(GameController.cheat(textField.getText()));
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Tw Cen MT", 20));
        text.setTextAlignment(TextAlignment.valueOf("CENTER"));
        HBox hBox = new HBox(text);
        hBox.setTranslateX(20);
        hBox.setTranslateY(hBox.getLayoutY() + 20);
        hBox.setMaxWidth(text.getLayoutBounds().getWidth());
        cheatBox.getChildren().add(hBox);
        textField.clear();
    }

    public void type(KeyEvent event) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > 70) {
                    textField.setText(oldValue);
                } else {
                    textField.setText(newValue);
                }
            }
        });
    }
}
