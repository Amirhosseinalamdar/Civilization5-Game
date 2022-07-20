package App;

import Controller.UserController;
import Model.Game;
import Model.Map.Resource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;
import java.net.URL;

public class Main extends Application {
    public static Stage stage;
    public static Scene scene;
    public static AudioClip music;


    public static void main(String[] args) {
        UserController.readDataFromJson();
        launch();
        UserController.writeDataToJson();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Main.stage = stage;
        FXMLLoader fxmlLoader1 = new FXMLLoader(Main.class.getResource("/fxml/LoginMenu.fxml"));
        Scene scene = new Scene(fxmlLoader1.load(), 1600, 900);
        Main.scene = scene;
        stage.setTitle("Civilization V");
        stage.getIcons().add(new Image("/Pictures/steamworkshop_guide_1393684511_guide_branding.jpg"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        playSound("Menu.mp3");
    }


    public static void clickSound() {
        Media media = new Media(Main.class.getResource("/sounds/click.wav").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public static void playSound(String name) {
        if (music != null) music.stop();
        URL url = Main.class.getResource("/sounds/" + name);
        music = new AudioClip(url.toExternalForm());
        music.setVolume(0.3f);
        music.play();
    }

    public static void changeScene(String name) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/" + name + ".fxml"));
            scene.setRoot(fxmlLoader.load());
        } catch (Exception exception) {
            System.out.println("wrong fxml");
        }
    }

    public static void unitActionsSound(String string) {
        Media media = new Media(Main.class.getResource("/sounds/" + string + ".wav").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }
}
