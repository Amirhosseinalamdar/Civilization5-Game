package View.Controller;

import Model.Chat.PublicChat;
import Model.Game;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PublicChatController {
    public TextField textField;

    public VBox chatBox;

    public Button sendButton;

    public PublicChatController(){
        this.chatBox = PublicChat.getInstance().getChatBox();
    }

    public void sendMsg(MouseEvent mouseEvent) {
        if(textField.getText().length() == 0) return;
        Text text = new Text(textField.getText());
        text.setTextAlignment(TextAlignment.valueOf("CENTER"));
        text.setStyle("-fx-font-family: 'Tw Cen MT'; -fx-font-size: 30;");
        HBox hBox = new HBox(text);
        hBox.setStyle("-fx-background-color: lightBlue; -fx-background-radius: 7 7 7 0;");
        hBox.setTranslateX(20);
        hBox.setTranslateY(hBox.getLayoutY() + 20);
        hBox.setMaxWidth(text.getLayoutBounds().getWidth());
        chatBox.getChildren().add(hBox);
        textField.clear();
        addMsgInfo();

    }
    private void addMsgInfo(){
        String sender;
        if(Game.getLoggedInUser() == null) sender = "Null";
        else sender = Game.getLoggedInUser().getUsername();
        if(sender.length() > 10){
            sender = sender.substring(0,7);
            sender.concat("...");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = formatter.format(date).toString().substring(11,16);
        String info = sender + " " + time +"  ";
        Text text = new Text(info);
        text.setTextAlignment(TextAlignment.valueOf("CENTER"));
        text.setStyle("-fx-font-family: 'Tw Cen MT'; -fx-font-size: 17;");
        HBox hBox = new HBox(text);
        hBox.setMaxWidth(text.getLayoutBounds().getWidth() + 30);
        hBox.setStyle("-fx-background-color: lightBlue; -fx-background-radius: 0 0 7 7;");
        hBox.setTranslateX(20);
        hBox.setTranslateY(chatBox.getChildren().get(chatBox.getChildren().size()-1).getTranslateY() +
                chatBox.getChildren().get(chatBox.getChildren().size()-1).getLayoutBounds().getHeight() - 10);
        //TODO if sent
        Circle circle = new Circle();
        circle.setRadius(5);
        circle.setStyle("-fx-fill: orange;");
        circle.setTranslateY(hBox.getLayoutY() + hBox.getLayoutBounds().getHeight()/2 + 5);
        hBox.getChildren().add(circle);
        chatBox.getChildren().add(hBox);
        PublicChat.getInstance().setChatBox(this.chatBox);
    }

    public void type(KeyEvent event) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > 70) {
                    textField.setText(oldValue);
                } else {
                    textField.setText(newValue);
                }
            }
        });

    }

    public void back(MouseEvent mouseEvent) {
        //TODO go to last menu
    }
}