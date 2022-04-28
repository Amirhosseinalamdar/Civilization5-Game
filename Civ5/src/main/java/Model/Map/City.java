package Model.Map;

import Model.Civilization;
import Model.UnitPackage.UnitType;

import java.util.ArrayList;
import java.util.HashMap;

public class City {
    private CityName name;
    private ArrayList <Tile> tiles;
    private Civilization civilization;
    private int storedFood;
    private int foodPerTurn;
    private int productionPerTurn;
    private HashMap <UnitType, Integer> turnsUntilNewProductions;
    private UnitType inProgressUnit;
    private ArrayList <Citizen> citizens;
    private int goldPerTurn;
    private int turnsUntilGrowth;
    private int HP;
    private int combatStrength;
    private int rangedCombatStrength;
    private CityStatus cityStatus;


    public City (Civilization civilization, Tile tile) {
        tiles = new ArrayList<>();
        tiles.add(tile);
        this.civilization = civilization;
        this.citizens = new ArrayList<>();

    }

    public CityName getName() {
        return name;
    }

    public void updateStoredFood(){

    }

    public void birthCitizen(){

    }

    public void calculateStrength() {
        //TODO update strength based on the unit inside the city
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public Civilization getCivilization() {
        return civilization;
    }

    public int getStoredFood() {
        return storedFood;
    }

    public int getFoodPerTurn() {
        return foodPerTurn;
    }

    public int getProductionPerTurn() {
        return productionPerTurn;
    }

    public HashMap<UnitType, Integer> getTurnsUntilNewProductions() {
        return turnsUntilNewProductions;
    }

    public UnitType getInProgressUnit() {
        return inProgressUnit;
    }

    public ArrayList<Citizen> getCitizens() {
        return citizens;
    }

    public int getGoldPerTurn() {
        return goldPerTurn;
    }

    public int getTurnsUntilGrowth() {
        return turnsUntilGrowth;
    }

    public int getHP() {
        return HP;
    }

    public int getCombatStrength() {
        return combatStrength;
    }

    public int getRangedCombatStrength() {
        return rangedCombatStrength;
    }

    public CityStatus getCityStatus() {
        return cityStatus;
    }
}
