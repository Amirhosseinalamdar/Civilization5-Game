package View;

import Controller.UserController;
import View.MainMenu;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MainMenuTest {

    @Test
    void run() throws FileNotFoundException {
        UserController.readDataFromJson("json.json");
        File file = new File("C:/Users/user/Desktop/Civ 5/project-group-10/Civ5/src/test/java/test.txt");
        Scanner scanner = new Scanner(file);
        MainMenu.run(scanner);
        UserController.writeDataToJson("json.json");
    }
}