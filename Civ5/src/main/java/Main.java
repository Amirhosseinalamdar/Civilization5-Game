import Controller.UserController;
import Model.Civilization;
import Model.Game;
import View.GameMenu;
import View.MainMenu;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        UserController.readDataFromJson("json.json");
        Scanner scanner = new Scanner(System.in);
        MainMenu.run(scanner);
        UserController.writeDataToJson("json.json");
    }
}
