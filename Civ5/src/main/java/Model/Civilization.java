package Model;

import Model.Map.City;
import Model.Map.Improvement;
import Model.Map.Resource;
import Model.Map.Tile;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitType;
import com.google.gson.annotations.Expose;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class Civilization {
    @Expose(serialize = true, deserialize = true)
    private int score;
    @Expose(serialize = true, deserialize = true)
    private int totalGold;
    @Expose(serialize = true, deserialize = true)
    private ArrayList<City> cities;
    @Expose(serialize = true, deserialize = true)
    private ArrayList<Unit> units;
    @Expose(serialize = true, deserialize = true)
    private TileStatus[][] tileVisionStatuses = new TileStatus[Game.getInstance().getMapSize()][Game.getInstance().getMapSize()];
    @Expose(serialize = true, deserialize = true)
    private HashMap<Technology, Integer> lastCostUntilNewTechnologies;
    @Expose(serialize = true, deserialize = true)
    private Technology inProgressTech;
    @Expose(serialize = true, deserialize = true)
    private int science;
    @Expose(serialize = true, deserialize = true)
    private int happiness;
    @Expose(serialize = true, deserialize = true)
    private final Color color;
    @Expose(serialize = true, deserialize = true)
    private int showingCenterI;
    @Expose(serialize = true, deserialize = true)
    private int showingCenterJ;
    @Expose(serialize = true, deserialize = true)
    private ArrayList<String> notifications;
    @Expose(serialize = true, deserialize = true)
    private HashMap<Resource, Integer> luxuryResources;
    @Expose(serialize = true, deserialize = true)
    private HashMap<Resource, Integer> strategicResources;
    @Expose(serialize = true, deserialize = true)
    private ArrayList<Request> requests;
    @Expose(serialize = true, deserialize = true)
    private ArrayList<Civilization> inWarCivilizations;

    public HashMap<Resource, Integer> getStrategicResources() {
        return strategicResources;
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public Civilization() {
        cities = new ArrayList<>();
        units = new ArrayList<>();
        notifications = new ArrayList<>();
        lastCostUntilNewTechnologies = new HashMap<>();
        luxuryResources = new HashMap<>();
        strategicResources = new HashMap<>();
        requests = new ArrayList<>();
        inWarCivilizations = new ArrayList<>();
        for (int i = 0; i < Game.getInstance().getMapSize(); i++)
            for (int j = 0; j < Game.getInstance().getMapSize(); j++)
                this.tileVisionStatuses[i][j] = TileStatus.FOGGY;
        this.color = initCivSymbol();
        this.showingCenterI = 1;
        this.showingCenterJ = 2;
        lastCostUntilNewTechnologies.put(Technology.AGRICULTURE, -1);
        inProgressTech = null;
        happiness = 50;
        score = 0;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    public ArrayList<String> getNotifications() {
        return notifications;
    }

    public int getScore() {
        return score;
    }

    public HashMap<Resource, Integer> getLuxuryResources() {
        return luxuryResources;
    }

    public void increaseScore(int score) {
        this.score += score;
    }

    public void setTotalGold(int totalGold) {
        this.totalGold = totalGold;
    }

    public Technology getInProgressTech() {
        return inProgressTech;
    }

    public void setInProgressTech(Technology inProgressTech) {
        this.inProgressTech = inProgressTech;
    }

    public int getShowingCenterI() {
        return showingCenterI;
    }

    public int getShowingCenterJ() {
        return showingCenterJ;
    }

    public void setShowingCenterI(int showingCenterI) {
        this.showingCenterI = showingCenterI;
    }

    public void setShowingCenterJ(int showingCenterJ) {
        this.showingCenterJ = showingCenterJ;
    }

    public ArrayList<Civilization> getInWarCivilizations() {
        return inWarCivilizations;
    }

    private Color initCivSymbol() {
        if (!CivSymbol.BLUE.isTaken()){
            CivSymbol.BLUE.setTaken(true);
            return CivSymbol.BLUE.getColor();
        } else if (!CivSymbol.RED.isTaken()){
            CivSymbol.RED.setTaken(true);
            return CivSymbol.RED.getColor();
        } else if (!CivSymbol.GREEN.isTaken()){
            CivSymbol.GREEN.setTaken(true);
            return CivSymbol.GREEN.getColor();
        } else if (!CivSymbol.WHITE.isTaken()) {
            CivSymbol.WHITE.setTaken(true);
            return CivSymbol.WHITE.getColor();
        }else if (!CivSymbol.BLACK.isTaken()) {
            CivSymbol.BLACK.setTaken(true);
            return CivSymbol.BLACK.getColor();
        }else {
            System.out.println("not enough symbols");
            return null;
        }
    }

    public Color getColor() {
        return color;
    }

    public void createSettlerAndWarriorOnTile(Tile tile) {
        Unit settler = new Unit(UnitType.SETTLER);
        settler.setCivilization(this);
        this.units.add(settler);
        settler.setTile(tile);
        tile.setCivilian(settler);

        Military warrior = new Military(UnitType.WARRIOR);
        warrior.setCivilization(this);
        this.units.add(warrior);
        warrior.setTile(tile);
        tile.setMilitary(warrior);
    }

    public void addCity(City city) {
        this.cities.add(city);
    }

    public int getTotalGold() {
        return totalGold;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void addUnit(Unit unit) {
        this.units.add(unit);
    }

    public TileStatus[][] getTileVisionStatuses() {
        return tileVisionStatuses;
    }

    public HashMap<Technology, Integer> getLastCostUntilNewTechnologies() {
        return lastCostUntilNewTechnologies;
    }

    public int getScience() {
        return science;
    }

    public int getHappiness() {
        return happiness;
    }

    public void setTotalScience(int science) {
        this.science = science;
    }

    public ArrayList<Resource> getResources() {
        ArrayList<Resource> resources = new ArrayList<>();
        for (City city : cities)
            for (Tile tile : city.getTiles())
                if (tile.canUseItsResource()) resources.add(tile.getResource());
        return resources;
    }

    public boolean hasReachedTech(Technology technology) {
        try {
            return lastCostUntilNewTechnologies.get(technology) <= 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasPrerequisitesOf (Technology tech) {
        for (Technology parent : tech.getParents())
            if (!this.hasReachedTech(parent)) return false;
        return true;
    }
}
