import Model.Game;
import Model.UnitPackage.*;
import View.GameMenu;

public class Main {
    public static void main(String[] args) {
        Game.generateMap();
        GameMenu.showMap();
    }
}