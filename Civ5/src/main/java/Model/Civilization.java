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
import java.util.Random;

public class Civilization {
    @Expose
    private int score;
    @Expose
    private int totalGold;
    @Expose
    private ArrayList<City> cities;
    @Expose
    private ArrayList<Unit> units;
    @Expose
    private TileStatus[][] tileVisionStatuses = new TileStatus[Game.getInstance().getMapSize()][Game.getInstance().getMapSize()];
    @Expose
    private HashMap<Technology, Integer> lastCostUntilNewTechnologies;
    @Expose
    private Technology inProgressTech;
    @Expose
    private int science;
    @Expose
    private int happiness;
    @Expose
    private double R;
    @Expose
    private double G;
    @Expose
    private double B;
    @Expose
    private int showingCenterI;
    @Expose
    private int showingCenterJ;
    @Expose
    private ArrayList<String> notifications;
    @Expose
    private HashMap<Resource, Integer> luxuryResources;
    @Expose
    private HashMap<Resource, Integer> strategicResources;
    @Expose
    private ArrayList<Request> requests;
    @Expose
    private ArrayList<String> inWarCivilizations;
    @Expose
    private String username;

    public String getUsername() {
        return username;
    }

    public HashMap<Resource, Integer> getStrategicResources() {
        return strategicResources;
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public Civilization(String username) {
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
        Color color = initCivSymbol();
        if (color == null) {
            Random random = new Random();
            color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255), 1);
        }
        R = color.getRed();
        G = color.getGreen();
        B = color.getBlue();
        this.showingCenterI = 1;
        this.showingCenterJ = 2;
        lastCostUntilNewTechnologies.put(Technology.AGRICULTURE, -1);
        inProgressTech = null;
        happiness = 50;
        score = 0;
        this.username = username;
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

    public ArrayList<String> getInWarCivilizations() {
        return inWarCivilizations;
    }

    private Color initCivSymbol() {
        if (!CivSymbol.BLUE.isTaken()) {
            CivSymbol.BLUE.setTaken(true);
            return CivSymbol.BLUE.getColor();
        } else if (!CivSymbol.RED.isTaken()) {
            CivSymbol.RED.setTaken(true);
            return CivSymbol.RED.getColor();
        } else if (!CivSymbol.GREEN.isTaken()) {
            CivSymbol.GREEN.setTaken(true);
            return CivSymbol.GREEN.getColor();
        } else if (!CivSymbol.WHITE.isTaken()) {
            CivSymbol.WHITE.setTaken(true);
            return CivSymbol.WHITE.getColor();
        } else if (!CivSymbol.BLACK.isTaken()) {
            CivSymbol.BLACK.setTaken(true);
            return CivSymbol.BLACK.getColor();
        } else {
            return null;
        }
    }

    public Color getColor() {
        return new Color(R, G, B, 1);
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

    public boolean hasPrerequisitesOf(Technology tech) {
        if (tech.getParents() == null) return true;
        for (Technology parent : tech.getParents())
            if (!this.hasReachedTech(parent)) return false;
        return true;
    }
}
