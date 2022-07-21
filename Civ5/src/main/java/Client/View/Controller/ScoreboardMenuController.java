package Client.View.Controller;

import Client.App.Main;
import Client.Model.User;
import Client.Transiton.NavigationTransition;
import Client.Controller.UserController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class ScoreboardMenuController {
    @FXML
    private ImageView icon1;
    @FXML
    private ImageView icon2;
    @FXML
    private ImageView icon3;
    @FXML
    private VBox vbox1;
    @FXML
    private VBox vbox2;
    @FXML
    private VBox vbox3;
    @FXML
    private Label back;

    public void initialize() {
        ArrayList<User> users = UserController.getBestUsers();
        int j = 1;
        for (int i = 0; i < users.size() && i < 8; i++) {
            Text text1 = new Text();
            Text text2 = new Text();
            Text text3 = new Text();
            text1.setText(j + ". " + users.get(i).getUsername());
            text2.setText(String.valueOf(users.get(i).getScore()));
            text3.setText(users.get(i).getTime());
            text1.setFont(Font.font(24));
            text2.setFont(Font.font(24));
            text3.setFont(Font.font(24));
            if (j == 1) {
                text1.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 22));
                text1.setFill(Color.RED);
                text2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 22));
                text2.setFill(Color.RED);
                text3.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 22));
                text3.setFill(Color.RED);
                icon1.setImage(new Image(users.get(i).getIconAddress()));
            }
            if (j == 2) {
                text1.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 21));
                text1.setFill(Color.SILVER);
                text2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 21));
                text2.setFill(Color.SILVER);
                text3.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 21));
                text3.setFill(Color.SILVER);
                icon2.setImage(new Image(users.get(i).getIconAddress()));
            }
            if (j == 3) {
                text1.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
                text1.setFill(Color.BROWN);
                text2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
                text2.setFill(Color.BROWN);
                text3.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
                text3.setFill(Color.BROWN);
                icon3.setImage(new Image(users.get(i).getIconAddress()));
            }
            vbox1.getChildren().add(text1);
            vbox2.getChildren().add(text2);
            vbox3.getChildren().add(text3);
            j++;
        }
    }

    public void back(MouseEvent mouseEvent) {
        Main.clickSound();
        NavigationTransition.fadeTransition(back, "MainMenu");
    }
}
