package Client.View.Controller;

import Client.App.Main;
import Client.Controller.*;
import Client.Model.*;
import Client.Model.Map.*;
import Client.View.Commands;
import Client.Controller.*;
import Client.Model.*;
import Client.Model.Map.*;
import Client.Model.UnitPackage.Unit;
import Client.Model.UnitPackage.UnitStatus;
import Client.Model.UnitPackage.UnitType;
import Client.Transiton.NavigationTransition;
import Client.View.GameMenu;
import Server.Menu;
import com.google.gson.GsonBuilder;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class MapController {
    @FXML
    private Pane backgroundPane;
    private ImageView unitAvatarImageView;
    private Label movesLabel;
    private Unit chosenUnit;
    private City chosenCity;
    private Tile hoveredTile;
    private Tile chosenTarget;
    private boolean isEnded;

    public void setChosenTarget(Tile chosenTarget) {
        this.chosenTarget = chosenTarget;
    }

    public Tile getChosenTarget() {
        return chosenTarget;
    }

    public City getChosenCity() {
        return chosenCity;
    }

    private ArrayList<Node> unitOptionsNodes = new ArrayList<>();
    private final int cityPanelIconsSize = 100;

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

    public void setChosenCity(City chosenCity) {
        tileImageViews.clear();
        citizenImageViews.clear();
        this.chosenCity = chosenCity;
    }

    public void setHoveredTile(Tile tile) {
        this.hoveredTile = tile;
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
        if (tile.getCity() != null && tile.isCenterOfCity(tile.getCity())) {
            ImageView imageView = new ImageView(getCityImage(tile.getCity()));
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            imageView.setX(tile.getX() + 20);
            imageView.setY(tile.getY() + 30);
            if (tile.getCity().getCivilization().equals(GameController.getCivilization())) {
                Button openCityPanelButton = new Button(tile.getCity().getName().toUpperCase());
                openCityPanelButton.setLayoutX(tile.getX() + 18);
                openCityPanelButton.setLayoutY(tile.getY() - 5);
                openCityPanelButton.getStylesheets().add("css/MapStyle.css");
                openCityPanelButton.getStyleClass().add("openCityPanelButton");
                openCityPanelButton.setOnMouseClicked(mouseEvent -> {
                    if (tile.getCity().equals(chosenCity))
                        setChosenCity(null);
                    else if (tile.getCity().getCivilization().equals(GameController.getCivilization()))
                        chosenCity = tile.getCity();
                    showMap();
                });
                backgroundPane.getChildren().add(openCityPanelButton);
            }
            backgroundPane.getChildren().add(imageView);
            addCityHealthBar(tile.getCity(), imageView, tile);
        }
    }

    private void addCityHealthBar(City city, ImageView imageView, Tile tile) {
        Rectangle rectangle = new Rectangle(tile.getX() + 18, tile.getY() - 5 + 40
                , imageView.getFitWidth(), 10);
        rectangle.setFill(Color.BLACK);
        rectangle.setOpacity(0.7);
        backgroundPane.getChildren().add(rectangle);
        rectangle = new Rectangle(tile.getX() + 18, tile.getY() - 5 + 40
                , imageView.getFitWidth() * city.getHP() / getCityMaxHealth(city), 10);
        rectangle.setFill(city.getCivilization().getColor());
        rectangle.setOpacity(0.8);
        backgroundPane.getChildren().add(rectangle);
    }

    private Image getCityImage(City city) {
        if (city.getBuildings().size() >= 30)
            return ImageBase.CITY_4.getImage();
        else if (city.getBuildings().size() >= 23)
            return ImageBase.CITY_3.getImage();
        else if (city.getBuildings().size() >= 15)
            return ImageBase.CITY_2.getImage();
        else if (city.getBuildings().size() >= 7)
            return ImageBase.CITY_1.getImage();
        else return ImageBase.CITY_0.getImage();
    }

    public void showMap() {
        if (isEnded) return;
        backgroundPane.getChildren().clear();
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
                        showCity(tile, i, j);
                        showRuins(tile, i, j);
                    }
                }
            }
            flag2 = true;
        }
        showUnits();
        showTileContentIfNeeded();
        showChosenCityPanel();
        showStatusBar();
        showPanelsButtons();
        showChangeTurnSymbols();
        showTileInfoButton();
        if (chosenUnit != null) {
            showUserPanelDownLeft();
            showUnitAvatar();
            if (chosenUnit.getType().isCivilian()) showCivilianOptions();
            else showMilitaryOptions();
        }
        showHoveredTileInfo();
        showTechTreeButton();
        showDiplomacyPanelButton();
        showRequests();
    }

    private void showDiplomacyPanelButton() {
        if (GameController.getCivilization().getCities().size() == 0) return;
        ImageView openDiplomacyPanel = new ImageView(ImageBase.OPEN_DIPLOMACY_PANEL.getImage());
        openDiplomacyPanel.setFitWidth(80);
        openDiplomacyPanel.setFitHeight(80);
        openDiplomacyPanel.setX(1480);
        openDiplomacyPanel.setY(170);
        Tooltip.install(openDiplomacyPanel, new Tooltip("Diplomacy Panel"));

        Rectangle clip = new Rectangle(1480, 170, 80, 80);
        clip.setArcWidth(25);
        clip.setArcHeight(25);
        openDiplomacyPanel.setClip(clip);

        setUpperRightButton(openDiplomacyPanel, clip);
        openDiplomacyPanel.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader1 = new FXMLLoader(Main.class.getResource("/fxml/DiplomacyPanel.fxml"));
            Scene scene;
            try {
                Stage stage = new Stage();
                scene = new Scene(fxmlLoader1.load(), 800, 450);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setOnCloseRequest(windowEvent -> showMap());
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        backgroundPane.getChildren().add(openDiplomacyPanel);
    }

    private void showTechTreeButton() {
        if (GameController.getCivilization().getCities().size() == 0) return;
        ImageView openTechTree = new ImageView(ImageBase.OPEN_TECH_TREE.getImage());
        openTechTree.setFitWidth(80);
        openTechTree.setFitHeight(80);
        openTechTree.setX(1480);
        openTechTree.setY(70);
        Tooltip.install(openTechTree, new Tooltip("Tech Tree"));
        setUpperRightButton(openTechTree, null);
        openTechTree.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader1 = new FXMLLoader(Main.class.getResource("/fxml/TechTree.fxml"));
            Scene scene;
            try {
                Stage stage = new Stage();
                scene = new Scene(fxmlLoader1.load(), 1600, 900);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setOnCloseRequest(windowEvent -> showMap());
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        backgroundPane.getChildren().add(openTechTree);
    }

    private void setUpperRightButton(ImageView imageView, Rectangle clip) {
        imageView.setStyle("-fx-cursor:hand; -fx-font-family: 'Tw Cen MT'; -fx-font-size: 17;");
        imageView.setOnMouseEntered(mouseEvent -> {
            imageView.setX(imageView.getX() - 4);
            imageView.setY(imageView.getY() - 4);
            imageView.setFitWidth(imageView.getFitWidth() + 10);
            imageView.setFitHeight(imageView.getFitHeight() + 10);
            if (clip != null) {
                clip.setX(clip.getX() - 4);
                clip.setY(clip.getY() - 4);
                clip.setWidth(clip.getWidth() + 10);
                clip.setHeight(clip.getHeight() + 10);
            }
        });
        imageView.setOnMouseExited(mouseEvent -> {
            imageView.setX(imageView.getX() + 4);
            imageView.setY(imageView.getY() + 4);
            imageView.setFitWidth(imageView.getFitWidth() - 10);
            imageView.setFitHeight(imageView.getFitHeight() - 10);
            if (clip != null) {
                clip.setX(clip.getX() + 4);
                clip.setY(clip.getY() + 4);
                clip.setWidth(clip.getWidth() - 10);
                clip.setHeight(clip.getHeight() - 10);
            }
        });
    }

    private void showPanelsButtons() {
        if (chosenCity != null) return;
        VBox vBox = new VBox();
        vBox.setLayoutX(10);
        vBox.setLayoutY(70);
        vBox.setStyle("-fx-background-color: rgba(216,118,118,0.87); -fx-background-radius: 20; -fx-spacing: 10;");
        addButton(vBox, "NOTIFICATION_HISTORY_ICON");
        addButton(vBox, "DEMOGRAPHIC_PANEL_ICON");
        addButton(vBox, "ECONOMIC_PANEL_ICON");
        addButton(vBox, "MILITARY_OVERVIEW_PANEL_ICON");
        addButton(vBox, "UNITS_PANEL_ICON");
        addButton(vBox, "CITIES_PANEL_ICON");
        backgroundPane.getChildren().addAll(vBox);
    }

    private void addButton(VBox vBox, String string) {
        ImageView imageView = new ImageView(ImageBase.valueOf(string).getImage());
        imageView.setOnMouseClicked(event -> {
            if (string.equals("DEMOGRAPHIC_PANEL_ICON"))
                showDemographicPanel();
            else if (string.equals("NOTIFICATION_HISTORY_ICON"))
                showNotificationPanel();
            else if (string.equals("UNITS_PANEL_ICON"))
                showUnitsPanel();
            else if (string.equals("ECONOMIC_PANEL_ICON"))
                showEconomicPanel();
            else if (string.equals("MILITARY_OVERVIEW_PANEL_ICON"))
                showMilitaryOverView();
            else if (string.equals("CITIES_PANEL_ICON"))
                showCitiesPanel();
        });
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        setImageViewOpacity(imageView);
        Tooltip.install(imageView, new Tooltip(string.substring(0, string.length() - 5)));
        vBox.getChildren().add(imageView);
    }


    private void showMilitaryOverView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Game.getInstance().getClass().getResource("/fxml/MilitaryOverView.fxml"));
            Parent root = fxmlLoader.load();
            MilitaryOverViewController militaryOverViewController = fxmlLoader.getController();
            stageShower(root, "Military Overview", ImageBase.MILITARY_OVERVIEW_PANEL_ICON.getImage());
            VBox vBox = militaryOverViewController.getMainVBox();
            vBox.setStyle("-fx-spacing: 15;");
            vBox.setTranslateX(10);
            vBox.setTranslateY(10);
            showMilitaryInfo(vBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showMilitaryInfo(VBox vBox) {
        Civilization civilization = GameController.getCivilization();
        for (Unit unit : civilization.getUnits()) {
            if (unit.getType().isCivilian()) continue;
            String damage;
            if (unit.getType().getRangedCombatStrength() == 0) damage = unit.getType().getCombatStrength() + "";
            else damage = unit.getType().getRangedCombatStrength() + "";
            Text text = new Text(" " + "Name: " + unit.getType().name() + "  |  Status: " + unit.getStatus().name()
                    + "  |  Health: " + unit.getHealth() + "  |  Damage: " + damage + " ");
            text.setFill(Color.rgb(155, 183, 237));
            text.setStyle("-fx-font-size: 25;");
            ImageView imageView = new ImageView(unit.getImage());
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            HBox hBox = new HBox();
            hBox.setStyle("-fx-background-color: #740c3e; -fx-background-radius: 20;");
            hBox.getChildren().add(imageView);
            hBox.getChildren().add(text);
            vBox.getChildren().add(hBox);
        }

    }

    private void showDemographicPanel() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Game.getInstance().getClass().getResource("/fxml/DemographicPanel.fxml"));
            Parent root = fxmlLoader.load();
            DemographicPanelController demographicPanelController = fxmlLoader.getController();
            stageShower(root, "Demographics", ImageBase.DEMOGRAPHIC_PANEL_ICON.getImage());
            VBox vBox = demographicPanelController.getMainVBox();
            vBox.setStyle("-fx-spacing: 15;");
            vBox.setTranslateX(0);
            vBox.setTranslateY(40);
            showDemographicLists(vBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDemographicLists(VBox vBox) {
        Civilization civilization = GameController.getCivilization();
        HBox[] hBox = new HBox[5];
        Text[] texts = new Text[5];
        texts[0] = new Text(" Cities: " + civilization.getCities().size() + "  |  Best: " + GameController.findBestCity()
                + "  |  Average: " + GameController.findAverageCity() + "  |  Worst: " + GameController.findWorstCity()
                + "  |  Rank: " + GameController.findRankInCities() + " ");
        texts[1] = new Text("Gold: " + civilization.getTotalGold() + "  |  Best: " + GameController.findBestGold()
                + "  |  Average: " + GameController.findAverageGold() + "  |  Worst: " + GameController.findWorstGold()
                + "  |  Rank: " + GameController.findRankInGolds() + " ");
        texts[2] = new Text(" " + "Units: " + civilization.getUnits().size() + "  |  Best: " + GameController.findBestUnit()
                + "  |  Average: " + GameController.findAverageUnit() + "  |  Worst: " + GameController.findWorstUnit()
                + "  |  Rank: " + GameController.findRankInUnits() + " ");
        texts[3] = new Text(" " + "Science: " + civilization.getScience() + "  |  Best: " + GameController.findBestScience()
                + "  |  Average: " + GameController.findAverageScience() + "  |  Worst: " + GameController.findWorstScience()
                + "  |  Rank: " + GameController.findRankInScience() + " ");
        texts[4] = new Text(" " + "Happiness: " + civilization.getHappiness() + "  |  Best: " + GameController.findBestHappiness()
                + "  |  Average: " + GameController.findAverageHappiness() + "  |  Worst: " + GameController.findWorstHappiness()
                + "  |  Rank: " + GameController.findRankInHappiness() + " ");
        for (int j = 0; j < texts.length; j++) {
            texts[j].setFill(Color.rgb(155, 183, 237));
            texts[j].setStyle("-fx-font-size: 25;");
            hBox[j] = new HBox();
            hBox[j].setAlignment(Pos.CENTER);
            hBox[j].setStyle("-fx-background-color: #740c3e; -fx-background-radius: 20;");
            hBox[j].getChildren().add(texts[j]);
        }
        vBox.getChildren().addAll(hBox);
    }

    private void showNotificationPanel() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Game.getInstance().getClass().getResource("/fxml/NotificationPanel.fxml"));
            Parent root = fxmlLoader.load();
            NotificationController notificationController = fxmlLoader.getController();
            stageShower(root, "Notifications", ImageBase.NOTIFICATION_HISTORY_ICON.getImage());
            VBox vBox = notificationController.getMainVBox();
            vBox.setStyle("-fx-spacing: 15;");
            vBox.setTranslateX(10);
            vBox.setTranslateY(10);
            showNotifications(vBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Stage stageShower(Parent root, String title, Image image) {
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.getIcons().add(image);
        stage.show();
        return stage;
    }

    private void showUnitsPanel() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Game.getInstance().getClass().getResource("/fxml/UnitsPanel.fxml"));
            Parent root = fxmlLoader.load();
            UnitsPanelController unitsPanelController = fxmlLoader.getController();
            Stage stage = stageShower(root, "Units Panel", ImageBase.UNITS_PANEL_ICON.getImage());
            VBox vBox = unitsPanelController.getMainVBox();
            vBox.setStyle("-fx-spacing: 15;");
            vBox.setTranslateX(10);
            vBox.setTranslateY(10);
            showUnitsList(vBox, stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCitiesPanel() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Game.getInstance().getClass().getResource("/fxml/CitiesPanel.fxml"));
            Parent root = fxmlLoader.load();
            CitiesPanelController citiesPanelController = fxmlLoader.getController();
            Stage stage = stageShower(root, "Cities Panel", ImageBase.CITIES_PANEL_ICON.getImage());
            VBox vBox = citiesPanelController.getMainVBox();
            vBox.setStyle("-fx-spacing: 15;");
            vBox.setTranslateX(10);
            vBox.setTranslateY(10);
            showCitiesList(vBox, stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCitiesList(VBox vBox, Stage stage) {
        ArrayList<City> cities = GameController.getCivilization().getCities();
        for (int i = 0; i < cities.size(); i++) {
            ImageView imageView = new ImageView(getCityImage(cities.get(i)));
            imageView.setFitWidth(70);
            imageView.setFitHeight(70);
            Text[] texts = new Text[3];
            texts[0] = new Text(" " + cities.get(i).getName());
            texts[1] = new Text(String.valueOf(cities.get(i).getCitizens().size()));
            texts[2] = new Text(cities.get(i).getHP() + " ");
            for (int j = 0; j < texts.length; j++) {
                texts[j].setFill(Color.rgb(155, 183, 237));
                texts[j].setStyle("-fx-font-size: 25;");
            }
            HBox hBox = new HBox(10);
            hBox.setStyle("-fx-background-color: #740c3e; -fx-background-radius: 20;");
            hBox.getChildren().add(imageView);
            hBox.getChildren().addAll(texts);
            setCityBoxActions(hBox, cities.get(i), stage);
            vBox.getChildren().add(hBox);
        }
    }


    private void panelChangeStateForMouse(HBox hBox) {
        hBox.setOnMouseEntered(event -> {
            hBox.setOpacity(0.3);
        });
        hBox.setOnMouseExited(event -> {
            hBox.setOpacity(1);
        });
    }

    private void showUnitsList(VBox vBox, Stage stage) {
        ArrayList<Unit> units = GameController.getCivilization().getUnits();
        for (int i = 0; i < units.size(); i++) {
            ImageView imageView = new ImageView(units.get(i).getType().getImage());
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            Text text = new Text(" " + units.get(i).getType());
            Text text1 = new Text(units.get(i).getStatus().toString());
            Text text2 = new Text(units.get(i).getHealth() + " ");
            text.setStyle("-fx-font-size: 25;");
            text.setFill(Color.rgb(155, 183, 237));
            text1.setStyle("-fx-font-size: 25;");
            text1.setFill(Color.rgb(155, 183, 237));
            text2.setStyle("-fx-font-size: 25;");
            text2.setFill(Color.rgb(155, 183, 237));
            HBox hBox = new HBox(10);

            hBox.setStyle("-fx-background-color: #740c3e; -fx-background-radius: 20;");
            hBox.getChildren().add(imageView);
            hBox.getChildren().add(text);
            hBox.getChildren().add(text1);
            hBox.getChildren().add(text2);
            hBox.setPrefWidth(text.getLayoutBounds().getWidth() + text1.getLayoutBounds().getWidth() +
                    text2.getLayoutBounds().getWidth() + imageView.getLayoutBounds().getWidth());
            hBox.setAlignment(Pos.CENTER);
            setUnitBoxActions(hBox, units.get(i), stage);
            vBox.getChildren().add(hBox);
        }
    }

    private void setCityBoxActions(HBox hBox, City city, Stage stage) {
        hBox.setOnMouseClicked(event -> {
            stage.close();
            xStartingIndex = city.getTiles().get(0).getIndexInMapI() - 3;
            yStartingIndex = city.getTiles().get(0).getIndexInMapJ() - 5;
            if (xStartingIndex + 9 > Game.getInstance().getMapSize())
                xStartingIndex = Game.getInstance().getMapSize() - 9;
            if (yStartingIndex + 14 > Game.getInstance().getMapSize())
                yStartingIndex = Game.getInstance().getMapSize() - 14;
            chosenCity = city;
            showMap();
        });
        panelChangeStateForMouse(hBox);
    }

    private void setUnitBoxActions(HBox hBox, Unit unit, Stage stage) {
        hBox.setOnMouseClicked(event -> {
            stage.close();
            xStartingIndex = unit.getTile().getIndexInMapI() - 3;
            yStartingIndex = unit.getTile().getIndexInMapJ() - 5;
            if (xStartingIndex < 1) xStartingIndex = 1;
            if (yStartingIndex < 1) yStartingIndex = 1;
            if (xStartingIndex + 9 > Game.getInstance().getMapSize())
                xStartingIndex = Game.getInstance().getMapSize() - 9;
            if (yStartingIndex + 14 > Game.getInstance().getMapSize())
                yStartingIndex = Game.getInstance().getMapSize() - 14;
            chosenUnit = unit;
            showMap();
        });
        panelChangeStateForMouse(hBox);
    }

    private void showNotifications(VBox vBox) {
        ArrayList<String> notifications = GameController.getCivilization().getNotifications();
        for (int i = 0; i < notifications.size(); i++) {
            Text text = new Text(" " + notifications.get(i) + " ");
            text.setStyle("-fx-font-size: 25;");
            text.setFill(Color.rgb(155, 183, 237));
            HBox hBox = new HBox(text);
            hBox.setStyle("-fx-background-color: #740c3e; -fx-background-radius: 20;");

            hBox.setMaxWidth(text.getLayoutBounds().getWidth());
            vBox.getChildren().add(hBox);
        }
    }

    private void showEconomicPanel() {
//
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Game.getInstance().getClass().getResource("/fxml/EconomicPanel.fxml"));
            Parent root = fxmlLoader.load();
            EconomicPanelController economicPanelController = fxmlLoader.getController();
            Stage stage = stageShower(root, "Economic Overview", ImageBase.ECONOMIC_PANEL_ICON.getImage());
            VBox vBox = economicPanelController.getMainVBox();
            vBox.setStyle("-fx-spacing: 15;");
            vBox.setTranslateX(10);
            vBox.setTranslateY(10);
            showEconomicStatus(vBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showEconomicStatus(VBox vBox) {
        Civilization civilization = GameController.getCivilization();
        for (City city : civilization.getCities()) {
            CityController.updateCityInfos(city);
            Text[] texts = new Text[5];
            texts[0] = new Text(" Name: " + city.getName());
            texts[1] = new Text(" " + "Population: " +
                    city.getCitizens().size() + "  |  Defensive Strength: " + city.getHP() + " ");
            texts[2] = new Text(" food: " + city.getFoodPerTurn() + "  |  " + "production: " + city.getProductionPerTurn() + " ");
            texts[3] = new Text(" gold: " + city.getGoldPerTurn() + "  |  " + "science: " + city.getSciencePerTurn() + " ");
            texts[4] = new Text(" turns for new unit: " + CityController.turnsForNewUnit(city) + " ");
            VBox thisV = new VBox();
            for (int i = 0; i < texts.length; i++) {
                texts[i].setStyle("-fx-font-size: 25;");
                texts[i].setFill(Color.rgb(155, 183, 237));
            }
            thisV.setStyle("-fx-background-color: #740c3e; -fx-background-radius: 20;");
            thisV.getChildren().addAll(texts);
            vBox.getChildren().add(thisV);
        }

    }


    private void showHoveredTileInfo() {
        if (hoveredTile == null) return;
        ImageView imageView = new ImageView(hoveredTile.getImage());
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        imageView.setX(1380);
        imageView.setY(100);
        Label type = new Label("Type: " + hoveredTile.getType().toString());
        Label feature = new Label("Feature: " + hoveredTile.getFeature().toString());
        type.getStylesheets().add("css/MapStyle.css");
        feature.getStylesheets().add("css/MapStyle.css");
        type.getStyleClass().add("hoveredTileInfo");
        feature.getStyleClass().add("hoveredTileInfo");
        type.setLayoutX(1417);
        type.setLayoutY(140);
        feature.setLayoutX(1417);
        feature.setLayoutY(180);
        backgroundPane.getChildren().add(imageView);
        backgroundPane.getChildren().addAll(type, feature);
    }

    private void showChosenCityPanel() {
        if (chosenCity == null) return;
        CityController.setCity(chosenCity, "");
        openCityPanel(chosenCity);
    }

    private void openCityPanel(City city) {
        Color color = new Color(GameController.getCivilization().getColor().getRed(),
                GameController.getCivilization().getColor().getGreen(),
                GameController.getCivilization().getColor().getBlue(), 0.3);
        Button cityPanel = null;
        for (Node child : backgroundPane.getChildren()) {
            if (!(child instanceof Button)) continue;
            Button button = (Button) child;
            if (button.getLayoutX() == city.getTiles().get(0).getX() + 18 &&
                    button.getLayoutY() == city.getTiles().get(0).getY() - 5) {
                cityPanel = button;
                break;
            }
        }
        if (cityPanel == null) throw new RuntimeException();
        backgroundPane.getChildren().remove(cityPanel);
        for (Tile tile : city.getTiles()) {
            double x = tile.getX(), y = tile.getY();
            Polygon polygon = new Polygon();
            polygon.setFill(color);
            polygon.getPoints().addAll(
                    x + 60, y,
                    x, y + 33,
                    x, y + 100,
                    x + 60, y + 136,
                    x + 120, y + 100,
                    x + 120, y + 33);
            backgroundPane.getChildren().add(polygon);
        }
        tileImageViews.clear();
        tileImageViews.addAll(city.getTiles());
        showTilesFoodProductionGold();
        showExclusiveCitizens();
        initButtonsForCityPanel();
        showPurchasableTiles();
        showAttackOptionsForCity();
        backgroundPane.getChildren().add(cityPanel);
    }

    private HBox createOutputHBox(Label label) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(33);
        imageView.setFitHeight(33);
        HBox hBox = new HBox(4);
        if (label.getText().startsWith("Food"))
            imageView.setImage(ImageBase.FOOD_ICON.getImage());
        if (label.getText().startsWith("Production"))
            imageView.setImage(ImageBase.PRODUCTION_ICON.getImage());
        if (label.getText().startsWith("Gold"))
            imageView.setImage(ImageBase.GOLD_ICON.getImage());
        if (label.getText().startsWith("Science"))
            imageView.setImage(ImageBase.SCIENCE_ICON.getImage());
        if (label.getText().startsWith("Unemployed"))
            imageView.setImage(ImageBase.UNEMPLOYED_CITIZEN.getImage());
        hBox.getChildren().add(label);
        hBox.getChildren().add(imageView);
        return hBox;
    }

    private void showPurchasableTiles() {
        ArrayList<Tile> purchasableTiles = new ArrayList<>();
        for (Tile cityTile : chosenCity.getTiles())
            for (Tile neighbor : cityTile.getNeighbors())
                if (!chosenCity.getTiles().contains(neighbor) && neighbor.isPurchasableFor(chosenCity) &&
                        !purchasableTiles.contains(neighbor)) purchasableTiles.add(neighbor);
        for (Tile tile : purchasableTiles) {
            int necessaryAmountOfGoldForPurchase = tile.getCost();
            Button purchase = new Button(Integer.toString(necessaryAmountOfGoldForPurchase));
            purchase.setLayoutX(tile.getX() + 35);
            purchase.setLayoutY(tile.getY() - 5);
            purchase.getStylesheets().add("css/MapStyle.css");
            purchase.getStyleClass().add("purchaseTileButton");
            Tooltip tooltip = new Tooltip("purchase for " + necessaryAmountOfGoldForPurchase + " golds");
            Tooltip.install(purchase, tooltip);
            purchase.setOnMouseClicked(mouseEvent -> {
                CityController.setCity(chosenCity, "purchase -t -c " + tile.getIndexInMapI() + " " + tile.getIndexInMapJ());
                String message = CityController.handleCityOptions();
                if (message.length() == 0)
                    chosenCity = null;
                else
                    showPopup(mouseEvent, message.toUpperCase() + "!");
                showMap();
            });
            backgroundPane.getChildren().add(purchase);
        }
    }

    private void showAttackOptionsForCity() {
        if (!chosenCity.canAttack()) return;
        ArrayList<Tile> borderTiles = new ArrayList<>();

        for (Tile cityTile : chosenCity.getTiles())
            for (Tile neighbor : cityTile.getNeighbors())
                if (!neighbor.equals(chosenCity.getTiles().get(0)) && !borderTiles.contains(neighbor))
                    borderTiles.add(neighbor);

        for (Tile tile : borderTiles) {
            if (tile.getMilitary() != null && !tile.getMilitary().getCivilization().equals(chosenCity.getCivilization())) {
                ImageView target = new ImageView(ImageBase.CITY_ATTACK_TARGET.getImage());
                target.setFitHeight(30);
                target.setFitWidth(30);
                target.setX(tile.getX() + tile.getImage().getWidth() / 2 - target.getFitWidth() / 2);
                target.setY(tile.getY() + 10);
                target.setStyle("-fx-cursor: hand;");

                target.setOnMouseEntered(mouseEvent -> target.setImage(ImageBase.CITY_ATTACK_LOCK.getImage()));
                target.setOnMouseExited(mouseEvent -> target.setImage(ImageBase.CITY_ATTACK_TARGET.getImage()));

                target.setOnMouseClicked(mouseEvent -> {
                    CityController.setCity(chosenCity, "attack to -c " + tile.getIndexInMapI() + " " + tile.getIndexInMapJ());
                    String message = CityController.handleCityOptions();
                    if (message.length() > 0)
                        showPopup(mouseEvent, message.toUpperCase() + "!");
                    else
                        setChosenCity(null);
                    showMap();
                });
                backgroundPane.getChildren().add(target);
            }
        }
    }

    private void setUnitImageViewClickInCityPanel(UnitType unitType, HBox hBox) {
        ImageView imageView = new ImageView(unitType.getImage());
        imageView.setFitWidth(cityPanelIconsSize);
        imageView.setFitHeight(cityPanelIconsSize);
        if (chosenCity.getInProgressUnit() == null || chosenCity.getInProgressUnit() != unitType) {
            Tooltip tooltip = new Tooltip("Left Click For Construction, Right Click For Purchase");
            Tooltip.install(imageView, tooltip);
            setMouseMovementForCityPanelIcons(imageView, 5);
            imageView.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    chosenCity.setInProgressUnit(unitType);
                    if (!chosenCity.getLastCostsUntilNewProductions().containsKey(unitType))
                        chosenCity.getLastCostsUntilNewProductions().put(unitType, unitType.getCost());
                    setChosenCity(null);
                    showMap();
                }
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    CityController.setCity(chosenCity, "purchase -u " + unitType);
                    String message = CityController.handleCityOptions();
                    if (message.length() == 0)
                        setChosenCity(null);
                    else
                        showPopup(mouseEvent, message.toUpperCase() + "!");
                }
            });
        } else {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setSaturation(-1);
            colorAdjust.setBrightness(-0.2);
            imageView.setEffect(colorAdjust);
        }
        hBox.getChildren().add(imageView);
    }

    private void setBuildingImageViewClickInCityPanel(Building building, HBox hBox) {
        ImageView imageView = new ImageView(building.getImage());
        imageView.setFitWidth(cityPanelIconsSize);
        imageView.setFitHeight(cityPanelIconsSize);
        if (chosenCity.getInProgressBuilding() == null || chosenCity.getInProgressBuilding() != building) {
            Tooltip tooltip = new Tooltip("Left Click For Construction, Right Click For Purchase");
            Tooltip.install(imageView, tooltip);
            setMouseMovementForCityPanelIcons(imageView, 5);
            imageView.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    chosenCity.setInProgressBuilding(building);
                    if (!chosenCity.getBuildings().containsKey(building))
                        chosenCity.getBuildings().put(building, building.getCost());
                    setChosenCity(null);
                    showMap();
                }
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    CityController.setCity(chosenCity, "purchase -b " + building);
                    String message = CityController.handleCityOptions();
                    if (message.length() == 0)
                        setChosenCity(null);
                    else
                        showPopup(mouseEvent, message.toUpperCase() + "!");
                }
            });
        } else {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setSaturation(-1);
            colorAdjust.setBrightness(-0.2);
            imageView.setEffect(colorAdjust);
        }
        hBox.getChildren().add(imageView);
    }

    private void showExclusiveCitizens() {
        for (Tile tile : chosenCity.getTiles()) {
            ImageView imageView = new ImageView();
            if (tile.getWorkingCitizen() == null) {
                imageView.setCursor(Cursor.HAND);
                imageView.setImage(ImageBase.UNEMPLOYED_CITIZEN.getImage());
                imageView.setOnMouseClicked(mouseEvent -> {
                    CityController.setCity(chosenCity, "lock citizen on tile -c " + tile.getIndexInMapI() + " " + tile.getIndexInMapJ());
                    String message = CityController.handleCityOptions();
                    if (message.length() != 0)
                        showPopup(mouseEvent, message.toUpperCase() + "!");
                    else
                        renderCitizenIcons();
                });
            } else {
                imageView.setImage(ImageBase.EMPLOYED_CITIZEN.getImage());
                if (!tile.isCenterOfCity(tile.getCity())) {
                    imageView.setCursor(Cursor.HAND);
                    imageView.setOnMouseClicked(mouseEvent -> {
                        Citizen workingCitizen = tile.getWorkingCitizen();
                        workingCitizen.getTile().setWorkingCitizen(null);
                        workingCitizen.changeWorkingTile(null);
                        renderCitizenIcons();
                    });
                }
            }
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            int i = tile.getIndexInMapI(), j = tile.getIndexInMapJ();
            imageView.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 50);
            imageView.setY(105 * (i - xStartingIndex) + 50);
            backgroundPane.getChildren().add(imageView);
        }
    }

    private void renderCitizenIcons() {
        for (int i = 0; i < backgroundPane.getChildren().size(); i++) {
            Node child = backgroundPane.getChildren().get(i);
            if (!(child instanceof ImageView)) continue;
            ImageView childImg = (ImageView) child;
            if (childImg.getImage().equals(ImageBase.EMPLOYED_CITIZEN.getImage()) ||
                    childImg.getImage().equals(ImageBase.UNEMPLOYED_CITIZEN.getImage())) {
                backgroundPane.getChildren().remove(childImg);
                i--;
            }
        }
        showExclusiveCitizens();
    }

    private void setMouseMovementForCityPanelIcons(ImageView imageView, int d) {
        imageView.setOnMouseEntered(mouseEvent -> {
            imageView.setX(imageView.getX() - d);
            imageView.setY(imageView.getY() - d);
            imageView.setFitWidth(imageView.getFitWidth() + 2 * d);
            imageView.setFitHeight(imageView.getFitHeight() + 2 * d);
        });
        imageView.setOnMouseExited(mouseEvent -> {
            imageView.setX(imageView.getX() + d);
            imageView.setY(imageView.getY() + d);
            imageView.setFitWidth(imageView.getFitWidth() - 2 * d);
            imageView.setFitHeight(imageView.getFitHeight() - 2 * d);
        });
    }

    private void initButtonsForCityPanel() {
        ImageView production = new ImageView(ImageBase.CITY_PRODUCTION_MENU.getImage());
        production.setX(chosenCity.getTiles().get(0).getX() + 110);
        production.setY(chosenCity.getTiles().get(0).getY() - 5);
        production.setFitWidth(40);
        production.setFitHeight(40);
        production.setCursor(Cursor.HAND);
        setMouseMovementForCityPanelIcons(production, 2);
        production.setOnMouseClicked(mouseEvent -> {
            showProductionMenuForCity();
        });
        backgroundPane.getChildren().add(production);
    }

    private void showProductionMenuForCity() {
        ScrollPane scrollPane = new ScrollPane();
        VBox vBox = new VBox(5);
        vBox.setPrefWidth(410);
        vBox.setPrefHeight(900);
        vBox.setLayoutY(60);
        vBox.setStyle("-fx-background-color: rgba(0,0,0,0.6);" +
                "-fx-fill: transparent; -fx-background-radius: 15");
        vBox.getChildren().add(new Line());

        Label[] labels = new Label[8];
        labels[0] = new Label("Food: " + (chosenCity.getFoodPerTurn() >= 0 ? "+" : "-") + chosenCity.getFoodPerTurn());
        labels[1] = new Label("Production: " + (chosenCity.getProductionPerTurn() >= 0 ? "+" : "-") + chosenCity.getProductionPerTurn());
        labels[2] = new Label("Gold: " + (chosenCity.getGoldPerTurn() >= 0 ? "+" : "-") + chosenCity.getGoldPerTurn());
        labels[3] = new Label("Science: " + (chosenCity.getSciencePerTurn() >= 0 ? "+" : "-") + chosenCity.getSciencePerTurn());

        if (chosenCity.getStoredFood() > 0)
            labels[4] = new Label("Turns until growth citizen: " + chosenCity.getTurnsUntilBirthCitizen());
        else if (chosenCity.getStoredFood() < 0)
            labels[4] = new Label("Turns until lose citizen: " + chosenCity.getTurnsUntilDeathCitizen());
        else
            labels[4] = new Label("Turns until growth citizen: N/A");

        if (chosenCity.getTurnsUntilGrowthBorder() == 0)
            labels[5] = new Label("Turns until growth border: N/A");
        else
            labels[5] = new Label("Turns until growth border: " + chosenCity.getTurnsUntilGrowthBorder());

        labels[6] = new Label("Total Number of Citizens: " + chosenCity.getCitizens().size());

        int numOfUnemployed = 0;
        for (Citizen citizen : chosenCity.getCitizens())
            if (citizen.getTile() == null)
                numOfUnemployed++;

        labels[7] = new Label("Unemployed Citizens: " + numOfUnemployed);

        for (Label label : labels) {
            label.setStyle("-fx-font-family: 'Tw Cen MT'; -fx-font-size: 29; -fx-text-fill: white;");
            vBox.getChildren().add(createOutputHBox(label));
        }

        Rectangle space = new Rectangle();
        space.setFill(Color.TRANSPARENT);
        space.setWidth(410);
        space.setHeight(35);
        vBox.getChildren().add(space);

        ArrayList<UnitType> units = new ArrayList<>();
        ArrayList<Building> buildings = new ArrayList<>();
        for (UnitType unitType : UnitType.values()) if (CityController.canCreateUnit(unitType)) units.add(unitType);
        for (Building building : Building.values())
            if (CityController.canConstructBuilding(building)) buildings.add(building);


        Label unitsLabel = new Label("Units");
        unitsLabel.setStyle("-fx-font-size: 35; -fx-font-family: 'Tw Cen MT'; -fx-text-fill: white; -fx-alignment: center");
        unitsLabel.setAlignment(Pos.CENTER);
        vBox.getChildren().add(unitsLabel);

        for (int i = 0; i < units.size(); i++) {
            UnitType unitType = units.get(i);
            HBox hBox = new HBox(15);
            Rectangle rectangle = new Rectangle(5, 160);
            rectangle.setFill(Color.TRANSPARENT);
            hBox.getChildren().add(rectangle);
            setUnitImageViewClickInCityPanel(unitType, hBox);
            if (i < units.size() - 1) {
                i++;
                unitType = units.get(i);
                setUnitImageViewClickInCityPanel(unitType, hBox);
                if (i < units.size() - 1) {
                    i++;
                    unitType = units.get(i);
                    setUnitImageViewClickInCityPanel(unitType, hBox);
                }
            }
            hBox.setStyle("-fx-background-color: transparent; -fx-fill: transparent;");
            vBox.getChildren().add(hBox);
        }

        vBox.getChildren().add(new Rectangle(vBox.getWidth(), 10));

        Label buildingsLabel = new Label("Buildings");
        buildingsLabel.setStyle("-fx-font-size: 35; -fx-font-family: 'Tw Cen MT'; -fx-text-fill: white; -fx-alignment: center");
        buildingsLabel.setAlignment(Pos.CENTER);
        vBox.getChildren().add(buildingsLabel);

        for (int i = 0; i < buildings.size(); i++) {
            Building building = buildings.get(i);
            HBox hBox = new HBox(15);
            Rectangle rectangle = new Rectangle(5, 160);
            rectangle.setFill(Color.TRANSPARENT);
            hBox.getChildren().add(rectangle);
            setBuildingImageViewClickInCityPanel(building, hBox);
            if (i < buildings.size() - 1) {
                i++;
                building = buildings.get(i);
                setBuildingImageViewClickInCityPanel(building, hBox);
                if (i < buildings.size() - 1) {
                    i++;
                    building = buildings.get(i);
                    setBuildingImageViewClickInCityPanel(building, hBox);
                }
            }
            hBox.setStyle("-fx-background-color: transparent; -fx-fill: transparent;");
            vBox.getChildren().add(hBox);
        }
        scrollPane.setLayoutX(-2);
        scrollPane.setLayoutY(60);
        scrollPane.setContent(vBox);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        backgroundPane.getChildren().add(scrollPane);

        Button close = new Button("close");
        close.getStylesheets().add("css/MapStyle.css");
        close.getStyleClass().add("closeCityPanel");
        close.setLayoutX(409);
        close.setLayoutY(853);
        close.setOnMouseClicked(mouseEvent -> {
            setChosenCity(null);
            tileImageViews.clear();
            hideCitizen();
            hideTilesFoodProductionGold();
            showMap();
        });
        backgroundPane.getChildren().add(close);
    }

    private void showTileInfoButton() {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.85);
        ImageView imageView = new ImageView(ImageBase.HEXAGON_ICON.getImage());
        Tooltip.install(imageView, new Tooltip("tiles content"));
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
                } else {
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
        backgroundPane.getChildren().addAll(getUnitHealthLabel(unitAvatarImageView));
        backgroundPane.getChildren().add(movesLabel);
    }

    private ArrayList<Label> getUnitHealthLabel(ImageView imageView) {
        Label label = new Label("Health: " + chosenUnit.getHealth());
        label.setStyle("-fx-text-fill: white; -fx-font-size: 30;");
        label.setLayoutX(imageView.getX() + imageView.getLayoutBounds().getWidth() + 20);
        label.setLayoutY(imageView.getY() + 20);
        ArrayList<Label> labels = new ArrayList<>();
        labels.add(label);
        Label label1;
        if (!chosenUnit.getType().isCivilian()) {
            if (chosenUnit.getType().getRangedCombatStrength() == 0)
                label = new Label("Damage: " + chosenUnit.getType().getCombatStrength());
            else {
                label = new Label("Damage: " + chosenUnit.getType().getRangedCombatStrength());
                label1 = new Label("Range: " + chosenUnit.getType().getRange());
                label.setStyle("-fx-text-fill: white; -fx-font-size: 30;");
                label.setLayoutX(imageView.getX() + imageView.getLayoutBounds().getWidth() + 20);
                label.setLayoutY(imageView.getY() + 100);
                labels.add(label1);
            }
            label.setStyle("-fx-text-fill: white; -fx-font-size: 30;");
            label.setLayoutX(imageView.getX() + imageView.getLayoutBounds().getWidth() + 20);
            label.setLayoutY(imageView.getY() + 60);
            labels.add(label);
        }
        return labels;
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
        setDiscoveredTileBrightness(tile, i, j, colorAdjust);
        backgroundPane.getChildren().add(tile);
    }

    private void setDiscoveredTileBrightness(Tile tile, int i, int j, ColorAdjust colorAdjust) {
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
            setDiscoveredTileBrightness(tile, i, j, colorAdjust);
            backgroundPane.getChildren().add(imageView1);
        }
        if (tile.isRiverAtLeft()) {
            Image image;
            if (i % 2 == 1) image = ImageBase.RIVER_O.getImage();
            else image = ImageBase.RIVER_E.getImage();
            ImageView imageView2 = new ImageView(image);
            imageView2.setX(120 * (j - yStartingIndex) + (i % 2) * 60);
            imageView2.setY(105 * (i - xStartingIndex));
            setDiscoveredTileBrightness(tile, i, j, colorAdjust);
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
        if (tile.getRouteInProgress() != null) {
            ImageView imageView;
            if (tile.getRouteInProgress().equals("road")) imageView = new ImageView(ImageBase.ROAD_ICON.getImage());
            else imageView = new ImageView(ImageBase.RAIL_ROAD_ICON.getImage());
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
            imageView.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 60);
            imageView.setY(105 * (i - xStartingIndex) + 10);
            backgroundPane.getChildren().add(imageView);
        }
        if (tile.getImprovementInProgress() != null) {
            ImageView improvementImage = new ImageView(tile.getImprovementInProgress().getKey().getImage());
            improvementImage.setFitHeight(60);
            improvementImage.setFitWidth(60);
            improvementImage.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 60);
            improvementImage.setY(105 * (i - xStartingIndex) + 10);
            backgroundPane.getChildren().add(improvementImage);
        }
        if ((tile.getImprovementInProgress() != null && tile.getImprovementInProgress().getValue() != 0)
                || (tile.getRouteInProgress() != null && tile.getRouteInProgress().getValue() != 0)) {
            ImageView improvementImage = new ImageView(ImageBase.IN_PROGRESS_IMPROVEMENT.getImage());
            improvementImage.setFitHeight(20);
            improvementImage.setFitWidth(20);
            improvementImage.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 60);
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
                    tile.getCivilian().setY(tile.getY() + 70);
                    addProgressUnitBar(tile.getX() + 75, tile.getY() + 60, tile.getCivilian());

                    backgroundPane.getChildren().add(tile.getCivilian());
                    showUnitStatus(tile.getCivilian());
                }
                if (tile.getMilitary() != null) {
                    tile.getMilitary().setX(tile.getX() + 10);
                    tile.getMilitary().setY(tile.getY() + 70);
                    addProgressUnitBar(tile.getX() + 20, tile.getY() + 60, tile.getMilitary());

                    backgroundPane.getChildren().add(tile.getMilitary());
                    showUnitStatus(tile.getMilitary());
                }
            }
        }
    }

    private int getCityMaxHealth(City city) {
        if (city.getBuildings().containsKey(Building.MILITARY_BASE) && city.getBuildings().get(Building.MILITARY_BASE) <= 0)
            return 45;
        else if (city.getBuildings().containsKey(Building.CASTLE) && city.getBuildings().get(Building.CASTLE) <= 0)
            return 33;
        else if (city.getBuildings().containsKey(Building.WALLS) && city.getBuildings().get(Building.WALLS) <= 0)
            return 25;
        return 20;
    }

    private void addProgressUnitBar(double v, double v1, Unit unit) {
        Rectangle rectangle = new Rectangle(v, v1, 3 * Unit.getMaxHealth(), 7);
        if (unit.getHealth() > Unit.MAX_HEALTH) {
            rectangle = new Rectangle(v, v1, 3 * unit.getHealth(), 7);
        }
        rectangle.setFill(Color.BLACK);
        rectangle.setOpacity(0.7);
        backgroundPane.getChildren().add(rectangle);
        rectangle = new Rectangle(v, v1, 3 * unit.getHealth(), 7);
        rectangle.setFill(unit.getCivilization().getColor());
        rectangle.setOpacity(0.8);
        backgroundPane.getChildren().add(rectangle);
    }

    private void showUnitStatus(Unit unit) {
        if (unit.getStatus().getImage() != null) {
            ImageView imageView = new ImageView(unit.getStatus().getImage());
            imageView.setFitWidth(unit.getFitWidth() / 1.5);
            imageView.setFitHeight(unit.getFitHeight() / 1.5);
            imageView.setX(unit.getX());
            imageView.setY(unit.getY() + 20);
            backgroundPane.getChildren().add(imageView);
        }
    }

    public void showStatusBar() {//TODO ADD TEXT BOXES
        ImageView imageView = new ImageView(ImageBase.STATUSBAR_BOX.getImage());
        backgroundPane.getChildren().add(imageView);
        ImageView[] imageViews = new ImageView[6];
        imageViews[0] = new ImageView(ImageBase.SCIENCE_ICON.getImage());
        imageViews[1] = new ImageView(ImageBase.GOLD_ICON.getImage());
        imageViews[2] = new ImageView(ImageBase.HAPPINESS_ICON.getImage());
        imageViews[3] = new ImageView(ImageBase.TURN_ICON.getImage());
        imageViews[4] = new ImageView(ImageBase.SAVE_ICON.getImage());
        imageViews[4].setCursor(Cursor.HAND);
        imageViews[4].setOnMouseClicked(mouseEvent -> {
            Main.clickSound();
            GameController.saveGameToJson(true);
        });
        imageViews[5] = new ImageView(ImageBase.HOME_ICON.getImage());
        imageViews[5].setCursor(Cursor.HAND);
        imageViews[5].setOnMouseClicked(mouseEvent -> {
            Main.playSound("Menu.mp3");
            GameController.saveGameToJson(true);
            Main.changeScene("GamePage");
        });
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].setFitHeight(40);
            imageViews[i].setFitWidth(40);
            imageViews[i].setY(10);
            imageViews[i].setX(40 + 140 * i);
            if (i >= 3) imageViews[i].setX(imageViews[i].getX() + 600);
            backgroundPane.getChildren().add(imageViews[i]);
        }

        Label[] labels = new Label[6];
        labels[0] = new Label(String.valueOf(GameController.getCivilization().getScience()));
        labels[1] = new Label(String.valueOf(GameController.getCivilization().getTotalGold()));
        labels[2] = new Label(String.valueOf(GameController.getCivilization().getHappiness()));
        labels[3] = new Label(String.valueOf(Game.getInstance().getTime()));
        labels[4] = new Label("Save");
        labels[5] = new Label("Main Menu");
        for (int i = 0; i < labels.length; i++) {
            labels[i].setLayoutY(5);
            labels[i].setLayoutX(40 + 140 * i + 50);
            labels[i].setStyle("-fx-text-fill: white; -fx-font-size: 30; -fx-font-family: 'Tw Cen MT'");
            if (i >= 3) labels[i].setLayoutX(labels[i].getLayoutX() + 600);
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

    private Image chooseFoodLevelImage(int key) {
        if (key == 1) return ImageBase.FOOD_1.getImage();
        else if (key == 2) return ImageBase.FOOD_2.getImage();
        else if (key == 3) return ImageBase.FOOD_3.getImage();
        else if (key == 4) return ImageBase.FOOD_4.getImage();
        else return null;
    }

    private Image chooseProductionLevelImage(int key) {
        if (key == 1) return ImageBase.PRODUCTION_1.getImage();
        else if (key == 2) return ImageBase.PRODUCTION_2.getImage();
        else if (key == 3) return ImageBase.PRODUCTION_3.getImage();
        else if (key == 4) return ImageBase.PRODUCTION_4.getImage();
        else return null;
    }

    private Image chooseGoldLevelImage(int key) {
        if (key == 1) return ImageBase.GOLD_1.getImage();
        else if (key == 2) return ImageBase.GOLD_2.getImage();
        else if (key == 3) return ImageBase.GOLD_3.getImage();
        else if (key == 4) return ImageBase.GOLD_4.getImage();
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
                if (tile.getWorkingCitizen() != null && tile.getCity().equals(chosenCity)) {
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

    public void nextTurn(MouseEvent event) {
        String message = GameController.updateGame();
        if (message.length() > 0)
            showPopup(event, message.toUpperCase() + "!");
        hideUnitAvatar();
        hideUnitOptions();
        chosenUnit = null;
        if (message.length() == 0) {
            String jsonSave = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(Game.getInstance());
            String response = NetworkController.send(new ArrayList<>(Arrays.asList(Menu.GAME.getMenuName()
                    , Server.Request.NEXT_TURN.getString(), Main.username, jsonSave)));
            System.out.println("i said next turn, response was : " + response);
            backgroundPane.getChildren().clear();
        }
        else {
            showMap();
        }
    }

    private void showRequests() {
        ArrayList<Request> sorted = new ArrayList<>(GameController.getCivilization().getRequests());
        Comparator<Request> comparator = Comparator.comparing(Request::getAction);
        if (!sorted.isEmpty()) {
            sorted = (ArrayList<Request>) sorted.stream().sorted(comparator).collect(Collectors.toList());
            sorted.sort(comparator);
        }
        for (Request request : sorted) {
            Popup popup = new Popup();
            AnchorPane anchorPane = new AnchorPane();
            Label label = new Label();
            if (request.getAction().equals("Demand")) {
                String message = "";
                for (Map.Entry<String, Object> set :
                        request.getParams().entrySet()) {
                    message = message.concat(set.getKey() + ": " + set.getValue() + " - ");
                }
                message = message.substring(0, message.length() - 2);
                label.setText("Sender: " + request.getSender() + "\nType: " + request.getAction() + "\n" + message);
            } else if (request.getAction().equals("Trade")) {
                String messageGive = "Give: ";
                String messageGet = "Get: ";
                for (Map.Entry<String, Object> set :
                        request.getParams().entrySet()) {
                    String[] args = set.getKey().split(" ");
                    if (set.getKey().startsWith("Give"))
                        messageGive = messageGive.concat(args[1] + ": " + set.getValue() + " - ");
                    else messageGet = messageGet.concat(args[1] + ": " + set.getValue() + " - ");
                }
                messageGive = messageGive.substring(0, messageGive.length() - 2);
                messageGet = messageGet.substring(0, messageGet.length() - 2);
                label.setText("Sender: " + request.getSender() + "\nType: " + request.getAction() + "\n" + messageGive + "\n" + messageGet);
            } else if (request.getAction().equals("Peace")) {
                label.setText("Sender: " + request.getSender() + "\nType: " + request.getAction() + "\nMake Peace");
            } else if (request.getAction().equals("War")) {
                label.setText("Sender: " + request.getSender() + "\nType: " + request.getAction() + "\nDeclare War");
            }
            label.setTextFill(Color.rgb(180, 0, 0, 1));
            label.setMinHeight(100);
            label.setMinWidth(400);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setStyle("-fx-font-size: 30; -fx-font-family: 'Tw Cen MT'; -fx-font-weight: bold;" +
                    "-fx-background-color: white; -fx-background-radius: 5; -fx-alignment: center;" +
                    "-fx-border-color: black; -fx-border-width: 4.5; -fx-border-radius: 5;");
            anchorPane.getChildren().add(label);
            if (request.getAction().equals("War")) {
                Button button = new Button("Ok");
                button.setStyle("-fx-font-size: 30; -fx-font-family: 'Tw Cen MT'; -fx-font-weight: bold;" +
                        "-fx-background-color: white; -fx-background-radius: 5; -fx-alignment: center;" +
                        "-fx-border-color: black; -fx-border-width: 4.5; -fx-border-radius: 5;");
                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        for (int i = GameController.getCivilization().getRequests().size() - 1; i >= 0; i--) {
                            Request request1 = GameController.getCivilization().getRequests().get(i);
                            if (request1.getSender().equals(request.getSender()) &&
                                    request1.getAction().equals(request.getAction()) &&
                                    request1.getParams().equals(request.getParams())) {
                                GameController.getCivilization().getRequests().remove(i);
                                break;
                            }
                        }
                        popup.hide();
                    }
                });
                button.setLayoutY(220);
                anchorPane.getChildren().add(button);
            } else {
                Button button = new Button("Accept");
                button.setStyle("-fx-font-size: 30; -fx-font-family: 'Tw Cen MT'; -fx-font-weight: bold;" +
                        "-fx-background-color: white; -fx-background-radius: 5; -fx-alignment: center;" +
                        "-fx-border-color: black; -fx-border-width: 4.5; -fx-border-radius: 5;");
                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (canAcceptRequest(request)) {
                            acceptRequest(request);
                            for (int i = GameController.getCivilization().getRequests().size() - 1; i >= 0; i--) {
                                Request request1 = GameController.getCivilization().getRequests().get(i);
                                if (request1.getSender().equals(request.getSender()) &&
                                        request1.getAction().equals(request.getAction()) &&
                                        request1.getParams().equals(request.getParams())) {
                                    GameController.getCivilization().getRequests().remove(i);
                                    break;
                                }
                            }
                            popup.hide();
                        } else {
                            Label label = new Label("Trade can't be completed because lack of products");
                            label.setTextFill(Color.rgb(180, 0, 0, 1));
                            label.setMinHeight(50);
                            label.setMinWidth(300);
                            label.setTextAlignment(TextAlignment.CENTER);
                            label.setStyle("-fx-font-size: 30; -fx-font-family: 'Tw Cen MT'; -fx-font-weight: bold;" +
                                    "-fx-background-color: white; -fx-background-radius: 5; -fx-alignment: center;" +
                                    "-fx-border-color: black; -fx-border-width: 4.5; -fx-border-radius: 5;");
                            label.setLayoutY(150);
                            anchorPane.getChildren().add(label);
                        }
                    }
                });
                button.setLayoutY(220);
                Button button1 = new Button("Reject");
                button1.setStyle("-fx-font-size: 30; -fx-font-family: 'Tw Cen MT'; -fx-font-weight: bold;" +
                        "-fx-background-color: white; -fx-background-radius: 5; -fx-alignment: center;" +
                        "-fx-border-color: black; -fx-border-width: 4.5; -fx-border-radius: 5;");
                button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        for (int i = GameController.getCivilization().getRequests().size() - 1; i >= 0; i--) {
                            Request request1 = GameController.getCivilization().getRequests().get(i);
                            if (request1.getSender().equals(request.getSender()) &&
                                    request1.getAction().equals(request.getAction()) &&
                                    request1.getParams().equals(request.getParams())) {
                                GameController.getCivilization().getRequests().remove(i);
                                break;
                            }
                        }
                        popup.hide();
                    }
                });
                button1.setLayoutY(290);
                anchorPane.getChildren().add(button);
                anchorPane.getChildren().add(button1);
            }
            popup.getContent().add(anchorPane);
            popup.show(backgroundPane.getScene().getWindow());
        }
    }

    private void acceptRequest(Request request) {
        User user = null;
        for (User player : Game.getInstance().getPlayers()) {
            if (player.getUsername().equals(request.getSender())) {
                user = player;
                break;
            }
        }
        if (request.getAction().equals("Peace")) {
            GameController.getCivilization().getInWarCivilizations().remove(request.getSender());
            user.getCivilization().getInWarCivilizations().remove(GameController.getCivilization().getUsername());
            return;
        }
        for (Map.Entry<String, Object> set :
                request.getParams().entrySet()) {
            String[] args = set.getKey().split(" ");
            if (set.getKey().startsWith("Get")) {
                if (args[1].equals("Gold")) {
                    GameController.getCivilization().setTotalGold(GameController.getCivilization().getTotalGold() - (Integer) set.getValue());
                    user.getCivilization().setTotalGold(user.getCivilization().getTotalGold() + (Integer) set.getValue());
                } else if (GameController.getCivilization().getLuxuryResources().containsKey(Resource.valueOf(args[1]))) {
                    GameController.getCivilization().getLuxuryResources().replace(Resource.valueOf(args[1]), GameController.getCivilization().getLuxuryResources().get(Resource.valueOf(args[1])) - (Integer) set.getValue());
                    if (user.getCivilization().getLuxuryResources().containsKey(Resource.valueOf(args[1])))
                        user.getCivilization().getLuxuryResources().replace(Resource.valueOf(args[1]), user.getCivilization().getLuxuryResources().get(Resource.valueOf(args[1])) + (Integer) set.getValue());
                    else
                        user.getCivilization().getLuxuryResources().put(Resource.valueOf(args[1]), (Integer) set.getValue());
                } else if (GameController.getCivilization().getStrategicResources().containsKey(Resource.valueOf(args[1]))) {
                    GameController.getCivilization().getStrategicResources().replace(Resource.valueOf(args[1]), GameController.getCivilization().getStrategicResources().get(Resource.valueOf(args[1])) - (Integer) set.getValue());
                    if (user.getCivilization().getStrategicResources().containsKey(Resource.valueOf(args[1])))
                        user.getCivilization().getStrategicResources().replace(Resource.valueOf(args[1]), user.getCivilization().getStrategicResources().get(Resource.valueOf(args[1])) + (Integer) set.getValue());
                    else
                        user.getCivilization().getStrategicResources().put(Resource.valueOf(args[1]), (Integer) set.getValue());
                }
            } else {
                if (args[1].equals("Gold")) {
                    GameController.getCivilization().setTotalGold(GameController.getCivilization().getTotalGold() + (Integer) set.getValue());
                    user.getCivilization().setTotalGold(user.getCivilization().getTotalGold() - (Integer) set.getValue());
                } else if (user.getCivilization().getLuxuryResources().containsKey(Resource.valueOf(args[1]))) {
                    if (GameController.getCivilization().getLuxuryResources().containsKey(Resource.valueOf(args[1])))
                        GameController.getCivilization().getLuxuryResources().replace(Resource.valueOf(args[1]), GameController.getCivilization().getLuxuryResources().get(Resource.valueOf(args[1])) + (Integer) set.getValue());
                    else
                        GameController.getCivilization().getLuxuryResources().put(Resource.valueOf(args[1]), (Integer) set.getValue());
                    user.getCivilization().getLuxuryResources().replace(Resource.valueOf(args[1]), user.getCivilization().getLuxuryResources().get(Resource.valueOf(args[1])) - (Integer) set.getValue());
                } else if (user.getCivilization().getStrategicResources().containsKey(Resource.valueOf(args[1]))) {
                    if (GameController.getCivilization().getStrategicResources().containsKey(Resource.valueOf(args[1])))
                        GameController.getCivilization().getStrategicResources().replace(Resource.valueOf(args[1]), GameController.getCivilization().getStrategicResources().get(Resource.valueOf(args[1])) + (Integer) set.getValue());
                    else
                        GameController.getCivilization().getStrategicResources().put(Resource.valueOf(args[1]), (Integer) set.getValue());
                    user.getCivilization().getStrategicResources().replace(Resource.valueOf(args[1]), user.getCivilization().getStrategicResources().get(Resource.valueOf(args[1])) - (Integer) set.getValue());
                }
            }
        }
    }

    private boolean canAcceptRequest(Request request) {
        if (request.getAction().equals("Peace")) return true;
        User user = null;
        for (User player : Game.getInstance().getPlayers()) {
            if (player.getUsername().equals(request.getSender())) {
                user = player;
                break;
            }
        }
        HashMap<Resource, Integer> hashmap = new HashMap<>(GameController.getCivilization().getStrategicResources());
        hashmap.putAll(GameController.getCivilization().getLuxuryResources());
        HashMap<Resource, Integer> hashmap1 = new HashMap<>(user.getCivilization().getStrategicResources());
        hashmap1.putAll(user.getCivilization().getLuxuryResources());
        for (Map.Entry<String, Object> set :
                request.getParams().entrySet()) {
            String[] args = set.getKey().split(" ");
            if (set.getKey().startsWith("Get")) {
                if (args[1].equals("Gold")) {
                    if (GameController.getCivilization().getTotalGold() < (Integer) set.getValue()) return false;
                } else if (hashmap.containsKey(Resource.valueOf(args[1]))) {
                    if (hashmap.get(Resource.valueOf(args[1])) < (Integer) set.getValue()) {
                        return false;
                    }
                } else return false;
            } else {
                if (args[1].equals("Gold")) {
                    if (user.getCivilization().getTotalGold() < (Integer) set.getValue()) return false;
                } else if (hashmap1.containsKey(Resource.valueOf(args[1]))) {
                    if (hashmap1.get(Resource.valueOf(args[1])) < (Integer) set.getValue()) {
                        return false;
                    }
                } else return false;
            }
        }
        return true;
    }

    private void workerExclusiveOptions(HBox hBox) {
        ArrayList<ImageView> imageViews = new ArrayList<>();
        imageViews.add(new ImageView(ImageBase.AXE_ICON.getImage()));
        clearLandButton(imageViews.get(0));
        imageViews.add(new ImageView(ImageBase.HAMMER_ICON.getImage()));
        repairButton(imageViews.get(1));
        roadButton(imageViews);
        railroadButton(imageViews);
        improvementButton(imageViews, "FARM");
        improvementButton(imageViews, "MINE");
        improvementButton(imageViews, "CAMP");
        improvementButton(imageViews, "PASTURE");
        improvementButton(imageViews, "LUMBER_MILL");
        improvementButton(imageViews, "PLANTATION");
        improvementButton(imageViews, "QUARRY");
        improvementButton(imageViews, "TRADING_POST");
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
                if (chosenUnit.getTile().getImprovementInProgress() != null) {
                    UnitController.setUnit(chosenUnit, "repair -i " + chosenUnit.getTile().getImprovementInProgress().getKey());
                    String message = UnitController.handleUnitOptions();
                    if (message.length() == 0) {
                        setChosenUnit(null);
                        showMap();
                    } else
                        showPopup(event, message.toUpperCase() + "!");
                }
            }
        });
    }

    private void clearLandButton(ImageView imageView) {
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (chosenUnit.getTile().getFeature() == TerrainFeature.FOREST) {
                    UnitController.setUnit(chosenUnit, "clear forest");
                    String message = UnitController.handleUnitOptions();
                    if (message.length() == 0) {
                        setChosenUnit(null);
                        showMap();
                    } else
                        showPopup(event, message.toUpperCase() + "!");
                } else if (chosenUnit.getTile().getFeature() == TerrainFeature.JUNGLE) {
                    UnitController.setUnit(chosenUnit, "clear jungle");
                    String message = UnitController.handleUnitOptions();
                    if (message.length() == 0) {
                        setChosenUnit(null);
                        showMap();
                    } else
                        showPopup(event, message.toUpperCase() + "!");
                } else if (chosenUnit.getTile().getRouteInProgress() != null &&
                        chosenUnit.getTile().getRouteInProgress().getKey().equals("road")) {
                    UnitController.setUnit(chosenUnit, "clear road");
                    String message = UnitController.handleUnitOptions();
                    if (message.length() == 0) {
                        setChosenUnit(null);
                        showMap();
                    } else
                        showPopup(event, message.toUpperCase() + "!");
                } else if (chosenUnit.getTile().getRouteInProgress() != null &&
                        chosenUnit.getTile().getRouteInProgress().getKey().equals("railroad")) {
                    UnitController.setUnit(chosenUnit, "clear railroad");
                    String message = UnitController.handleUnitOptions();
                    if (message.length() == 0) {
                        setChosenUnit(null);
                        showMap();
                    } else
                        showPopup(event, message.toUpperCase() + "!");
                }
            }
        });
    }

    private void improvementButton(ArrayList<ImageView> imageViews, String string) {
        UnitController.setUnit(chosenUnit, "");
        if (UnitController.canBuildImprovementHere(Improvement.valueOf(string)).length() == 0) {
            ImageView imageView = new ImageView(ImageBase.valueOf(string + "_ICON").getImage());
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    UnitController.setUnit(chosenUnit, "build improvement -t " + string);//TODO SHOW THE ERR TO USER
                    String message = UnitController.handleUnitOptions();
                    if (message.length() == 0) {
                        setChosenUnit(null);
                        showMap();
                    } else
                        showPopup(event, message.toUpperCase() + "!");
                }
            });
            Tooltip.install(imageView, new Tooltip(string));
            imageViews.add(imageView);
        }
    }

    private void roadButton(ArrayList<ImageView> imageViews) {
        if (UnitController.canBuildRoadHere()) {
            ImageView imageView = new ImageView(ImageBase.ROAD_ICON.getImage());
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    UnitController.setUnit(chosenUnit, "build improvement -t ROAD");
                    String message = UnitController.handleUnitOptions();
                    if (message.length() == 0) {
                        setChosenUnit(null);
                        showMap();
                    } else
                        showPopup(event, message.toUpperCase() + "!");
                }
            });
            imageViews.add(imageView);
        }
    }

    private void railroadButton(ArrayList<ImageView> imageViews) {
        if (UnitController.getCivilization().hasReachedTech(Technology.RAILROAD)
                && UnitController.canBuildRailroadHere()) {
            ImageView imageView = new ImageView(ImageBase.RAIL_ROAD_ICON.getImage());
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    UnitController.setUnit(chosenUnit, "build improvement -t RAILROAD");
                    String message = UnitController.handleUnitOptions();
                    if (message.length() == 0) {
                        setChosenUnit(null);
                        showMap();
                    } else
                        showPopup(event, message.toUpperCase() + "!");
                }
            });
            imageViews.add(imageView);
        }
    }

    private void settlerExclusiveOptions(HBox hBox) {
        ImageView imageView = new ImageView(ImageBase.FOUND_CITY_ICON.getImage());
        setImageViewOpacity(imageView);
        imageView.setOnMouseClicked(event -> {
            UnitController.setUnit(chosenUnit, Commands.FOUND_CITY.getRegex());
            String message = UnitController.handleUnitOptions();
            if (message.length() == 0) {
                setChosenUnit(null);
                showMap();
            } else
                showPopup(event, message.toUpperCase() + "!");
        });
        Tooltip.install(imageView, new Tooltip("found city"));
        imageView.setFitWidth(70);
        imageView.setFitHeight(70);
        hBox.getChildren().add(imageView);
    }

    public void showMilitaryOptions() {
        unitOptionsNodes = new ArrayList<>();
        HBox hBox = new HBox();
        showCivAndMilSameOptions();
        ArrayList<ImageView> imageViews = new ArrayList<>();
        setMilitaryDecisionButtons(imageViews, "ATTACK");
        setMilitaryDecisionButtons(imageViews, "ALERT");
        setMilitaryDecisionButtons(imageViews, "FORTIFY");
        if (UnitController.militaryIsInCityTiles(chosenUnit))
            setMilitaryDecisionButtons(imageViews, "GARRISON");
        if (chosenUnit.getHealth() < Unit.MAX_HEALTH)
            setMilitaryDecisionButtons(imageViews, "HEAL");
        if (chosenUnit.getTile().getImprovementInProgress() != null || chosenUnit.getTile().getRouteInProgress() != null)//TODO ERFAN DAGHIGH CHECK KON SHRTESHO
            setMilitaryDecisionButtons(imageViews, "PILLAGE");
        if (chosenUnit.getType().isSiege())
            setMilitaryDecisionButtons(imageViews, "SETUP_RANDED");
        hBox.setLayoutY(900 - 70 - 70);
        hBox.setLayoutX(456);
        hBox.setStyle("-fx-background-color: rgba(216,118,118,0.87); -fx-background-radius: 0 20 0 0;");
        hBox.getChildren().addAll(imageViews);
        unitOptionsNodes.add(hBox);
        backgroundPane.getChildren().addAll(unitOptionsNodes);
    }

    private void setMilitaryDecisionButtons(ArrayList<ImageView> imageViews, String string) {
        ImageView imageView = new ImageView(ImageBase.valueOf(string + "_ICON").getImage());
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (string.equals("PILLAGE")) {
                    String type;
                    if (chosenUnit.getTile().getImprovementInProgress() != null)
                        type = chosenUnit.getTile().getImprovementInProgress().getKey().toString();
                    else type = chosenUnit.getTile().getRouteInProgress().getKey();
                    UnitController.setUnit(chosenUnit, "pillage -i " + type);//TODO SHOW ERR TO USER
                    String message = UnitController.handleUnitOptions();
                    if (message.length() == 0) {
                        setChosenUnit(null);
                        showMap();
                    } else
                        showPopup(event, message.toUpperCase() + "!");
                } else if (string.equals("ATTACK")) {
                    Main.unitActionsSound("attack");
                    chosenUnit.realSetStatus(UnitStatus.ATTACK);
                } else {
                    doUnitOptions(string, event);
                }
            }
        });
        Tooltip.install(imageView, new Tooltip(string));
        imageView.setFitWidth(70);
        imageView.setFitHeight(70);
        setImageViewOpacity(imageView);
        imageViews.add(imageView);
    }


    public void showCivilianOptions() {
        unitOptionsNodes = new ArrayList<>();
        showCivAndMilSameOptions();
        HBox hBox = new HBox();
        hBox.setLayoutY(900 - 70 - 70);
        hBox.setLayoutX(456);
        if (chosenUnit.getType() == UnitType.WORKER) {
            workerExclusiveOptions(hBox);
            System.out.println("are chaghal");
        } else settlerExclusiveOptions(hBox);
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
        setSameOptionsFunctions(imageViews[0], "DO_NOTHING");
        setSameOptionsFunctions(imageViews[1], "CHANGE_STATE");
        setSameOptionsFunctions(imageViews[2], "CANCEL_MISSION");
        setSameOptionsFunctions(imageViews[3], "DELETE");

        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].setFitHeight(70);
            imageViews[i].setFitWidth(70);
            setImageViewOpacity(imageViews[i]);
            hBox.getChildren().add(imageViews[i]);
        }

        unitOptionsNodes.add(hBox);
    }

    private void setSameOptionsFunctions(ImageView imageView, String string) {
        imageView.setOnMouseClicked(event -> {
            if (string.equals("CHANGE_STATE")) {
                if (chosenUnit.getStatus() == UnitStatus.SLEEP) {
                    doUnitOptions("WAKE_UNIT", event);
                } else {
                    doUnitOptions("SLEEP_UNIT", event);
                }
            } else {
                if (string.equals("DELETE")) Main.unitActionsSound("unitKill");
                doUnitOptions(string, event);
            }
        });
        Tooltip.install(imageView, new Tooltip(string));
    }

    private void doUnitOptions(String string, Event event) {
        UnitController.setUnit(chosenUnit, Commands.valueOf(string).getRegex());//TODO SHOW ERR TO USER
        String message = UnitController.handleUnitOptions();
        if (message.length() == 0) {
            setChosenUnit(null);
            showMap();
        } else
            showPopup((MouseEvent) event, message.toUpperCase() + "!");
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
        nextTurn.setOnMouseClicked(this::nextTurn);
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
                    stage.setTitle("Research");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.getIcons().add(new Image("/Pictures/cityIcons/research.png"));
                    stage.show();
                } catch (IOException e) {
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
                    stage.setTitle("Production");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.getIcons().add(new Image("/Pictures/cityIcons/production.png"));
                    stage.show();
                } catch (IOException e) {
                    System.out.println("failed to load production fxml");
                    e.printStackTrace();
                }
            });
            backgroundPane.getChildren().add(prodImgView);
        }
    }

    private void setMouseClicksForIcon(ImageView imageView) {
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

    public void showPopup(MouseEvent mouseEvent, String message) {
        message = message + " ";
        message = " " + message;
        Popup popup = new Popup();
        Label label = new Label(message);
        label.setTextFill(Color.rgb(200, 0, 0, 1));
        label.setMinHeight(100);
        label.setMinWidth(400);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setStyle("-fx-font-size: 30; -fx-font-family: 'Tw Cen MT'; -fx-font-weight: bold;" +
                "-fx-background-color: white; -fx-background-radius: 5; -fx-alignment: center;" +
                "-fx-border-color: black; -fx-border-width: 3; -fx-border-radius: 5;");
        popup.getContent().add(label);
        popup.setAutoHide(true);
        //TODO... play error sound;
        popup.show(((Node) (mouseEvent.getSource())).getScene().getWindow());
    }

    public void getConquerorDecision(City city) {
        HBox hBox = new HBox();
        Button[] buttons = new Button[4];

        buttons[0] = new Button("do nothing");
        buttons[1] = new Button("attach");
        buttons[2] = new Button("destroy");
        buttons[3] = new Button("raze");
        for (Button button : buttons) {
            button.getStylesheets().add("css/MapStyle.css");
            button.getStyleClass().add("conquerorButton");
        }
        Popup popup = new Popup();
        setButtonFunction(popup, buttons[0], "do nothing", city);
        setButtonFunction(popup, buttons[1], "attach", city);
        setButtonFunction(popup, buttons[2], "destroy", city);
        setButtonFunction(popup, buttons[3], "raze", city);
        hBox.getChildren().addAll(buttons);
        hBox.setTranslateX(800 - hBox.getWidth() / 2);
        hBox.setTranslateY(700);
        popup.getContent().add(hBox);
        popup.show(backgroundPane.getScene().getWindow());
    }

    private void setButtonFunction(Popup popup, Button button, String string, City city) {
        button.setOnMouseClicked(event -> {
            Civilization civilization = city.getCivilization();
            if (string.equals("attach")) CivilizationController.attachCity(city);
            else if (string.equals("destroy")) CivilizationController.destroyCity(city);
            else if (string.equals("raze")) CivilizationController.razeCity(city);
            UnitController.checkIfDefeated(civilization);
            popup.hide();
            showMap();
        });
        button.setOnMouseEntered(event -> {
            button.setOpacity(0.3);
        });
        button.setOnMouseExited(event -> {
            button.setOpacity(1);
        });
    }

    public void setEnded(boolean ended) {
        isEnded = ended;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public void showScores() {
        Rectangle rectangle = new Rectangle(0, 0, 1600, 900);
        rectangle.setFill(Color.BLACK);
        rectangle.setOpacity(0.7);
        backgroundPane.getChildren().add(rectangle);
        VBox vBox = new VBox();
        Comparator<User> cmp = Comparator.comparing(User::getScore);
        Game.getInstance().getPlayers().sort(cmp);
        showSortedPlayers(vBox);
        vBox.setTranslateX(600);
        vBox.setTranslateY(100);
        Button button = new Button("Main menu");
        button.setOnMouseEntered(event -> {
            button.setOpacity(0.3);
        });
        button.setOnMouseExited(event -> {
            button.setOpacity(1);
        });
        button.setOnMouseClicked(event -> {
            NavigationTransition.fadeTransition(button, "MainMenu");
            Main.playSound("Menu.mp3");
        });
        button.setLayoutX(600);
        vBox.getChildren().add(button);
        backgroundPane.getChildren().add(vBox);
    }

    private void showSortedPlayers(VBox vBox) {
        Label label;
        boolean isEntered = true;
        for (User player : Game.getInstance().getPlayers()) {
            Game.getInstance().getPlayerScores().put(player.getUsername(), player.getScore());
        }
        ArrayList<Integer> scores = new ArrayList(Game.getInstance().getPlayerScores().values());
        scores.sort(Collections.reverseOrder());
        for (Integer score : scores) {
            for (Map.Entry<String, Integer> set :
                    Game.getInstance().getPlayerScores().entrySet()) {
                if (set.getValue().equals(score)) {
                    if (isEntered) {
                        label = new Label(set.getKey() + " won!");
                        label.setStyle("-fx-text-fill: purple; -fx-font-size: 70;");
                        vBox.getChildren().add(label);
                        isEntered = false;
                    }
                    label = new Label(set.getKey() + ":   " + set.getValue());
                    label.setStyle("-fx-font-size: 40; -fx-text-fill: white;");
                    vBox.getChildren().add(label);
                    break;
                }
            }
        }
        for (User allUser : UserController.getAllUsers()) {
            if (Game.getInstance().getPlayerScores().containsKey(allUser.getUsername()))
                allUser.setScore(allUser.getScore() + Game.getInstance().getPlayerScores().get(allUser.getUsername()));
        }
    }
}
