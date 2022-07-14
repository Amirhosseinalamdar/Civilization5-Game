package View.Controller;

import Controller.GameController;
import Controller.UnitController;
import Model.Civilization;
import Model.Game;
import Model.Map.*;
import Model.TileStatus;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitStatus;
import Model.UnitPackage.UnitType;
import View.Commands;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class MapController {
    @FXML
    private Pane backgroundPane;
    private ImageView unitAvatarImageView;
    private Label movesLabel;
    private Unit chosenUnit;
    private ArrayList<Node> unitOptionsNodes = new ArrayList<>();

    public Pane getBackgroundPane() {
        return backgroundPane;
    }

    private int xStartingIndex = 1;
    private int yStartingIndex = 1;
    private ArrayList<ImageView> tileImageViews = new ArrayList<>();
    private ArrayList<ImageView> citizenImageViews = new ArrayList<>();
    public int getxStartingIndex() {
        return xStartingIndex;
    }

    public void setChosenUnit(Unit chosenUnit) {
        this.chosenUnit = chosenUnit;
    }

    public Unit getChosenUnit() {
        return chosenUnit;
    }

    public void setxStartingIndex(int xStartingIndex) {
        this.xStartingIndex = xStartingIndex;
    }

    public void setyStartingIndex(int yStartingIndex) {
        this.yStartingIndex = yStartingIndex;
    }

    public int getyStartingIndex() {
        return yStartingIndex;
    }

    public void initialize() {
        backgroundPane.setOnMouseClicked(event -> System.out.println(event.getX() + " " + event.getY()));
        showMap();
    }
    private void setVisionStatuses(){
        Civilization civilization = GameController.getCivilization();
        TileStatus[][] previousStatuses = new TileStatus[Game.getInstance().getMapSize()][Game.getInstance().getMapSize()];
        for (int i = 0; i < Game.getInstance().getMapSize(); i++) {
            for(int j=0;j<Game.getInstance().getMapSize();j++){
                previousStatuses[i][j] = civilization.getTileVisionStatuses()[i][j];
            }
        }
        for (int i = 0; i < Game.getInstance().getMapSize(); i++)
            for (int j = 0; j < Game.getInstance().getMapSize(); j++)
                civilization.getTileVisionStatuses()[i][j] = TileStatus.FOGGY;

        for (Unit unit : civilization.getUnits()) {
            ArrayList<Tile> clearTiles = new ArrayList<>((unit.getTile().getNeighbors()));
            if (!unit.getType().hasLimitedVisibility()) {
                int clearTileLength = clearTiles.size();
                for (int i = 0; i < clearTileLength; i++)
                    clearTiles.addAll((clearTiles.get(i).getNeighbors()));
            }
            for (Tile tileNeighbor : clearTiles)
                civilization.getTileVisionStatuses()[tileNeighbor.getIndexInMapI()][tileNeighbor.getIndexInMapJ()] = TileStatus.CLEAR;
        }
        for (City city : civilization.getCities()) {
            ArrayList<Tile> clearTiles = new ArrayList<>();
            for (Tile tile : city.getTiles()) {
                clearTiles.add(tile);
                clearTiles.addAll(tile.getNeighbors());
            }
            for (Tile tileNeighbor : clearTiles)
                civilization.getTileVisionStatuses()[tileNeighbor.getIndexInMapI()][tileNeighbor.getIndexInMapJ()] = TileStatus.CLEAR;
        }

        for (int i = 0; i < Game.getInstance().getMapSize(); i++)
            for (int j = 0; j < Game.getInstance().getMapSize(); j++)
                if ((previousStatuses[i][j].equals(TileStatus.CLEAR) || previousStatuses[i][j].equals(TileStatus.DISCOVERED))
                        && civilization.getTileVisionStatuses()[i][j].equals(TileStatus.FOGGY))
                    civilization.getTileVisionStatuses()[i][j] = TileStatus.DISCOVERED;

    }

    public void showCity(Tile tile, int i, int j) {
        if (tile.getCity() != null && tile.getCity().getTiles().get(0).equals(tile)) {
            ImageView imageView;
            if (tile.getCity() != null && tile.getCity().getBuildings().size() >= 30)
                imageView = new ImageView(new Image("Pictures/City/4.jpg"));
            else if (tile.getCity() != null && tile.getCity().getBuildings().size() >= 23)
                imageView = new ImageView(new Image("Pictures/City/3.jpg"));
            else if (tile.getCity() != null && tile.getCity().getBuildings().size() >= 15)
                imageView = new ImageView(new Image("Pictures/City/2.jpg"));
            else if (tile.getCity() != null && tile.getCity().getBuildings().size() >= 7)
                imageView = new ImageView(new Image("Pictures/City/1.jpg"));
            else imageView = new ImageView(new Image("Pictures/City/0.jpg"));
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
            imageView.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 30);
            imageView.setY(105 * (i - xStartingIndex) + 40);
            backgroundPane.getChildren().add(imageView);
        }
    }
    public void showMap() {
        backgroundPane.getChildren().
                removeAll(backgroundPane.getChildren());
        setVisionStatuses();
        boolean flag1 = true;
        boolean flag2 = true;
        for (int i = xStartingIndex; i < xStartingIndex + 9; i++) {
            if (i > 0 && flag1) {
                i = xStartingIndex - 1;
            }
            flag1 = false;
            for (int j = yStartingIndex; j < yStartingIndex + 14; j++) {
                if (j > 0 && flag2) {
                    j = yStartingIndex - 1;
                }
                flag2 = false;
                Tile tile = Game.getInstance().getTiles()[i][j];
                showTile(tile, i, j);
                if(GameController.getCivilization().getTileVisionStatuses()[i][j] != TileStatus.FOGGY) {
                    showRiverAndDelta(tile, i, j);
                    if(GameController.getCivilization().getTileVisionStatuses()[i][j] == TileStatus.CLEAR) {
                        showResourceAndImprovements(tile, i, j);
                        showCities(tile, i, j);
                        showRuins(tile, i, j);
                    }
                }
            }
            flag2 = true;
        }
        showTileContentIfNeeded();
        showUnits();
        showStatusBar();
        showUserPanelDownLeft();
        showChangeTurnSymbols();
    }

