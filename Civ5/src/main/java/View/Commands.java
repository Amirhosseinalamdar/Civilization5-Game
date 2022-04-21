package View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Commands {
    REGISTER1("^user create (--username|-u) (?<username>\\S+) (--nickname|-n) (?<nickname>\\S+) (--password|-p) (?<password>\\S+)$"),
    REGISTER2("^user create (--username|-u) (?<username>\\S+) (--password|-p) (?<password>\\S+) (--nickname|-n) (?<nickname>\\S+)$"),
    REGISTER3("^user create (--nickname|-n) (?<nickname>\\S+) (--username|-u) (?<username>\\S+) (--password|-p) (?<password>\\S+)$"),
    REGISTER4("^user create (--nickname|-n) (?<nickname>\\S+) (--password|-p) (?<password>\\S+) (--username|-u) (?<username>\\S+)$"),
    REGISTER5("^user create (--password|-p) (?<password>\\S+) (--username|-u) (?<username>\\S+) (--nickname|-n) (?<nickname>\\S+)$"),
    REGISTER6("^user create (--password|-p) (?<password>\\S+) (--nickname|-n) (?<nickname>\\S+) (--username|-u) (?<username>\\S+)$"),
    LOGIN1("^user login (--username|-u) (?<username>\\S+) (--password|-p) (?<password>\\S+)$"),
    LOGIN2("^user login (--password|-p) (?<password>\\S+) (--username|-u) (?<username>\\S+)$"),
    LOGOUT("^user logout$"),
//    STARTGAME("^play game (--player1|-p1) (?<username>\\S+) (?<moves>\\d+)$"),
    ENTERMENU("^menu enter (?<menuName>Login Menu|Main Menu|Game Menu|Profile Menu)$"),
    EXITMENU("^menu exit$"),
    CURRENTMENU("^menu show-current$");


    private String regex;

    Commands(String regex) {
        this.regex = regex;
    }

    public static Matcher getMatcher(String input, Commands command) {
        Matcher matcher = Pattern.compile(command.regex, Pattern.CASE_INSENSITIVE).matcher(input);
        if (matcher.matches()) {
            return matcher;
        }
        return null;
    }
}
