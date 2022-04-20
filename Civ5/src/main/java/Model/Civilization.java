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
    private ArrayList<TileStatus> tileVisionStatuses;//change name +vision
    private HashMap<Technology, Integer> turnsUntilNewTechnologies;
    private Technology inProgressTech;
    private ArrayList<Improvement> reachedImprovements;
    private ArrayList<City> builtCities;
    private int science;
    private int happiness;
    private HashMap<Resource, Boolean> isLuxuryResourceReached;//key faghat luxury ha and moghe construct

    public void increaseTotalGold() {
        //TODO add cities gold per turn to tatal gold
    }




}
