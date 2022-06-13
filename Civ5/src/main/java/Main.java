import Controller.UserController;
import Model.Chat.DataBase;
import Model.Civilization;
import Model.Game;
import View.GameMenu;
import View.MainMenu;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Main extends Application{
    public static void main(String[] args) {
        UserController.readDataFromJson("json.json");
//        Scanner scanner = new Scanner(System.in);
//        MainMenu.run(scanner);
//        UserController.writeDataToJson("json.json");
        launch();
        DataBase.getInstance().writeDataToJson();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = new URL(Main.class.getResource("/fxml/ChooseChat.fxml").toExternalForm());
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
