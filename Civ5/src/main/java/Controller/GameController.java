package Controller;

import Model.Civilization;
import Model.Game;
import Model.UnitPackage.Unit;
import View.GameMenu;

public class GameController {
    private static Civilization civilization;

    public static void doTurn (Game game, String command) {
        //update game
        changeMyCivilization(game);
        changeControllersCivilization();
        if (command.equals("unit")) {
            UnitController.handleUnitOption(getUnitFromCommand(GameMenu.nextCommand()));
        }
    }

    private static Unit getUnitFromCommand (String command) {
        return civilization.getUnits().get(Integer.parseInt(command));
    }

    private static void changeMyCivilization (Game game) {
        civilization = game.getPlayers().get(game.getTurn()).getCivilization();
    }

    private static void changeControllersCivilization(){
        UnitController.changeCivilization(civilization);
        CivilizationController.changeCivilization(civilization);
        CityController.changeCivilization(civilization);
    }

    public static void updateGame (Game game) {

    }

}
