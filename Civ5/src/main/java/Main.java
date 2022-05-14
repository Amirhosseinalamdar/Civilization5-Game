import Controller.UserController;
import Model.Civilization;
import Model.Game;
import View.GameMenu;
import View.MainMenu;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        UserController.readDataFromJson("json.json");
        MainMenu.run();
        UserController.writeDataToJson("json.json");
    }
}
