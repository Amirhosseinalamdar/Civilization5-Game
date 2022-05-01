package Controller;

import Model.Civilization;
import Model.Game;
import Model.Map.Citizen;
import Model.Map.City;
import Model.Map.Tile;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitType;
import View.Commands;
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
            UnitType type = getUnitTypeFromString(matcher.group("unitName"));
            if (type == null) {
                GameMenu.noSuchUnitType();
                return;
            }
            tryCreateUnit(type);
        }

        if (matcher.pattern().toString().startsWith("purchase tile")) {
            Tile targetTile = Game.getTiles()[Integer.parseInt(matcher.group("x"))][Integer.parseInt(matcher.group("y"))];
            if (tileIsPurchasable(targetTile))
                purchaseTile(targetTile);
        }

        if (matcher.pattern().toString().startsWith("lock citizen on tile")) {
            int x = Integer.parseInt(matcher.group("x")), y = Integer.parseInt(matcher.group("y"));
            for (Citizen citizen : city.getCitizens())
                if (citizen.getTile() == null) {
                    lockCitizenOnTile(citizen, Game.getTiles()[x][y]);
                    return;
                }
            GameMenu.noUnemployedCitizenAvailable();
            if (city.getCitizens().size() == 0) return;
            Citizen workingCitizen = getWorkingCitizen();
            if (workingCitizen == null) {
                GameMenu.citizenLockError();
                return;
            }
            lockCitizenOnTile(workingCitizen, Game.getTiles()[x][y]);
        }
        if (matcher.pattern().toString().startsWith("show")) GameMenu.showCityOutput(city);
    }

    public static Matcher getCityDecision() {
        Matcher matcher;

        while (true) {
            String command = GameMenu.nextCommand();
            if ((matcher = Commands.getMatcher(command, Commands.CREATE_UNIT)) != null) {
                if (unitIsValid(matcher.group("unitName")))
                    return matcher;
                GameMenu.invalidUnitType();
            }
            if ((matcher = Commands.getMatcher(command, Commands.PURCHASE_TILE)) != null) {
                if (!GameController.invalidPos(Integer.parseInt(matcher.group("x")),
                                                Integer.parseInt(matcher.group("y"))))
                    return matcher;
                GameMenu.indexOutOfArray();
            }
            if ((matcher = Commands.getMatcher(command, Commands.LOCK_CITIZEN)) != null) {
                if (!GameController.invalidPos(Integer.parseInt(matcher.group("x")),
                                                Integer.parseInt(matcher.group("y"))))
                    return matcher;
                GameMenu.indexOutOfArray();
            }
            if (Commands.getMatcher(command, Commands.SHOW_OUTPUT) != null) {
                return matcher;
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

    private static Citizen getWorkingCitizen() {
        String[] args = GameMenu.nextCommand().split(" ");
        try {
            int x = Integer.parseInt(args[0]), y = Integer.parseInt(args[1]);
            if (GameController.invalidPos(x, y)) {
                GameMenu.indexOutOfArray();
                return null;
            }
            Citizen citizen = Game.getTiles()[x][y].getWorkingCitizen();
            if (citizen == null) return null;
            if (citizen.getCity().equals(city)) return citizen;
            GameMenu.citizenNotYours();
            return null;
        }
        catch (Exception e) {
            GameMenu.invalidPosForCitizen();
            return null;
        }
    }

    public static void lockCitizenOnTile (Citizen citizen, Tile tile) {
        citizen.changeWorkingTile(tile);
        tile.setWorkingCitizen(citizen);
    }

    private void expandCity(City city) {

    }

    private static UnitType getUnitTypeFromString (String string) {
        try {
            return UnitType.valueOf(string);
        }
        catch (IllegalArgumentException i) {
            return null;
        }
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

    private static void tryCreateUnit (UnitType unitType){
        if (! hasReachedTechForUnit(unitType)) {
            GameMenu.unreachedTech();
            return;
        }
        if (! hasEnoughResources(unitType)) {
            GameMenu.notEnoughResource();
            return;
        }
        city.getTurnsUntilNewProductions().put(unitType, calcTurnsForNewUnit(unitType));
    }

    private static int calcTurnsForNewUnit (UnitType unitType) {
        return unitType.getCost() / city.getProductionPerTurn();
    }

    private static boolean hasReachedTechForUnit (UnitType unitType) {
        if (unitType.isPrimary()) return true; //TODO
        System.out.println("you are looking for advance tech :)");
        return false;
    }

    private static boolean hasEnoughResources (UnitType unitType) {

        return true; //TODO
    }
}
