package Model.Map;

public enum TerrainFeature {
    DELTA(2,0,0,-0.33,1),//TODO add river then this
    JUNGLE(1,1,0,0.25,2),
    ICE(0,0,0,0,1000),
    FOREST(1,-1,0,0.25,2),
    SWAMP(-1,0,0,-0.33,2),
    OASIS(3,0,1,-0.33,1);
    //RIVER

    private int food;
    private int production;
    private int gold;
    private double battleEffect;
    private int movementCost;

    TerrainFeature(int food, int production, int gold, double battleEffect, int movementCost) {
        this.food = food;
        this.production = production;
        this.gold = gold;
        this.battleEffect = battleEffect;
        this.movementCost = movementCost;
    }
}
