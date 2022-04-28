package Controller;

import Model.Civilization;
import Model.Map.City;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitType;
import View.GameMenu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CityController {
    private static Civilization civilization;
    private static City city;

    public static void changeCivilization(Civilization civilization){
        CityController.civilization = civilization;
    }


    public static void setCity (City city) {
        CityController.city = city;
    }

    public static void handleCityOption() {
        Matcher matcher = getCityDecision();

    }

    public static Matcher getCityDecision() {
        String regex;

        while (true) {
            String command = GameMenu.nextCommand();
            regex = "create (-u|--unit) (?<unitName>\\S+)";
            if (command.matches(regex)) {
                Matcher matcher = Pattern.compile(regex).matcher(command);
                if (!matcher.find()) throw new RuntimeException();
                if (unitIsValid(matcher.group("unitName")))
                    return matcher;
                GameMenu.invalidUnitType();
            }
            System.out.println("city decision wasn't valid");
        }
    }

    private static boolean unitIsValid (String unitName) {
        try {
            System.out.println("you have chosen: " + UnitType.valueOf(unitName) + " to create");
            return true;
        }
        catch (IllegalArgumentException i) {
            return false;
        }
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
