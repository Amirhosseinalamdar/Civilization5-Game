package View.Controller;

import App.Main;
import Model.Game;
import Model.Map.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;


public class MapController {
    @FXML
    private Pane backgroundPane;

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

    public void initialize() {
        showMap();

    }

    public void showMap() {
        String picture;
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
                if (tile.getFeature() == TerrainFeature.MARSH) {
                    if (tile.getType() == TerrainType.PLAIN) picture = "PLAIN_MARSH";
                    else picture = "GRASS_MARSH";
                } else if (tile.getFeature() != TerrainFeature.NONE && tile.getFeature() != TerrainFeature.DELTA)
                    picture = tile.getFeature().toString();
                else picture = tile.getType().toString();
                ImageView imageView = new ImageView(new Image("Pictures/tiles/" + picture + ".png"));
                imageView.setX(120 * (j - yStartingIndex) + (i % 2) * 60);
                imageView.setY(105 * (i - xStartingIndex));
                backgroundPane.getChildren().add(imageView);
                if (tile.getFeature() == TerrainFeature.DELTA) {
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
                if (tile.getResource() != Resource.NONE) {
                    ImageView imageView1 = new ImageView
                            (new Image("Pictures/resources/"+tile.getResource().toString()+".png"));
                    imageView1.setFitWidth(60);
                    imageView1.setFitHeight(60);
                    imageView1.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 10);
                    imageView1.setY(105 * (i - xStartingIndex) + 10);
                    backgroundPane.getChildren().add(imageView1);
                }
                if(tile.canUseItsResource()){
                    ImageView improvementImage = new ImageView(new Image("Pictures/improvments/"
                            +tile.getResource().getPrerequisiteImprovement().toString()+".png"));
                    improvementImage.setFitHeight(60);
                    improvementImage.setFitWidth(60);
                    improvementImage.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 60);
                    improvementImage.setY(105 * (i - xStartingIndex) + 10);
                    backgroundPane.getChildren().add(improvementImage);
                }else if(tile.getImprovementInProgress() != null){
                    ImageView improvementImage = new ImageView(new Image("Pictures/improvments/inProgress.png"));
                    improvementImage.setFitHeight(20);
                    improvementImage.setFitWidth(20);
                    improvementImage.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 20);
                    improvementImage.setY(105 * (i - xStartingIndex) + 25);
                    backgroundPane.getChildren().add(improvementImage);
                }
                if(tile.isRuined()) {
                    ImageView imageView1 = new ImageView(new Image("Pictures/tiles/ruins.png"));
                    imageView1.setFitWidth(80);
                    imageView1.setFitHeight(80);
                    imageView1.setX(120 * (j - yStartingIndex) + (i % 2) * 60 + 30);
                    imageView1.setY(105 * (i - xStartingIndex) + 40);
                    backgroundPane.getChildren().add(imageView1);
                }
            }
            flag2 = true;
        }
    }
}
