package View;

import Model.User;

public class MainMenu {

    public void run () {
        LoginMenu loginMenu = new LoginMenu();
        ProfileMenu profileMenu = new ProfileMenu();
        User loggedInUser = loginMenu.handleRegistrationsAndLoggingIn();
        while (true) {
            //if (command == profile)
                profileMenu.run();
            //if (command == play) {
            // gameMenu.startGame(command);
            //if (command == logout)
                loginMenu.handleRegistrationsAndLoggingIn();
        }
    }
}
