package View.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CitiesPanelController {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox mainVBox;
    @FXML
    private Pane pane;

    public VBox getMainVBox() {
        return mainVBox;
    }

    public Pane getPane() {
        return pane;
    }
}
