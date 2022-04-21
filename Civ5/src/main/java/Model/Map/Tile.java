package Model.Map;

import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import de.scravy.pair.Pair;

import java.util.ArrayList;

public class Tile {
    private TerrainType type;
    private TerrainFeature feature;
    private Resource resource;
    private int foodPerTurn;
    private int goldPerTurn;
    private int productionPerTurn;
    private int movementCost;
    private double combatEffect;
    private Military military;
    private Unit civilian;
    private Pair <Improvement, Integer> improvementInProgress;
    private boolean isRaided;
    //private boolean isCity;
    private City city; // age null bashe city nis
    private ArrayList<River> rivers;
    private int centerX; // vertical
    private int CenterY; // horizontal
    private int indexInMapI;
    private int indexInMapJ;

    public void setIndexInMapI(int indexInMapI) {
        this.indexInMapI = indexInMapI;
    }

    public void setIndexInMapJ(int indexInMapJ) {
        this.indexInMapJ = indexInMapJ;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY) {
        CenterY = centerY;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return CenterY;
    }

    public void setType(TerrainType type) {
        this.type = type;
    }

    public TerrainType getType() {
        return type;
    }

    public void initializeTile(TerrainType type,TerrainFeature feature){//first time init
        this.goldPerTurn = type.getGold() + feature.getGold();
        this.foodPerTurn = type.getFood() + feature.getFood();
        this.productionPerTurn = type.getProduction() +feature.getProduction();
        this.combatEffect = type.getBattleEffect() + feature.getBattleEffect();
        this.movementCost = type.getMovementCost()+feature.getMovementCost();
    }

    public void setFeature(TerrainFeature feature) {
        this.feature = feature;
    }

    public TerrainFeature getFeature() {
        return feature;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }
}
