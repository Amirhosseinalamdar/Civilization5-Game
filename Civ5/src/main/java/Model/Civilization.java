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
    private int totalGold;
    private final ArrayList <City> cities;
    private final ArrayList <Unit> units;
    private TileStatus[][] tileVisionStatuses = new TileStatus[20][20];
    private HashMap <Technology, Integer> turnsUntilNewTechnologies = new HashMap<>();
    private ArrayList <Improvement> reachedImprovements;
    private ArrayList <City> builtCities;
    private int science;
    private int happiness;
    private CivSymbol civColor;

    public void setTotalGold(int totalGold) {
        this.totalGold = totalGold;
    }

    public Civilization() {
        cities = new ArrayList<>();
        units = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            for (int j = 0; j < 20; j++)
                tileVisionStatuses[i][j] = TileStatus.FOGGY;
        civColor = initCivSymbol();
        turnsUntilNewTechnologies.put(Technology.AGRICULTURE, -1);
    }

    private CivSymbol initCivSymbol(){
        if(!CivSymbol.WHITE.isTaken()) return CivSymbol.WHITE;
        else if(!CivSymbol.PURPLE.isTaken()) return CivSymbol.PURPLE;
        else if(!CivSymbol.BLUE.isTaken()) return CivSymbol.BLUE;
        else if(!CivSymbol.RED.isTaken()) return CivSymbol.RED;
        else if(!CivSymbol.BLACK.isTaken()) return CivSymbol.BLACK;
        else {
            System.out.println("not enough symbols");
            return null;
        }

     }

    public String getCivColor() {
        return civColor.getSymbol();
    }

    public void createSettlerAndWarriorOnTile (Tile tile) {
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

    public void addCity (City city) {
        this.cities.add(city);
    }

    public void setTileVisionStatuses(int i,int j,TileStatus type) {
        this.tileVisionStatuses[i][j] = type;
    }

    private HashMap<Resource, Boolean> isLuxuryResourceReached;//key faghat luxury ha and moghe construct
    
    public int getTotalGold() {
        return totalGold;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public TileStatus[][] getTileVisionStatuses() {
        return tileVisionStatuses;
    }

    public HashMap<Technology, Integer> getTurnsUntilNewTechnologies() {
        return turnsUntilNewTechnologies;
    }

    public ArrayList<Improvement> getReachedImprovements() {
        return reachedImprovements;
    }

    public ArrayList<City> getBuiltCities() {
        return builtCities;
    }

    public int getScience() {
        return science;
    }

    public int getHappiness() {
        return happiness;
    }

    public HashMap<Resource, Boolean> getIsLuxuryResourceReached() {
        return isLuxuryResourceReached;
    }

    public void increaseTotalGold() {
        //TODO add cities gold per turn to total gold
    }

    public ArrayList <Resource> getResources() {
        ArrayList <Resource> resources = new ArrayList<>();
        for (City city : cities)
            for (Tile tile :city.getTiles())
                resources.add(tile.getResource());
        return resources;
    }
}
