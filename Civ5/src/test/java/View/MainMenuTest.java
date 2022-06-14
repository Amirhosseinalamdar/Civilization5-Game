package View;

import Controller.UserController;
import View.MainMenu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MainMenuTest {

    @Test
    void run() throws FileNotFoundException {
        UserController.readDataFromJson();
        File file1 = new File("E:/Ehsan Uni/AP/Project/CIV/Civ5/src/test/java/test1.txt");
        Scanner scanner = new Scanner(file1);
        MainMenu.run(scanner);
        UserController.writeDataToJson();
    }
}