//    private void showFogStatus() {
//        Image image = new Image("Pictures/tiles/cloud.png");
//        for(int i=0;i<Game.getInstance().getMapSize();i++){
//            for(int j=0;j<Game.getInstance().getMapSize();j++){
//                if(GameController.getCivilization().getTileVisionStatuses()[i][j] == TileStatus.FOGGY){
//                    ImageView imageView = new ImageView(image);
//                    imageView.setX(120 * (j-yStartingIndex) + (i%2) * 60 - 10);
//                    imageView.setY(105 * (i-xStartingIndex));
//                    backgroundPane.getChildren().add(imageView);
//                }
//            }
//        }
//    }

    public void showCities(Tile tile, int i, int j){
        if(tile.getCity() != null && tile.getCity().getTiles().get(0).equals(tile)){
            ImageView imageView1 = new ImageView(new Image("Pictures/tiles/City0.png"));
            imageView1.setFitWidth(100);
            imageView1.setFitHeight(100);
            imageView1.setX(tile.getX() + 20);
            imageView1.setY(tile.getY() + 30);
            backgroundPane.getChildren().add(imageView1);
        }
    }

    public void showTileContentIfNeeded() {
        if (tileImageViews.size() > 0) {//not tested
            showTilesFoodProductionGold();
        }
        if (citizenImageViews.size() > 0) {
            showCitizens();
        }
    }

    public void showUserPanelDownLeft() {
        ImageView imageView = new ImageView(new Image("Pictures/Panels/myCiv.png"));
        imageView.setFitHeight(250);
        imageView.setX(0);
        imageView.setY(900 - imageView.getLayoutBounds().getHeight());
        backgroundPane.getChildren().add(imageView);
        if (chosenUnit != null) {
            showUnitAvatar();
            if(chosenUnit.getType().isCivilian()) showCivilianOptions();
            else showMilitaryOptions();
        }
        System.out.println(imageView.getLayoutBounds().getWidth());
    }

    public void showUnitAvatar() {
        String picture = chosenUnit.getType().toString();
        unitAvatarImageView = new ImageView(new Image("Pictures/units/" + picture + ".png"));
        unitAvatarImageView.setFitHeight(160);
        unitAvatarImageView.setFitWidth(160);
        unitAvatarImageView.setX(30);
        unitAvatarImageView.setY(675);
        backgroundPane.getChildren().add(unitAvatarImageView);
        String remainingMoves = String.valueOf(chosenUnit.getMP() - chosenUnit.getMovesInTurn());
        String totalMoves = String.valueOf(chosenUnit.getMP());
        movesLabel = new Label("remaining moves: " + remainingMoves + "/" + totalMoves);
        movesLabel.setStyle("-fx-text-fill: white; -fx-font-size: 30;");
        movesLabel.setLayoutX(20);
        movesLabel.setLayoutY(835);
        backgroundPane.getChildren().add(movesLabel);
    }

    public void hideUnitAvatar() {
        if (unitAvatarImageView != null && backgroundPane.getChildren().contains(unitAvatarImageView)) {
            backgroundPane.getChildren().remove(unitAvatarImageView);
            backgroundPane.getChildren().remove(movesLabel);
        }
    }
    public void showTile(Tile tile,int i, int j){
        Image image = new Image("Pictures/tiles/cloud.png");
        String picture;
        if (GameController.getCivilization().getTileVisionStatuses()[i][j] == TileStatus.FOGGY) {
            tile.setImage(image);
            tile.setFitWidth(image.getWidth());
            tile.setFitHeight(image.getHeight());
            tile.setX(120 * (j-yStartingIndex) + (i%2) * 60 - 10);
            tile.setY(105 * (i-xStartingIndex));
            backgroundPane.getChildren().add(tile);
            return;
        }
        if (tile.getFeature() == TerrainFeature.MARSH) {
            if (tile.getType() == TerrainType.PLAIN) picture = "PLAIN_MARSH";
            else picture = "GRASS_MARSH";
        } else if (tile.getFeature() != TerrainFeature.NONE && tile.getFeature() != TerrainFeature.DELTA)
            picture = tile.getFeature().toString();
        else picture = tile.getType().toString();
        tile.setImage(new Image("Pictures/tiles/" + picture + ".png"));
        tile.setX(120 * (j - yStartingIndex) + (i % 2) * 60);
        tile.setY(105 * (i - xStartingIndex));
        tile.setFitHeight(140);
        tile.setFitWidth(120);
        if(GameController.getCivilization().getTileVisionStatuses()[i][j] == TileStatus.DISCOVERED){
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(-0.4);
            tile.setEffect(colorAdjust);
        }
        backgroundPane.getChildren().add(tile);
    }

    public void showRiverAndDelta(Tile tile, int i, int j) {
        if (tile.getFeature() == TerrainFeature.DELTA) {
            ImageView imageView1 = new ImageView(new Image("Pictures/tiles/DELTA.png"));
            imageView1.setX(120 * (j - yStartingIndex) + (i % 2) * 60);
            imageView1.setY(105 * (i - xStartingIndex));
            if(GameController.getCivilization().getTileVisionStatuses()[i][j] == TileStatus.DISCOVERED){
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness(-0.4);
                tile.setEffect(colorAdjust);
            }
            backgroundPane.getChildren().add(imageView1);
        }
        if (tile.isRiverAtLeft()) {
            String str;
            if (i % 2 == 1) str = "RIVER_LL";
            else str = "RIVER_L";
            ImageView imageView2 = new ImageView(new Image("Pictures/tiles/" + str + ".png"));
            imageView2.setX(120 * (j - yStartingIndex) + (i % 2) * 60);
            imageView2.setY(105 * (i - xStartingIndex));
            if(GameController.getCivilization().getTileVisionStatuses()[i][j] == TileStatus.DISCOVERED){
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness(-0.4);
                tile.setEffect(colorAdjust);
            }
            backgroundPane.getChildren().add(imageView2);
        }
    }

    public void showResourceAndImprovements(Tile tile, int i, int j) {
        if (tile.getResource() != Resource.NONE) {
            ImageView imageView1 = new ImageView
                    (new Image("Pictures/resources/" + tile.getResource().toString() + ".png"));
            imageView1.setFitWidth(60);
            imageView1.setFitHeight(60);
            imageView1.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 10);
            imageView1.setY(105 * (i - xStartingIndex) + 10);
            backgroundPane.getChildren().add(imageView1);
        }
        if (tile.canUseItsResource()) {
            ImageView improvementImage = new ImageView(new Image("Pictures/improvments/"
                    + tile.getResource().getPrerequisiteImprovement().toString() + ".png"));
            improvementImage.setFitHeight(60);
            improvementImage.setFitWidth(60);
            improvementImage.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 60);
            improvementImage.setY(105 * (i - xStartingIndex) + 10);
            backgroundPane.getChildren().add(improvementImage);
        } else if (tile.getImprovementInProgress() != null) {
            ImageView improvementImage = new ImageView(new Image("Pictures/improvments/inProgress.png"));
            improvementImage.setFitHeight(20);
            improvementImage.setFitWidth(20);
            improvementImage.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 20);
            improvementImage.setY(105 * (i - xStartingIndex) + 25);
            backgroundPane.getChildren().add(improvementImage);
        }
    }

    public void showRuins(Tile tile, int i, int j) {
        if (tile.isRuined()) {
            ImageView imageView1 = new ImageView(new Image("Pictures/tiles/ruins.png"));
            imageView1.setFitWidth(80);
            imageView1.setFitHeight(80);
            imageView1.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 30);
            imageView1.setY(105 * (i - xStartingIndex) + 40);
            backgroundPane.getChildren().add(imageView1);
        }
    }
    public void showUnits(){
        for(int i=xStartingIndex;i<xStartingIndex+9;i++){
            for(int j=yStartingIndex;j<yStartingIndex+14;j++){
                Tile tile = Game.getInstance().getTiles()[i][j];
                if(GameController.getCivilization().getTileVisionStatuses()[i][j] != TileStatus.CLEAR) continue;
                if (tile.getCivilian() != null) {
                    tile.getCivilian().setX(tile.getX() + 65);
                    tile.getCivilian().setY(tile.getY() + 40);
                    backgroundPane.getChildren().add(tile.getCivilian());
                }
                if (tile.getMilitary() != null) {
                    tile.getMilitary().setX(tile.getX() + 10);
                    tile.getMilitary().setY(tile.getY() + 40);
                    backgroundPane.getChildren().add(tile.getMilitary());
                }
            }
        }
    }

    public void showStatusBar() {//TODO ADD TEXT BOXES
        ImageView imageView = new ImageView(new Image("Pictures/Panels/statusBar.png"));
        backgroundPane.getChildren().add(imageView);
        ImageView[] imageViews = new ImageView[4];
        imageViews[0] = new ImageView(new Image("Pictures/Panels/Science.png"));
        imageViews[1] = new ImageView(new Image("Pictures/Panels/Gold.png"));
        imageViews[2] = new ImageView(new Image("Pictures/Panels/Happiness.png"));
        imageViews[3] = new ImageView(new Image("Pictures/Panels/Turn.png"));
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].setFitHeight(40);
            imageViews[i].setFitWidth(40);
            imageViews[i].setY(10);
            imageViews[i].setX(40 + 140 * i);
            if (i == 3) imageViews[i].setX(imageViews[i].getX() + 700);
            backgroundPane.getChildren().add(imageViews[i]);
        }

        Label[] labels = new Label[4];
        labels[0] = new Label(String.valueOf(GameController.getCivilization().getScience()));
        labels[1] = new Label(String.valueOf(GameController.getCivilization().getTotalGold()));
        labels[2] = new Label(String.valueOf(GameController.getCivilization().getHappiness()));
        labels[3] = new Label(String.valueOf(Game.getInstance().getTurn()));
        for (int i = 0; i < labels.length; i++) {
            labels[i].setLayoutY(5);
            labels[i].setLayoutX(40 + 140 * i + 50);
            labels[i].setStyle("-fx-text-fill: white; -fx-font-size: 30;");
            if (i == 3) labels[i].setLayoutX(labels[i].getLayoutX() + 700);
            backgroundPane.getChildren().add(labels[i]);
        }

    }

    public void showTilesFoodProductionGold() {//not tested
        tileImageViews = new ArrayList<>();
        boolean flag1 = true;
        boolean flag2 = true;
        for (int i = xStartingIndex; i < xStartingIndex + 9; i++) {
            if (i > 0 && flag1) {
                i = xStartingIndex - 1;
            }
            flag1 = false;
            for (int j = yStartingIndex; j < yStartingIndex + 14; j++) {
                if (j > 0 && flag2) {
                    j = yStartingIndex - 1;
                }
                flag2 = false;
                Tile tile = Game.getInstance().getTiles()[i][j];
                if(GameController.getCivilization().getTileVisionStatuses()[i][j] != TileStatus.CLEAR) continue;
                if (tile.getFoodPerTurn() > 0) {
                    String food = "Food" + String.valueOf(tile.getFoodPerTurn()) + ".png";
                    ImageView IFood = new ImageView(new Image("Pictures/tiles/" + food));
                    IFood.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 10);
                    IFood.setY(105 * (i - xStartingIndex) + 50);
                    tileImageViews.add(IFood);
                }
                if (tile.getGoldPerTurn() > 0) {
                    String gold = "Gold" + String.valueOf(tile.getGoldPerTurn()) + ".png";
                    ImageView IGold = new ImageView(new Image("Pictures/tiles/" + gold));
                    IGold.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 60);
                    IGold.setY(105 * (i - xStartingIndex) + 70);
                    tileImageViews.add(IGold);
                }
                if (tile.getProductionPerTurn() > 0) {
                    String production = "Production" + String.valueOf(tile.getProductionPerTurn()) + ".png";
                    ImageView IProduction = new ImageView(new Image("Pictures/tiles/" + production));
                    IProduction.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 40);
                    IProduction.setY(105 * (i - xStartingIndex) + 10);
                    tileImageViews.add(IProduction);
                }

                if (tile.getMilitary() != null) {
                    tile.getMilitary().setX(tile.getX() + 10);
                    tile.getMilitary().setY(tile.getY() + 40);
                    backgroundPane.getChildren().add(tile.getMilitary());
                }

                if (tile.getCivilian() != null) {
                    tile.getCivilian().setX(tile.getX() + 65);
                    tile.getCivilian().setY(tile.getY() + 40);
                    backgroundPane.getChildren().add(tile.getCivilian());
                }

            }
            flag2 = true;
        }
        backgroundPane.getChildren().addAll(tileImageViews);
    }

    public void hideTilesFoodProductionGold() {//not tested
        backgroundPane.getChildren().removeAll(tileImageViews);
    }

    public void showCitizens() {
        citizenImageViews = new ArrayList<>();
        boolean flag1 = true;
        boolean flag2 = true;
        for (int i = xStartingIndex; i < xStartingIndex + 9; i++) {
            if (i > 0 && flag1) {
                i = xStartingIndex - 1;
            }
            flag1 = false;
            for (int j = yStartingIndex; j < yStartingIndex + 14; j++) {
                if (j > 0 && flag2) {
                    j = yStartingIndex - 1;
                }
                flag2 = false;
                Tile tile = Game.getInstance().getTiles()[i][j];
                if (tile.getWorkingCitizen() != null) {
                    ImageView imageView = new ImageView(new Image("Pictures/tiles/Population.png"));
                    imageView.setFitWidth(30);
                    imageView.setFitHeight(30);
                    imageView.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 40);
                    imageView.setY(105 * (i - xStartingIndex) + 50);
                    citizenImageViews.add(imageView);
                }
            }
            flag2 = true;
        }
        backgroundPane.getChildren().addAll(citizenImageViews);
    }

    public void hideCitizen() {
        backgroundPane.getChildren().removeAll(citizenImageViews);
    }

    public void nextTurn() {
        GameController.updateGame();
        hideUnitAvatar();
        hideUnitOptions();
        chosenUnit = null;
        showMap();
    }

    public void showCivilianOptions() {
        unitOptionsNodes = new ArrayList<>();
        showCivAndMilSameOptions();
        HBox hBox = new HBox();
        hBox.setLayoutY(900 - 70 - 70);
        hBox.setLayoutX(456);
        if(chosenUnit.getType() == UnitType.WORKER) workerExclusiveOptions(hBox);
        else settlerExclusiveOptions(hBox);
        hBox.setStyle("-fx-background-color: rgba(216,118,118,0.87); -fx-background-radius: 10;");
        unitOptionsNodes.add(hBox);
        backgroundPane.getChildren().addAll(unitOptionsNodes);
    }
    private void workerExclusiveOptions(HBox hBox){
    }
    private void settlerExclusiveOptions(HBox hBox){
        ImageView imageView = new ImageView(new Image("Pictures/unitIcons/CityState.png"));
        imageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                imageView.setOpacity(0.3);
            }
        });
        imageView.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                imageView.setOpacity(1);
            }
        });
        imageView.setOnMouseClicked(event -> {
            UnitController.setUnit(chosenUnit, Commands.FOUND_CITY.getRegex());
            UnitController.handleUnitOptions();
            ImageView imageView1 = new ImageView(new Image("Pictures/tiles/City0.png"));
            imageView1.setFitWidth(100);
            imageView1.setFitHeight(100);
            imageView1.setX(chosenUnit.getX() - 45);
            imageView1.setY(chosenUnit.getY() - 10);
            backgroundPane.getChildren().add(imageView1);
            backgroundPane.getChildren().remove(chosenUnit);
            hideUnitOptions();
            hideUnitAvatar();
            chosenUnit = null;
        });
        imageView.setFitWidth(70);
        imageView.setFitHeight(70);
        hBox.getChildren().add(imageView);
    }
    public void showMilitaryOptions() {
        unitOptionsNodes = new ArrayList<>();
        showCivAndMilSameOptions();
        backgroundPane.getChildren().addAll(unitOptionsNodes);
    }
    private void showCivAndMilSameOptions(){
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setLayoutY(900 - 70);
        hBox.setLayoutX(456);
        hBox.setStyle("-fx-background-color: rgba(216,118,118,0.87); -fx-background-radius: 0 20 20 0;");
        ImageView[] imageViews = new ImageView[4];
        imageViews[0] = new ImageView(new Image("Pictures/unitIcons/Load.png"));
        imageViews[1] = new ImageView(new Image("Pictures/unitIcons/Sleep.png"));
        if(chosenUnit.getStatus() == UnitStatus.SLEEP)
            imageViews[1] = new ImageView(new Image("Pictures/unitIcons/Quickstart.png"));
        imageViews[2] = new ImageView(new Image("Pictures/unitIcons/Stop.png"));
        imageViews[3] = new ImageView(new Image("Pictures/unitIcons/DisbandUnit.png"));
        imageViews[0].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                UnitController.setUnit(chosenUnit,Commands.DO_NOTHING.getRegex());
                UnitController.handleUnitOptions();
                chosenUnit = null;
                showMap();
            }
        });
        imageViews[1].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(chosenUnit.getStatus() == UnitStatus.SLEEP){
                    UnitController.setUnit(chosenUnit,Commands.WAKE_UNIT.getRegex());
                    UnitController.handleUnitOptions();
                    chosenUnit = null;
                    showMap();
                }else{
                    UnitController.setUnit(chosenUnit,Commands.SLEEP_UNIT.getRegex());
                    UnitController.handleUnitOptions();
                    chosenUnit = null;
                    showMap();
                }
            }
        });
        imageViews[2].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                UnitController.setUnit(chosenUnit,Commands.CANCEL_MISSION.getRegex());
                UnitController.handleUnitOptions();
                chosenUnit = null;
                showMap();
            }
        });
        imageViews[3].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                UnitController.setUnit(chosenUnit,Commands.DELETE.getRegex());
                UnitController.handleUnitOptions();
                hideUnitOptions();
                hideUnitAvatar();
                chosenUnit = null;
                showMap();
            }
        });
        for(int i=0;i<imageViews.length;i++){
            imageViews[i].setFitHeight(70);
            imageViews[i].setFitWidth(70);
            int a = i;
            imageViews[i].setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    imageViews[a].setOpacity(0.3);
                }
            });
            imageViews[i].setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    imageViews[a].setOpacity(1);
                }
            });
            hBox.getChildren().add(imageViews[i]);
        }

        unitOptionsNodes.add(hBox);
    }
    public void hideUnitOptions(){
        backgroundPane.getChildren().removeAll(unitOptionsNodes);
    }

    private void showChangeTurnSymbols() {
        Button nextTurn = new Button("Next Turn");
        nextTurn.setLayoutX(1450);
        nextTurn.setLayoutY(820);
        nextTurn.getStylesheets().add("css/MapStyle.css");
        nextTurn.getStyleClass().add("nextTurn");
        nextTurn.setOnMouseClicked(event -> nextTurn());
        backgroundPane.getChildren().add(nextTurn);

        if (techIconMustBeShown()) {
            ImageView techImgView = new ImageView(new Image("/Pictures/Map/research.png"));
            techImgView.setX(1480);
            techImgView.setY(730);
            techImgView.setStyle("-fx-cursor: hand;");
            setMouseClicksForIcon(techImgView);
            techImgView.setOnMouseClicked(event -> {
                try {
                    Scene scene = new Scene(FXMLLoader.load(this.getClass().getResource("/fxml/ChooseResearchPage.fxml")));
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                }
                catch (IOException e) {
                    System.out.println("failed to load research fxml");
                    e.printStackTrace();
                }
            });
            backgroundPane.getChildren().add(techImgView);
        }

        City c;
        if ((c = productionIconMustBeShown()) != null) {
            ImageView prodImgView = new ImageView(new Image("/Pictures/Map/production.png"));
            prodImgView.setX(1480);
            prodImgView.setY(640);
            prodImgView.setStyle("-fx-cursor: hand;");
            Tooltip.install(prodImgView, new Tooltip(c.getName()));
            setMouseClicksForIcon(prodImgView);
            prodImgView.setOnMouseClicked(event -> {
                try {
                    ChooseProductionPageController.setCity(c);
                    Scene scene = new Scene(FXMLLoader.load(this.getClass().getResource("/fxml/ChooseProductionPage.fxml")));
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                }
                catch (IOException e) {
                    System.out.println("failed to load production fxml");
                    e.printStackTrace();
                }
            });
            backgroundPane.getChildren().add(prodImgView);
        }
    }

    private void setMouseClicksForIcon (ImageView imageView) {
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        imageView.setOnMouseEntered(event -> {
            imageView.setX(imageView.getX() - 5);
            imageView.setY(imageView.getY() - 5);
            imageView.setFitWidth(90);
            imageView.setFitHeight(90);
        });
        imageView.setOnMouseExited(event -> {
            imageView.setX(imageView.getX() + 5);
            imageView.setY(imageView.getY() + 5);
            imageView.setFitWidth(80);
            imageView.setFitHeight(80);
        });
    }

    private boolean techIconMustBeShown() {
        return GameController.getCivilization().getInProgressTech() == null && GameController.getCivilization().getCities().size() > 0;
    }

    private City productionIconMustBeShown() {
        for (City city : GameController.getCivilization().getCities())
            if (city.getInProgressUnit() == null)
                return city;
        return null;
    }

}
