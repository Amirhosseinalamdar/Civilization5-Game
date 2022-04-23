package Model;

import Model.Map.City;
import Model.Map.Improvement;
import Model.Map.Resource;
import Model.Map.Tile;
import Model.UnitPackage.Unit;

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
        //TODO add cities gold per turn to tatal gold
    }
}
