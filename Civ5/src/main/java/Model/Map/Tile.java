package Model.Map;

import Controller.GameController;
import Model.Civilization;
import Model.Game;
import Model.TileStatus;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import javafx.util.Pair;

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
    private Pair<Improvement, Integer> improvementInProgress;
    private Pair<String, Integer> routeInProgress;
    private boolean isRaided;
    private City city; // age null bashe city nis agar na capitale citie
    //    private City memberOfThisCity;//TODO initialize she pls
    //private ArrayList<River> rivers;
    private boolean isRiverAtLeft;
    private int centerX; // vertical
    private int centerY; // horizontal
    private int indexInMapI;
    private int indexInMapJ;
    private Citizen workingCitizen;

    public void setWorkingCitizen(Citizen citizen) {
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
        int movementCost = this.movementCost;
        if (this.getRouteInProgress() != null) {
            if (this.getRouteInProgress().getValue() <= 0) {
                if (this.getRouteInProgress().getKey().equals("road")) movementCost -= 1; //TODO... set this
                else if (this.getRouteInProgress().getKey().equals("railroad")) movementCost -= 3; //TODO
            }
        }
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

    public Pair<String, Integer> getRouteInProgress() {
        return routeInProgress;
    }

    public boolean isRaided() {
        return isRaided;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
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

    public void setMilitary(Military military) {
        this.military = military;
    }

    public void setCivilian(Unit civilian) {
        this.civilian = civilian;
    }

    public void setType(TerrainType type) {
        this.type = type;
    }

    public void setImprovementInProgress (Pair <Improvement, Integer> improvementInProgress) {
        this.improvementInProgress = improvementInProgress;
    }

    public void setRouteInProgress (Pair <String, Integer> routeInProgress) {
        this.routeInProgress = routeInProgress;
    }

    public TerrainType getTypeForCiv(Civilization civilization, int i, int j) {
        if (civilization.getTileVisionStatuses()[i][j] == TileStatus.FOGGY) return TerrainType.FOGGY;
        return type;
    }

    public void initializeTile(TerrainType type, TerrainFeature feature) {//first time init
        this.goldPerTurn = type.getGold() + feature.getGold();
        this.foodPerTurn = type.getFood() + feature.getFood();
        this.productionPerTurn = type.getProduction() + feature.getProduction();
        this.combatEffect = type.getBattleEffect() + feature.getBattleEffect();
        this.movementCost = type.getMovementCost() + feature.getMovementCost();
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

    public boolean canUseItsResource() {
        if (resource == null || improvementInProgress == null) return false;
        return improvementInProgress.getKey().equals(resource.getPrerequisiteImprovement()) &&
                improvementInProgress.getValue() <= 0;
    }

    public boolean isEnemyZoneOfControl(Civilization civilization) {
        ArrayList <Tile> neighbors = this.getNeighbors();
        for (Tile neighbor : neighbors)
            if (neighbor.getMilitary() != null && !neighbor.getMilitary().getCivilization().equals(civilization))
                return true;
        return false;
    }

    public ArrayList <Tile> getNeighbors() {
        ArrayList<Tile> neighbors = new ArrayList<>();
        int indexI = this.getIndexInMapI(), indexJ = this.getIndexInMapJ();

        for (int i = indexI - 1; i <= indexI + 1; i += 2) {
            if (GameController.invalidPos(i, indexJ)) continue;
            neighbors.add(Game.getTiles()[i][indexJ]);
        }

        for (int j = indexJ - 1; j <= indexJ + 1; j += 2) {
            if (GameController.invalidPos(indexI, j)) continue;
            neighbors.add(Game.getTiles()[indexI][j]);
        }

        if (indexJ % 2 == 0) indexI--;
        else indexI++;

        for (int j = indexJ - 1; j <= indexJ + 1; j += 2) {
            if (GameController.invalidPos(indexI, j)) continue;
            neighbors.add(Game.getTiles()[indexI][j]);
        }
        return neighbors;
    }

    public boolean isRoughTerrain() {
        return type.equals(TerrainType.HILL) || feature.equals(TerrainFeature.JUNGLE) || feature.equals(TerrainFeature.FOREST);
    }

    public boolean hasClearable (String clearable) {
        if (clearable.equals("road"))
            return hasRoad();
        else if (clearable.equals("railroad"))
            return hasRailRoad();
        else
            return hasFeature(clearable);
    }

    private boolean hasRoad() {
        try {
            return routeInProgress.getKey().equals("road") &&
                    routeInProgress.getValue() == 0;
        }
        catch (Exception e) {
            return false;
        }
    }

    private boolean hasRailRoad() {
        try {
            return routeInProgress.getKey().equals("railroad") &&
                    routeInProgress.getValue() == 0;
        }
        catch (Exception e) {
            return false;
        }
    }

    private boolean hasFeature (String forestOrJungle) {
        return (forestOrJungle.equals("jungle") && feature.equals(TerrainFeature.JUNGLE)) ||
                (forestOrJungle.equals("forest") && feature.equals(TerrainFeature.FOREST));
    }

    public boolean isCenterOfCity (City city) {
        return this.city != null && this.city.equals(city) && this.city.getTiles().get(0).equals(this);
    }
}
