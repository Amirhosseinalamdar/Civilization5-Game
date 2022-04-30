package Controller;

import Model.Civilization;
import Model.Game;
import Model.Map.City;
import Model.UnitPackage.Unit;
import Model.User;
import View.Commands;
import View.GameMenu;

import java.util.ConcurrentModificationException;
import java.util.regex.Matcher;

public class GameController {
    private static Civilization civilization;

    public static Civilization getCivilization() {
        return civilization;
    }

    public static void setCivilization() {
        checkMyCivilization();
    }

    public static void doTurn (String command) {

        Matcher matcher;
        if((matcher =Commands.getMatcher(command, Commands.SHOW_MAP_GLOBAL)) != null)
            GameMenu.showMap(civilization,0,0,true);
        else if (command.equals("show map"))
            GameMenu.showMap(civilization, civilization.getShowingCenterI(),civilization.getShowingCenterJ(),false);
        else if ((matcher = Commands.getMatcher(command, Commands.CHOOSE_UNIT1)) != null ||
                (matcher = Commands.getMatcher(command, Commands.CHOOSE_UNIT2)) != null) {
            Unit chosenUnit = getUnitFromCommand(matcher);
            if (chosenUnit == null) return;
            if (chosenUnit.getMovesInTurn() >= chosenUnit.getMP()) {
                System.out.println("no moves remaining"); //TODO... take it to view :)
                return;
            }
            UnitController.setUnit(chosenUnit);
            UnitController.handleUnitOption();
        } else if ((matcher = Commands.getMatcher(command, Commands.CHOOSE_CITY1)) != null ||
                (matcher = Commands.getMatcher(command, Commands.CHOOSE_CITY2)) != null) {
            City chosenCity = getCityFromCommand(matcher);
            if (chosenCity == null) return;
            System.out.println("name: " + chosenCity.getName());
            CityController.setCity(chosenCity);
            CityController.handleCityOption();
        }else if((matcher = Commands.getMatcher(command,Commands.SCROLL_MAP)) != null) {
            scrollOnMap(matcher);
        }
        else System.out.println("game controller, invalid command");
    }

    private static Unit getUnitFromCommand (Matcher matcher) {
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

    private static City getCityFromCommand(Matcher matcher) {
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
            System.out.println(matcher.group("name") + ", " + x + ", " + y);
            return Game.getTiles()[x][y].getCity();
        } catch (IllegalArgumentException i) {
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
        Game.nextTurn();
        checkMyCivilization();
        checkControllersCivilization();
        User player = Game.getPlayers().get(Game.getTurn());
        for (Unit unit : player.getCivilization().getUnits()) {
            unit.setMovesInTurn(0);
            UnitController.setUnit(unit);
            UnitController.doRemainingMissions();
        }
    }

    public static boolean gameIsOver() {
        return false;
    }

    private static void scrollOnMap(Matcher matcher) {
        int moveParameter = Integer.parseInt(matcher.group("number"));
        if (matcher.group("direction").equals("right")) {
            GameMenu.showMap(civilization, civilization.getShowingCenterI(), civilization.getShowingCenterJ() + moveParameter,false);
            if(civilization.getShowingCenterJ() + moveParameter > 16) civilization.setShowingCenterJ(16);
            else civilization.setShowingCenterJ(civilization.getShowingCenterJ() + moveParameter);
        } else if (matcher.group("direction").equals("left")) {
            GameMenu.showMap(civilization, civilization.getShowingCenterI(), civilization.getShowingCenterJ() - moveParameter,false);
            if(civilization.getShowingCenterJ() - moveParameter < 2) civilization.setShowingCenterJ(2);
            else civilization.setShowingCenterJ(civilization.getShowingCenterJ() - moveParameter);
        } else if (matcher.group("direction").equals("up")) {
            GameMenu.showMap(civilization, civilization.getShowingCenterI() - moveParameter, civilization.getShowingCenterJ(),false);
            if(civilization.getShowingCenterI() - moveParameter < 1) civilization.setShowingCenterI(1);
            else civilization.setShowingCenterI(civilization.getShowingCenterI() - moveParameter);
        } else if (matcher.group("direction").equals("down")) {
            GameMenu.showMap(civilization, civilization.getShowingCenterI() + moveParameter, civilization.getShowingCenterJ(),false);
            if(civilization.getShowingCenterI() + moveParameter > 18) civilization.setShowingCenterI(18);
            else civilization.setShowingCenterI(civilization.getShowingCenterI() + moveParameter);
        }
    }

}
