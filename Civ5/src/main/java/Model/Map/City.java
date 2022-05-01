package Model.Map;

import Controller.UnitController;
import Model.Civilization;
import Model.UnitPackage.UnitType;

import java.util.ArrayList;
import java.util.HashMap;

public class City {
    private ArrayList <Tile> tiles;
    private Civilization civilization;
    private int storedFood;
    private int foodPerTurn;
    private int productionPerTurn;
    private int sciencePerTurn;
    private HashMap <UnitType, Integer> turnsUntilNewProductions;
    private UnitType inProgressUnit;
    private ArrayList <Citizen> citizens;
    private int goldPerTurn;
    private int HP;
    private int combatStrength;
    private int rangedCombatStrength;
    private CityStatus cityStatus;
    private final String name;

    public City (Civilization civilization, Tile centerTile, String name) {
        turnsUntilNewProductions = new HashMap<>();
        tiles = new ArrayList<>();
        tiles.add(centerTile);
        tiles.addAll(UnitController.getTileNeighbors(centerTile));

        for (Tile tile : tiles)
            tile.setCity(this);

        this.civilization = civilization;
        civilization.addCity(this);
        int goldSum = 0;
        for (Tile tile : tiles) goldSum += tile.getGoldPerTurn();
        System.out.println("now my gold = " + goldSum);
        civilization.setTotalGold(goldSum + civilization.getTotalGold());

        this.citizens = new ArrayList<>();
        this.name = name;
    }

    public String getName() {
        return name;
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
        foodPerTurn = 0;
        for (Tile tile : tiles)
            foodPerTurn += tile.getFoodPerTurn();
        return foodPerTurn;
    }

    public int getProductionPerTurn() {
        productionPerTurn = 0;
        for (Tile tile : tiles)
            productionPerTurn += tile.getProductionPerTurn();
        return productionPerTurn;
    }

    public int getSciencePerTurn() {
        return sciencePerTurn;
    }

    public int getGoldPerTurn() {
        return goldPerTurn;
    }

    public int getTurnsUntilGrowthBorder() {
        //TODO
        return 0;
    }

    public int getTurnsUntilGrowthPopulation() {
        //TODO
        return 0;
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

    public void updateStoredFood(){

    }

    public void birthCitizen(){

    }

    public void calculateStrength() {
        //TODO update strength based on the unit inside the city
    }
}
