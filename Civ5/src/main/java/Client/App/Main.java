package Client.App;

import Client.Controller.NetworkController;
import Client.Controller.UserController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    public static Stage stage;
    public static Scene scene;
    public static AudioClip music;
    public static String username;

    public static void main(String[] args) {
//        UserController.readDataFromJson();
        if(!NetworkController.connect()) {
            System.out.println("connection failed");
            System.exit(0);
        }
        else {
            launch();
            UserController.logUserOut();
        }
//        UserController.writeDataToJson();
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
