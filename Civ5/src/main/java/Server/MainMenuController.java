package Server;

import java.util.ArrayList;

public class MainMenuController {
    public static  String run(ArrayList<String> args){
        if(args.get(1).equals(Request.LOGOUT.getString())) return UserController.logUserOut(args);
        return "Main Menu typo";
    }
}
