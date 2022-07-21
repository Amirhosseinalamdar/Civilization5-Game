package Client.View.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class EconomicPanelController {
    @FXML
    private Pane pane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox mainVBox;

    public Pane getPane() {
        return pane;
    }

    public VBox getMainVBox() {
        return mainVBox;
    }
}
