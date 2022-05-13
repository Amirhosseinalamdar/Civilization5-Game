package Controller;

import Model.Civilization;
import Model.Map.*;
import Model.Technology;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitType;
import View.Commands;
import View.GameMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
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

//    private void makeImprovement(Unit worker){
//
//    }

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
        civilization.increaseScore(score);
        updateInProgressTech();
        updateHappiness();
    }

    private static void updateHappiness() {
        int unhappiness = 2 * civilization.getCities().size();
        for (City city : civilization.getCities()) {
            if (city.getCityStatus().equals(CityStatus.POPPET)) unhappiness++;
            unhappiness += city.getCitizens().size();
        }
        for (HashMap.Entry<Resource, Integer> set : civilization.getLuxuryResources().entrySet()) {
            if (set.getValue() > 0) unhappiness -= 4;
        }
        civilization.setHappiness(civilization.getHappiness() - unhappiness);
    }

    private static void handleRoadsMaintenance() {
        int cost = 0;
        for (City city : civilization.getCities()) {
            for (Tile tile : city.getTiles()) {
                if (tile.getRouteInProgress().getValue() == 0) cost++;
            }
        }
        cost /= 2;
        civilization.setTotalGold(civilization.getTotalGold() - cost);
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
                    civilization.getUnits().remove(unit);
                    cost--;
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
            if (i <= 0) civilization.setInProgressTech(null);
        }
    }

    private static boolean canAskForTech(Technology newTech) {
        HashMap<Technology, Integer> civTechs = civilization.getLastCostUntilNewTechnologies();

        try {
            if (civTechs.get(newTech) <= 0) System.out.println("you already have this tech :)");
            else {
                System.out.println("tech is in progress;" + turnsForNewTech());
                civilization.setInProgressTech(newTech);
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
