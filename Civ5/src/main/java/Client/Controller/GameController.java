package Client.Controller;

import Client.Model.Civilization;
import Client.Model.Game;
import Client.Model.Map.Building;
import Client.Model.Map.City;
import Client.Model.UnitPackage.Military;
import Client.Model.UnitPackage.Unit;
import Client.Model.UnitPackage.UnitStatus;
import Client.Model.UnitPackage.UnitType;
import Client.Model.User;
import Client.View.Commands;
import Client.View.GameMenu;
import com.google.gson.GsonBuilder;


import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;

public class GameController {
    private static Civilization civilization;

    public static Civilization getCivilization() {
        return civilization;
    }

    public static void setCivilization(Civilization civilization) {
        GameController.civilization = civilization;
    }

    public static String noTaskRemaining() {
        for (Unit unit : civilization.getUnits())
            if (!unit.getStatus().equals(UnitStatus.DO_NOTHING) && !unit.getStatus().equals(UnitStatus.HEAL)
                    && !unit.getStatus().equals(UnitStatus.FORTIFY) && !unit.getStatus().equals(UnitStatus.SLEEP) &&
                    !unit.getStatus().equals(UnitStatus.ALERT) && !unit.getStatus().equals(UnitStatus.BUILD_IMPROVEMENT) &&
                    !unit.getStatus().equals(UnitStatus.REPAIR) && !unit.getStatus().equals(UnitStatus.CLEAR_LAND) && unit.getMovesInTurn() < unit.getMP()) {
                return "a " + unit.getType().toString() + " unit on " + unit.getTile().getIndexInMapI() + ", " +
                        unit.getTile().getIndexInMapJ() + " has remaining moves";
            }

        for (City city : civilization.getCities())
            if (city.getInProgressUnit() == null && canCityHaveProduction(city)) {
                return city.getName() + " has no production currently; choose production for it";
            }

        if (civilization.getInProgressTech() == null && civilization.getCities().size() > 0)
            return "civilization has no tech in progress; choose a research";

        return "";
    }

    public static boolean canCityHaveProduction (City city) {
        CityController.setCity(city, "");
        for (UnitType unitType : UnitType.values())
            if (CityController.canCreateUnit(unitType))
                return true;
        for (Building building : Building.values())
            if (CityController.canConstructBuilding(building))
                return true;
        return false;
    }

    public static void doTurn(String command) {

        Matcher matcher;
        if (Commands.getMatcher(command, Commands.SHOW_MAP_GLOBAL) != null)
            GameMenu.showMap(civilization, 0, 0, true);
        else if (command.equals("show map"))
            GameMenu.showMap(civilization, civilization.getShowingCenterI(), civilization.getShowingCenterJ(), false);
        else if ((matcher = Commands.getMatcher(command, Commands.CHOOSE_UNIT1)) != null ||
                (matcher = Commands.getMatcher(command, Commands.CHOOSE_UNIT2)) != null) {
            Unit chosenUnit = getUnitFromCommand(matcher);
            if (chosenUnit == null) return;
            UnitController.setUnit(chosenUnit, "");
            UnitController.handleUnitOptions();
        } else if ((matcher = Commands.getMatcher(command, Commands.CHOOSE_CITY1)) != null ||
                (matcher = Commands.getMatcher(command, Commands.CHOOSE_CITY2)) != null) {
            City chosenCity = getCityFromCommand(matcher);
            if (chosenCity == null) return;
            CityController.handleCityOptions();
        } else if (Commands.getMatcher(command, Commands.MANAGE_CIVILIZATION) != null) {
            CivilizationController.handleCivilizationOptions();
        } else if ((matcher = Commands.getMatcher(command, Commands.SCROLL_MAP)) != null) {
            scrollOnMap(matcher);
        } else if (Commands.getMatcher(command, Commands.RESEARCH_INFO_SCREEN) != null) {
            GameMenu.researchInfoScreen(civilization);
        } else if (Commands.getMatcher(command, Commands.UNIT_LIST_PANEL) != null) {
            handleUnitListPanel();
        } else if (Commands.getMatcher(command, Commands.CITY_LIST_PANEL) != null) {
            handleCityListPanel();
        } else if (Commands.getMatcher(command, Commands.DIPLOMACY_INFO_PANEL) != null) {
            handleDiplomacyPanel();
        } else if (Commands.getMatcher(command, Commands.DEMOGRAPHICS_SCREEN) != null) {
            GameMenu.demographicsInfoScreen(civilization);
        } else if (Commands.getMatcher(command, Commands.NOTIFICATION_HISTORY) != null) {
            GameMenu.notificationHistory(civilization);
        } else if (Commands.getMatcher(command, Commands.MILITARY_OVERVIEW) != null) {
            GameMenu.militaryOverview(civilization);
        } else if (Commands.getMatcher(command, Commands.ECONOMIC_OVERVIEW) != null) {
            handleEconomicOverview();
        } else if (Commands.getMatcher(command, Commands.SHOW_BANNER) != null) {
            handleBanner();
        } else if (Commands.getMatcher(command, Commands.CIVILIZATION_OUTPUT) != null) {
            GameMenu.civilizationOutput(civilization);
        } else System.out.println("game controller, invalid command");
    }

