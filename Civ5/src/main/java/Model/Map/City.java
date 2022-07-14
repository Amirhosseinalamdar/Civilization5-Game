package Model.Map;

import Model.Civilization;
import Model.UnitPackage.UnitStatus;
import Model.UnitPackage.UnitType;
import View.GameMenu;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;

public class City {
    @Expose(serialize = true, deserialize = true)
    private ArrayList<Tile> tiles;
    @Expose(serialize = false, deserialize = false)
    private Civilization civilization;
    @Expose(serialize = true, deserialize = true)
    private int storedFood;
    @Expose(serialize = true, deserialize = true)
    private int goldPerTurn;
    @Expose(serialize = true, deserialize = true)
    private int foodPerTurn;
    @Expose(serialize = true, deserialize = true)
    private int productionPerTurn;
    @Expose(serialize = true, deserialize = true)
    private int sciencePerTurn;
    @Expose(serialize = true, deserialize = true)
    private HashMap<UnitType, Integer> lastCostsUntilNewProductions;
    @Expose(serialize = true, deserialize = true)
    private HashMap<Building, Integer> buildings;
    @Expose(serialize = true, deserialize = true)
    private Building inProgressBuilding;
    @Expose(serialize = true, deserialize = true)
    private UnitType inProgressUnit;
    @Expose(serialize = true, deserialize = true)
    private ArrayList<Citizen> citizens;
    @Expose(serialize = true, deserialize = true)
    private int turnsUntilBirthCitizen;
    @Expose(serialize = true, deserialize = true)
    private int turnsUntilDeathCitizen;
    @Expose(serialize = true, deserialize = true)
    private int citizenNecessityFood;
    @Expose(serialize = true, deserialize = true)
    private int gainCitizenLastFood;
    @Expose(serialize = true, deserialize = true)
    private int lostCitizenLastFood;
    @Expose(serialize = true, deserialize = true)
    private int turnsUntilGrowthBorder;
    @Expose(serialize = true, deserialize = true)
    private int borderExpansionCost;
    @Expose(serialize = true, deserialize = true)
    private int borderLastCost;
    @Expose(serialize = true, deserialize = true)
    private double HP;
    @Expose(serialize = true, deserialize = true)
    private double combatStrength;
    @Expose(serialize = true, deserialize = true)
    private double rangedCombatStrength;
    @Expose(serialize = true, deserialize = true)
    private CityStatus cityStatus;
    @Expose(serialize = true, deserialize = true)
    private final String name;

    public City(Civilization civilization, Tile centerTile, String name) {
        lastCostsUntilNewProductions = new HashMap<>();
        buildings = new HashMap<>();
        tiles = new ArrayList<>();
        tiles.add(centerTile);
        tiles.addAll(centerTile.getNeighbors());

        for (Tile tile : tiles)
            tile.setCity(this);

        this.civilization = civilization;
        if (civilization.getCities().isEmpty()) cityStatus = CityStatus.CAPITAL;
        else cityStatus = CityStatus.NORMAL;
        civilization.addCity(this);
        this.citizens = new ArrayList<>();
        Citizen citizen = new Citizen(this, centerTile);
        centerTile.setWorkingCitizen(citizen);
        this.citizens.add(citizen);
        this.citizens.add(new Citizen(this, null));
        this.citizenNecessityFood = 10;
        this.gainCitizenLastFood = 10;
        this.lostCitizenLastFood = 10;
        this.borderExpansionCost = 50;
        this.borderLastCost = 50;
        this.HP = 20;
        if (this.tiles.get(0).getType().equals(TerrainType.HILL)) this.combatStrength = 18;
        else this.combatStrength = 15;
        this.rangedCombatStrength = 0;
        this.name = name;
        this.foodPerTurn = 1;
        this.productionPerTurn = 1;
    }

    public HashMap<Building, Integer> getBuildings() {
        return buildings;
    }

    public Building getInProgressBuilding() {
        return inProgressBuilding;
    }

    public void setInProgressBuilding(Building inProgressBuilding) {
        this.inProgressBuilding = inProgressBuilding;
    }

    public void setInProgressUnit(UnitType unitType) {
        this.inProgressUnit = unitType;
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

    public void setCivilization(Civilization civilization) {
        this.civilization = civilization;
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

    public int getTurnsUntilGrowthBorder() {
        return turnsUntilGrowthBorder;
    }

    public int getCitizenNecessityFood() {
        return citizenNecessityFood;
    }

    public int getGainCitizenLastFood() {
        return gainCitizenLastFood;
    }

    public int getLostCitizenLastFood() {
        return lostCitizenLastFood;
    }

    public int getBorderExpansionCost() {
        return borderExpansionCost;
    }

    public int getBorderLastCost() {
        return borderLastCost;
    }

    public HashMap<UnitType, Integer> getLastCostsUntilNewProductions() {
        return lastCostsUntilNewProductions;
    }

    public UnitType getInProgressUnit() {
        return inProgressUnit;
    }

    public ArrayList<Citizen> getCitizens() {
        return citizens;
    }

    public double getHP() {
        return HP;
    }

    public void setHP(double HP) {
        this.HP = HP;
    }

    public double getCombatStrength() {
        double defaultCombatStrength = combatStrength;

        defaultCombatStrength += this.citizens.size();
        defaultCombatStrength += garrisonBonus();

        return defaultCombatStrength;
    }

    public void setCombatStrength(double combatStrength) {
        this.combatStrength = combatStrength;
    }

    public void setRangedCombatStrength(double rangedCombatStrength) {
        this.rangedCombatStrength = rangedCombatStrength;
    }

    private double garrisonBonus() {
        Tile centerTile = this.tiles.get(0);
        if (centerTile.getMilitary() != null && centerTile.getMilitary().getStatus().equals(UnitStatus.GARRISON))
            return centerTile.getMilitary().getCombatStrength();
        return 0;
    }

    public double getRangedCombatStrength() {
        return rangedCombatStrength;
    }

    public CityStatus getCityStatus() {
        return cityStatus;
    }

    public void setCityStatus(CityStatus status) {
        this.cityStatus = status;
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


    public void setCitizenNecessityFood(int citizenNecessityFood) {
        this.citizenNecessityFood = citizenNecessityFood;
    }

    public void setGainCitizenLastFood(int gainCitizenLastFood) {
        this.gainCitizenLastFood = gainCitizenLastFood;
    }

    public void setLostCitizenLastFood(int lostCitizenLastFood) {
        this.lostCitizenLastFood = lostCitizenLastFood;
    }

    public void setTurnsUntilGrowthBorder(int turnsUntilGrowthBorder) {
        this.turnsUntilGrowthBorder = turnsUntilGrowthBorder;
    }

    public void setBorderExpansionCost(int borderExpansionCost) {
        this.borderExpansionCost = borderExpansionCost;
    }

    public void setBorderLastCost(int borderLastCost) {
        this.borderLastCost = borderLastCost;
    }

    public void setTurnsUntilBirthCitizen(int turnsUntilBirthCitizen) {
        this.turnsUntilBirthCitizen = turnsUntilBirthCitizen;
    }

    public void setTurnsUntilDeathCitizen(int turnsUntilDeathCitizen) {
        this.turnsUntilDeathCitizen = turnsUntilDeathCitizen;
    }

    public void updateStoredFood() {
        int food = this.foodPerTurn - ((this.citizens.size() - 1) * 2);
        if (food < 0) storedFood = food;
        else if (inProgressUnit != null && inProgressUnit.equals(UnitType.SETTLER)) storedFood = 0;
        else storedFood = food;
    }

    public int getCitizensNumber() {
        return citizens.size();
    }
}
