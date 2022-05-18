package Model.Map;

public enum Resource {
    BANANA("bonus", 1, 0, 0, Improvement.PLANTATION),
    COW("bonus", 1, 0, 0, Improvement.PASTURE),
    DEER("bonus", 1, 0, 0, Improvement.CAMP),
    SHEEP("bonus", 1, 0, 0, Improvement.PASTURE),
    WHEAT("bonus", 1, 0, 0, Improvement.FARM),
    COAL("strategic", 0, 1, 0, Improvement.MINE),
    HORSE("strategic", 0, 1, 0, Improvement.PASTURE),
    IRON("strategic", 0, 1, 0, Improvement.MINE),
    COTTON("luxury", 0, 0, 2, Improvement.PLANTATION),
    COLOR("luxury", 0, 0, 2, Improvement.PLANTATION),
    FUR("luxury", 0, 0, 2, Improvement.CAMP),
    JEWELERY("luxury", 0, 0, 3, Improvement.MINE),
    GOLD("luxury", 0, 0, 2, Improvement.MINE),
    BOKHOOR("luxury", 0, 0, 2, Improvement.PLANTATION),
    TUSK("luxury", 0, 0, 2, Improvement.CAMP),
    MARBLE("luxury", 0, 0, 2, Improvement.QUARRY),
    SILK("luxury", 0, 0, 2, Improvement.PLANTATION),
    SILVER("luxury", 0, 0, 2, Improvement.MINE),
    SUGAR("luxury", 0, 0, 2, Improvement.PLANTATION),
    NONE("", 0, 0, 0, Improvement.NONE);

    private final String type;
    private final int food;
    private final int production;
    private final int gold;
    private final Improvement prerequisiteImprovement;

    Resource(String type, int food, int production, int gold, Improvement improvement) {
        this.type = type;
        this.food = food;
        this.gold = gold;
        this.production = production;
        this.prerequisiteImprovement = improvement;
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
