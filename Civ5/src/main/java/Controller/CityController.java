package Controller;

import Model.Civilization;
import Model.Map.City;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitType;
public class CityController {
    private static Civilization civilization;

    public static void changeCivilization(Civilization civilization){
        CityController.civilization = civilization;
    }

    private void expandCity(City city) {

    }

    private void purchaseTile(City city){
        //view show options and check enough tiles //TODO
        //purchase that option
    }

    private void askForNewProduction(City city){

    }

    private void updateCitiesInfos() {
        /**
         * food
         * stored food
         * consumed food by settlers
         * birth citizen if enough food
         * + production and other infos
         */
    }

    private void cityAttackToUnit(City city, Unit unit) {

    }

    private void createNewUnit(City city, UnitType unitType){
        //hazine ha ra kam konim... turn oke she...
    }

    private void lockCitizen(){

    }
}
