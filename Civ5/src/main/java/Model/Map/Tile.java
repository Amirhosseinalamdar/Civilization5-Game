package Model.Map;

import Model.Civilization;
import Model.TileStatus;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitType;
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
    private City city; // age null bashe city nis agar na capitale citie
//    private City memberOfThisCity;//TODO initialize she pls
    //private ArrayList<River> rivers;
    private boolean isRiverAtLeft;
    private int centerX; // vertical
    private int centerY; // horizontal
    private int indexInMapI;
    private int indexInMapJ;
    private Citizen workingCitizen;

    public void setWorkingCitizen (Citizen citizen) {
        this.workingCitizen = citizen;
    }

    public Citizen getWorkingCitizen() {
        return workingCitizen;
    }

//    //public City getMemberOfThisCity() {
//        return memberOfThisCity;
//    }

    public void setRiverAtLeft(boolean riverAtLeft) {
        isRiverAtLeft = riverAtLeft;
    }

    public boolean isRiverAtLeft() {
        return isRiverAtLeft;
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

    public void setCity (City city) {
        this.city = city;
    }

    public TerrainType getType() {
        return type;
    }

    public int getIndexInMapI() {
        return indexInMapI;
    }

    public int getIndexInMapJ() {
        return indexInMapJ;
    }

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

    public TerrainType getTypeForCiv(Civilization civilization,int i,int j) {
        if(civilization.getTileVisionStatuses()[i][j] == TileStatus.FOGGY) return TerrainType.FOGGY;
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
