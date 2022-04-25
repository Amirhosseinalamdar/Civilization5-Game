package View;

import Controller.UserController;

import java.util.Scanner;
import java.util.regex.Matcher;

public class ProfileMenu {
    public static void run (Scanner scanner) {
        String command;
        while (true) {
            Matcher matcher;
            command = scanner.nextLine();
            if (Commands.getMatcher(command, Commands.EXIT_MENU) != null) break;
            if (Commands.getMatcher(command, Commands.CURRENT_MENU) != null) {
                System.out.println("Login Menu");
            } else if ((matcher = Commands.getMatcher(command, Commands.CHANGE_PASSWORD1)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.CHANGE_PASSWORD2)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.CHANGE_PASSWORD3)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.CHANGE_PASSWORD4)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.CHANGE_PASSWORD5)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.CHANGE_PASSWORD6)) != null) {
                System.out.println(UserController.changePassword(matcher));
            } else if ((matcher = Commands.getMatcher(command, Commands.CHANGE_NICKNAME)) != null) {
                System.out.println(UserController.changeNickname(matcher));
            } else {
                System.out.println("invalid command");
            }
        }
    }
}
