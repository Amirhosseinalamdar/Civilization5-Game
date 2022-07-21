package Client.View.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class NotificationController {
    public VBox getMainVBox() {
        return mainVBox;
    }

    @FXML
    private VBox mainVBox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Pane pane;


    public Pane getPane() {
        return pane;
    }
}
