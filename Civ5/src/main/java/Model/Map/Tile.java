package Model.Map;

import Controller.GameController;
import Controller.UnitController;
import Model.Civilization;
import Model.Game;
import Model.TileStatus;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import View.Controller.MapController;
import View.GameMenu;
import com.google.gson.annotations.Expose;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.util.ArrayList;

public class Tile extends ImageView {
    @Expose(serialize = true, deserialize = true)
    private TerrainType type;
    @Expose(serialize = true, deserialize = true)
    private TerrainFeature feature;
    @Expose(serialize = true, deserialize = true)
    private Resource resource;
    @Expose(serialize = true, deserialize = true)
    private int foodPerTurn;
    @Expose(serialize = true, deserialize = true)
    private int goldPerTurn;
    @Expose(serialize = true, deserialize = true)
    private int productionPerTurn;
    @Expose(serialize = true, deserialize = true)
    private int movementCost;
    @Expose(serialize = true, deserialize = true)
    private double combatEffect;
    @Expose(serialize = false, deserialize = false)
    private Military military;
    @Expose(serialize = false, deserialize = false)
    private Unit civilian;
    @Expose(serialize = true, deserialize = true)
    private Pair<Improvement, Integer> improvementInProgress;
    @Expose(serialize = true, deserialize = true)
    private Pair<String, Integer> routeInProgress;
    @Expose(serialize = true, deserialize = true)
    private Pair<String, Integer> removeInProgress;
    @Expose(serialize = true, deserialize = true)
    private boolean isRaided;
    @Expose(deserialize = false, serialize = false)
    private City city;
    @Expose(serialize = true, deserialize = true)
    private boolean isRiverAtLeft;
    @Expose(serialize = true, deserialize = true)
    private int centerX;
    @Expose(serialize = true, deserialize = true)
    private int centerY;
    @Expose(serialize = true, deserialize = true)
    private int indexInMapI;
    @Expose(serialize = true, deserialize = true)
    private int indexInMapJ;
    @Expose(serialize = true, deserialize = true)
    private Citizen workingCitizen;

    private boolean isRuined;


    public boolean isRuined() {
        return isRuined;
    }

    public void setWorkingCitizen(Citizen citizen) {
        this.workingCitizen = citizen;
    }

    public Citizen getWorkingCitizen() {
        return workingCitizen;
    }

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
                if (this.getRouteInProgress().getKey().equals("road")) movementCost -= 1;
                else if (this.getRouteInProgress().getKey().equals("railroad")) movementCost -= 3;
            }
        }
        return movementCost;
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

    public Pair<String, Integer> getRemoveInProgress() {
        return removeInProgress;
    }

    public void setRemoveInProgress(Pair<String, Integer> removeInProgress) {
        this.removeInProgress = removeInProgress;
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

    public void setMilitary (Military military) {
        this.military = military;
    }

    public void setCivilian(Unit civilian) {
        this.civilian = civilian;
    }

    public void setType(TerrainType type) {
        this.type = type;
    }

    public void setImprovementInProgress(Pair<Improvement, Integer> improvementInProgress) {
        this.improvementInProgress = improvementInProgress;
    }

    public void setRouteInProgress(Pair<String, Integer> routeInProgress) {
        this.routeInProgress = routeInProgress;
    }

    public TerrainType getTypeForCiv(Civilization civilization, int i, int j) {
        if (civilization.getTileVisionStatuses()[i][j] == TileStatus.FOGGY) return TerrainType.FOGGY;
        return type;
    }

    public void initializeTile(TerrainType type, TerrainFeature feature) {
        this.goldPerTurn = type.getGold() + feature.getGold();
        this.foodPerTurn = type.getFood() + feature.getFood();
        this.productionPerTurn = type.getProduction() + feature.getProduction();
        if (this.foodPerTurn < 0) this.foodPerTurn = 0;
        if (this.productionPerTurn < 0) this.productionPerTurn = 0;
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
                improvementInProgress.getValue() == 0;
    }

    public boolean isEnemyZoneOfControl(Civilization civilization) {
        ArrayList<Tile> neighbors = this.getNeighbors();
        for (Tile neighbor : neighbors)
            if (neighbor.getMilitary() != null && !neighbor.getMilitary().getCivilization().equals(civilization))
                return true;
        return false;
    }

    public ArrayList<Tile> getNeighbors() {
        ArrayList<Tile> neighbors = new ArrayList<>();
        int indexI = this.getIndexInMapI(), indexJ = this.getIndexInMapJ();

        for (int i = indexI - 1; i <= indexI + 1; i += 2) {
            if (GameController.invalidPos(i, indexJ)) continue;
            neighbors.add(Game.getInstance().getTiles()[i][indexJ]);
        }

        for (int j = indexJ - 1; j <= indexJ + 1; j += 2) {
            if (GameController.invalidPos(indexI, j)) continue;
            neighbors.add(Game.getInstance().getTiles()[indexI][j]);
        }

        if (indexI % 2 == 0) indexJ--;
        else indexJ++;

        for (int i = indexI - 1; i <= indexI + 1; i += 2) {
            if (GameController.invalidPos(i, indexJ)) continue;
            neighbors.add(Game.getInstance().getTiles()[i][indexJ]);
        }
        return neighbors;
    }

    public boolean isRoughTerrain() {
        return type.equals(TerrainType.HILL) || feature.equals(TerrainFeature.JUNGLE) || feature.equals(TerrainFeature.FOREST);
    }

    public boolean hasClearable(String clearable) {
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
        } catch (Exception e) {
            return false;
        }
    }

    private boolean hasRailRoad() {
        try {
            return routeInProgress.getKey().equals("railroad") &&
                    routeInProgress.getValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean hasFeature(String forestOrJungle) {
        return (forestOrJungle.equals("jungle") && feature.equals(TerrainFeature.JUNGLE)) ||
                (forestOrJungle.equals("forest") && feature.equals(TerrainFeature.FOREST));
    }

    public boolean isCenterOfCity(City city) {
        return this.city != null && this.city.equals(city) && this.city.getTiles().get(0).equals(this);
    }
}
