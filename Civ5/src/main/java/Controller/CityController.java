package Controller;

import Model.Civilization;
import Model.Game;
import Model.Map.City;
import Model.Map.Tile;
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

        if (matcher.pattern().toString().startsWith("create")) {

        }

        if (matcher.pattern().toString().startsWith("purchase tile")) {
            Tile targetTile = Game.getTiles()[Integer.parseInt(matcher.group("x"))][Integer.parseInt(matcher.group("y"))];
            if (tileIsPurchasable(targetTile))
                purchaseTile(targetTile);
        }
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
            regex = "purchase tile (-c|--coordinates) (?<x>\\d+) (?<y>\\d+)";
            if (command.matches(regex)) {
                Matcher matcher = Pattern.compile(regex).matcher(command);
                if (!matcher.find()) throw new RuntimeException();
                if (!GameController.invalidPos(Integer.parseInt(matcher.group("x")),
                                                Integer.parseInt(matcher.group("y"))))
                    return matcher;
                GameMenu.indexOutOfArray();
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

    private static boolean tileIsPurchasable (Tile targetTile) {
        boolean isNeighbor = false;
        for (Tile tile : city.getTiles()) {
            if (tile.equals(targetTile)) {
                GameMenu.cityAlreadyHasTile();
                return false;
            }
            if (!UnitController.areNeighbors(tile, targetTile)) {
                isNeighbor = true;
                break;
            }
        }
        if (!isNeighbor) GameMenu.unreachableTileForCity();
        return isNeighbor;
    }

    private static void purchaseTile (Tile targetTile) {
        //view show options and check enough tiles //TODO
        //purchase that option
        int necessaryAmountOfGoldForPurchase = targetTile.getGoldPerTurn() * 3 + targetTile.getProductionPerTurn() +
                                                targetTile.getFoodPerTurn() * 2;
        if (civilization.getTotalGold() >= necessaryAmountOfGoldForPurchase) {
            civilization.setTotalGold(civilization.getTotalGold() - necessaryAmountOfGoldForPurchase);
            city.getTiles().add(targetTile);
            targetTile.setCity(city);
        }
        else
            GameMenu.notEnoughGoldForTilePurchase();
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
