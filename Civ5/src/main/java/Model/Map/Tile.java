package Model.Map;

import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import de.scravy.pair.Pair;

import java.util.ArrayList;

public class Tile {
    private TerrainType type;
    private TerrainFeature feature;
    private int foodPerTurn;

    public TerrainFeature getFeature() {
        return feature;
    }

    public int getFoodPerTurn() {
        return foodPerTurn;
    }

    public int getGoldPerTurn() {
        return goldPerTurn;
    }

    public int getProductionPerTurn() {
        return productionPerTurn;
    }

    public int getMovementCost() {
        return movementCost;
    }

    public double getCombatEffect() {
        return combatEffect;
    }

    public Military getMilitary() {
        return military;
    }

    public Unit getCivilian() {
        return civilian;
    }

    public Pair<Improvement, Integer> getImprovementInProgress() {
        return improvementInProgress;
    }

    public boolean isRaided() {
        return isRaided;
    }

    public City getCity() {
        return city;
    }

    public ArrayList<River> getRivers() {
        return rivers;
    }

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
    private int centerY; // horizontal


    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setMilitary (Military military) {
        this.military = military;
    }

    public void setCivilian (Unit civilian) {
        this.civilian = civilian;
    }

    public void setType(TerrainType type) {
        this.type = type;
    }

    public TerrainType getType() {
        return type;
    }
}
