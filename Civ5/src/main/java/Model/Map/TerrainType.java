package Model.Map;

public enum TerrainType {
    DESERT(0,0,0,-0.33,1,"\u001B[43m"),
    GRASS(2,0,0,-0.33,1,"\u001B[42m"),
    HILL(0,0,2,0.25,2,"\u001B[41m"),
    MOUNTAIN(0,0,0,0,1000,"\u001B[40m"),
    OCEAN(0,0,0,0,1000,"\u001B[44m"),
    PLAIN(0,1,1,-0.33,1,"\u001B[46m"),
    SNOW(0,0,0,-0.33,1,"\u001B[47m"),
    TUNDRA(0,1,0,-0.33,1,"\u001B[45m");

    private int gold;
    private int food;
    private int production;
    private double battleEffect;
    private int movementCost;
    private String color;

    TerrainType(int gold, int food,int production,double battleEffect,int movementCost,String color) {
        this.gold = gold;
        this.food = food;
        this.production = production;
        this.movementCost = movementCost;
        this.color = color;
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
