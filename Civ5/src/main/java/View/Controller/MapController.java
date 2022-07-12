package View.Controller;

import Controller.UnitController;
import Model.Game;
import Model.Map.Resource;
import Model.Map.TerrainFeature;
import Model.Map.TerrainType;
import Model.Map.Tile;
import Model.UnitPackage.Unit;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;


public class MapController {
    @FXML
    private Pane backgroundPane;

    private Unit chosenUnit;

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
        showMap();
    }

    public void showMap() {
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
                showTile(tile,i,j);
                showRiverAndDelta(tile,i,j);
                showResourceAndImprovements(tile,i,j);
                showRuins(tile,i,j);
            }
            flag2 = true;
        }
        showTileContentIfNeeded();
        showUnits();
        showStatusBar();
    }
    public void showTileContentIfNeeded(){
        if(tileImageViews.size() > 0){//not tested
            showTilesFoodProductionGold();
        }
        if(citizenImageViews.size() > 0){
            showCitizens();
        }
    }
    public void showTile(Tile tile,int i, int j){
        String picture;
        if (tile.getFeature() == TerrainFeature.MARSH) {
            if (tile.getType() == TerrainType.PLAIN) picture = "PLAIN_MARSH";
            else picture = "GRASS_MARSH";
        } else if (tile.getFeature() != TerrainFeature.NONE && tile.getFeature() != TerrainFeature.DELTA)
            picture = tile.getFeature().toString();
        else picture = tile.getType().toString();
        tile.setImage(new Image("Pictures/tiles/"+picture+".png"));
        tile.setX(120 * (j-yStartingIndex) + (i%2) * 60);
        tile.setY(105 * (i-xStartingIndex));
        tile.setFitHeight(140);
        tile.setFitWidth(120);
        backgroundPane.getChildren().add(tile);
    }
    public void showRiverAndDelta(Tile tile, int i, int j){
        if(tile.getFeature() == TerrainFeature.DELTA){
            ImageView imageView1 = new ImageView(new Image("Pictures/tiles/DELTA.png"));
            imageView1.setX(120 * (j - yStartingIndex) + (i % 2) * 60);
            imageView1.setY(105 * (i - xStartingIndex));
            backgroundPane.getChildren().add(imageView1);
        }
        if (tile.isRiverAtLeft()) {
            String str;
            if (i % 2 == 1) str = "RIVER_LL";
            else str = "RIVER_L";
            ImageView imageView2 = new ImageView(new Image("Pictures/tiles/" + str + ".png"));
            imageView2.setX(120 * (j - yStartingIndex) + (i % 2) * 60);
            imageView2.setY(105 * (i - xStartingIndex));
            backgroundPane.getChildren().add(imageView2);
        }
    }
    public void showResourceAndImprovements(Tile tile, int i, int  j){
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
    public void showRuins(Tile tile, int i, int j){
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
        for(int i=0;i<Game.getInstance().getMapSize();i++){
            for(int j=0;j<Game.getInstance().getMapSize();j++){
                Tile tile = Game.getInstance().getTiles()[i][j];
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
    public void showStatusBar(){//TODO ADD TEXT BOXES
        ImageView imageView = new ImageView(new Image("Pictures/Panels/statusBar.png"));
        backgroundPane.getChildren().add(imageView);
        ImageView[] imageViews = new ImageView[5];
        imageViews[0] = new ImageView(new Image("Pictures/Panels/Science.png"));
        imageViews[1] = new ImageView(new Image("Pictures/Panels/Gold.png"));
        imageViews[2] = new ImageView(new Image("Pictures/Panels/Happiness.png"));
        imageViews[3] = new ImageView(new Image("Pictures/Panels/Food.png"));
        imageViews[4] = new ImageView(new Image("Pictures/Panels/Turn.png"));
        for(int i=0;i<imageViews.length;i++){
            imageViews[i].setFitHeight(40);
            imageViews[i].setFitWidth(40);
            imageViews[i].setY(10);
            imageViews[i].setX(40 + 140 * i);
            if(i == 4) imageViews[i].setX(imageViews[i].getX() + 700);
            backgroundPane.getChildren().add(imageViews[i]);
        }
        imageView = new ImageView(new Image("Pictures/Panels/myCiv.png"));
        imageView.setFitHeight(250);
        imageView.setX(0);
        imageView.setY(900 - imageView.getLayoutBounds().getHeight());
        backgroundPane.getChildren().add(imageView);

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

    public void hideTilesFoodProductionGold(){//not tested
        backgroundPane.getChildren().removeAll(tileImageViews);
    }

    public void showCitizens(){
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
                if(tile.getWorkingCitizen() != null) {
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

    public void hideCitizen(){
        backgroundPane.getChildren().removeAll(citizenImageViews);
    }
}
