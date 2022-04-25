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
    private ArrayList<City> cities;
    private ArrayList<Unit> units;
    private TileStatus[][] tileVisionStatuses = new TileStatus[20][20];//change name +vision
    private HashMap<Technology, Integer> turnsUntilNewTechnologies;
    private Technology inProgressTech;
    private ArrayList<Improvement> reachedImprovements;
    private ArrayList<City> builtCities;
    private int science;
    private int happiness;

    public Civilization() {
        cities = new ArrayList<>();
        units = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            for (int j = 0; j < 20; j++)
                tileVisionStatuses[i][j] = TileStatus.FOGGY;
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

    public Technology getInProgressTech() {
        return inProgressTech;
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
}
