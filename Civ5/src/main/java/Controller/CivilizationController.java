package Controller;

import Model.Map.Building;
import Model.Civilization;
import Model.Game;
import Model.Map.*;
import Model.Technology;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitType;
import View.Commands;
import View.GameMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class CivilizationController {

    private static Civilization civilization;

    public static void changeCivilization(Civilization civilization) {
        CivilizationController.civilization = civilization;
    }

    public static void handleCivilizationOptions() {
        Matcher matcher = getCivilizationDecision();

        if (matcher.pattern().toString().startsWith("new tech")) {
            try {
                Technology newTech = Technology.valueOf(matcher.group("techName"));
                if (canAskForTech(newTech)) {
                    if (civilization.getInProgressTech() != null)
                        GameMenu.canceledTech(civilization.getInProgressTech());
                    civilization.getLastCostUntilNewTechnologies().put(newTech, newTech.getCost());
                    civilization.setInProgressTech(newTech);
                    GameMenu.techAdded();
                }
            } catch (Exception e) {
                GameMenu.invalidTechName();
            }
        }
    }

    private static Matcher getCivilizationDecision() {
        Matcher matcher;

        while (true) {
            String command = GameMenu.nextCommand();

            if ((matcher = Commands.getMatcher(command, Commands.ASK_FOR_TECH)) != null)
                return matcher;
        }
    }

    private void addImprovement(Unit worker, Improvement improvement) {

    }

    private void updateTilesVisionStatus() {

    }

    public static void updateCivilization() {
        int science = 0;
        int gold = 0;
        int score = 0;
        for (City city : civilization.getCities()) {
            CityController.updateCity(city);
            science = city.getSciencePerTurn();
            gold += city.getGoldPerTurn();
            score += city.getTiles().size();
            score += city.getCitizens().size();
        }
        score += civilization.getCities().size();
        score += civilization.getLastCostUntilNewTechnologies().size();
        civilization.setTotalScience(science);
        civilization.setTotalGold(civilization.getTotalGold() + gold);
        handleUnitsMaintenance();
        handleRoadsMaintenance();
        handleBuildingsMaintenance();
        civilization.increaseScore(score);
        updateInProgressTech();
        updateHappiness();
    }

    private static void handleBuildingsMaintenance() {
        int cost = 0;
        for (City city : civilization.getCities()) {
            for (Map.Entry<Building, Integer> set : city.getBuildings().entrySet()) {
                if (set.getValue() <= 0) {
                    cost += set.getKey().getMaintenance();
                }
            }
        }
        civilization.setTotalGold(civilization.getTotalGold() - cost);
        if (civilization.getTotalGold() < 0) civilization.setTotalGold(0);
    }

    private static void updateHappiness() {
        int unhappiness = 2 * civilization.getCities().size();
        for (City city : civilization.getCities()) {
            if (city.getCityStatus().equals(CityStatus.PUPPET)) unhappiness++;
            unhappiness += city.getCitizens().size();
            for (Map.Entry<Building, Integer> set : city.getBuildings().entrySet()) {
                if (set.getValue() <= 0) {
                    unhappiness -= set.getKey().getHappinessAdder();
                }
            }
        }
        for (HashMap.Entry<Resource, Integer> set : civilization.getLuxuryResources().entrySet()) {
            if (set.getValue() > 0) unhappiness -= 4;
        }
        civilization.setHappiness(civilization.getHappiness() - unhappiness);
        if (civilization.getHappiness() < 1) civilization.setHappiness(1);
    }

    private static void handleRoadsMaintenance() {
        int cost = 0;
        for (City city : civilization.getCities())
            for (Tile tile : city.getTiles())
                if (tile.getRouteInProgress() != null && tile.getRouteInProgress().getValue() == 0) cost++;
        cost /= 2;
        civilization.setTotalGold(civilization.getTotalGold() - cost);
        if (civilization.getTotalGold() < 0) civilization.setTotalGold(0);
    }

    private static void handleUnitsMaintenance() {
        int cost = 0;
        for (Unit unit : civilization.getUnits()) {
            if (!unit.getType().equals(UnitType.WORKER) && !unit.getType().equals(UnitType.SETTLER) &&
                    !unit.getType().equals(UnitType.WARRIOR))
                cost++;
        }
        while (cost > civilization.getTotalGold()) {
            for (Unit unit : civilization.getUnits()) {
                if (!unit.getType().equals(UnitType.WORKER) && !unit.getType().equals(UnitType.SETTLER) &&
                        !unit.getType().equals(UnitType.WARRIOR)) {
                    civilization.setTotalScience(civilization.getScience() - 1);
                    civilization.setHappiness(civilization.getHappiness() - 1);
                    cost--;
                    if (civilization.getUnits().size() == 0) {
                        cost = 0;
                        break;
                    }
                }
            }
        }
        civilization.setTotalGold(civilization.getTotalGold() - cost);
    }

    private static void updateInProgressTech() {
        if (civilization.getInProgressTech() != null) {
            int i = civilization.getLastCostUntilNewTechnologies().get(civilization.getInProgressTech());
            i -= civilization.getScience();
            civilization.getLastCostUntilNewTechnologies().replace(civilization.getInProgressTech(), i);
            if (i <= 0) {
                civilization.getNotifications().add(civilization.getInProgressTech().name() + " is now unlocked.     time: " + Game.getInstance().getTime());
                civilization.setInProgressTech(null);
            }
        }
    }

    private static boolean canAskForTech(Technology newTech) {
        HashMap<Technology, Integer> civTechs = civilization.getLastCostUntilNewTechnologies();

        try {
            if (civTechs.get(newTech) <= 0) System.out.println("you already have this tech :)");
            else {
                civilization.setInProgressTech(newTech);
                System.out.println("tech is in progress;" + turnsForNewTech());
            }
            return false;
        } catch (Exception e1) {
            ArrayList<Technology> parents = newTech.getParents();

            for (Technology parent : parents) {
                try {
                    if (civTechs.get(parent) > 0) {
                        GameMenu.unreachedTech(parent);
                        return false;
                    }
                } catch (Exception e2) {
                    GameMenu.unreachedTech(parent);
                    return false;
                }
            }
            return true;
        }

    }

    public static String turnsForNewTech() {
        String output;
        int turn;
        if (civilization.getInProgressTech() == null) output = "there is no technology in progress";
        else {
            if (civilization.getScience() != 0) {
                turn = civilization.getLastCostUntilNewTechnologies().get(civilization.getInProgressTech()) / civilization.getScience();
                output = "turns until reaching" + civilization.getInProgressTech().name() + " : " + turn + ".";
            } else output = "turns until reaching" + civilization.getInProgressTech().name() + " : " + "-";
        }
        return output;
    }

    public static void enterCityAsConqueror(City city) {
        while (true) {
            String decision = GameMenu.nextCommand();
            if (decision.equals("do nothing")) return;
            else if (decision.equals("attach")) {
                attachCity(city);
                return;
            }
            else if (decision.equals("puppet")) {
                puppetCity(city);
                return;
            }
            else if (decision.equals("raze")) {
                razeCity(city);
                return;
            }
            else {
                GameMenu.invalidDecisionForConqueredCity();
            }
        }
    }

    private static void puppetCity(City city) {
        city.setCityStatus(CityStatus.PUPPET);
        civilization.addCity(city);
    }

    private static void attachCity(City city) {
        city.setCivilization(civilization);
        civilization.getCities().add(city);
        GameMenu.attachCitySuccessful(city);
    }

    private static void razeCity(City city) {
        civilization.setTotalGold(civilization.getTotalGold() + city.getGoldPerTurn());
        int firstPopulation = city.getCitizens().size();
        if (firstPopulation / 2 > 0)
            city.getCitizens().subList(0, firstPopulation / 2).clear();
    }
}
