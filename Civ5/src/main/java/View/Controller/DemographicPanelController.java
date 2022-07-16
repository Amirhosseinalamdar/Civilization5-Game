package View.Controller;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class DemographicPanelController {

    @FXML
    private Pane pane;
    @FXML
    private VBox mainVBox;

    public Pane getPane() {
        return pane;
    }

    public VBox getMainVBox() {
        return mainVBox;
    }
}
