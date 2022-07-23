package Client.View;

import Client.App.Main;
import Client.Controller.CityController;
import Client.Controller.GameController;
import Client.Controller.NetworkController;
import Client.Controller.UnitController;
import Client.Model.*;
import Client.Model.Map.*;
import Client.Model.UnitPackage.Military;
import Client.Model.UnitPackage.Unit;
import Client.Model.UnitPackage.UnitStatus;
import Client.Model.UnitPackage.UnitType;
import Client.View.Controller.MapController;
import Server.Menu;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GameMenu {
    private static Scanner scanner;
    private static MapController gameMapController;
    private static int mapSize;
    private static int autoSaveDuration;

    public static void setMapSize(int mapSize) {
        GameMenu.mapSize = mapSize;
    }

    public static void setAutoSaveDuration(int number) {
        if (number == 0) GameMenu.autoSaveDuration = 0;
        else if (number == 1) GameMenu.autoSaveDuration = 1;
        else GameMenu.autoSaveDuration = (number - 1) * 5;
    }

    public static MapController getGameMapController() {
        return gameMapController;
    }

    public static void setScanner(Scanner scanner) {
        GameMenu.scanner = scanner;
    }

    private static void loadGame(int saveCode,String json) {
        try {
//            String json = new String(Files.readAllBytes(Paths.get("Game" + saveCode + ".json")));
            Game.loadInstance(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(json, Game.class));
            System.out.println("loaded");
            Game.getInstance().createRelations();
        } catch (Exception e) {
            System.out.println("ignored");
            e.printStackTrace();
        }
    }

    public static void receiveGame(String json) {
        Game.loadInstance(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(json, Game.class));
        System.out.println("loaded");
        Game.getInstance().createRelations();
        GameMenu.setMapSize(mapSize);
        GameMenu.startGame(new ArrayList<>(), new Scanner(System.in), 1,json);
    }

    public static void startGame(ArrayList<User> players, Scanner scanner, int saveCode,String json) {
        if (saveCode < 0) {
            Game.getInstance().generateGame(players, mapSize, autoSaveDuration);
        }else if(saveCode == 0){
            loadGame(saveCode,json);
        }
        GameMenu.scanner = scanner;
        GameController.checkMyCivilization();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("my username is : " + Main.username);
                Popup popup = new Popup();
                if (!Game.getInstance().getPlayers().get(Game.getInstance().getTurn()).getUsername().equals(Main.username)) {
                    popup.show(Main.stage);
                }
                else {
                    popup.hide();
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(Game.getInstance().getClass().getResource("/fxml/Map.fxml"));
                        Parent root = fxmlLoader.load();
                        MapController mapController = fxmlLoader.getController();
                        gameMapController = mapController;
                        Main.scene.setRoot(root);
                        KeyCombination kc = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
                        Runnable rn = () -> {
                            try {
                                FXMLLoader fxmlLoader1 = new FXMLLoader(Game.getInstance().getClass().getResource("/fxml/Cheat.fxml"));
                                Parent root1 = fxmlLoader1.load();
                                Stage stage1 = new Stage();
                                Scene scene1 = new Scene(root1);
                                stage1.setTitle("Cheat Box");
                                stage1.setScene(scene1);
                                stage1.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        };
                        Main.scene.getAccelerators().put(kc, rn);
                        setMapNavigation(Main.scene, mapController);
                        setUnitMovement(mapController);
                        Main.stage.setScene(Main.scene);
                        Main.stage.show();
                        if (saveCode < 0) {
                            String jsonSave = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(Game.getInstance());
                            String response = NetworkController.send(new ArrayList<String>(Arrays.asList(Menu.GAME.getMenuName()
                                    , Server.Request.INIT_GAME.getString(), Main.username, jsonSave)));
                            System.out.println("generating game: " + response);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    public static void setMapNavigation(Scene scene, MapController mapController) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().getName().equals("Right") &&
                        mapController.getyStartingIndex() + 14 < Game.getInstance().getMapSize()) {
                    mapController.setHoveredTile(null);
                    mapController.setyStartingIndex(1 + mapController.getyStartingIndex());
                    mapController.showMap();
                } else if (event.getCode().getName().equals("Left") && mapController.getyStartingIndex() > 1) {
                    mapController.setHoveredTile(null);
                    mapController.setyStartingIndex(mapController.getyStartingIndex() - 1);
                    mapController.showMap();
                } else if (event.getCode().getName().equals("Down") &&
                        mapController.getxStartingIndex() + 9 < Game.getInstance().getMapSize()) {
                    mapController.setHoveredTile(null);
                    mapController.setxStartingIndex(mapController.getxStartingIndex() + 1);
                    mapController.showMap();
                } else if (event.getCode().getName().equals("Up") &&
                        mapController.getxStartingIndex() > 1) {
                    mapController.setHoveredTile(null);
                    mapController.setxStartingIndex(mapController.getxStartingIndex() - 1);
                    mapController.showMap();
                }
            }
        });
    }

    public static void setUnitMovement(MapController mapController) {
        for (int i = 0; i < Game.getInstance().getMapSize(); i++) {
            for (int j = 0; j < Game.getInstance().getMapSize(); j++) {
                Tile tile = Game.getInstance().getTiles()[i][j];
                tile.setOnMouseClicked(event -> {
                    if (mapController.getChosenUnit() != null && mapController.getChosenUnit().getStatus() == UnitStatus.ATTACK) {
                        mapController.setChosenTarget(tile);
                        UnitController.setUnit(mapController.getChosenUnit(), "attack to -c " +
                                tile.getIndexInMapI() + " " + tile.getIndexInMapJ());
                        String message = UnitController.handleUnitOptions();
                        if (message.equals("done")) {
                            Request request = new Request();
                            request.setSender(Game.getInstance().getPlayers().get(Game.getInstance().getTurn()).getUsername());
                            request.setAction("War");
                            if (mapController.getChosenTarget().getCivilian() != null) {
                                if (!GameController.getCivilization().getInWarCivilizations().contains(mapController.getChosenTarget().getCivilian().getCivilization().getUsername()))
                                    GameController.getCivilization().getInWarCivilizations().add(mapController.getChosenTarget().getCivilian().getCivilization().getUsername());
                                mapController.getChosenTarget().getCivilian().getCivilization().getInWarCivilizations().add(GameController.getCivilization().getUsername());
                                mapController.getChosenTarget().getCivilian().getCivilization().getRequests().add(request);
                            } else if (mapController.getChosenTarget().getMilitary() != null) {
                                if (!GameController.getCivilization().getInWarCivilizations().contains(mapController.getChosenTarget().getMilitary().getCivilization().getUsername()))
                                    GameController.getCivilization().getInWarCivilizations().add(mapController.getChosenTarget().getMilitary().getCivilization().getUsername());
                                mapController.getChosenTarget().getMilitary().getCivilization().getInWarCivilizations().add(GameController.getCivilization().getUsername());
                                mapController.getChosenTarget().getMilitary().getCivilization().getRequests().add(request);
                            } else if (mapController.getChosenTarget().getCity() != null && mapController.getChosenTarget().getCity().getTiles().get(0).equals(mapController.getChosenTarget())) {
                                if (!GameController.getCivilization().getInWarCivilizations().contains(mapController.getChosenTarget().getCity().getCivilization().getUsername()))
                                    GameController.getCivilization().getInWarCivilizations().add(mapController.getChosenTarget().getCity().getCivilization().getUsername());
                                mapController.getChosenTarget().getCity().getCivilization().getInWarCivilizations().add(GameController.getCivilization().getUsername());
                                mapController.getChosenTarget().getCity().getCivilization().getRequests().add(request);
                            }
                        }
                        if (message.endsWith("is zero"))
                            mapController.getConquerorDecision(tile.getCity());
                        mapController.showPopup(event, message.toUpperCase() + "!");
                        mapController.getChosenUnit().realSetStatus(UnitStatus.ACTIVE);
                        mapController.setChosenUnit(null);
                        mapController.showMap();
                    } else {
                        if (event.getButton() == MouseButton.SECONDARY) {
                            mapController.setHoveredTile(tile);
                            showMap();
                            return;
                        }
                        if (mapController.getChosenUnit() != null) {
                            UnitController.setUnit(mapController.getChosenUnit(), "move to -c " + tile.getIndexInMapI() + " " + tile.getIndexInMapJ());
                            String message = UnitController.handleUnitOptions();
                            if (message.length() == 0) {
                                if (mapController.getChosenUnit().getType().isCivilian()) {
                                    mapController.getChosenUnit().setX(mapController.getChosenUnit().getTile().getX() + 65);
                                    mapController.getChosenUnit().setY(mapController.getChosenUnit().getTile().getY() + 40);
                                } else {
                                    mapController.getChosenUnit().setX(mapController.getChosenUnit().getTile().getX() + 10);
                                    mapController.getChosenUnit().setY(mapController.getChosenUnit().getTile().getY() + 40);
                                }
                                mapController.setChosenUnit(null);
                                mapController.showMap();
                            } else {
                                Main.unitActionsSound("moveSound");
                                mapController.showPopup(event, message.toUpperCase() + "!");
                            }
                        }
                    }
                });
            }
        }
    }

    public static void showMap() {
        gameMapController.showMap();
    }

    public static String nextCommand() {
        return scanner.nextLine();
    }

    public static Tile showRangedAttackOptions(Military military) {
        return null;
    }

    public static void showUnitOptions(Unit unit) {
        System.out.println("one option for now... please enter \"move\"");
    }

    public static String notEnoughMoves() {
        return "unit doesn't have enough moves";
    }

    public static void showBanner(City city) {
        System.out.println("city: " + city.getName());
        System.out.println("combat strength: " + city.getCombatStrength());
        System.out.println("food: " + city.getFoodPerTurn());
        System.out.println("production: " + city.getProductionPerTurn());
        System.out.println("gold: " + city.getGoldPerTurn());
        System.out.println("science: " + city.getSciencePerTurn());
        System.out.println("citizens: " + city.getCitizens().size());
        int i = 1;
        for (Citizen citizen : city.getCitizens()) {
            if (citizen.getTile() != null)
                System.out.println(i + " : x " + citizen.getTile().getIndexInMapI() + " | y : " + citizen.getTile().getIndexInMapJ());
            else System.out.println(i + " : citizen is unemployed");
            i++;
        }
    }

    public static void civilizationOutput(Civilization civilization) {
        System.out.println("gold: " + civilization.getTotalGold());
        System.out.println("science: " + civilization.getScience());
        System.out.println("happiness: " + civilization.getHappiness());
    }

    public static void showCityOutput(City city) {
        System.out.println("food: " + city.getFoodPerTurn());
        System.out.println("production: " + city.getProductionPerTurn());
        System.out.println("gold: " + city.getGoldPerTurn());
        System.out.println("science: " + city.getSciencePerTurn());
        if (city.getStoredFood() > 0)
            System.out.println("turns until growth citizen: " + city.getTurnsUntilBirthCitizen());
        else if (city.getStoredFood() < 0)
            System.out.println("turns until lose citizen: " + city.getTurnsUntilDeathCitizen());
        else System.out.println("turns until growth citizen: N/A");
        if (city.getTurnsUntilGrowthBorder() == 0) System.out.println("turns until growth border: N/A");
        else System.out.println("turns until growth border: " + city.getTurnsUntilGrowthBorder());
    }

    public static UnitType cityProductionMenu(City city) {
        return null;
    }

    public static void showResearchOptions(Civilization civilization) {

    }

    public static Citizen chooseCitizenToLock() {

        return null;
    }

    public static Improvement chooseImprovement() {
        return null;
    }

    public static CityStatus conqueredCityOptions() {
        return null;
    }

    public static void researchInfoScreen(Civilization civilization) {
        if (civilization.getInProgressTech() == null) System.out.println("there is no research in progress");
        else {
            System.out.println("current research project is : " + civilization.getInProgressTech().name());
            int turn = 0;
            if (civilization.getScience() != 0)
                turn = civilization.getLastCostUntilNewTechnologies().get(civilization.getInProgressTech()) / civilization.getScience();
            if (civilization.getScience() == 0) System.out.println("N/A turns remain for the research");
            else System.out.println(turn + " turns remain for the research");
            System.out.println("this research will unlock:");
            int i = 1;
            for (String unlock : civilization.getInProgressTech().getUnlocks()) {
                System.out.println(i + " : " + unlock);
                i++;
            }
        }
    }

    public static void cityList(Civilization civilization) {
        for (City city : civilization.getCities()) {
            System.out.println("Name: " + city.getName() + "  |  Population: " + city.getCitizens().size() + "  |  Defensive Strength: " + city.getHP());
            System.out.println(CityController.turnsForNewUnit(city));
        }
    }

    public static void showDiplomacyInfo(Civilization civilization) {
        System.out.println("Game Score: " + civilization.getScore());
    }

    private static void victoryProgressInfo() {

    }

    public static void demographicsInfoScreen(Civilization civilization) {
        System.out.println("Cities: " + civilization.getCities().size() + "  |  Best: " + GameController.findBestCity()
                + "  |  Average: " + GameController.findAverageCity() + "  |  Worst: " + GameController.findWorstCity()
                + "  |  Rank: " + GameController.findRankInCities());
        System.out.println("Gold: " + civilization.getTotalGold() + "  |  Best: " + GameController.findBestGold()
                + "  |  Average: " + GameController.findAverageGold() + "  |  Worst: " + GameController.findWorstGold()
                + "  |  Rank: " + GameController.findRankInGolds());
        System.out.println("Units: " + civilization.getUnits().size() + "  |  Best: " + GameController.findBestUnit()
                + "  |  Average: " + GameController.findAverageUnit() + "  |  Worst: " + GameController.findWorstUnit()
                + "  |  Rank: " + GameController.findRankInUnits());
        System.out.println("Science: " + civilization.getScience() + "  |  Best: " + GameController.findBestScience()
                + "  |  Average: " + GameController.findAverageScience() + "  |  Worst: " + GameController.findWorstScience()
                + "  |  Rank: " + GameController.findRankInScience());
        System.out.println("Happiness: " + civilization.getHappiness() + "  |  Best: " + GameController.findBestHappiness()
                + "  |  Average: " + GameController.findAverageHappiness() + "  |  Worst: " + GameController.findWorstHappiness()
                + "  |  Rank: " + GameController.findRankInHappiness());
    }

    public static void notificationHistory(Civilization civilization) {
        for (String notification : civilization.getNotifications()) {
            System.out.println(notification);
        }
    }

    public static void militaryOverview(Civilization civilization) {
        for (Unit unit : civilization.getUnits()) {
            System.out.println("Name: " + unit.getType().name() + "  |  Status: " + unit.getStatus().name() + "  |  Health: "
                    + unit.getHealth() + "  |  X: " + unit.getTile().getIndexInMapI() + "  |  Y: " + unit.getTile().getIndexInMapJ());
        }
    }

    public static void economicOverview(Civilization civilization) {
        for (City city : civilization.getCities()) {
            CityController.updateCityInfos(city);
            System.out.println("Name: " + city.getName() + "  |  Population: " + city.getCitizens().size() + "  |  Defensive Strength: " + city.getHP());
            System.out.println("food: " + city.getFoodPerTurn());
            System.out.println("production: " + city.getProductionPerTurn());
            System.out.println("gold: " + city.getGoldPerTurn());
            System.out.println("science: " + city.getSciencePerTurn());
            System.out.println(CityController.turnsForNewUnit(city));
        }
    }

    private static void tradingHistory() {

    }

    private static void generalMilitaryCheck() {

    }

    private static void generalEconomicCheck() {

    }

    private static void generalDiplomaticCheck() {

    }

    public static final String RESET = "\u001B[0m";
    public static final String BLUE = "\033[0;104m";

    private static boolean isRiverValidToShow(int i, int j, Civilization civilization) {
        if (Game.getInstance().getTiles()[i][j].isRiverAtLeft() &&
                Game.getInstance().getTiles()[i][j].getTypeForCiv(civilization, i, j) != TerrainType.FOGGY)
            return true;
        return false;
    }

    private static void printLine1(int i, int j, Civilization civilization) {
        if (i == 120) {
            if (j == 0) System.out.print("   ");
            System.out.print("     \\"
                    + Game.getInstance().getTiles()[19][2 * j + 1].getTypeForCiv(civilization, 19, 2 * j + 1).getColor()
                    + "         " + RESET + "/");
        } else {
            showXAndY(i / 6, 2 * j, true, civilization);
        }
    }

    private static void printLine2(int i, int j, Civilization civilization) {
        if (i == 121) {
            if (j == 0) System.out.print("  ");
            System.out.print("       \\");
            System.out.print(Game.getInstance().getTiles()[19][2 * j + 1].getTypeForCiv(civilization, 19, 2 * j + 1).getColor());
            showUnitAndMilitary((i - 1) / 6 - 1, 2 * j + 1, false, civilization);
            System.out.print(RESET);
            System.out.print("/");

        } else {
            showResource((i - 1) / 6, 2 * j, true, civilization);
        }
    }

    private static void printLine3(int i, int j, Civilization civilization) {
        if (i == 122) {
            if (j == 0) System.out.print(" ");
            System.out.print("         \\" +
                    Game.getInstance().getTiles()[19][2 * j + 1].getTypeForCiv(civilization, 19, 2 * j + 1).getColor()
                    + "_____" + RESET + "/");
        } else {
            int I = (i - 2) / 6;
            int J = j * 2;
            if (civilization.getTileVisionStatuses()[I][J] != TileStatus.FOGGY) {
                if (isRiverValidToShow(I, J, civilization)) System.out.print(BLUE + "/" + RESET);
                else System.out.print(RESET + "/");
                System.out.print(Game.getInstance().getTiles()[(i - 2) / 6][j * 2].getTypeForCiv(civilization, I, J).getColor());
                showRoadAndRailRoadAndFoodAndProduction(I, J, true);
                System.out.print(getTypeFirstChar(Game.getInstance().getTiles()[(i - 2) / 6][2 * j].getTypeForCiv(civilization, I, J)) + "," +
                        getFeatureFirstChar(Game.getInstance().getTiles()[(i - 2) / 6][2 * j].getFeature()));
                showRoadAndRailRoadAndFoodAndProduction(I, J, false);
                System.out.print(RESET);
            } else {
                System.out.print("/         ");
            }
            if (I > 0 && isRiverValidToShow(I - 1, J + 1, civilization))
                System.out.print(BLUE + "\\" + RESET);
            else if (I > 0) System.out.print(RESET + "\\");
            if (I > 0)
                System.out.print(Game.getInstance().getTiles()[I - 1][J + 1].getTypeForCiv(civilization, I - 1, J + 1).getColor() + "_____" + RESET);
            else System.out.print("\\_____");
        }
    }

    private static void showRoadAndRailRoadAndFoodAndProduction(int i, int j, boolean isAtLeft) {
        if (isAtLeft) {
            System.out.print("f" + Game.getInstance().getTiles()[i][j].getFoodPerTurn());
            if (Game.getInstance().getTiles()[i][j].getRouteInProgress() != null) {
                if (Game.getInstance().getTiles()[i][j].getRouteInProgress().getKey().equals("road"))
                    System.out.print("r");
                else System.out.print("R");
            } else System.out.print(" ");
        } else {
            if (Game.getInstance().getTiles()[i][j].getRouteInProgress() != null &&
                    Game.getInstance().getTiles()[i][j].getRouteInProgress().getValue() != 0) System.out.print("&");
            else if (Game.getInstance().getTiles()[i][j].getRouteInProgress() != null &&
                    Game.getInstance().getTiles()[i][j].getRouteInProgress().getValue() == 0) System.out.print("$");
            else System.out.print(" ");
            System.out.print("p" + Game.getInstance().getTiles()[i][j].getProductionPerTurn());
        }
    }

    private static void printLine6(int i, int j, Civilization civilization) {
        int I = (i - 5) / 6;
        int J = (2 * j + 1);
        if (j == 0) System.out.print("  ");
        if (civilization.getTileVisionStatuses()[I][J - 1] != TileStatus.FOGGY) {
            if (isRiverValidToShow(I, J - 1, civilization)) System.out.print(BLUE + "\\" + RESET);
            else System.out.print("\\");
            System.out.print(Game.getInstance().getTiles()[I][J - 1].getTypeForCiv(civilization, I, J - 1).getColor() + "_____" + RESET);
            if (isRiverValidToShow(I, J, civilization)) System.out.print(BLUE + "/" + RESET);
            else System.out.print("/");
        } else System.out.print("\\_____/");
        if (civilization.getTileVisionStatuses()[I][J] != TileStatus.FOGGY) {
            System.out.print(Game.getInstance().getTiles()[I][J].getTypeForCiv(civilization, I, J).getColor());
            showRoadAndRailRoadAndFoodAndProduction(I, J, true);
            System.out.print(getTypeFirstChar(Game.getInstance().getTiles()[(i - 5) / 6][2 * j + 1].getTypeForCiv(civilization, I, J)) + "," +
                    getFeatureFirstChar(Game.getInstance().getTiles()[(i - 5) / 6][2 * j + 1].getFeature()));
            showRoadAndRailRoadAndFoodAndProduction(I, J, false);
            System.out.print(RESET);
        } else System.out.print("         ");
    }

    private static void printRoof() {
        for (int j = 0; j < 3; j++) {
            System.out.print("   _____        ");
        }
        System.out.print('\n');
    }

    private static int calculateStartingJ(int centerJ) {
        if (centerJ % 2 == 1) centerJ--;
        if (centerJ < 1) return 0;
        else if (centerJ > 17) return 14;
        else return centerJ - 2;
    }

    private static int calculateStartingI(int centerI) {
        if (centerI < 1) return 0;
        else if (centerI > 18) return 17;
        else return centerI - 1;
    }

    public static void showMap(Civilization civilization, int centerI, int centerJ, boolean global) {
        TileStatus[][] previousStatuses = civilization.getTileVisionStatuses().clone();
        int startingJ = calculateStartingJ(centerJ) / 2;
        int startingI = calculateStartingI(centerI) * 6;
        boolean flag = false;
        boolean rightSideFlag = false;
        if (startingJ == 7) flag = true;
        if (startingJ != 0) rightSideFlag = true;
        int length = 3, height = 18;
        if (global) {
            startingI = 0;
            startingJ = 0;
            height = 123;
            length = 10;
        }
        for (int i = startingI; i < startingI + height; i++) {
            for (int j = startingJ; j < startingJ + length; j++) {
                if (i % 6 == 0) {
                    if (j == startingJ && j != 0) System.out.print("  ");
                    printLine1(i, j, civilization);
                } else if (i % 6 == 1) {
                    if (j == startingJ && j != 0) System.out.print(" ");
                    printLine2(i, j, civilization);
                } else if (i % 6 == 2) {
                    printLine3(i, j, civilization);
                } else if (i % 6 == 3) {
                    showXAndY((i - 3) / 6, 2 * j + 1, false, civilization);
                } else if (i % 6 == 4) {
                    if (j == startingJ && j != 0) System.out.print(" ");
                    showResource((i - 4) / 6, j * 2 + 1, false, civilization);
                } else {
                    if (j == startingJ && j != 0) System.out.print("  ");
                    printLine6(i, j, civilization);
                }
            }

            if (i % 6 == 3) System.out.print("\\");
            else if (i % 6 == 4) {
                System.out.print(Game.getInstance().getTiles()[(i - 4) / 6][19].getTypeForCiv(civilization, (i - 4) / 6, 19).getColor());
                if (flag || global) System.out.print(" ");
                System.out.print(RESET + "\\");
            } else if (i % 6 == 5) System.out.print("\\");
            else if (i % 6 == 0 && i > 2 && i != 120) System.out.print("/");
            else if (i % 6 == 1 && i > 2 && i != 121) System.out.print("/");
            else if (i % 6 == 2 && i > 2 && i != 122) System.out.print("/");
            System.out.print('\n');
        }
    }

    private static char getTypeFirstChar(TerrainType type) {
        if (type == TerrainType.HILL) return 'H';
        else if (type == TerrainType.SNOW) return 'S';
        else if (type == TerrainType.TUNDRA) return 'T';
        else if (type == TerrainType.GRASS) return 'G';
        else if (type == TerrainType.MOUNTAIN) return 'M';
        else if (type == TerrainType.DESERT) return 'D';
        else if (type == TerrainType.OCEAN) return '#';
        else return 'P';
    }

    private static char getFeatureFirstChar(TerrainFeature feature) {
        if (feature == TerrainFeature.MARSH) return 'S';
        else if (feature == TerrainFeature.FOREST) return 'F';
        else if (feature == TerrainFeature.JUNGLE) return 'J';
        else if (feature == TerrainFeature.ICE) return 'I';
        else if (feature == TerrainFeature.OASIS) return 'O';
        else if (feature == TerrainFeature.DELTA) return 'D';
        else return ' ';
    }

    private static void showResource(int i, int j, boolean isEven, Civilization civilization) {
        if (isEven) {
            if (j / 2 == 0) System.out.print(" ");
            if (isRiverValidToShow(i, j, civilization)) System.out.print(BLUE + "/" + RESET);
            else System.out.print("/");
            System.out.print(Game.getInstance().getTiles()[i][j].getTypeForCiv(civilization, i, j).getColor());
            System.out.print(outputResource(civilization, i, j));
            if (Game.getInstance().getTiles()[i][j].canUseItsResource()) System.out.print("$");
            else if (Game.getInstance().getTiles()[i][j].getImprovementInProgress() != null) System.out.print("&");
            else System.out.print(" ");
            for (int k = 0; k < 6 - outputResource(civilization, i, j).length(); k++) System.out.print(' ');
            if (i > 0 && isRiverValidToShow(i - 1, j + 1, civilization))
                System.out.print(BLUE + "\\" + RESET);
            else System.out.print(RESET + "\\");
            if (i > 0) showUnitAndMilitary(i - 1, j + 1, false, civilization);
            else System.out.print("       ");
        } else {
            if ((j - 1) / 2 == 0) System.out.print(" ");
            if (j > 0) showUnitAndMilitary(i, j - 1, true, civilization);
            System.out.print(Game.getInstance().getTiles()[i][j].getTypeForCiv(civilization, i, j).getColor());
            System.out.print(outputResource(civilization, i, j));
            if (Game.getInstance().getTiles()[i][j].canUseItsResource()) System.out.print("$");
            else if (Game.getInstance().getTiles()[i][j].getImprovementInProgress() != null) System.out.print("&");
            else System.out.print(" ");
            for (int k = 0; k < 5 - outputResource(civilization, i, j).length(); k++) System.out.print(' ');

            if ((j - 1) / 2 != 9 && outputResource(civilization, i, j).length() != 6)
                System.out.print(" ");
            System.out.print(RESET);
        }
    }

    private static String outputResource(Civilization civilization, int i, int j) {
        if (civilization.getTileVisionStatuses()[i][j] == TileStatus.CLEAR) {
            if (Game.getInstance().getTiles()[i][j].getResource() == Resource.JEWELERY) return "JEWEL";
            else if (Game.getInstance().getTiles()[i][j].getResource() == Resource.BOKHOOR) return "BOKHOO";
            else if (Game.getInstance().getTiles()[i][j].getResource() == Resource.NONE) return " ";
            else return Game.getInstance().getTiles()[i][j].getResource().toString();
        } else {
            return " ";
        }
    }

    private static void showCitiesOnMap(int i, int j, Civilization civilization) {
    }

    private static void showXAndY(int i, int j, boolean isEven, Civilization civilization) {
        if (isEven) {
            if (j == 0) System.out.print("  ");
            if (isRiverValidToShow(i, j, civilization)) System.out.print(BLUE + "/" + RESET);
            else System.out.print("/");
            System.out.print(Game.getInstance().getTiles()[i][j].getTypeForCiv(civilization, i, j).getColor());
            String loc = i + "," + j;
            System.out.print(loc);
            for (int k = 0; k < 5 - loc.length(); k++) System.out.print(' ');
            if (i > 0 && isRiverValidToShow(i - 1, j + 1, civilization))
                System.out.print(BLUE + "\\" + RESET);
            else System.out.print(RESET + "\\");
            if (i > 0) showCitiesOnMap(i - 1, j + 1, civilization);
            else System.out.print("         ");
        } else {
            if (j > 0 && isRiverValidToShow(i, j - 1, civilization)) System.out.print(BLUE + "\\" + RESET);
            else System.out.print("\\");
            showCitiesOnMap(i, j - 1, civilization);
            if (isRiverValidToShow(i, j, civilization))
                System.out.print(BLUE + "/" + RESET);
            else System.out.print(RESET + "/");
            System.out.print(Game.getInstance().getTiles()[i][j].getTypeForCiv(civilization, i, j).getColor());
            String loc = i + "," + j;
            System.out.print(loc);
            for (int k = 0; k < 5 - loc.length(); k++) System.out.print(' ');
            System.out.print(RESET);
        }
    }

    private static void showUnitAndMilitary(int i, int j, boolean isEven, Civilization civilization) {
        String output1, output2;
        if (Game.getInstance().getTiles()[i][j].getMilitary() == null) output1 = "   ";
        else output1 = Game.getInstance().getTiles()[i][j].getMilitary().getType().toString().substring(0, 3);
        if (Game.getInstance().getTiles()[i][j].getCivilian() == null) output2 = "   ";
        else output2 = Game.getInstance().getTiles()[i][j].getCivilian().getType().toString().substring(0, 3);
        if (isEven) {
            if (isRiverValidToShow(i, j, civilization)) System.out.print(BLUE + "\\" + RESET);
            else System.out.print("\\");
            System.out.print(Game.getInstance().getTiles()[i][j].getTypeForCiv(civilization, i, j).getColor());
            setColor(i, j);
            if (civilization.getTileVisionStatuses()[i][j] == TileStatus.CLEAR)
                System.out.print(output1 + "," + output2);
            else System.out.print("       ");
            if (isRiverValidToShow(i, j + 1, civilization))
                System.out.print(BLUE + "/" + RESET);
            else System.out.print(RESET + "/");
        } else {
            System.out.print(Game.getInstance().getTiles()[i][j].getTypeForCiv(civilization, i, j).getColor());
            setColor(i, j);
            if (civilization.getTileVisionStatuses()[i][j] == TileStatus.CLEAR)
                System.out.print(output1 + "," + output2 + RESET);
            else System.out.print("       " + RESET);
        }
    }

    private static void setColor(int i, int j) {
    }

    public static void notYourUnit() {
        System.out.println("this is not your unit");
    }

    public static void invalidChosenUnit() {
        System.out.println("no unit exists in chosen pos");
    }

    public static void invalidPosForCity() {
        System.out.println("no city exists in chosen pos");
    }

    public static void indexOutOfArray() {
        System.out.println("chosen x or chosen y are out of array");
    }

    public static void invalidNameForCity() {
        System.out.println("no city exists with this name");
    }

    public static void siegeNotPrepared() {
        System.out.println("you have to prepare the siege unit first");
    }

    public static void cantMakeGarrison() {
        System.out.println("can't make garrison, no city available");
    }

    public static void unitIsNot(String unitName) {
        System.out.println("chosen unit is not " + unitName);
    }

    public static void siegeAlreadyPrepared() {
        System.out.println("this unit is already prepared");
    }

    public static void invalidChosenTile() {
        System.out.println("no tile exists in chosen tile");
    }

    public static void unitIsCivilianError() {
        System.out.println("error: this unit is civilian");
    }

    public static void unitIsMilitaryError() {
        System.out.println("error: this unit is military");
    }

    public static void unitHasFullHealth() {
        System.out.println("unit's health is already full");
    }

    public static void invalidUnitType() {
        System.out.println("unit type is invalid");
    }

    public static void invalidBuildingName() {
        System.out.println("building name is invalid");
    }

    public static void cantFoundCityHere() {
        System.out.println("can't found city here");
    }

    public static void cityAlreadyHasTile() {
        System.out.println("city already has this tile");
    }

    public static void unreachableTileForCity() {
        System.out.println("can't purchase tile : it's not near the city");
    }

    public static void notEnoughGoldForTilePurchase() {
        System.out.println("can't purchase tile : not enough gold");
    }

    public static void noUnemployedCitizenAvailable() {
        System.out.println("no unemployed citizen available: please choose one tile's citizen:");
    }

    public static void invalidPosForCitizen() {
        System.out.println("chosen position is invalid");
    }

    public static void citizenNotYours() {
        System.out.println("this citizen doesn't belong to chosen city");
    }

    public static void citizenLockError() {
        System.out.println("couldn't lock any citizen");
    }

    public static void unreachedTech(Technology prerequisiteTech) {
        System.out.println("you haven't reached " + prerequisiteTech.toString() + " yet");
    }

    public static void notEnoughResource() {
        System.out.println("you don't have enough resource");
    }

    public static void cantBuild() {
        System.out.println("you can't build this building here");
    }

    public static void invalidTechName() {
        System.out.println("invalid technology name");
    }

    public static void techAdded() {
        System.out.println("technology added successfully");
    }

    public static void canceledTech(Technology canceled) {
        System.out.println(canceled.toString() + " is now canceled");
    }

    public static void noSuchImprovement() {
        System.out.println("no such improvement even exists");
    }

    public static void cantBuildImprovementOnTile() {
        System.out.println("can not build chosen improvement on this tile");
    }

    public static void cityNameAlreadyExists() {
        System.out.println("city name already exists... please pick another name:");
    }

    public static void unitHasRemainingMove(Unit unit) {
        System.out.println("a " + unit.getType().toString() + " unit on " + unit.getTile().getIndexInMapI() + ", " +
                unit.getTile().getIndexInMapJ() + " has remaining moves");
    }

    public static void chooseProductionForCity(String cityName) {
        System.out.println(cityName + " has no production currently; choose production for it");
    }

    public static void chooseTechForCivilization() {
        System.out.println("civilization has no tech in progress; choose a research");
    }

    public static void cityIsOccupied(String type) {
        System.out.println("city is already occupied by a " + type + " unit. move the unit and try again");
    }

    public static void notEnoughGoldForUnit(String string) {
        System.out.println("cant purchase " + string + "; not enough gold");
    }

    public static void cantBuildRoadHere() {
        System.out.println("can not build road here");
    }

    public static void invalidClearingTarget() {
        System.out.println("tile doesn't have this feature to be cleared");
    }

    public static void cantBuildRailroadHere() {
        System.out.println("can not build railroad here");
    }

    public static void tileAlreadyHas(String improvementName) {
        System.out.println("this tile already has " + improvementName);
    }

    public static void tileIsNotInTerritory(Improvement improvement) {
        System.out.println("can not build " + improvement.toString() + " here; tile is out of city limits");
    }

    public static void pillageSuccessful(String improvement) {
        System.out.println(improvement + " was successfully pillaged");
    }

    public static void repairStarted(String improvement) {
        System.out.println("successfully started repairing " + improvement);
    }

    public static void impassableTile() {
        System.out.println("can not walk on that tile");
    }

    public static String cityOutOfUnitRange() {
        return "can not range attack, city out of range";
    }

    public static void rangedAttackToCitySuccessfully(City city) {
        System.out.println("ranged attack to " + city.getName() + " was a success");
    }

    public static String cityHPIsZero(City city) {
        return city.getName() + " is zero";
    }

    public static void invalidDecisionForConqueredCity() {
        System.out.println("city conquer decision was invalid");
    }

    public static void attachCitySuccessful(City city) {
        System.out.println(city.getName() + " attached successfully");
    }

    public static String invalidTileForAttack() {
        return "can not attack to chosen tile, there are no enemy units/city";
    }


    public static void workerStated(String improvement) {
        System.out.println("worker unit started " + improvement);
    }

    public static void pillaged(String pillageTarget) {
        System.out.println(pillageTarget + " got pillaged successfully");
    }

    public static void unreachedBuilding(ArrayList<Building> prerequisiteBuildings) {
        System.out.println("you haven't reached " + prerequisiteBuildings.toString() + " yet");
    }
}