    public static String cheat(String command) {
        Matcher matcher;
        if ((matcher = Commands.getMatcher(command, Commands.CHEAT1)) != null) {
            return cheatTurn(matcher);
        } else if ((matcher = Commands.getMatcher(command, Commands.CHEAT2)) != null) {
            return cheatGold(matcher);
        } else if ((matcher = Commands.getMatcher(command, Commands.CHEAT3)) != null) {
            return cheatHappiness(matcher);
        } else if ((matcher = Commands.getMatcher(command, Commands.CHEAT4)) != null) {
            return cheatScore(matcher);
        } else if ((matcher = Commands.getMatcher(command, Commands.CHEAT5)) != null) {
            return cheatCitiesHP(matcher);
        } else if ((matcher = Commands.getMatcher(command, Commands.CHEAT6)) != null) {
            return cheatUnitsHealth(matcher);
        } else if ((matcher = Commands.getMatcher(command, Commands.CHEAT7)) != null) {
            return cheatCitiesCombatStrength(matcher);
        } else if ((matcher = Commands.getMatcher(command, Commands.CHEAT8)) != null) {
            return cheatCitiesRangedCombatStrength(matcher);
        } else if ((matcher = Commands.getMatcher(command, Commands.CHEAT9)) != null) {
            return cheatUnitsMP(matcher);
        } else if ((matcher = Commands.getMatcher(command, Commands.CHEAT10)) != null) {
            return cheatUnitsCombatStrength(matcher);
        } else if ((matcher = Commands.getMatcher(command, Commands.CHEAT11)) != null) {
            return cheatUnitsRangedCombatStrength(matcher);
        } else return "invalid command";
    }

    private static String cheatTurn(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        for (int i = 0; i < x; i++) {
            Game.getInstance().nextTurn();
            checkMyCivilization();
            checkControllersCivilization();
            CivilizationController.updateCivilization();
        }
        for (Unit unit : Game.getInstance().getPlayers().get(Game.getInstance().getTurn()).getCivilization().getUnits()) {
            unit.setMovesInTurn(0);
            UnitController.setUnit(unit, "");
            UnitController.doRemainingMissions();
        }
        return "turn increased " + x + "times";
    }

    private static String cheatGold(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        civilization.setTotalGold(civilization.getTotalGold() + x);
        return "gold increased " + x;
    }

    private static String cheatHappiness(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        civilization.setHappiness(civilization.getHappiness() + x);
        return "happiness increased " + x;
    }

    private static String cheatScore(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        civilization.increaseScore(civilization.getScore() + x);
        return "score increased " + x;
    }

    private static String cheatCitiesHP(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        for (City city : civilization.getCities()) {
            city.setHP(city.getHP() + x);
        }
        return "cities hps increased " + x;
    }

    private static String cheatUnitsHealth(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        for (Unit unit : civilization.getUnits()) {
            unit.setHealth(unit.getHealth() + x);
        }
        return "units health increased " + x;
    }

    private static String cheatCitiesCombatStrength(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        for (City city : civilization.getCities()) {
            city.setCombatStrength(city.getCombatStrength() + x);
        }
        return "cities combat strength increased " + x;
    }

    private static String cheatCitiesRangedCombatStrength(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        for (City city : civilization.getCities()) {
            city.setRangedCombatStrength(city.getRangedCombatStrength() + x);
        }
        return "cities ranged combat strength increased " + x;
    }

    private static String cheatUnitsMP(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        for (Unit unit : civilization.getUnits()) {
            unit.setMP(unit.getMP() + x);
        }
        return "units MP increased " + x;
    }

