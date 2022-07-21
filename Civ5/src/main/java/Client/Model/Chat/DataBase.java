package Client.Model.Chat;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DataBase {
    @Expose
    private static DataBase dataBase;

    private ArrayList<PrivateChat> privateChats = new ArrayList<>();
    private ArrayList<Room> rooms = new ArrayList<>();


    private PrivateChat currentPrivateChat;
    private Room currentRoom;

    public void setCurrentPrivateChat(PrivateChat currentPrivateChat) {
        this.currentPrivateChat = currentPrivateChat;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public PrivateChat getCurrentPrivateChat() {
        return currentPrivateChat;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }


    private DataBase() {
    }

    public static DataBase getInstance() {
        if (dataBase == null) dataBase = new DataBase();
        return dataBase;
    }

    public ArrayList<PrivateChat> getPrivateChats() {
        return privateChats;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public PrivateChat getPrivateChatByUsers(String username1, String username2) {
        for (PrivateChat key : privateChats) {
            if ((key.getUsername1().equals(username1) && key.getUsername2().equals(username2))
                    || (key.getUsername2().equals(username1) && key.getUsername1().equals(username1))) {
                return key;
            }
        }
        return null;
    }

    public void writeDataToJson() {
        try {
            FileWriter fileWriter = new FileWriter("ChatDataBase.json");
            fileWriter.write(new Gson().toJson(privateChats));
            fileWriter.write(new Gson().toJson(currentPrivateChat));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
