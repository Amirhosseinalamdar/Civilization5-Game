package Client.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
    START_GAME("^play game( (--player|-p)\\d+ \\S+)+$"),
    ENTER_MENU("^menu enter (?<menuName>Login Menu|Main Menu|Game Menu|Profile Menu)$"),
    EXIT_MENU("^menu exit$"),
    CURRENT_MENU("^menu show-current$"),
    CHANGE_NICKNAME("^profile change (--nickname|-n) (?<nickname>\\S+)$"),
    CHANGE_PASSWORD1("^profile change (--password|-p) (--current|-c) (?<currentPassword>\\S+) (--new|-n) (?<newPassword>\\S+)$"),
    CHANGE_PASSWORD2("^profile change (--password|-p) (--new|-n) (?<newPassword>\\S+) (--current|-c) (?<currentPassword>\\S+)$"),
    CHANGE_PASSWORD3("^profile change (--current|-c) (?<currentPassword>\\S+) (--password|-p) (--new|-n) (?<newPassword>\\S+)$"),
    CHANGE_PASSWORD4("^profile change (--current|-c) (?<currentPassword>\\S+) (--new|-n) (?<newPassword>\\S+) (--password|-p)$"),
    CHANGE_PASSWORD5("^profile change (--new|-n) (?<newPassword>\\S+) (--password|-p) (--current|-c) (?<currentPassword>\\S+)$"),
    CHANGE_PASSWORD6("^profile change (--new|-n) (?<newPassword>\\S+) (--current|-c) (?<currentPassword>\\S+) (--password|-p)$"),
    CHOOSE_UNIT1("^unit (--coordinates|-c) (?<x>\\d+) (?<y>\\d+) (--type|-t) (?<unitType>(combat|noncombat))$"),
    CHOOSE_UNIT2("^unit (--type|-t) (?<unitType>(combat|noncombat)) (--coordinates|-c) (?<x>\\d+) (?<y>\\d+)$"),
    CHOOSE_CITY1("^city (--name|-n) (?<name>\\S+)$"),
    CHOOSE_CITY2("^city (--coordinates|-c) (?<x>\\d+) (?<y>\\d+)$"),
    SHOW_OUTPUT("^show output$"),
    CREATE_UNIT("create (-u|--unit) (?<unitName>\\S+)"),
    PURCHASE_TILE("purchase (-t|--tile) (-c|--coordinates) (?<x>\\d+) (?<y>\\d+)"),
    LOCK_CITIZEN("lock citizen on tile (-c|--coordinates) (?<x>\\d+) (?<y>\\d+)"),
    SCROLL_MAP("scroll (-d|--direction) (?<direction>(right|left|up|down)) (?<number>\\d+)"),
    SHOW_MAP_GLOBAL("show global map"),
    MANAGE_CIVILIZATION("manage civilization"),
    ASK_FOR_TECH("new tech (-n|--name) (?<techName>\\S+)"),
    SHOW_BANNER("^show cities banner$"),
    CIVILIZATION_OUTPUT("^show civilization output$"),
    PURCHASE_UNIT("purchase (--unit|-u) (?<unitName>\\S+)"),
    MOVE_UNIT("move to (--coordinates|-c) (?<x>\\d+) (?<y>\\d+)"),
    ATTACK("attack to (-c|--coordinates) (?<x>\\d+) (?<y>\\d+)"),
    BUILD_IMPROVEMENT("build improvement (-t|--type) (?<improvement>\\S+)"),
    CLEAR_LAND("clear (?<clearable>(forest|jungle|road|railroad))"),
    REPAIR("repair (-i|--improvement) (?<improvement>\\S+)"),
    BACK("back"), SLEEP_UNIT("sleep"), WAKE_UNIT("wake"), DELETE("delete"),
    ALERT("alert"), FORTIFY("fortify"), HEAL("heal"), GARRISON("garrison"),
    SETUP_RANDED("setup ranged"), DO_NOTHING("do nothing"),
    FOUND_CITY("found city"), CANCEL_MISSION("cancel mission"),
    PILLAGE("pillage (-i|--improvement) (?<improvement>\\S+)"),
    RESEARCH_INFO_SCREEN("^show research info screen$"),
    UNIT_LIST_PANEL("^show unit list panel$"),
    CITY_LIST_PANEL("^show city list panel$"),
    DIPLOMACY_INFO_PANEL("^show diplomacy info panel$"),
    DEMOGRAPHICS_SCREEN("^show demographics screen$"),
    NOTIFICATION_HISTORY("^show notification history$"),
    MILITARY_OVERVIEW("^show military overview$"),
    ECONOMIC_OVERVIEW("^show economic overview$"),
    SHOW_CITY_OUTPUT("show city output"),
    CHEAT1("^increase (--turn|-t) (?<x>\\d+)$"),
    CHEAT2("^increase (--gold|-g) (?<x>\\d+)$"),
    CHEAT3("^increase (--happiness|-h) (?<x>\\d+)$"),
    CHEAT4("^increase (--score|-s) (?<x>\\d+)$"),
    CHEAT5("^increase (--cities HP|-cHP) (?<x>\\d+)$"),
    CHEAT6("^increase (--units health|-uh) (?<x>\\d+)$"),
    CHEAT7("^increase (--cities combat strength|-ccs) (?<x>\\d+)$"),
    CHEAT8("^increase (--cities ranged combat strength|-crcs) (?<x>\\d+)$"),
    CHEAT9("^increase (--units MP|-uMP) (?<x>\\d+)$"),
    CHEAT10("^increase (--units combat strength|-ucs) (?<x>\\d+)$"),
    CHEAT11("^increase (--units ranged combat strength|-urcs) (?<x>\\d+)$"),
    CREATE_BUILDING("^build (-b|--building) (?<buildingName>\\S+)$"),
    PURCHASE_BUILDING("^purchase (-b|--building) (?<buildingName>\\S+)$");

    private final String regex;

    public String getRegex() {
        return regex;
    }

    Commands(String regex) {
        this.regex = regex;
    }

    public static ArrayList<String> getUsernames(String command) {
        HashMap<Integer, String> players = new HashMap<>();
        String[] strings = command.split("-");
        strings = Arrays.copyOfRange(strings, 1, strings.length);
        for (String string : strings) {
            if (string.startsWith("player")) {
                players.put(Integer.parseInt(string.substring(6, 7)), string.substring(8));
            } else if (string.startsWith("p")) {
                players.put(Integer.parseInt(string.substring(1, 2)), string.substring(3));
            }
        }
        ArrayList<String> usernames = new ArrayList<>();
        for (Map.Entry<Integer, String> e : players.entrySet()) {
            usernames.add(e.getKey() - 1, e.getValue());
        }
        return usernames;
    }

    public static Matcher getMatcher(String input, Commands command) {
        Matcher matcher = Pattern.compile(command.regex, Pattern.CASE_INSENSITIVE).matcher(input);
        if (matcher.matches()) {
            return matcher;
        }
        return null;
    }
}
