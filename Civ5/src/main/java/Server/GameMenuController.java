package Server;

import Client.App.Main;
import Client.Model.Chat.DataBase;
import Client.Model.User;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GameMenuController {
    public static String run(ArrayList<String > args) {
        String command = args.get(1);
        if(command.equals(Request.INVITE.getString())) return invite(args);
        else if(command.equals(Request.START.getString())) return startGame(args);
        else if (command.equals(Request.ANSWER.getString())) return answerToInvite(args);
        return "typo";
    }

    private static String startGame(ArrayList<String> args) {
        //TODO ferari
        return "";
    }

    private static String invite (ArrayList <String> args) {
        String sender = args.get(2), receiver = args.get(3);
        try {
            for (User user : GameDatabase.getAllUsers()) {
                if (!user.isLoggedIn()) continue;
                DataOutputStream outputStream = new DataOutputStream(GameDatabase.getPlayerReaderSockets().get(receiver).getOutputStream());
                outputStream.writeUTF(new Gson().toJson(new ArrayList<>(Arrays.asList("game menu", "invite", sender)))); //receive invite, sender
                outputStream.flush();
                return "invitation sent successfully";
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "nafrestadam ding ding ding";
    }

    private static String answerToInvite (ArrayList <String> args) {//game menu //answer //inviter //invited //<answer>
        String inviter = args.get(2);
        String invited = args.get(3);
        try {
            for (User user : GameDatabase.getAllUsers()) {
                if (!user.getUsername().equals(inviter)) continue;
                DataOutputStream outputStream = new DataOutputStream(GameDatabase.getPlayerReaderSockets().get(inviter).getOutputStream());
                if(args.get(4).equals(Request.YES.getString())){
                    GameDatabase.getPlayerSockets().put(inviter,GameDatabase.getPlayerReaderSockets().get(inviter));
                    GameDatabase.getPlayerSockets().put(invited,GameDatabase.getPlayerReaderSockets().get(invited));
                }
                outputStream.writeUTF(args.get(3));
                outputStream.flush();
                return "answer sent successfully";
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "nafrestadam";
    }
}
