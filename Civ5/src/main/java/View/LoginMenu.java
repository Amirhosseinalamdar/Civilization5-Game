package View;

import Controller.UserController;

import java.util.Scanner;
import java.util.regex.Matcher;

public class LoginMenu {
    public static int run(Scanner scanner) {
        String command;
        String output;
        while (true) {
            Matcher matcher;
            command = scanner.nextLine();
            if (Commands.getMatcher(command, Commands.EXIT_MENU) != null) {
                scanner.close();
                return 1;
            }
            else if (Commands.getMatcher(command, Commands.CURRENT_MENU) != null) {
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
                output = UserController.logUserIn(matcher);
                System.out.println(output);
                if (output.equals("user logged in successfully!")) return 0;
            } else if (Commands.getMatcher(command, Commands.ENTER_MENU) != null) {
                System.out.println("please login first");
            } else {
                System.out.println("invalid command");
            }
        }
    }
}
