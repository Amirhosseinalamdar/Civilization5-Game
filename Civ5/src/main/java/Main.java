import Controller.UserController;
import Model.Civilization;
import Model.Game;
import View.GameMenu;
import View.MainMenu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserController.readDataFromJson();
        MainMenu.run();
        UserController.writeDataToJson();
    }
}
