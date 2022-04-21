package View;

import Controller.UserController;

import java.util.Scanner;
import java.util.regex.Matcher;

public class LoginMenu {
    public static int run(Scanner scanner) {
        String command;
        while (true) {
            Matcher matcher;
            command = scanner.nextLine();
            if (Commands.getMatcher(command, Commands.EXITMENU) != null) {
                scanner.close();
                return 1;
            }
            if (Commands.getMatcher(command, Commands.CURRENTMENU) != null) {
                System.out.println("Login Menu");
            } else if ((matcher = Commands.getMatcher(command, Commands.REGISTER1)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.REGISTER2)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.REGISTER3)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.REGISTER4)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.REGISTER5)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.REGISTER6)) != null) {
                System.out.println(UserController.registerUser(matcher));
            } else if ((matcher = Commands.getMatcher(command, Commands.LOGIN1)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.LOGIN2)) != null) {
                System.out.println(UserController.logUserIn(matcher));
                return 0;
            } else {
                System.out.println("invalid command");
            }
        }
    }
}
