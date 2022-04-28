package Controller;

import Model.Civilization;
import Model.Game;
import Model.Map.City;
import Model.UnitPackage.Unit;
import Model.User;
import View.Commands;
import View.GameMenu;

import java.util.regex.Matcher;

public class GameController {
    private static Civilization civilization;

    public static Civilization getCivilization() {
        return civilization;
    }

    public static void setCivilization() {
        checkMyCivilization();
    }

    public static void doTurn(String command) {
        Matcher matcher;
        if ((matcher = Commands.getMatcher(command, Commands.CHOOSE_UNIT1)) != null ||
                (matcher = Commands.getMatcher(command, Commands.CHOOSE_UNIT2)) != null) {
            Unit chosenUnit = getUnitFromCommand(matcher);
            if (chosenUnit == null) return;
            if (chosenUnit.getMovesInTurn() >= chosenUnit.getMP()) {
                System.out.println("no moves remaining"); //TODO... take it to view :)
                return;
            }
            UnitController.setUnit(chosenUnit);
            UnitController.handleUnitOption();
            GameMenu.showMap(civilization);
        }
        else if ((matcher = Commands.getMatcher(command, Commands.CHOOSE_CITY1)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.CHOOSE_CITY2)) != null) {
            City chosenCity = getCityFromCommand(matcher);
            if (chosenCity == null) return;
            CityController.setCity(chosenCity);
            CityController.handleCityOption();
        }
        else System.out.println("game controller, invalid command");
    }

    private static Unit getUnitFromCommand(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x")), y = Integer.parseInt(matcher.group("y"));
        System.out.println(matcher.group("unitType") + ", " + x + ", " + y);
        if (invalidPos(x, y)) {
            GameMenu.indexOutOfArray();
            return null;
        }
        if (matcher.group("unitType").equals("combat")) {
            if (Game.getTiles()[x][y].getMilitary() == null) {
                GameMenu.invalidChosenUnit();
                return null;
            }
            if (!Game.getTiles()[x][y].getMilitary().getCivilization().equals(civilization)) {
                GameMenu.notYourUnit();
                return null;
            }
            return Game.getTiles()[x][y].getMilitary();
        } else if (matcher.group("unitType").equals("noncombat")) {
            if (Game.getTiles()[x][y].getCivilian() == null) {
                GameMenu.invalidChosenUnit();
                return null;
            }
            if (!Game.getTiles()[x][y].getCivilian().getCivilization().equals(civilization)) {
                GameMenu.notYourUnit();
                return null;
            }
            return Game.getTiles()[x][y].getCivilian();
        }
        return null;
    }

    private static City getCityFromCommand (Matcher matcher) {
        try {
            int x = Integer.parseInt(matcher.group("x")), y = Integer.parseInt(matcher.group("y"));
            if (invalidPos(x, y)) {
                GameMenu.indexOutOfArray();
                return null;
            }
            if (Game.getTiles()[x][y].getCity() == null) {
                GameMenu.invalidPosForCity();
                return null;
            }
            return Game.getTiles()[x][y].getCity();
        }
        catch (IllegalArgumentException i) {
            for (City city : civilization.getCities())
                if (city.getName().equals(matcher.group("name"))) return city;
            GameMenu.invalidNameForCity();
            return null;
        }
    }

    public static boolean invalidPos(int x, int y) {
        return x > 19 || x < 0 || y > 19 || y < 0;
    }

    private static void checkMyCivilization() {
        civilization = Game.getPlayers().get(Game.getTurn()).getCivilization();
        checkControllersCivilization();
    }

    private static void checkControllersCivilization() {
        UnitController.changeCivilization(civilization);
        CivilizationController.changeCivilization(civilization);
        CityController.changeCivilization(civilization);
    }

    public static void updateGame() {
        for (User player : Game.getPlayers())
            for (Unit unit : player.getCivilization().getUnits()) {
                unit.setMovesInTurn(0);
                UnitController.setUnit(unit);
                UnitController.doRemainingMissions();
            }
        Game.nextTurn();
        checkMyCivilization();
        checkControllersCivilization();
    }

    public static boolean gameIsOver() {
        return false;
    }

}
