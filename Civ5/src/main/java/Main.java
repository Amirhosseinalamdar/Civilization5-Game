import Controller.UserController;
import View.MainMenu;

public class Main {
    public static void main(String[] args) {
        UserController.readDataFromJson();
        MainMenu.run();
        UserController.writeDataToJson();
    }
}
