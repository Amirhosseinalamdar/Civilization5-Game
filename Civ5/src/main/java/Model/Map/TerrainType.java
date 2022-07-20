package Model.Map;

import Model.ImageBase;
import javafx.scene.image.Image;

public enum TerrainType {
    DESERT(0, 0, 0, -0.33, 1, "\u001B[43m", ImageBase.DESERT.getImage()),
    GRASS(2, 0, 0, -0.33, 1, "\u001B[42m", ImageBase.GRASS.getImage()),
    HILL(0, 0, 2, 0.25, 2, "\u001B[41m", ImageBase.HILL.getImage()),
    MOUNTAIN(0, 0, 0, 0, 1000, "\u001B[40m", ImageBase.MOUNTAIN.getImage()),
    OCEAN(0, 0, 0, 0, 1000, "\u001B[44m", ImageBase.OCEAN.getImage()),
    PLAIN(0, 1, 1, -0.33, 1, "\u001B[46m", ImageBase.PLAIN.getImage()),
    SNOW(0, 0, 0, -0.33, 1, "\u001B[47m", ImageBase.SNOW.getImage()),
    TUNDRA(0, 1, 0, -0.33, 1, "\u001B[45m", ImageBase.TUNDRA.getImage()),
    FOGGY(0, 0, 0, 0, 0, "\u001B[0m", null);

    private final int gold;
    private final int food;
    private final int production;
    private double battleEffect;
    private final int movementCost;
    private final String color;
    private Image image;

    TerrainType(int gold, int food, int production, double battleEffect, int movementCost, String color, Image image) {
        this.gold = gold;
        this.food = food;
        this.production = production;
        this.movementCost = movementCost;
        this.color = color;
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public int getGold() {
        return gold;
    }

    public int getFood() {
        return food;
    }

    public int getProduction() {
        return production;
    }

    public double getBattleEffect() {
        return battleEffect;
    }

    public int getMovementCost() {
        return movementCost;
    }

    public String getColor() {
        return color;
    }
}
