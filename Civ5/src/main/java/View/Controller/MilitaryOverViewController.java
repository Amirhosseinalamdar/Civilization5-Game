package View.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MilitaryOverViewController {
    @FXML
    private Pane pane;
    @FXML
    private ScrollPane scrollPane;

    public Pane getPane() {
        return pane;
    }

    public VBox getMainVBox() {
        return mainVBox;
    }

    @FXML
    private VBox mainVBox;
}
