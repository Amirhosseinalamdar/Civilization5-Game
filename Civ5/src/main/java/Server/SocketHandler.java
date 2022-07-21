package Server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.bytebuddy.description.method.MethodDescription;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketHandler extends Thread{
    public static int PORT = 8000;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    @Override
    public void run() {//menu,//request,//userID,
        try {
            while (true) {
                ArrayList<String> input = new Gson().fromJson(dataInputStream.readUTF(), new TypeToken<List<String>>() {
                }.getType());
                String output = "raw output";
                if (input.get(0).equals(Menu.LOGIN_MENU.getMenuName())) output = UserController.run(input, socket);
                else if(input.get(0).equals(Menu.MAIN_MENU.getMenuName())) output = MainMenuController.run(input);
                else if (input.get(0).equals(Menu.GAME_MENU.getMenuName())) output = GameMenuController.run(input);
                else if (input.get(0).equals(Menu.GAME.getMenuName())) output = GameDatabase.run(input);
                else if(input.get(0).equals(Menu.GLOBAL.getMenuName())) output = GameDatabase.runGlobal(input, socket);
//                if (output != null) {
                    dataOutputStream.writeUTF(output);
                    dataOutputStream.flush();
                    System.out.println("sifoono bekesh");
//                }
            }
        }catch (IOException e){

            e.printStackTrace();
        }

    }

    public SocketHandler(Socket socket) {
        try {
            this.socket = socket;
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