    private static String cheatUnitsCombatStrength(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        for (Unit unit : civilization.getUnits()) {
            if (!unit.getType().isCivilian()) {
                Military military = (Military) unit;
                military.setCombatStrength(military.getCombatStrength() + x);
            }
        }
        return "units combat strength increased " + x;
    }

    private static String cheatUnitsRangedCombatStrength(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x"));
        for (Unit unit : civilization.getUnits()) {
            if (!unit.getType().isCivilian()) {
                Military military = (Military) unit;
                if (military.getRangedCombatStrength() > 0) {
                    military.setRangedCombatStrength(military.getRangedCombatStrength() + x);
                }
            }
        }
        return "units ranged combat strength increased " + x;
    }

    private static Unit getUnitFromCommand(Matcher matcher) {
        int x = Integer.parseInt(matcher.group("x")), y = Integer.parseInt(matcher.group("y"));
        if (invalidPos(x, y)) {
            GameMenu.indexOutOfArray();
            return null;
        }
        if (matcher.group("unitType").equals("combat")) {
            if (Game.getInstance().getTiles()[x][y].getMilitary() == null) {
                GameMenu.invalidChosenUnit();
                return null;
            }
            if (!Game.getInstance().getTiles()[x][y].getMilitary().getCivilization().equals(civilization)) {
                GameMenu.notYourUnit();
                return null;
            }
            return Game.getInstance().getTiles()[x][y].getMilitary();
        } else if (matcher.group("unitType").equals("noncombat")) {
            if (Game.getInstance().getTiles()[x][y].getCivilian() == null) {
                GameMenu.invalidChosenUnit();
                return null;
            }
            if (!Game.getInstance().getTiles()[x][y].getCivilian().getCivilization().equals(civilization)) {
                GameMenu.notYourUnit();
                return null;
            }
            return Game.getInstance().getTiles()[x][y].getCivilian();
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
            if (Game.getInstance().getTiles()[x][y].getCity() == null || !Game.getInstance().getTiles()[x][y].isCenterOfCity(Game.getInstance().getTiles()[x][y].getCity())) {
                GameMenu.invalidPosForCity();
                return null;
            }
            return Game.getInstance().getTiles()[x][y].getCity();
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

    public static void checkMyCivilization() {
        civilization = Game.getInstance().getPlayers().get(Game.getInstance().getTurn()).getCivilization();
        checkControllersCivilization();
    }

    private static void checkControllersCivilization() {
        UnitController.changeCivilization(civilization);
        CivilizationController.changeCivilization(civilization);
        CityController.changeCivilization(civilization);
    }

    public static String updateGame() {
        String message = noTaskRemaining();
        if (message.length() > 0) return message;
        Game.getInstance().nextTurn();
        checkMyCivilization();
        checkControllersCivilization();
        User player = Game.getInstance().getPlayers().get(Game.getInstance().getTurn());
        for (Unit unit : player.getCivilization().getUnits()) {
            unit.setMovesInTurn(0);
            UnitController.setUnit(unit, "");
            UnitController.doRemainingMissions();
        }
        CivilizationController.updateCivilization();

        if (Game.getInstance().getAutoSaveDuration() != 0)
            if (Game.getInstance().getTime() % Game.getInstance().getAutoSaveDuration() == 0)
                saveGameToJson(false);

        return "";
    }

    public static boolean gameIsOver() {
        return false;
    }

    private static void scrollOnMap(Matcher matcher) {
        int moveParameter = Integer.parseInt(matcher.group("number"));
        if (matcher.group("direction").equals("right")) {
            GameMenu.showMap(civilization, civilization.getShowingCenterI(), civilization.getShowingCenterJ() + moveParameter, false);
            if (civilization.getShowingCenterJ() + moveParameter > 16) civilization.setShowingCenterJ(16);
            else civilization.setShowingCenterJ(civilization.getShowingCenterJ() + moveParameter);
        } else if (matcher.group("direction").equals("left")) {
            GameMenu.showMap(civilization, civilization.getShowingCenterI(), civilization.getShowingCenterJ() - moveParameter, false);
            if (civilization.getShowingCenterJ() - moveParameter < 2) civilization.setShowingCenterJ(2);
            else civilization.setShowingCenterJ(civilization.getShowingCenterJ() - moveParameter);
        } else if (matcher.group("direction").equals("up")) {
            GameMenu.showMap(civilization, civilization.getShowingCenterI() - moveParameter, civilization.getShowingCenterJ(), false);
            if (civilization.getShowingCenterI() - moveParameter < 1) civilization.setShowingCenterI(1);
            else civilization.setShowingCenterI(civilization.getShowingCenterI() - moveParameter);
        } else if (matcher.group("direction").equals("down")) {
            GameMenu.showMap(civilization, civilization.getShowingCenterI() + moveParameter, civilization.getShowingCenterJ(), false);
            if (civilization.getShowingCenterI() + moveParameter > 18) civilization.setShowingCenterI(18);
            else civilization.setShowingCenterI(civilization.getShowingCenterI() + moveParameter);
        }
    }

    public static void handleBanner() {
        if (civilization.getCities().isEmpty()) {
        } else {
            int i = 0;
            CityController.updateCityInfos(civilization.getCities().get(i));
            GameMenu.showBanner(civilization.getCities().get(i));
            while (true) {
                String command = GameMenu.nextCommand();
                if (command.equals("next")) {
                    i++;
                    i = i % civilization.getCities().size();
                    CityController.updateCityInfos(civilization.getCities().get(i));
                    GameMenu.showBanner(civilization.getCities().get(i));
                } else if (command.equals("past")) {
                    i--;
                    i = i % civilization.getCities().size();
                    CityController.updateCityInfos(civilization.getCities().get(i));
                    GameMenu.showBanner(civilization.getCities().get(i));
                } else if (command.equals("exit banner")) break;
                else System.out.println("invalid command");
            }
        }
    }

    private static void handleUnitListPanel() {
        GameMenu.militaryOverview(civilization);
        Matcher matcher;
        while (true) {
            String command = GameMenu.nextCommand();
            if ((matcher = Commands.getMatcher(command, Commands.CHOOSE_UNIT1)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.CHOOSE_UNIT2)) != null) {
                Unit chosenUnit = getUnitFromCommand(matcher);
                if (chosenUnit != null) {
                    System.out.println("calling from there");
                    UnitController.setUnit(chosenUnit, "");
                    UnitController.handleUnitOptions();
                }
                break;
            } else if (Commands.getMatcher(command, Commands.MILITARY_OVERVIEW) != null) {
                GameMenu.militaryOverview(civilization);
                break;
            } else if (command.equals("close")) break;
            else System.out.println("invalid command");
        }
    }

    private static void handleCityListPanel() {
        GameMenu.cityList(civilization);
        Matcher matcher;
        while (true) {
            String command = GameMenu.nextCommand();
            if ((matcher = Commands.getMatcher(command, Commands.CHOOSE_CITY1)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.CHOOSE_CITY2)) != null) {
                City chosenCity = getCityFromCommand(matcher);
                if (chosenCity != null) {
                    CityController.handleCityOptions();
                }
                break;
            } else if (Commands.getMatcher(command, Commands.ECONOMIC_OVERVIEW) != null) {
                handleEconomicOverview();
                break;
            } else if (command.equals("close")) break;
            else System.out.println("invalid command");
        }
    }

