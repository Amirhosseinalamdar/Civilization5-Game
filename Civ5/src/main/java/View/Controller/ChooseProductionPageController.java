package View.Controller;

import Controller.CityController;
import Model.Map.City;
import Model.UnitPackage.UnitType;
import View.GameMenu;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ChooseProductionPageController {
    private static City city;
    @FXML
    private VBox unitBox;
    @FXML
    private VBox buildingBox;

    public static void setCity(City city) {
        ChooseProductionPageController.city = city;
        CityController.setCity(city, "");
    }

    public void initialize() {
        setUnitBox();
        setBuildingBox();
    }

    private void setUnitBox() {
        ArrayList<UnitType> availableUnits = new ArrayList<>();
        for (UnitType unitType : UnitType.values())
            if (CityController.canCreateUnit(unitType))
                availableUnits.add(unitType);
        for (int i = 0; i < availableUnits.size(); i++) {
            UnitType unitType = availableUnits.get(i);
            HBox hBox = new HBox();

            hBox.getChildren().add(getVboxForUnit(unitType));
            if (i < availableUnits.size() - 1) {
                i++;
                unitType = availableUnits.get(i);
                hBox.getChildren().add(getVboxForUnit(unitType));
                if (i < availableUnits.size() - 1) {
                    i++;
                    unitType = availableUnits.get(i);
                    hBox.getChildren().add(getVboxForUnit(unitType));
                }
            }

            hBox.setSpacing(2.8);
            unitBox.getChildren().add(hBox);
        }
    }

    private void setBuildingBox() {

    }

    private ImageView getVboxForUnit(UnitType unitType) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-1);
        Tooltip tooltip = new Tooltip(unitType.toString());
        ImageView unitImgView = new ImageView(new Image("/Pictures/units/" + unitType + ".png"));
        unitImgView.setFitWidth(130);
        unitImgView.setFitHeight(130);
        unitImgView.setEffect(colorAdjust);
        unitImgView.setOnMouseEntered(event -> {
            colorAdjust.setSaturation(0);
            unitImgView.setEffect(colorAdjust);
        });
        unitImgView.setOnMouseExited(event -> {
            colorAdjust.setSaturation(-1);
            unitImgView.setEffect(colorAdjust);
        });
        unitImgView.setOnMouseClicked(event -> {
            city.setInProgressUnit(unitType);
            int remainingCost = unitType.getCost();
            if (city.getLastCostsUntilNewProductions().containsKey(unitType))
                remainingCost = city.getLastCostsUntilNewProductions().get(unitType);
            city.getLastCostsUntilNewProductions().put(unitType, remainingCost);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            GameMenu.getGameMapController().showMap();
        });
        Tooltip.install(unitImgView, tooltip);
        return unitImgView;
    }
}
