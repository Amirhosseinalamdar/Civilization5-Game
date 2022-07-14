package View.Controller;

import Controller.GameController;
import Controller.UnitController;
import Model.*;
import Model.Map.*;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;


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

    private void setVisionStatuses() {
        Civilization civilization = GameController.getCivilization();
        TileStatus[][] previousStatuses = new TileStatus[Game.getInstance().getMapSize()][Game.getInstance().getMapSize()];
        for (int i = 0; i < Game.getInstance().getMapSize(); i++) {
            for (int j = 0; j < Game.getInstance().getMapSize(); j++) {
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
                if (GameController.getCivilization().getTileVisionStatuses()[i][j] != TileStatus.FOGGY) {
                    showRiverAndDelta(tile, i, j);
                    if (GameController.getCivilization().getTileVisionStatuses()[i][j] == TileStatus.CLEAR) {
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
        showTileInfoButton();
    }

    private void showTileInfoButton() {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.85);
        ImageView imageView = new ImageView(new Image("Pictures/Icons/Hexagon.png"));//TODO
        imageView.setEffect(colorAdjust);
        imageView.setX(1390);
        imageView.setY(825);
        imageView.setFitWidth(45);
        imageView.setFitHeight(40);
        setImageViewOpacity(imageView);
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (tileImageViews.size() == 0) {
                    showTilesFoodProductionGold();
                    showCitizens();
                    showMap();
                }else{
                    hideCitizen();
                    hideTilesFoodProductionGold();
                    showMap();
                }
            }
        });
        backgroundPane.getChildren().add(imageView);
    }

    private void setImageViewOpacity(ImageView imageView) {
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
    }



    public void showCities(Tile tile, int i, int j) {
        if (tile.getCity() != null && tile.getCity().getTiles().get(0).equals(tile)) {
            ImageView imageView1 = new ImageView(ImageBase.CITY_0.getImage());
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
        ImageView imageView = new ImageView(ImageBase.LEFT_DOWN_PANEL_BOX.getImage());
        imageView.setFitHeight(250);
        imageView.setX(0);
        imageView.setY(900 - imageView.getLayoutBounds().getHeight());
        backgroundPane.getChildren().add(imageView);
        if (chosenUnit != null) {
            showUnitAvatar();
            if (chosenUnit.getType().isCivilian()) showCivilianOptions();
            else showMilitaryOptions();
        }
        System.out.println(imageView.getLayoutBounds().getWidth());
    }

    public void showUnitAvatar() {
        unitAvatarImageView = new ImageView(chosenUnit.getType().getImage());
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

    public void showTile(Tile tile, int i, int j) {
        Image image;
        ColorAdjust colorAdjust = new ColorAdjust();
        if (GameController.getCivilization().getTileVisionStatuses()[i][j] == TileStatus.FOGGY) {
            image = ImageBase.CLOUD.getImage();
            tile.setImage(image);
            tile.setFitWidth(image.getWidth());
            tile.setFitHeight(image.getHeight());
            tile.setX(120 * (j - yStartingIndex) + (i % 2) * 60 - 10);
            tile.setY(105 * (i - xStartingIndex));
            colorAdjust.setBrightness(0);
            tile.setEffect(colorAdjust);
            backgroundPane.getChildren().add(tile);
            return;
        }
        if (tile.getFeature() == TerrainFeature.MARSH) {
            if (tile.getType() == TerrainType.PLAIN) image = ImageBase.PLAIN_MARSH.getImage();
            else image = ImageBase.GRASS_MARSH.getImage();
        } else if (tile.getFeature() != TerrainFeature.NONE && tile.getFeature() != TerrainFeature.DELTA)
            image = tile.getFeature().getImage();
        else image = tile.getType().getImage();
        tile.setImage(image);
        tile.setX(120 * (j - yStartingIndex) + (i % 2) * 60);
        tile.setY(105 * (i - xStartingIndex));
        tile.setFitHeight(140);
        tile.setFitWidth(120);
        setDiscoveredTileBrightness(tile,i,j,colorAdjust);
        backgroundPane.getChildren().add(tile);
    }
    private void setDiscoveredTileBrightness(Tile tile, int i, int j,ColorAdjust colorAdjust){
        if (GameController.getCivilization().getTileVisionStatuses()[i][j] == TileStatus.DISCOVERED) {
            colorAdjust.setBrightness(-0.4);
            tile.setEffect(colorAdjust);
        } else {
            colorAdjust.setBrightness(0);
            tile.setEffect(colorAdjust);
        }
    }

    public void showRiverAndDelta(Tile tile, int i, int j) {
        ColorAdjust colorAdjust = new ColorAdjust();
        if (tile.getFeature() == TerrainFeature.DELTA) {
            ImageView imageView1 = new ImageView(ImageBase.DELTA.getImage());
            imageView1.setX(120 * (j - yStartingIndex) + (i % 2) * 60);
            imageView1.setY(105 * (i - xStartingIndex));
            setDiscoveredTileBrightness(tile,i,j,colorAdjust);
            backgroundPane.getChildren().add(imageView1);
        }
        if (tile.isRiverAtLeft()) {
            Image image;
            if (i % 2 == 1) image = ImageBase.RIVER_O.getImage();
            else image = ImageBase.RIVER_E.getImage();
            ImageView imageView2 = new ImageView(image);
            imageView2.setX(120 * (j - yStartingIndex) + (i % 2) * 60);
            imageView2.setY(105 * (i - xStartingIndex));
            setDiscoveredTileBrightness(tile,i,j,colorAdjust);
            backgroundPane.getChildren().add(imageView2);
        }
    }

    public void showResourceAndImprovements(Tile tile, int i, int j) {
        if (tile.getResource() != Resource.NONE) {
            ImageView imageView1 = new ImageView
                    (tile.getResource().getImage());
            imageView1.setFitWidth(60);
            imageView1.setFitHeight(60);
            imageView1.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 10);
            imageView1.setY(105 * (i - xStartingIndex) + 10);
            backgroundPane.getChildren().add(imageView1);
        }
        if (tile.getImprovementInProgress() != null && tile.getImprovementInProgress().getValue() == 0) {
            ImageView improvementImage = new ImageView(tile.getImprovementInProgress().getKey().getImage());
            improvementImage.setFitHeight(60);
            improvementImage.setFitWidth(60);
            improvementImage.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 60);
            improvementImage.setY(105 * (i - xStartingIndex) + 10);
            backgroundPane.getChildren().add(improvementImage);
        } else if (tile.getImprovementInProgress() != null) {
            ImageView improvementImage = new ImageView(ImageBase.IN_PROGRESS_IMPROVEMENT.getImage());
            improvementImage.setFitHeight(20);
            improvementImage.setFitWidth(20);
            improvementImage.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 20);
            improvementImage.setY(105 * (i - xStartingIndex) + 25);
            backgroundPane.getChildren().add(improvementImage);
        }
    }

    public void showRuins(Tile tile, int i, int j) {
        if (tile.isRuined()) {
            ImageView imageView1 = new ImageView(ImageBase.RUINS.getImage());
            imageView1.setFitWidth(80);
            imageView1.setFitHeight(80);
            imageView1.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 30);
            imageView1.setY(105 * (i - xStartingIndex) + 40);
            backgroundPane.getChildren().add(imageView1);
        }
    }

    public void showUnits() {
        for (int i = xStartingIndex; i < xStartingIndex + 9; i++) {
            for (int j = yStartingIndex; j < yStartingIndex + 14; j++) {
                Tile tile = Game.getInstance().getTiles()[i][j];
                if (GameController.getCivilization().getTileVisionStatuses()[i][j] != TileStatus.CLEAR) continue;
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
        ImageView imageView = new ImageView(ImageBase.STATUSBAR_BOX.getImage());
        backgroundPane.getChildren().add(imageView);
        ImageView[] imageViews = new ImageView[4];
        imageViews[0] = new ImageView(ImageBase.SCIENCE_ICON.getImage());
        imageViews[1] = new ImageView(ImageBase.GOLD_ICON.getImage());
        imageViews[2] = new ImageView(ImageBase.HAPPINESS_ICON.getImage());
        imageViews[3] = new ImageView(ImageBase.TURN_ICON.getImage());
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
                if (GameController.getCivilization().getTileVisionStatuses()[i][j] != TileStatus.CLEAR) continue;
                if (tile.getFoodPerTurn() > 0) {
                    ImageView IFood = new ImageView(chooseFoodLevelImage(tile.getFoodPerTurn()));
                    IFood.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 10);
                    IFood.setY(105 * (i - xStartingIndex) + 50);
                    tileImageViews.add(IFood);
                }
                if (tile.getGoldPerTurn() > 0) {
                    ImageView IGold = new ImageView(chooseGoldLevelImage(tile.getGoldPerTurn()));
                    IGold.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 60);
                    IGold.setY(105 * (i - xStartingIndex) + 70);
                    tileImageViews.add(IGold);
                }
                if (tile.getProductionPerTurn() > 0) {
                    ImageView IProduction = new ImageView(chooseProductionLevelImage(tile.getProductionPerTurn()));
                    IProduction.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 40);
                    IProduction.setY(105 * (i - xStartingIndex) + 10);
                    tileImageViews.add(IProduction);
                }
            }
            flag2 = true;
        }
        backgroundPane.getChildren().addAll(tileImageViews);
    }
    private Image chooseFoodLevelImage(int key){
        if(key == 1) return ImageBase.FOOD_1.getImage();
        else if(key == 2) return ImageBase.FOOD_2.getImage();
        else if(key == 3) return ImageBase.FOOD_3.getImage();
        else if(key == 4) return ImageBase.FOOD_4.getImage();
        else return null;
    }
    private Image chooseProductionLevelImage(int key){
        if(key == 1) return ImageBase.PRODUCTION_1.getImage();
        else if(key == 2) return ImageBase.PRODUCTION_2.getImage();
        else if(key == 3) return ImageBase.PRODUCTION_3.getImage();
        else if(key == 4) return ImageBase.PRODUCTION_4.getImage();
        else return null;
    }
    private Image chooseGoldLevelImage(int key){
        if(key == 1) return ImageBase.GOLD_1.getImage();
        else if(key == 2) return ImageBase.GOLD_2.getImage();
        else if(key == 3) return ImageBase.GOLD_3.getImage();
        else if(key == 4) return ImageBase.GOLD_4.getImage();
        else return null;
    }
    public void hideTilesFoodProductionGold() {//not tested
        backgroundPane.getChildren().removeAll(tileImageViews);
        tileImageViews.removeAll(tileImageViews);
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
                    ImageView imageView = new ImageView(ImageBase.POPULATION.getImage());
                    imageView.setFitWidth(30);
                    imageView.setFitHeight(30);
                    imageView.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 50);
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
        citizenImageViews.removeAll(citizenImageViews);
    }

    public void nextTurn() {
        GameController.updateGame();
        hideUnitAvatar();
        hideUnitOptions();
        chosenUnit = null;
        showMap();
    }

    private void workerExclusiveOptions(HBox hBox) {
        ArrayList<ImageView> imageViews = new ArrayList<>();
        imageViews.add(new ImageView(ImageBase.AXE_ICON.getImage()));
        clearLandButton(imageViews.get(0));
        imageViews.add(new ImageView(ImageBase.HAMMER_ICON.getImage()));
        repairButton(imageViews.get(1));
        roadButton(imageViews);
        railroadButton(imageViews);
        improvementButton(imageViews,"FARM");
        improvementButton(imageViews,"MINE");
        improvementButton(imageViews,"CAMP");
        improvementButton(imageViews,"PASTURE");
        improvementButton(imageViews,"LUMBER_MILL");
        improvementButton(imageViews,"PLANTATION");
        improvementButton(imageViews,"QUARRY");
        improvementButton(imageViews,"TRADING_POST");
        for (ImageView imageView : imageViews) {
            setImageViewOpacity(imageView);
            imageView.setFitWidth(70);
            imageView.setFitHeight(70);
        }
        hBox.getChildren().addAll(imageViews);
    }

    private void repairButton(ImageView imageView) {
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(chosenUnit.getTile().getImprovementInProgress() != null) {
                    UnitController.setUnit(chosenUnit, "repair -i " + chosenUnit.getTile().getImprovementInProgress().getKey());
                    UnitController.handleUnitOptions();
                    showMap();
                }else System.out.println("repair nakardam (giga chad)");
            }
        });
    }

    private void clearLandButton(ImageView imageView) {
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(chosenUnit.getTile().getFeature() == TerrainFeature.FOREST) {
                    UnitController.setUnit(chosenUnit, "clear forest");
                    UnitController.handleUnitOptions();
                    showMap();
                }else if(chosenUnit.getTile().getFeature() == TerrainFeature.JUNGLE){
                    UnitController.setUnit(chosenUnit, "clear jungle");
                    UnitController.handleUnitOptions();
                    showMap();
                }else if(chosenUnit.getTile().getRouteInProgress() != null &&
                chosenUnit.getTile().getRouteInProgress().getKey().equals("road")){
                    UnitController.setUnit(chosenUnit, "clear road");
                    UnitController.handleUnitOptions();
                    showMap();
                }else if(chosenUnit.getTile().getRouteInProgress() != null &&
                        chosenUnit.getTile().getRouteInProgress().getKey().equals("railroad")){
                    UnitController.setUnit(chosenUnit, "clear railroad");
                    UnitController.handleUnitOptions();
                    showMap();
                }else System.out.println("clear nakardam (giga chad)");
            }
        });
    }

    private void improvementButton(ArrayList<ImageView> imageViews,String string){
        if(UnitController.canBuildImprovementHere(Improvement.valueOf(string))){
            ImageView imageView = new ImageView(ImageBase.valueOf(string+"_ICON").getImage());
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    UnitController.setUnit(chosenUnit,"build improvement -t "+string);//TODO SHOW THE ERR TO USER
                    UnitController.handleUnitOptions();
                    showMap();
                }
            });
            imageViews.add(imageView);
        }
    }
    private void roadButton(ArrayList<ImageView> imageViews){
        if(UnitController.canBuildRoadHere()){
            ImageView imageView = new ImageView(ImageBase.ROAD_ICON.getImage());
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    UnitController.setUnit(chosenUnit,"build improvement -t ROAD");
                    UnitController.handleUnitOptions();
                    showMap();
                }
            });
            imageViews.add(imageView);
        }
    }
    private void railroadButton(ArrayList<ImageView> imageViews){
        if(UnitController.getCivilization().hasReachedTech(Technology.RAILROAD)
                && UnitController.canBuildRailroadHere()){
            ImageView imageView = new ImageView(ImageBase.RAIL_ROAD_ICON.getImage());
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    UnitController.setUnit(chosenUnit,"build improvement -t RAILROAD");
                    UnitController.handleUnitOptions();
                    showMap();
                }
            });
            imageViews.add(imageView);
        }
    }

