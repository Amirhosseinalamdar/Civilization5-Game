package Client.Model.Map;

import Client.Model.ImageBase;
import javafx.scene.image.Image;

public enum Resource {
    BANANA("bonus", 1, 0, 0, Improvement.PLANTATION, ImageBase.BANANA.getImage()),
    COW("bonus", 1, 0, 0, Improvement.PASTURE, ImageBase.COW.getImage()),
    DEER("bonus", 1, 0, 0, Improvement.CAMP, ImageBase.DEER.getImage()),
    SHEEP("bonus", 1, 0, 0, Improvement.PASTURE, ImageBase.SHEEP.getImage()),
    WHEAT("bonus", 1, 0, 0, Improvement.FARM, ImageBase.WHEAT.getImage()),
    COAL("strategic", 0, 1, 0, Improvement.MINE, ImageBase.COAL.getImage()),
    HORSE("strategic", 0, 1, 0, Improvement.PASTURE, ImageBase.HORSE.getImage()),
    IRON("strategic", 0, 1, 0, Improvement.MINE, ImageBase.IRON.getImage()),
    COTTON("luxury", 0, 0, 2, Improvement.PLANTATION, ImageBase.COTTON.getImage()),
    COLOR("luxury", 0, 0, 2, Improvement.PLANTATION, ImageBase.COLOR.getImage()),
    FUR("luxury", 0, 0, 2, Improvement.CAMP, ImageBase.FUR.getImage()),
    JEWELERY("luxury", 0, 0, 3, Improvement.MINE, ImageBase.JEWELERY.getImage()),
    GOLD("luxury", 0, 0, 2, Improvement.MINE, ImageBase.GOLD.getImage()),
    BOKHOOR("luxury", 0, 0, 2, Improvement.PLANTATION, ImageBase.BOKHOOR.getImage()),
    TUSK("luxury", 0, 0, 2, Improvement.CAMP, ImageBase.TUSK.getImage()),
    MARBLE("luxury", 0, 0, 2, Improvement.QUARRY, ImageBase.MARBLE.getImage()),
    SILK("luxury", 0, 0, 2, Improvement.PLANTATION, ImageBase.SILK.getImage()),
    SILVER("luxury", 0, 0, 2, Improvement.MINE, ImageBase.SILVER.getImage()),
    SUGAR("luxury", 0, 0, 2, Improvement.PLANTATION, ImageBase.SUGAR.getImage()),
    NONE("", 0, 0, 0, Improvement.NONE, null);

    private final String type;
    private final int food;
    private final int production;
    private final int gold;
    private final Improvement prerequisiteImprovement;
    private final Image image;

    Resource(String type, int food, int production, int gold, Improvement improvement, Image image) {
        this.type = type;
        this.food = food;
        this.gold = gold;
        this.production = production;
        this.prerequisiteImprovement = improvement;
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

    public Improvement getPrerequisiteImprovement() {
        return prerequisiteImprovement;
    }

    public String getType() {
        return this.type;
    }
}
