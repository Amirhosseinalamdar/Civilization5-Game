package Model.Map;

import Model.Civilization;
import Model.Game;
import Model.UnitPackage.UnitType;
import Model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

class CityTest {


    City cnstrct(){
    Civilization civilization = new Civilization();
    User u1= new User("d","df","df",true,10);
    User u2= new User("ds","sdf","sdf",true,10);
        ArrayList<User> users = new ArrayList<User>();
        users.add(u1);
        users.add(u2);
        Game.generateGame(users);
        return new City(civilization,Game.getTiles()[9][9],"tehran");
    }

    @Test
    void setInProgressUnit() {
        City city=cnstrct();
        UnitType unit = city.getInProgressUnit();
        if(unit == UnitType.ARCHER) unit = UnitType.CHARIOT_ARCHER;
        city.setInProgressUnit(UnitType.ARCHER);
        Assertions.assertNotEquals(UnitType.ARCHER,unit);
    }

    @Test
    void getName() {
        City city = cnstrct();
        Assertions.assertEquals(city.getName(),city.getName());
    }

    @Test
    void getTiles() {
        City city = cnstrct();
        Assertions.assertEquals(city.getTiles(),city.getTiles());
    }

    @Test
    void Civilization() {
        City city = cnstrct();
        Civilization civilization = new Civilization();
        city.setCivilization(civilization);
        Assertions.assertEquals(civilization,city.getCivilization());
    }

    @Test
    void getStoredFood() {

    }

    @Test
    void getFoodPerTurn() {
    }

    @Test
    void getProductionPerTurn() {
    }

    @Test
    void getGoldPerTurn() {
    }

    @Test
    void getSciencePerTurn() {
    }

    @Test
    void getTurnsUntilBirthCitizen() {
    }

    @Test
    void getTurnsUntilDeathCitizen() {
    }

    @Test
    void getTurnsUntilGrowthBorder() {
    }

    @Test
    void getCitizenNecessityFood() {
    }

    @Test
    void getGainCitizenLastFood() {
    }

    @Test
    void getLostCitizenLastFood() {
    }

    @Test
    void getBorderExpansionCost() {
    }

    @Test
    void getBorderLastCost() {
    }

    @Test
    void getLastCostsUntilNewProductions() {
    }

    @Test
    void getInProgressUnit() {
    }

    @Test
    void getCitizens() {
    }

    @Test
    void getHP() {
    }

    @Test
    void setHP() {
    }

    @Test
    void getCombatStrength() {
    }

    @Test
    void setCombatStrength() {
    }

    @Test
    void setRangedCombatStrength() {
    }

    @Test
    void getRangedCombatStrength() {
    }

    @Test
    void getCityStatus() {
    }

    @Test
    void setStoredFood() {
    }

    @Test
    void setFoodPerTurn() {
    }

    @Test
    void setProductionPerTurn() {
    }

    @Test
    void setGoldPerTurn() {
    }

    @Test
    void setSciencePerTurn() {
    }

    @Test
    void setCitizenNecessityFood() {
    }

    @Test
    void setGainCitizenLastFood() {
    }

    @Test
    void setLostCitizenLastFood() {
    }

    @Test
    void setTurnsUntilGrowthBorder() {
    }

    @Test
    void setBorderExpansionCost() {
    }

    @Test
    void setBorderLastCost() {
    }

    @Test
    void setTurnsUntilBirthCitizen() {
    }

    @Test
    void setTurnsUntilDeathCitizen() {
    }

    @Test
    void updateStoredFood() {
    }

    @Test
    void calculateStrength() {
    }
}