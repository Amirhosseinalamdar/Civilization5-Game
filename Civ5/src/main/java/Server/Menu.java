package Server;

public enum Menu {
    LOGIN_MENU("login menu"),
    MAIN_MENU("main menu"),
    GAME("game"),
    GLOBAL("global"),
    GAME_MENU("game menu");
    private String menuName;

    public String getMenuName() {
        return menuName;
    }

    Menu(String menuName) {
        this.menuName = menuName;
    }
}
