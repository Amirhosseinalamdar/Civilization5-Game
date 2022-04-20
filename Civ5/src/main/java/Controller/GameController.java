package Controller;

import Model.Civilization;
import Model.Game;

public class GameController {
    private static Civilization civilization;

    public void doTurn (Game game, String command) {//ino beberaim too game menu controller
        //this.civilization = game.player.civilization; ...change current civilization
        //update game
        changeControllersCivilization();
    }

    private static void changeControllersCivilization(){
        UnitController.changeCivilization(civilization);
        CivilizationController.changeCivilization(civilization);
        CityController.changeCivilization(civilization);
    }

}
