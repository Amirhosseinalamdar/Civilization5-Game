package Client.View;

import java.util.Scanner;
import java.util.regex.Matcher;

public class ProfileMenu {
    public static void run(Scanner scanner) {
        String command;
        while (true) {
            Matcher matcher;
            command = scanner.nextLine();
            if (Commands.getMatcher(command, Commands.EXIT_MENU) != null) break;
            else if (Commands.getMatcher(command, Commands.CURRENT_MENU) != null) {
                System.out.println("Profile Menu");
            } else if ((matcher = Commands.getMatcher(command, Commands.CHANGE_PASSWORD1)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.CHANGE_PASSWORD2)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.CHANGE_PASSWORD3)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.CHANGE_PASSWORD4)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.CHANGE_PASSWORD5)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.CHANGE_PASSWORD6)) != null) {
            } else if ((matcher = Commands.getMatcher(command, Commands.CHANGE_NICKNAME)) != null) {
            } else if ((matcher = Commands.getMatcher(command, Commands.ENTER_MENU)) != null) {
                if (matcher.group("menuName").equals("Main Menu")) {
                    System.out.println("use \"exit menu\" command");
                } else System.out.println("menu navigation is not possible");
            } else {
                System.out.println("invalid command");
            }
        }
    }
}
