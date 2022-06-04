package Transiton;

import App.Main;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;

public class NavigationTransition {
    public static void fadeTransition(Node node, String name) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setNode(node);
        fadeTransition.play();
        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Main.changeScene(name);
            }
        });
    }
}
