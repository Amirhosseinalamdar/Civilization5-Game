package Client.View.Controller;

import Client.View.GameMenu;
import Client.Controller.GameController;
import Client.Model.Civilization;
import Client.Model.Technology;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ChooseResearchPageController {

    public VBox bigBox;

    private final Civilization civilization = GameController.getCivilization();

    public void initialize() {
        Label head = new Label("Technologies");
        head.setPrefSize(400, 40);
        head.setAlignment(Pos.CENTER);
        head.setStyle("-fx-font-size: 35; -fx-font-family: 'Tw Cen MT'; -fx-text-fill: #040457; -fx-alignment: center;");
        bigBox.getChildren().add(head);
        ArrayList<Technology> availableTechs = new ArrayList<>();
        if (civilization.getInProgressTech() != null) availableTechs.add(civilization.getInProgressTech());
        for (Technology technology : Technology.values())
            if (!civilization.hasReachedTech(technology) && civilization.hasPrerequisitesOf(technology))
                availableTechs.add(technology);
        for (int i = 0; i < availableTechs.size(); i++) {
            Technology tech = availableTechs.get(i);
            HBox hBox = new HBox();

            hBox.getChildren().add(getVBoxForTech(tech));
            if (i < availableTechs.size() - 1) {
                i++;
                tech = availableTechs.get(i);
                hBox.getChildren().add(getVBoxForTech(tech));
                if (i < availableTechs.size() - 1) {
                    i++;
                    tech = availableTechs.get(i);
                    hBox.getChildren().add(getVBoxForTech(tech));
                }
            }

            hBox.setSpacing(2.8);
            bigBox.getChildren().add(hBox);
        }
        bigBox.setStyle("-fx-background-color: none; -fx-fill: none;");
    }

    private VBox getVBoxForTech(Technology tech) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.2);
        VBox box = new VBox();
        Tooltip tooltip = new Tooltip(tech.toString());
        ImageView techImgView = new ImageView(tech.getImage());
        techImgView.setFitWidth(130);
        techImgView.setFitHeight(130);
        techImgView.setEffect(colorAdjust);
        techImgView.setOnMouseEntered(event -> {
            colorAdjust.setBrightness(+0.2);
            techImgView.setEffect(colorAdjust);
        });
        techImgView.setOnMouseExited(event -> {
            colorAdjust.setBrightness(-0.2);
            techImgView.setEffect(colorAdjust);
        });
        techImgView.setOnMouseClicked(event -> {
            civilization.setInProgressTech(tech);
            int remainingCost = tech.getCost();
            if (civilization.getLastCostUntilNewTechnologies().containsKey(tech))
                remainingCost = civilization.getLastCostUntilNewTechnologies().get(tech);
            civilization.getLastCostUntilNewTechnologies().put(tech, remainingCost);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            GameMenu.getGameMapController().showMap();
        });
        box.getChildren().add(techImgView);
        Tooltip.install(techImgView, tooltip);
        return box;
    }
}
