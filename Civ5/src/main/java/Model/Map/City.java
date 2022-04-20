package Model.Map;

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
    private HashMap <UnitType, Integer> turnsUntilNewProductions;
    private UnitType inProgressUnit;
    private ArrayList<Citizen> citizens;
    private int goldPerTurn;
    private int turnsUntilGrowth;
    private int HP;
    private int combatStrength;
    private int rangedCombatStrength;
    private CityStatus cityStatus;


    public void updateStoredFood(){

    }

    public void birthCitizen(){

    }

    public void calculateStrength() {
        //TODO update strength based on the unit inside the city
    }

}