//    private boolean canShowWorkerDecision(Improvement improvement){
//        if(improvement.getPrerequisiteTypes() == null && improvement.getPrerequisiteFeatures() == null)
//            return false;
//        else if(improvement.getPrerequisiteTypes() == null)
//            return improvement.getPrerequisiteFeatures().contains(chosenUnit.getTile().getFeature());
//        else if(improvement.getPrerequisiteFeatures() == null)
//            return improvement.getPrerequisiteTypes().contains(chosenUnit.getTile().getType());
//        else
//            return improvement.getPrerequisiteTypes().contains(chosenUnit.getTile().getType()) ||
//                improvement.getPrerequisiteFeatures().contains(chosenUnit.getTile().getFeature());
//    }

    private void settlerExclusiveOptions(HBox hBox) {
        ImageView imageView = new ImageView(ImageBase.FOUND_CITY_ICON.getImage());
        setImageViewOpacity(imageView);
        imageView.setOnMouseClicked(event -> {
            int before = UnitController.getCivilization().getCities().size();
            UnitController.setUnit(chosenUnit, Commands.FOUND_CITY.getRegex());
            UnitController.handleUnitOptions();
            int after = UnitController.getCivilization().getCities().size();
            if (after == before + 1) {
                ImageView imageView1 = new ImageView(ImageBase.CITY_0.getImage());
                imageView1.setFitWidth(100);
                imageView1.setFitHeight(100);
                imageView1.setX(chosenUnit.getX() - 45);
                imageView1.setY(chosenUnit.getY() - 10);
                backgroundPane.getChildren().add(imageView1);
                backgroundPane.getChildren().remove(chosenUnit);
                hideUnitOptions();
                hideUnitAvatar();
                chosenUnit = null;
            }
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
    public void showCivilianOptions(){
        unitOptionsNodes = new ArrayList<>();
        showCivAndMilSameOptions();
        HBox hBox = new HBox();
        hBox.setLayoutY(900 - 70 - 70);
        hBox.setLayoutX(456);
        if (chosenUnit.getType() == UnitType.WORKER){
            workerExclusiveOptions(hBox);
            System.out.println("are chaghal");
        }
        else settlerExclusiveOptions(hBox);
        hBox.setStyle("-fx-background-color: rgba(216,118,118,0.87); -fx-background-radius: 0 20 0 0;");
        unitOptionsNodes.add(hBox);
        backgroundPane.getChildren().addAll(unitOptionsNodes);
    }
    private void showCivAndMilSameOptions() {
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setLayoutY(900 - 70);
        hBox.setLayoutX(456);
        hBox.setStyle("-fx-background-color: rgba(216,118,118,0.87); -fx-background-radius: 0 20 20 0;");
        ImageView[] imageViews = new ImageView[4];
        imageViews[0] = new ImageView(ImageBase.DO_NOTHING_ICON.getImage());
        imageViews[1] = new ImageView(ImageBase.SLEEP_ICON.getImage());
        if (chosenUnit.getStatus() == UnitStatus.SLEEP)
            imageViews[1] = new ImageView(ImageBase.ACTIVE_ICON.getImage());
        imageViews[2] = new ImageView(ImageBase.STOP_ICON.getImage());
        imageViews[3] = new ImageView(ImageBase.KILL_ICON.getImage());
        imageViews[0].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                UnitController.setUnit(chosenUnit, Commands.DO_NOTHING.getRegex());
                UnitController.handleUnitOptions();
                chosenUnit = null;
                showMap();
            }
        });
        imageViews[1].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (chosenUnit.getStatus() == UnitStatus.SLEEP) {
                    UnitController.setUnit(chosenUnit, Commands.WAKE_UNIT.getRegex());
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
                UnitController.setUnit(chosenUnit, Commands.CANCEL_MISSION.getRegex());
                UnitController.handleUnitOptions();
                chosenUnit = null;
                showMap();
            }
        });
        imageViews[3].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                UnitController.setUnit(chosenUnit, Commands.DELETE.getRegex());
                UnitController.handleUnitOptions();
                hideUnitOptions();
                hideUnitAvatar();
                chosenUnit = null;
                showMap();
            }
        });
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].setFitHeight(70);
            imageViews[i].setFitWidth(70);
            setImageViewOpacity(imageViews[i]);
            hBox.getChildren().add(imageViews[i]);
        }

        unitOptionsNodes.add(hBox);
    }

    public void hideUnitOptions() {
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
            ImageView techImgView = new ImageView(ImageBase.CHOOSE_RESEARCH_ICON.getImage());
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
            ImageView prodImgView = new ImageView(ImageBase.CHOOSE_PRODUCTION_ICON.getImage());
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
