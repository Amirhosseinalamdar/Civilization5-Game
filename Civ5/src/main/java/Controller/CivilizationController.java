package Controller;

import Model.Civilization;
import Model.Map.City;
import Model.Map.Improvement;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;

public class CivilizationController {
    private static Civilization civilization;

    public static void changeCivilization(Civilization civilization) {
        CivilizationController.civilization = civilization;
    }

    private void addImprovement(Unit worker, Improvement improvement) {

    }

//    private void makeImprovement(Unit worker){
//
//    }

    private void updateTilesVisionStatus() {

    }

    private void updateCivilizationsInfos() {
        /**
         ...
         ...
         +update happiness
         +update technology
         */
    }

    private void askForNewResearch(Civilization civilization) {

    }

    private void conquerCity(City city, Military military) {
        //CityStatus ra taghir midahim va asarat ra eemal mikonim
        //call puppetCity or ownCity or destroyCity
    }

    private void puppetCity(City city, Civilization civilization) {

    }

    private void ownCity(City city, Civilization civilization) {

    }

    private void destroyCity(City city) {

    }
}
