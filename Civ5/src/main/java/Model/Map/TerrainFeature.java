package Model.Map;

import Model.ImageBase;
import javafx.scene.image.Image;

public enum TerrainFeature {
    DELTA(2, 0, 0, -0.33, 1, ImageBase.DELTA.getImage()),
    JUNGLE(1, 1, 0, 0.25, 2,ImageBase.JUNGLE.getImage()),
    ICE(0, 0, 0, 0, 1000,ImageBase.ICE.getImage()),
    FOREST(1, -1, 0, 0.25, 2,ImageBase.FOREST.getImage()),
    MARSH(-1, 0, 0, -0.33, 2,null),
    OASIS(3, 0, 1, -0.33, 1,ImageBase.OASIS.getImage()),
    NONE(0, 0, 0, 0, 0,null);

    private final int food;
    private final int production;
    private final int gold;
    private final double battleEffect;
    private final int movementCost;
    private final Image image;
    TerrainFeature(int food, int production, int gold, double battleEffect, int movementCost,Image image) {
        this.food = food;
        this.production = production;
        this.gold = gold;
        this.battleEffect = battleEffect;
        this.movementCost = movementCost;
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public int getFood() {
        return food;
    }

    public int getProduction() {
        return production;
    }

    public int getGold() {
        return gold;
    }

    public double getBattleEffect() {
        return battleEffect;
    }

    public int getMovementCost() {
        return movementCost;
    }
}
