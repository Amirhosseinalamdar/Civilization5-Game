package Controller;

import Model.Civilization;
import Model.Game;
import Model.User;
import View.Commands;
import View.GameMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static Controller.UnitController.handleUnitOptions;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Scanner;

import static javafx.beans.binding.Bindings.when;
import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    @Mock
    Civilization civilization;

    void erfan(){
        GameMenu.setScanner(new Scanner(System.in));
        User u1= new User("d","df","df",true,10);
        User u2= new User("ds","sdf","sdf",true,10);
        ArrayList<User> users = new ArrayList<User>();
        users.add(u1);
        users.add(u2);
        Game.generateGame(users);
        GameController.setCivilization(users.get(0).getCivilization());
    }
    @Test
    void getCivilization() {
    }

    @Test
    void noTaskRemaining() {
    }

    @Test
    void doTurn() {
        GameController.doTurn("sdf");
    }
    @Test
    void doTurn1() {
        erfan();
        GameController.doTurn("show map");
    }
    @Test
    void doTurn2() {
        erfan();
        GameController.doTurn("unit -c 9 9 -t noncombat");
    }
    /*@Test
    void doTurn3() {
        erfan();
        int i=GameController.getCivilization().getUnits().get(0).getTile().getIndexInMapI();
        int j=GameController.getCivilization().getUnits().get(0).getTile().getIndexInMapJ();
        String input = "unit -c "+i+" "+j+" -t noncombat";
        GameController.doTurn(input);
    }*/
    @Test
    void doTurn4() {
        GameController.doTurn(Commands.CHOOSE_UNIT2.toString());
    }


    @Test
    void invalidPos() {
    }

    @Test
    void updateGame() {
    }

    @Test
    void gameIsOver() {
    }

    @Test
    void handleBanner() {
    }

    @Test
    void findBestCity() {
    }

    @Test
    void findAverageCity() {
    }

    @Test
    void findWorstCity() {
    }

    @Test
    void findRankInCities() {
    }

    @Test
    void findBestGold() {
    }

    @Test
    void findAverageGold() {
    }

    @Test
    void findWorstGold() {
    }

    @Test
    void findRankInGolds() {
    }

    @Test
    void findBestUnit() {
    }

    @Test
    void findAverageUnit() {
    }

    @Test
    void findWorstUnit() {
    }

    @Test
    void findRankInUnits() {
    }

    @Test
    void findBestScience() {
    }

    @Test
    void findAverageScience() {
    }

    @Test
    void findWorstScience() {
    }

    @Test
    void findRankInScience() {
    }

    @Test
    void findBestHappiness() {
    }

    @Test
    void findAverageHappiness() {
    }

    @Test
    void findWorstHappiness() {
    }

    @Test
    void findRankInHappiness() {
    }
}