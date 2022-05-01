package Controller;

import Model.Civilization;
import Model.Map.City;
import Model.Map.Improvement;
import Model.Technology;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
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
                    civilization.getTurnsUntilNewTechnologies().put(newTech, turnsForNewTech(newTech));
                    civilization.setInProgressTech(newTech);
                    GameMenu.techAdded();
                }
            }
            catch (Exception e) {
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

    private void addImprovement (Unit worker, Improvement improvement) {

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

    private static boolean canAskForTech (Technology newTech) {
        HashMap <Technology, Integer> civTechs = civilization.getTurnsUntilNewTechnologies();

        try {
            if (civTechs.get(newTech) < 0) System.out.println("you already have this tech :)");
            else System.out.println("tech is in progress; remaining turns: " + civTechs.get(newTech));
            return false;
        }
        catch (Exception e1) {
            ArrayList <Technology> parents = new ArrayList<>();

            if (newTech.getParent1() != null) parents.add(newTech.getParent1());
            if (newTech.getParent2() != null) parents.add(newTech.getParent2());
            if (newTech.getParent3() != null) parents.add(newTech.getParent3());

            for (Technology parent : parents) {
                try {
                    if (civTechs.get(parent) > 0) return false;
                }
                catch (Exception e2) {
                    return false;
                }
            }

            return true; //TODO... consider cost too
        }
    }

    private static int turnsForNewTech (Technology newTech) {
        return 1; //TODO... calc turns till new tech
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
