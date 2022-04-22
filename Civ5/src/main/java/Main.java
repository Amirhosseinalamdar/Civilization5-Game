import Model.Game;
import Model.UnitPackage.*;
import View.GameMenu;

public class Main {
    public static void main(String[] args) {
        Unit unit = new Unit();
        Military military = new Military();
        Game game = new Game();
        game.generateMap();
        System.out.println("hile erfun");
        GameMenu.showMap(game);
    }
}
