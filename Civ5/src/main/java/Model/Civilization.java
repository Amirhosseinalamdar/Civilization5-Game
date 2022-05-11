package Model;

import Model.Map.City;
import Model.Map.Improvement;
import Model.Map.Resource;
import Model.Map.Tile;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitType;

import java.util.ArrayList;
import java.util.HashMap;

public class Civilization {
    private int score;
    private int totalGold;
    private ArrayList<City> cities;
    private ArrayList<Unit> units;
    private TileStatus[][] tileVisionStatuses = new TileStatus[20][20];//change name +vision
    private HashMap <Technology, Integer> lastCostUntilNewTechnologies;
    private Technology inProgressTech;
    private int science;
    private int happiness;
    private final CivSymbol civColor;
    private int showingCenterI;
    private int showingCenterJ;
    private ArrayList<String> notifications;
    private HashMap<Resource, Integer> luxuryResources;

    public Civilization() {
        cities = new ArrayList<>();
        units = new ArrayList<>();
        notifications = new ArrayList<>();
        lastCostUntilNewTechnologies = new HashMap<>();
        luxuryResources = new HashMap<>();
        for (int i = 0; i < 20; i++)
            for (int j = 0; j < 20; j++)
                this.tileVisionStatuses[i][j] = TileStatus.FOGGY;
        this.civColor = initCivSymbol();
        this.showingCenterI = 1;
        this.showingCenterJ = 2;
        lastCostUntilNewTechnologies.put(Technology.AGRICULTURE, -1);
        inProgressTech = null;
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

    public void setScore(int score) {
        this.score = score;
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

    private CivSymbol initCivSymbol() {
        if (!CivSymbol.WHITE.isTaken()) return CivSymbol.WHITE;
        else if (!CivSymbol.PURPLE.isTaken()) return CivSymbol.PURPLE;
        else if (!CivSymbol.BLUE.isTaken()) return CivSymbol.BLUE;
        else if (!CivSymbol.RED.isTaken()) return CivSymbol.RED;
        else if (!CivSymbol.BLACK.isTaken()) return CivSymbol.BLACK;
        else {
            System.out.println("not enough symbols");
            return null;
        }

    }

    public String getCivColor() {
        return civColor.getSymbol();
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

    public void setTileVisionStatuses(int i, int j, TileStatus type) {
        this.tileVisionStatuses[i][j] = type;
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

    public void setScience(int science) {
        this.science = science;
    }

    public void increaseTotalGold(int gold) {
        this.totalGold += gold;
    }

    public void increaseTotalScience(int science) {
        this.science += science;
    }

    public ArrayList<Resource> getResources() {
        ArrayList<Resource> resources = new ArrayList<>();
        for (City city : cities)
            for (Tile tile : city.getTiles())
                if (tile.canUseItsResource()) resources.add(tile.getResource());
        return resources;
    }

    public boolean hasReachedTech (Technology technology) {
        try {
            return lastCostUntilNewTechnologies.get(technology) <= 0;
        }
        catch (Exception e) {
            return false;
        }
    }
}