    private static void handleDiplomacyPanel() {
        GameMenu.showDiplomacyInfo(civilization);
    }

    private static void handleEconomicOverview() {
        GameMenu.economicOverview(civilization);
        Matcher matcher;
        while (true) {
            String command = GameMenu.nextCommand();
            if ((matcher = Commands.getMatcher(command, Commands.CHOOSE_CITY1)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.CHOOSE_CITY2)) != null) {
                City chosenCity = getCityFromCommand(matcher);
                if (chosenCity != null) {
                    CityController.handleCityOptions();
                }
                break;
            } else if (command.equals("close")) break;
            else System.out.println("invalid command");
        }
    }

    public static int findBestCity() {
        int size = 0;
        for (User player : Game.getInstance().getPlayers()) {
            if (player.getCivilization().getCities().size() > size) size = player.getCivilization().getCities().size();
        }
        return size;
    }

    public static int findAverageCity() {
        int size = 0;
        for (User player : Game.getInstance().getPlayers()) {
            size += player.getCivilization().getCities().size();
        }
        size = size / Game.getInstance().getPlayers().size();
        return size;
    }

    public static int findWorstCity() {
        int size = 10000;
        for (User player : Game.getInstance().getPlayers()) {
            if (player.getCivilization().getCities().size() < size) size = player.getCivilization().getCities().size();
        }
        return size;
    }

    public static int findRankInCities() {
        int rank = 1;
        for (User player : Game.getInstance().getPlayers()) {
            if (player.getCivilization().getCities().size() > civilization.getCities().size()) rank++;
        }
        return rank;
    }

    public static int findBestGold() {
        int gold = 0;
        for (User player : Game.getInstance().getPlayers()) {
            if (player.getCivilization().getTotalGold() > gold) gold = player.getCivilization().getTotalGold();
        }
        return gold;
    }

    public static int findAverageGold() {
        int gold = 0;
        for (User player : Game.getInstance().getPlayers()) {
            gold += player.getCivilization().getTotalGold();
        }
        gold = gold / Game.getInstance().getPlayers().size();
        return gold;
    }

    public static int findWorstGold() {
        int gold = 1000000;
        for (User player : Game.getInstance().getPlayers()) {
            if (player.getCivilization().getTotalGold() < gold) gold = player.getCivilization().getTotalGold();
        }
        return gold;
    }

    public static int findRankInGolds() {
        int rank = 1;
        for (User player : Game.getInstance().getPlayers()) {
            if (player.getCivilization().getTotalGold() > civilization.getTotalGold()) rank++;
        }
        return rank;
    }

    public static int findBestUnit() {
        int size = 0;
        for (User player : Game.getInstance().getPlayers()) {
            if (player.getCivilization().getUnits().size() > size) size = player.getCivilization().getUnits().size();
        }
        return size;
    }

    public static int findAverageUnit() {
        int size = 0;
        for (User player : Game.getInstance().getPlayers()) {
            size += player.getCivilization().getUnits().size();
        }
        size = size / Game.getInstance().getPlayers().size();
        return size;
    }

    public static int findWorstUnit() {
        int size = 10000;
        for (User player : Game.getInstance().getPlayers()) {
            if (player.getCivilization().getUnits().size() < size) size = player.getCivilization().getUnits().size();
        }
        return size;
    }

    public static int findRankInUnits() {
        int rank = 1;
        for (User player : Game.getInstance().getPlayers()) {
            if (player.getCivilization().getUnits().size() > civilization.getUnits().size()) rank++;
        }
        return rank;
    }

    public static int findBestScience() {
        int science = 0;
        for (User player : Game.getInstance().getPlayers()) {
            if (player.getCivilization().getScience() > science) science = player.getCivilization().getScience();
        }
        return science;
    }

    public static int findAverageScience() {
        int science = 0;
        for (User player : Game.getInstance().getPlayers()) {
            science += player.getCivilization().getScience();
        }
        science = science / Game.getInstance().getPlayers().size();
        return science;
    }

    public static int findWorstScience() {
        int science = 1000000;
        for (User player : Game.getInstance().getPlayers()) {
            if (player.getCivilization().getScience() < science) science = player.getCivilization().getScience();
        }
        return science;
    }

    public static int findRankInScience() {
        int rank = 1;
        for (User player : Game.getInstance().getPlayers()) {
            if (player.getCivilization().getScience() > civilization.getScience()) rank++;
        }
        return rank;
    }


    public static int findBestHappiness() {
        int happiness = 0;
        for (User player : Game.getInstance().getPlayers()) {
            if (player.getCivilization().getHappiness() > happiness)
                happiness = player.getCivilization().getHappiness();
        }
        return happiness;
    }

    public static int findAverageHappiness() {
        int happiness = 0;
        for (User player : Game.getInstance().getPlayers()) {
            happiness += player.getCivilization().getHappiness();
        }
        happiness = happiness / Game.getInstance().getPlayers().size();
        return happiness;
    }

    public static int findWorstHappiness() {
        int happiness = 1000000;
        for (User player : Game.getInstance().getPlayers()) {
            if (player.getCivilization().getHappiness() < happiness)
                happiness = player.getCivilization().getHappiness();
        }
        return happiness;
    }

    public static int findRankInHappiness() {
        int rank = 1;
        for (User player : Game.getInstance().getPlayers()) {
            if (player.getCivilization().getHappiness() > civilization.getHappiness()) rank++;
        }
        return rank;
    }

    public static void saveGameToJson (boolean forceSave) {
        if (Game.getInstance().getTurn() < Game.getInstance().getPlayers().size() - 1 && !forceSave) return;
        String json = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create().toJson(Game.getInstance());
        try {
            FileWriter fileWriter = new FileWriter("Game" + Game.getInstance().getSaveFileNum() + ".json");
            fileWriter.write(json);
            fileWriter.close();
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }
}
