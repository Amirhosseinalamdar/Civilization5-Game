package View.Controller;

import Controller.UnitController;
import Model.Game;
import Model.Map.TerrainFeature;
import Model.Map.TerrainType;
import Model.Map.Tile;
import Model.UnitPackage.Unit;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;


public class MapController {
    @FXML
    private Pane backgroundPane;

    private Unit chosenUnit;

    public Pane getBackgroundPane() {
        return backgroundPane;
    }

    private int xStartingIndex = 1;
    private int yStartingIndex = 1;

    public int getxStartingIndex() {
        return xStartingIndex;
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

    public void initialize(){
        showMap();
        backgroundPane.setOnMouseClicked(this::handleMouseClick);
        backgroundPane.setOnMouseReleased(this::handleMouseRelease);
    }
    public void showMap(){
        String picture;
        boolean flag1 = true;
        boolean flag2 = true;
        for(int i=xStartingIndex;i<xStartingIndex+9;i++){
            if(i > 0 && flag1){
                i=xStartingIndex - 1;
            }
            flag1 = false;
            for(int j=yStartingIndex;j<yStartingIndex+14;j++){
                if(j > 0 && flag2){
                    j=yStartingIndex - 1;
                }
                flag2 = false;
                Tile tile = Game.getInstance().getTiles()[i][j];
                if(tile.getFeature() == TerrainFeature.MARSH){
                    if(tile.getType() == TerrainType.PLAIN) picture = "PLAIN_MARSH";
                    else picture = "GRASS_MARSH";
                }
                else if(tile.getFeature() != TerrainFeature.NONE && tile.getFeature() != TerrainFeature.DELTA)
                    picture = tile.getFeature().toString();
                else picture = tile.getType().toString();
                tile.setImage(new Image("Pictures/tiles/"+picture+".png"));
                tile.setX(120 * (j-yStartingIndex) + (i%2) * 60);
                tile.setY(105 * (i-xStartingIndex));
                tile.setFitHeight(140);
                tile.setFitWidth(120);
                tile.setOnMouseClicked(event -> {
                    System.out.println("clicked");
                    if (chosenUnit == null) {
                        if (tile.getCivilian() == null) {
                            if (tile.getMilitary() == null) return;
                            chosenUnit = tile.getMilitary();
                        } else {
                            if (tile.getMilitary() != null) {
                                if (chosenUnit != null && chosenUnit.equals(tile.getMilitary()))
                                    chosenUnit = tile.getCivilian();
                                else chosenUnit = tile.getMilitary();
                            } else chosenUnit = tile.getCivilian();
                        }
                    }
                    else {
                        UnitController.setUnit(chosenUnit, "move to -c " + tile.getIndexInMapI() + " " + tile.getIndexInMapJ());
                        chosenUnit.setX(chosenUnit.getTile().getX() + 50);
                        chosenUnit.setY(chosenUnit.getTile().getY() + 60);
                        chosenUnit = null;
                    }
                });
                backgroundPane.getChildren().add(tile);

                if(tile.getFeature() == TerrainFeature.DELTA){
                    ImageView imageView1 = new ImageView(new Image("Pictures/tiles/DELTA.png"));
                    imageView1.setX(120 * (j-yStartingIndex) + (i%2) * 60);
                    imageView1.setY(105 * (i-xStartingIndex));
                    backgroundPane.getChildren().add(imageView1);
                }
                if(tile.isRiverAtLeft()){
                    String str;
                    if(i%2 == 1) str = "RIVER_LL";
                    else str = "RIVER_L";
                    ImageView imageView2 = new ImageView(new Image("Pictures/tiles/"+str+".png"));
                    imageView2.setX(120 * (j-yStartingIndex) + (i%2) * 60);
                    imageView2.setY(105 * (i-xStartingIndex));
                    backgroundPane.getChildren().add(imageView2);
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
    }

    private void handleMouseRelease (MouseEvent mouseEvent) {
        System.out.println("ok1");
        Tile tile = clickedTile(mouseEvent);
        if (tile == null) return;
        if (distFromCenterToMouse(mouseEvent.getX(), mouseEvent.getY(), tile.getX() + 60, tile.getY() + 70) < 60) {
            System.out.println(tile.getIndexInMapI() + " " + tile.getIndexInMapJ());

        }
    }

    private void handleMouseClick (MouseEvent mouseEvent) {
        System.out.println("ok2");

    }

    private Tile clickedTile (MouseEvent mouseEvent) {
        for (int i = 0; i < Game.getInstance().getMapSize(); i++) {
            for (int j = 0; j < Game.getInstance().getMapSize(); j++) {
                Tile tile = Game.getInstance().getTiles()[i][j];
                if (tile.getCivilian() != null || tile.getMilitary() != null) System.out.println(i + " va " + j);
            }
        }
        for (int i = 0; i < Game.getInstance().getMapSize(); i++) {
            for (int j = 0; j < Game.getInstance().getMapSize(); j++) {
                Tile tile = Game.getInstance().getTiles()[i][j];
                if (distFromCenterToMouse(mouseEvent.getX(), mouseEvent.getY(), tile.getX() + 60, tile.getY() + 70) < 60)
                    return tile;
            }
        }
        return null;
    }

    private double distFromCenterToMouse (double mouseX, double mouseY, double centerX, double centerY) {
        return Math.sqrt(Math.pow(mouseX - centerX, 2) + Math.pow(mouseY - centerY, 2));
    }
}
