package App;

import Controller.UserController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static Stage stage;
    public static Scene scene;
    public static AudioClip music;

    @Override
    public void start(Stage stage) throws IOException {
        Main.stage = stage;
        FXMLLoader fxmlLoader1 = new FXMLLoader(Main.class.getResource("/fxml/LoginMenu.fxml"));
        Scene scene = new Scene(fxmlLoader1.load(), 1600, 900);
        Main.scene = scene;
        stage.setTitle("Civilization V");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        UserController.readDataFromJson();
//        Scanner scanner = new Scanner(System.in);
        launch();
//        MainMenu.run(scanner);
        UserController.writeDataToJson();
    }

    public static void clickSound() {
        Media media = new Media(Main.class.getResource("/sounds/click.wav").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public static void changeScene(String name) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/" + name + ".fxml"));
            scene.setRoot(fxmlLoader.load());
        } catch (Exception exception) {
            System.out.println("wrong fxml");
        }
    }

}