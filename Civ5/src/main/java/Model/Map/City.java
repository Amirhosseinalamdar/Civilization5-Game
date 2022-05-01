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
    private int goldPerTurn;
    private int foodPerTurn;
    private int productionPerTurn;
    private int sciencePerTurn;
    private HashMap <UnitType, Integer> turnsUntilNewProductions;
    private UnitType inProgressUnit;
    private ArrayList <Citizen> citizens;
    private int turnsUntilBirthCitizen;
    private int turnsUntilDeathCitizen;
    private int citizenNecessityFood;
    private int gainCitizenLastFood;
    private int lostCitizenLastFood;
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
        if (civilization.getCities().isEmpty()) cityStatus = CityStatus.CAPITAL;
        else cityStatus = CityStatus.NORMAL;
        civilization.addCity(this);
        int goldSum = 0;
        for (Tile tile : tiles) goldSum += tile.getGoldPerTurn();
        System.out.println("now my gold = " + goldSum);
        civilization.setTotalGold(goldSum + civilization.getTotalGold());
        //TODO ina inja chi migan
        this.citizens = new ArrayList<>();
        //TODO ehtemalan bayad inchenin chizi inja dashte bashim
//        this.citizens.add(new Citizen(this, centerTile));
//        this.citizens.add(new Citizen(this, centerTile));
        this.citizenNecessityFood = 10;
        this.gainCitizenLastFood = 10;
        this.lostCitizenLastFood = 10;
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
        return foodPerTurn;
    }

    public int getProductionPerTurn() {
        return productionPerTurn;
    }

    public int getGoldPerTurn() {
        return goldPerTurn;
    }

    public int getSciencePerTurn() {
        return sciencePerTurn;
    }

    public int getTurnsUntilBirthCitizen() {
        return turnsUntilBirthCitizen;
    }

    public int getTurnsUntilDeathCitizen() {
        return turnsUntilDeathCitizen;
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

    public void setStoredFood(int storedFood) {
        this.storedFood = storedFood;
    }

    public void setFoodPerTurn(int foodPerTurn) {
        this.foodPerTurn = foodPerTurn;
    }

    public void setProductionPerTurn(int productionPerTurn) {
        this.productionPerTurn = productionPerTurn;
    }

    public void setGoldPerTurn(int goldPerTurn) {
        this.goldPerTurn = goldPerTurn;
    }

    public void setSciencePerTurn(int sciencePerTurn) {
        this.sciencePerTurn = sciencePerTurn;
    }

    public void setTurnsUntilBirthCitizen(int turnsUntilBirthCitizen) {
        this.turnsUntilBirthCitizen = turnsUntilBirthCitizen;
    }

    public void setTurnsUntilDeathCitizen(int turnsUntilDeathCitizen) {
        this.turnsUntilDeathCitizen = turnsUntilDeathCitizen;
    }

    public void updateStoredFood(){
        int food = this.foodPerTurn - ((this.citizens.size() - 1) * 2);
        if (food < 0) storedFood = food;
        else if (inProgressUnit.equals(UnitType.SETTLER)) storedFood = 0;
        else storedFood = food;
    }

    public void handlePopulation() {
        if (storedFood > 0) {
            lostCitizenLastFood = citizenNecessityFood;
            gainCitizenLastFood -= storedFood;
            if (gainCitizenLastFood <= 0) {
                Citizen citizen = new Citizen(this, tiles.get(0));
                citizens.add(citizen);
                citizenNecessityFood *= 1.5;
                gainCitizenLastFood = citizenNecessityFood;
            }
            turnsUntilBirthCitizen = gainCitizenLastFood / storedFood;
        } else if (storedFood == 0) turnsUntilBirthCitizen = 0;
        else if (citizens.size() > 1){
            gainCitizenLastFood = citizenNecessityFood;
            lostCitizenLastFood += storedFood;
            if (lostCitizenLastFood <= 0) {
                citizens.remove(citizens.size() - 1);
                citizenNecessityFood *= 0.66;
                lostCitizenLastFood = citizenNecessityFood;
            }
            turnsUntilDeathCitizen = lostCitizenLastFood / storedFood;
        }
    }

    public void calculateStrength() {
        //TODO update strength based on the unit inside the city
    }
}
