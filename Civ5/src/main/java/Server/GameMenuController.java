package Server;

import java.util.ArrayList;

public class GameMenuController {
    public static String run(ArrayList<String > args){
        String command = args.get(1);
        if(command.equals(Request.INVITE.getString())) return invitePlayer(args);
        else if(command.equals(Request.START.getString())) return startGame(args);
        return "typo";
    }

    private static String startGame(ArrayList<String> args) {
        //TODO ferari
        return "";
    }

    private static String invitePlayer(ArrayList<String> args) {
        //TODO be hashmap ezafe she
        return "";
    }
}
