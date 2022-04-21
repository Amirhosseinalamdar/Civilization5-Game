package Model.Map;

public enum TerrainType {
    DESERT(0,0,0,-0.33,1),
    GRASS(2,0,0,-0.33,1),
    HILL(0,0,2,0.25,2),
    MOUNTAIN(0,0,0,0,1000),
    OCEAN(0,0,0,0,1000),
    PLAIN(0,1,1,-0.33,1),
    SNOW(0,0,0,-0.33,1),
    TUNDRA(0,1,0,-0.33,1);

    private int gold;
    private int food;
    private int production;
    private double battleEffect;
    private int movementCost;

    TerrainType(int gold, int food,int production,double battleEffect,int movementCost) {
        this.gold = gold;
        this.food = food;
        this.production = production;
        this.movementCost = movementCost;
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
}
