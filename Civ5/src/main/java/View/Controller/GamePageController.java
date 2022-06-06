package View.Controller;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GamePageController {
    public VBox background;
    public Label autoSaveDuration;

    private int autoSave;
    private final String[] labels = {"Each Turn", "Each 5 Turns", "Each 10 Turns"};
    public void initialize() {
        autoSave = 0;
        autoSaveDuration.setText(labels[0]);
    }
    public void next() {
        autoSave++;
        autoSaveDuration.setText(labels[autoSave % 3]);
    }
}
