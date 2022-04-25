package Controller;

import Model.Civilization;
import Model.Game;
import Model.Map.City;
import Model.UnitPackage.Unit;
import View.GameMenu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameController {
    private static Civilization civilization;

    public static Civilization getCivilization() {
        return civilization;
    }

    public static void doTurn (String command) {
        checkMyCivilization();
        for (Unit unit : civilization.getUnits()) {
            UnitController.setUnit(unit);
            UnitController.doRemainingMissions();
        }
        String regex = "^unit (?<unitType>(combat|noncombat)) (?<x>\\d+) (?<y>\\d+)$";
        if (command.matches(regex)) {
            Matcher matcher = Pattern.compile(regex).matcher(command);
            if (matcher.find()) {
                Unit chosenUnit = getUnitFromCommand(matcher);
                if (chosenUnit == null) return;
                UnitController.setUnit(chosenUnit);
                UnitController.handleUnitOption();
                GameMenu.showMap(civilization);
            }
        }
        else System.out.println("game controller, invalid command");
    }

    private static Unit getUnitFromCommand (Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x")), y = Integer.parseInt(matcher.group("y"));
        System.out.println(matcher.group("unitType") + ", " + x + ", " + y);
        if (invalidPos(x, y)) {
            GameMenu.invalidChosenUnit();
            return null;
        }
        if (matcher.group("unitType").equals("combat")) {
            if (Game.getTiles()[x][y].getMilitary() == null) {
                GameMenu.invalidChosenUnit();
                return null;
            }
            if (! Game.getTiles()[x][y].getMilitary().getCivilization().equals(civilization)) {
                GameMenu.notYourUnit();
                return null;
            }
            return Game.getTiles()[x][y].getMilitary();
        }
        else if (matcher.group("unitType").equals("noncombat")) {
            if (Game.getTiles()[x][y].getCivilian() == null) {
                GameMenu.invalidChosenUnit();
                return null;
            }
            if (! Game.getTiles()[x][y].getCivilian().getCivilization().equals(civilization)) {
                GameMenu.notYourUnit();
                return null;
            }
            return Game.getTiles()[x][y].getCivilian();
        }
        return null;
    }

//    private static City getCityFromCommand (Matcher matcher) {
//        int x = Integer.parseInt(matcher.group("x")), y = Integer.parseInt(matcher.group("y"));
//        if (invalidPos(x, y)) {
//            GameMenu.invalidChosenCity();
//            return null;
//        }
//        if (Game.getTiles()[x][y].getCity() == null) {
//            GameMenu.invalidChosenCity();
//            return null;
//        }
//        return Game.getTiles()[x][y].getCity();
//    }

    public static boolean invalidPos (int x, int y) {
        return x > 19 || x < 0 || y > 19 || y < 0;
    }

    private static void checkMyCivilization () {
        civilization = Game.getPlayers().get(Game.getTurn()).getCivilization();
        checkControllersCivilization();
    }

    private static void checkControllersCivilization(){
        UnitController.changeCivilization(civilization);
        CivilizationController.changeCivilization(civilization);
        CityController.changeCivilization(civilization);
    }

    public static void updateGame () {
        Game.nextTurn();
        checkMyCivilization();
        checkControllersCivilization();
    }

}
