package Controller;

import App.Main;
import Model.*;
import Model.Map.*;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitStatus;
import Model.UnitPackage.UnitType;
import View.Commands;
import View.Controller.MapController;
import View.GameMenu;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnitController {
    private static Civilization civilization;
    private static Unit unit;
    private static String command;

    public static void changeCivilization(Civilization civilization) {
        UnitController.civilization = civilization;
    }

    public static void setUnit (Unit unit, String command) {
        UnitController.unit = unit;
        UnitController.command = command;
    }

    public static Civilization getCivilization() {
        return civilization;
    }

    public static String handleUnitOptions() {
        Matcher matcher = getUnitDecision();

        unit.setStatus(matcher.pattern().toString());

        if (unit.getStatus().equals(UnitStatus.MOVE)) {
            int destCenterX = Integer.parseInt(matcher.group("x")), destCenterY = Integer.parseInt(matcher.group("y"));

            if (unit.hasRemainingMoves()) return moveUnit(destCenterX, destCenterY);
            else return "unit doesn't have enough moves";
        }
        else if (unit.getStatus().equals(UnitStatus.ATTACK)) { //TODO... holy shit
            int x = Integer.parseInt(matcher.group("x"));
            int y = Integer.parseInt(matcher.group("y"));
            if (unit.hasRemainingMoves()) {
                if (canAttackTo(Game.getInstance().getTiles()[x][y])) return attack(Game.getInstance().getTiles()[x][y]);
                else return GameMenu.invalidTileForAttack();
            } else return GameMenu.notEnoughMoves();
        }
        else if (unit.getStatus().equals(UnitStatus.FOUND_CITY)) {
            if (canFoundCityHere()) {
                if (unit.hasRemainingMoves()) return foundCity();
                else return "unit doesn't have enough moves";
            }
            return "can't found city here";
        }
        else if (unit.getStatus().equals(UnitStatus.BUILD_IMPROVEMENT)) {
            try {
                Improvement improvement = Improvement.valueOf(matcher.group("improvement"));
                if (canBuildImprovementHere(improvement).length() == 0) {
                    if (unit.hasRemainingMoves()) return buildImprovement(improvement);
                    else return "unit doesn't have enough moves";
                }
            }
            catch (Exception e) {
                if (matcher.group("improvement").equals("ROAD")) {
                    if (canBuildRoadHere()) {
                        if (unit.hasRemainingMoves()) return buildRoute("road");
                        else return "unit doesn't have enough moves";
                    }
                    else return "can't build road here";
                }
                else if (matcher.group("improvement").equals("RAILROAD") && canBuildRailroadHere()) {
                    if (canBuildRailroadHere()) {
                        if (unit.hasRemainingMoves()) return buildRoute("railroad");
                        else return "unit doesn't have enough moves";
                    }
                    else if (civilization.hasReachedTech(Technology.RAILROAD)) return "can't build railroad here";
                    else return "you haven't reached " + Technology.RAILROAD + " yet";
                }
//                else GameMenu.noSuchImprovement();
            }
        }
        else if (unit.getStatus().equals(UnitStatus.PILLAGE)) {
            if (matcher.group("improvement").equals("road") || matcher.group("improvement").equals("railroad")) {
                if (canPillageRoute(matcher.group("improvement"))) {
                    if (unit.hasRemainingMoves()) return pillageRoute(matcher.group("improvement"));
                    else return "unit doesn't have enough moves";
                }
            }
            else {
                try {
                    Improvement improvement = Improvement.valueOf(matcher.group("improvement"));
                    if (canPillageImprovement(improvement)) {
                        if (unit.hasRemainingMoves()) return pillageImprovement();
                        else return "unit doesn't have enough moves";
                    }
                }
                catch (Exception e) {
                    GameMenu.noSuchImprovement(); //TODO... handled? @amirholmd
                }
            }
        }
        else if (unit.getStatus().equals(UnitStatus.REPAIR)) {
            if (matcher.group("improvement").equals("road") || matcher.group("improvement").equals("railroad")) {
                if (canRepairRoute(matcher.group("improvement"))) {
                    if (unit.hasRemainingMoves()) return repairRoute();
                    else return "unit doesn't have enough moves";
                }
            }
            else {
                try {
                    Improvement improvement = Improvement.valueOf(matcher.group("improvement"));
                    if (canRepairImprovement(improvement)) {
                        if (unit.hasRemainingMoves()) return repairImprovement();
                        else return "unit doesn't have enough moves";
                    }
                }
                catch (Exception e) {
                    GameMenu.noSuchImprovement(); //@amirholmd
                }
            }
        }
        else if (unit.getStatus().equals(UnitStatus.GARRISON)) {
            if (canGarrison()) {
                if (unit.hasRemainingMoves()) return garrison();
                else return "unit doesn't have enough moves";
            }
        }
        else if (unit.getStatus().equals(UnitStatus.CLEAR_LAND)) {
            String clearingTarget = matcher.group("clearable");
            if (unit.getTile().hasClearable(clearingTarget)) {
                if (unit.hasRemainingMoves()) return clearTileFrom(clearingTarget);
                else return "unit doesn't have enough moves";
            }
            else {
                unit.setStatus("active");
                return "tile doesn't have this feature to be cleared";
            }
        }
        else if (unit.getStatus().equals(UnitStatus.HEAL)) {
            unit.setHealth(unit.getHealth() + Unit.getHealRate());
            if (unit.getHealth() >= Unit.MAX_HEALTH) {
                unit.setHealth(Unit.MAX_HEALTH);
                unit.setStatus("active");
            }
        }

        else if (unit.getStatus().equals(UnitStatus.CANCEL_MISSION))
            unit.setStatus("active");

        else if (unit.getStatus().equals(UnitStatus.DO_NOTHING))
            System.out.println("doing nothing");
        else if (matcher.pattern().toString().equals("delete")) return ""; //TODO
        else System.out.println("unit controller, status: " + unit.getStatus());
        return ""; //TODO
    }

    private static Matcher getUnitDecision() {
        Matcher matcher;

        while (true) {
//            String command = GameMenu.nextCommand();

            if (command.equals("back")) {
                matcher = Pattern.compile(command).matcher(command);
                matcher.find();
                return matcher;
            }

            if ((matcher = Commands.getMatcher(command, Commands.MOVE_UNIT)) != null) {
                if (!GameController.invalidPos(Integer.parseInt(matcher.group("x")), Integer.parseInt(matcher.group("y"))))
                    return matcher;
                else
                    GameMenu.indexOutOfArray();
            }

            if ((matcher = Commands.getMatcher(command, Commands.SLEEP_UNIT)) != null)
                return matcher;

            if ((matcher = Commands.getMatcher(command, Commands.WAKE_UNIT)) != null && unit.getStatus().equals(UnitStatus.SLEEP))
                return matcher;

            if ((matcher = Commands.getMatcher(command, Commands.DELETE)) != null)
                return matcher;

            if ((matcher = Commands.getMatcher(command, Commands.ATTACK)) != null) {
                int x = Integer.parseInt(matcher.group("x")), y = Integer.parseInt(matcher.group("y"));
                if (unit.getType().isCivilian())
                    GameMenu.unitIsCivilianError();
                else if (GameController.invalidPos(x, y))
                    GameMenu.indexOutOfArray();
                else if (!unit.getType().isSiege() || unit.getStatus().equals(UnitStatus.SIEGEPREP))
                    return matcher;
                else if (unit.getType().isSiege()) GameMenu.siegeNotPrepared();
            }

            if ((matcher = Commands.getMatcher(command, Commands.ALERT)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.FORTIFY)) != null) {
                if (unit instanceof Military)
                    return matcher;
                else
                    GameMenu.unitIsCivilianError();
            }

            if ((matcher = Commands.getMatcher(command, Commands.HEAL)) != null) {
                if (unit instanceof Military) {
                    if (unit.getHealth() < Unit.MAX_HEALTH)
                        return matcher;
                    else
                        GameMenu.unitHasFullHealth();
                } else
                    GameMenu.unitIsCivilianError();
            }

            if ((matcher = Commands.getMatcher(command, Commands.GARRISON)) != null) {
                if (unit instanceof Military) {
                    if (militaryIsInCityTiles(unit))
                        return matcher;
                    else
                        GameMenu.cantMakeGarrison();
                } else
                    GameMenu.unitIsCivilianError();
            }

            if ((matcher = Commands.getMatcher(command, Commands.SETUP_RANDED)) != null) {
                if (unit instanceof Military) {
                    if (unit.getType().isSiege())
                        return matcher;
                    else
                        GameMenu.unitIsNot("siege");
                } else
                    GameMenu.unitIsCivilianError();
            }

            if ((matcher = Commands.getMatcher(command, Commands.BUILD_IMPROVEMENT)) != null) {
                if (unit.getType().equals(UnitType.WORKER))
                    return matcher;
                GameMenu.unitIsNot("worker");
            }

            if ((matcher = Commands.getMatcher(command, Commands.REPAIR)) != null) {
                if (unit.getType().equals(UnitType.WORKER))
                    return matcher;
                GameMenu.unitIsNot("worker");
            }

            if ((matcher = Commands.getMatcher(command, Commands.DO_NOTHING)) != null)
                return matcher;

            if ((matcher = Commands.getMatcher(command, Commands.FOUND_CITY)) != null) {
                if (unit.getType().equals(UnitType.SETTLER))
                    return matcher;
                GameMenu.unitIsNot("settler");
            }

            if ((matcher = Commands.getMatcher(command, Commands.CANCEL_MISSION)) != null)
                return matcher;

            if ((matcher = Commands.getMatcher(command, Commands.PILLAGE)) != null) {
                if (!unit.getType().isCivilian())
                    return matcher;
                GameMenu.unitIsCivilianError();
            }

            if ((matcher = Commands.getMatcher(command, Commands.CLEAR_LAND)) != null) {
                if (unit.getType().equals(UnitType.WORKER))
                    return matcher;
                GameMenu.unitIsNot("worker");
            }

            System.out.println("unit decision wasn't valid");
        }
    }

    public static boolean militaryIsInCityTiles(Unit unit) {
        for (City city : civilization.getCities())
            for (Tile tile : city.getTiles())
                if (tile.equals(unit.getTile()))
                    return true;
        return false;
    }

    private static String clearTileFrom(String clearingTarget) {
        int turns;
        switch (clearingTarget) {
            case "jungle":
                turns = 6;
                break;
            case "forest":
                turns = 4;
                break;
            default:
                turns = 3;
                break;
        }
        Pair<String, Integer> pair = new Pair<>(clearingTarget, turns);
        unit.getTile().setRemoveInProgress(pair);
        unit.setMovesInTurn(unit.getMP());
        return "";
    }

    private static boolean canGarrison() {
        return unit.getTile().getCity() != null && unit.getTile().getCity().getTiles().get(0).equals(unit.getTile());
    }

    private static String garrison() {
        unit.setStatus("garrison");
        unit.setMovesInTurn(unit.getMP());
        return "";
    }

    private static boolean canPillageRoute(String routeType) {
        return unit.getTile().getRouteInProgress() != null &&
                unit.getTile().getRouteInProgress().getKey().equals(routeType) &&
                unit.getTile().getRouteInProgress().getValue() == 0;
    }

    private static String pillageRoute(String routeType) {
        Pair<String, Integer> pair =
                new Pair<>(unit.getTile().getRouteInProgress().getKey(), -3);
        unit.getTile().setRouteInProgress(pair);
        unit.setMovesInTurn(unit.getMP());
        return "";
    }

    private static boolean canPillageImprovement(Improvement improvement) {
        return unit.getTile().getImprovementInProgress() != null &&
                unit.getTile().getImprovementInProgress().getValue() == 0 &&
                unit.getTile().getImprovementInProgress().getKey().equals(improvement);
    }

    private static String  pillageImprovement() {
        Pair<Improvement, Integer> pair =
                new Pair<>(unit.getTile().getImprovementInProgress().getKey(), -3);
        unit.getTile().setImprovementInProgress(pair);
        unit.setMovesInTurn(unit.getMP());
        return "";
    }

    private static boolean canRepairRoute(String routeType) {
        try {
            return unit.getTile().getRouteInProgress().getKey().equals(routeType) &&
                    unit.getTile().getRouteInProgress().getValue() < 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static String repairRoute() {
        Pair<String, Integer> pair =
                new Pair<>(unit.getTile().getRouteInProgress().getKey(), unit.getTile().getRouteInProgress().getValue() * -1);
        unit.getTile().setRouteInProgress(pair);
        unit.setMovesInTurn(unit.getMP());
        return "";
    }

    private static boolean canRepairImprovement(Improvement improvement) {
        try {
            return unit.getTile().getImprovementInProgress().getKey().equals(improvement) &&
                    unit.getTile().getImprovementInProgress().getValue() < 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static String repairImprovement() {
        Pair<Improvement, Integer> pair =
                new Pair<>(unit.getTile().getImprovementInProgress().getKey(), unit.getTile().getImprovementInProgress().getValue() * -1);
        unit.getTile().setImprovementInProgress(pair);
        unit.setMovesInTurn(unit.getMP());
        return "";
    }

    private static boolean canFoundCityHere() {
        ArrayList<Tile> beginningTiles = new ArrayList<>();
        beginningTiles.add(unit.getTile());
        beginningTiles.addAll(unit.getTile().getNeighbors());
        for (Tile tile : beginningTiles)
            if (tile.getCity() != null) return false;
        return !unit.getTile().getFeature().equals(TerrainFeature.ICE);
    }

    public static String canBuildImprovementHere(Improvement improvement) {
        if (unit.getTile().getCity() == null || !unit.getTile().getCity().getCivilization().equals(unit.getCivilization()))
            if (!civilization.hasReachedTech(improvement.getPrerequisiteTech()))
                return "you haven't reached " + improvement.getPrerequisiteTech() + " yet";

        if (improvement == Improvement.FARM) {
            if (unit.getTile().getFeature() != TerrainFeature.ICE ||
                    unit.getTile().getFeature() != TerrainFeature.FOREST ||
                    unit.getTile().getFeature() != TerrainFeature.JUNGLE ||
                    unit.getTile().getFeature() != TerrainFeature.MARSH)
                return "";
            return "can't build chosen improvement here";
        }

        if (improvement == Improvement.MINE)
            if (unit.getTile().getType() == TerrainType.HILL)
                return "";

        if (unit.getTile().getResource() != null && unit.getTile().getResource().getPrerequisiteImprovement() == improvement) {
            if (!tileIsValidForImprovement(unit.getTile(), improvement))
                return "can't build chosen improvement on this tile";
            return "";
        }

        else return "can't build chosen improvement on this tile";
    }

    public static boolean canBuildRoadHere() {
        return civilization.hasReachedTech(Technology.WHEEL) && !unit.getTile().getFeature().equals(TerrainFeature.ICE);
    }

    public static boolean canBuildRailroadHere() {
        return civilization.hasReachedTech(Technology.RAILROAD);
    }

    private static boolean tileIsValidForImprovement(Tile tile, Improvement improvement) {
        if (tile.getCity() == null || !tile.getCity().getCivilization().equals(civilization)) return false;
        if (improvement.getPrerequisiteTypes() != null)
            for (TerrainType type : improvement.getPrerequisiteTypes())
                if (type.equals(tile.getType())) return true;
        if (improvement.getPrerequisiteFeatures() != null)
            for (TerrainFeature feature : improvement.getPrerequisiteFeatures())
                if (feature.equals(tile.getFeature()))
                    return true;
        return false;
    }

    private static String buildRoute(String routeType) {
        if (tileAlreadyHas(routeType))
            return "tile already has " + routeType;

        else if (routeType.equals("road") && tileAlreadyHas("railroad"))
            return "tile already has railroad";

        Pair<String, Integer> pair = new Pair<>(routeType, calcTurnsFor(routeType));
        unit.getTile().setRouteInProgress(pair);
        unit.setMovesInTurn(unit.getMP());
        GameMenu.workerStated("building" + routeType);
        return "";
    }

    private static boolean tileAlreadyHas(String routeType) {
        try {
            return unit.getTile().getRouteInProgress().getKey().equals(routeType) &&
                    unit.getTile().getRouteInProgress().getValue() <= 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static int calcTurnsFor(String routeType) {
        if (unit.getTile().getRouteInProgress() != null &&
                unit.getTile().getRouteInProgress().getKey().equals(routeType))
            return unit.getTile().getRouteInProgress().getValue();
        return 3;
    }

    private static String buildImprovement(Improvement improvement) {
        if (tileAlreadyHasImprovement(improvement))
            return "this tile already has " + improvement;

        if (tileOutOfCityLimits())
            return "chosen tile is out of city limits";

        Pair<Improvement, Integer> pair = new Pair<>(improvement, calcTurnsForImprovement(improvement));
        unit.getTile().setImprovementInProgress(pair);
        unit.setMovesInTurn(unit.getMP());
        GameMenu.workerStated("building" + improvement.toString());
        return "";
    }

    private static boolean tileAlreadyHasImprovement(Improvement improvement) {
        try {
            return unit.getTile().getImprovementInProgress().getKey().equals(improvement) &&
                    unit.getTile().getImprovementInProgress().getValue() <= 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean tileOutOfCityLimits() {
        return unit.getTile().getCity() == null || !unit.getTile().getCity().getCivilization().equals(civilization);
    }

    private static int calcTurnsForImprovement(Improvement improvement) {
        if (unit.getTile().getImprovementInProgress() != null &&
                unit.getTile().getImprovementInProgress().getKey().equals(improvement))
            return unit.getTile().getImprovementInProgress().getValue();
        int additionalTurn = 0;
        if (improvement.equals(Improvement.FARM) || improvement.equals(Improvement.MINE)) {
            if (unit.getTile().getFeature().equals(TerrainFeature.FOREST)) additionalTurn = 4;
            else if (unit.getTile().getFeature().equals(TerrainFeature.JUNGLE)) additionalTurn = 7;
            else if (unit.getTile().getFeature().equals(TerrainFeature.MARSH)) additionalTurn = 6;
        }
        return improvement.getConstructionTime() + additionalTurn;
    }

    private static String moveUnit(int destIndexI, int destIndexJ) {

        if (tileIsImpassable(Game.getInstance().getTiles()[destIndexI][destIndexJ], unit))
            return "can't walk on that tile";

        Path chosenPath = findBestPath(destIndexI, destIndexJ);

        if (chosenPath == null) return "";
        moveOnPath(unit, chosenPath);

        if (chosenPath.tiles.size() > 0) unit.setPath(chosenPath);
        return "";
    }

    private static void continuePath() {
        Tile destTile = unit.getPath().tiles.get(unit.getPath().tiles.size() - 1);
        for (Tile unitNeighbor : unit.getTile().getNeighbors())
            if (unitNeighbor.equals(destTile)) {
                if (!unit.getType().isCivilian()) {
                    unitNeighbor.setMilitary((Military) unit);
                    unit.getTile().setMilitary(null);
                } else {
                    unitNeighbor.setCivilian(unit);
                    unit.getTile().setCivilian(null);
                }
                changeTileVisionStatus(unit.getTile(), TileStatus.DISCOVERED);
                unit.updateMovesInTurn(unitNeighbor);
                unit.setTile(unitNeighbor);
                changeTileVisionStatus(unit.getTile(), TileStatus.CLEAR);
                unit.setPath(null);
                unit.setStatus("active");
                return;
            }
        unit.setPath(findBestPath(destTile.getIndexInMapI(), destTile.getIndexInMapJ()));
        if (unit.getPath() == null) {
            unit.setStatus("active");
            return;
        }
        moveOnPath(unit, unit.getPath());
        if (unit.getPath().tiles.size() == 0) unit.setStatus("active");
    }

    private static Path findBestPath(int destIndexI, int destIndexJ) {
        ArrayList<Path> paths = new ArrayList<>();
        generateFirstPaths(paths, unit.getTile());
        for (Path path : paths)
            if (path.tiles.get(0).equals(Game.getInstance().getTiles()[destIndexI][destIndexJ]))
                return path;
        while (paths.size() > 0) {
            Path path = paths.get(0);
            Tile lastTile = path.tiles.get(path.tiles.size() - 1);
            for (Tile neighborTile : lastTile.getNeighbors()) {
                if (tileIsImpassable(neighborTile, unit)) continue;
                boolean isRouteRepetitive = false;
                for (Tile oneOfPreviousTiles : path.tiles) {
                    if (oneOfPreviousTiles.equals(lastTile)) break;
                    if (areNeighbors(neighborTile, oneOfPreviousTiles)) {
                        isRouteRepetitive = true;
                        break;
                    }
                }
                if (isRouteRepetitive) continue;
                Path child = new Path(path);
                child.tiles.add(neighborTile);
                if (neighborTile.equals(Game.getInstance().getTiles()[destIndexI][destIndexJ]))
                    return child;
                paths.add(child);
            }
            paths.remove(path);
        }
        return null;
    }

    public static void moveOnPath(Unit unit, Path chosenPath) {
        while (unit.getMovesInTurn() < unit.getMP() && chosenPath.tiles.size() > 0) {
            if (!unit.getType().isCivilian()) {
                chosenPath.tiles.get(0).setMilitary((Military) unit);
                unit.getTile().setMilitary(null);
            } else {
                chosenPath.tiles.get(0).setCivilian(unit);
                unit.getTile().setCivilian(null);
            }
            if (unit.equals(UnitController.unit)) changeTileVisionStatus(unit.getTile(), TileStatus.DISCOVERED);
            unit.updateMovesInTurn(chosenPath.tiles.get(0));
            unit.setTile(chosenPath.tiles.get(0));
            if (unit.equals(UnitController.unit)) changeTileVisionStatus(unit.getTile(), TileStatus.CLEAR);
            chosenPath.tiles.remove(0);
            if (unit.getTile().isRuined())
                handleRuinTile();
        }
        if (chosenPath.tiles.size() > 0) {
            unit.setStatus("has path");
            unit.setPath(chosenPath);
        }
        else {
            if (unit.getTile().isRuined())
                handleRuinTile();
            unit.setStatus("active");
        }
    }

    private static void handleRuinTile() {
        unit.getTile().setRuined(false);
        int rand = new Random().nextInt(5);
        if ((civilization.getCities().size() == 0 && (rand == 1 || rand == 3)) || (unit.getType().isCivilian() && rand == 4))
            rand = 0;
        Label label = new Label();
        label.setStyle("-fx-font-family: 'Tw Cen MT'; -fx-font-size: 35; -fx-font-weight: bold; -fx-alignment: center;" +
                "-fx-text-alignment: center; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-color: white;" +
                "-fx-background-radius: 5");
        switch (rand) {
            case 0:
                civilization.setTotalGold(unit.getCivilization().getTotalGold() + 20);
                label.setTextFill(Color.rgb(194,142,1,1));
                label.setText("You Found 20 Golds in the Ruins!");
                break;
            case 1:
                Technology discover = Technology.AGRICULTURE;
                for (Technology tech : Technology.values())
                    if (!civilization.hasReachedTech(tech) && civilization.hasPrerequisitesOf(tech)) {
                        discover = tech;
                        civilization.getLastCostUntilNewTechnologies().put(tech, 0);
                        break;
                    }
                if (discover.equals(Technology.AGRICULTURE))
                    return;
                label.setTextFill(Color.rgb(5,5,150,1));
                label.setText("You Discovered " + discover + " Technology in the Ruins!");
                break;
            case 2:
                int counter = 10;
                for (int i = 0; i < Game.getInstance().getMapSize(); i++) {
                    for (int j = 0; j < Game.getInstance().getMapSize(); j++)
                        if (civilization.getTileVisionStatuses()[i][j].equals(TileStatus.FOGGY)) {
                            civilization.getTileVisionStatuses()[i][j] = TileStatus.DISCOVERED;
                            counter--;
                            if (counter == 0) break;
                        }
                    if (counter == 0) break;
                }
                label.setTextFill(Color.rgb(0,0,0,1));
                label.setText("You Unlocked New Tiles' Visibility in the Ruins!");
                break;
            case 3:
                ArrayList <City> allCities = new ArrayList<>(civilization.getCities());
                Comparator <City> cmp = Comparator.comparing(City::getCitizensNumber).reversed();
                allCities.sort(cmp);
                City city = allCities.get(0);
                for (int i = 0; i < 2; i++) {
                    Citizen citizen = new Citizen(city, null);
                    city.getCitizens().add(citizen);
                }
                city.setCitizenNecessityFood((int) (city.getCitizenNecessityFood() * 2.25));
                city.setGainCitizenLastFood(city.getCitizenNecessityFood());
                civilization.getNotifications().add("The " + city.getName() + "'s population is increased (from ruins)     time: " + Game.getInstance().getTime());
                label.setTextFill(Color.rgb(0,100,0,1));
                label.setText("You Found Survivors in the Ruins!");
                break;
            case 4:
                if (unit.getType().isCivilian()) break;
                Unit settler = new Unit(UnitType.SETTLER);
                settler.setCivilization(civilization);
                civilization.getUnits().add(settler);
                settler.setTile(unit.getTile());
                unit.getTile().setCivilian(settler);
                label.setTextFill(civilization.getColor());
                label.setText("You Found a Settler Unit in the Ruins!");
                break;
        }
        VBox vBox = new VBox();
        vBox.getChildren().add(label);
        Popup popup = new Popup();
        popup.getContent().add(vBox);
        popup.setAutoHide(true);
        popup.show(GameMenu.getGameMapController().getBackgroundPane().getScene().getWindow());
    }

    private static void changeTileVisionStatus(Tile tile, TileStatus newStatus) {
        ArrayList<Tile> neighbors = tile.getNeighbors();
        neighbors.add(tile);
        for (Tile neighbor : neighbors)
            civilization.getTileVisionStatuses()[neighbor.getIndexInMapI()][neighbor.getIndexInMapJ()] = newStatus;
    }

    public static boolean areNeighbors(Tile first, Tile second) {
        ArrayList<Tile> neighborsOfFirst = first.getNeighbors();
        for (Tile tile : neighborsOfFirst)
            if (tile.equals(second)) return true;
        return false;
    }

    public static boolean tileIsImpassable(Tile tile, Unit unit) {
        if (unit != null) {
            if ((tile.getMilitary() != null && !tile.getMilitary().getCivilization().equals(unit.getCivilization())) ||
                    (tile.getCivilian() != null && !tile.getCivilian().getCivilization().equals(unit.getCivilization())))
                return true;
            if (tile.getMilitary() != null && !unit.getType().isCivilian()) return true;
            if (tile.getCivilian() != null && unit.getType().isCivilian()) return true;
        }
        return tile.getType().equals(TerrainType.OCEAN) ||
                tile.getType().equals(TerrainType.MOUNTAIN) ||
                tile.getFeature().equals(TerrainFeature.ICE);
    }

    private static void generateFirstPaths(ArrayList<Path> paths, Tile startingTile) {
        int indexI = startingTile.getIndexInMapI(), indexJ = startingTile.getIndexInMapJ();

        for (int i = indexI - 1; i <= indexI + 1; i += 2) {
            if (GameController.invalidPos(i, indexJ)) continue;
            Path path = new Path(null);
            path.tiles.add(Game.getInstance().getTiles()[i][indexJ]);
            paths.add(path);
        }

        for (int j = indexJ - 1; j <= indexJ + 1; j += 2) {
            if (GameController.invalidPos(indexI, j)) continue;
            Path path = new Path(null);
            path.tiles.add(Game.getInstance().getTiles()[indexI][j]);
            paths.add(path);
        }

        if (indexI % 2 == 0) indexJ--;
        else indexJ++;

        for (int i = indexI - 1; i <= indexI + 1; i += 2) {
            if (GameController.invalidPos(i, indexJ)) continue;
            Path path = new Path(null);
            path.tiles.add(Game.getInstance().getTiles()[i][indexJ]);
            paths.add(path);
        }

        paths.removeIf(path -> tileIsImpassable(path.tiles.get(0), unit));
    }

    public static void doRemainingMissions() {
        if (unit.getStatus().equals(UnitStatus.HAS_PATH)) {
            continuePath();
            return;
        }
        if (unit.getStatus().equals(UnitStatus.HEAL) && unit.getHealth() < 20) {
            unit.setHealth(unit.getHealth() + Unit.getHealRate());
            if (unit.getHealth() >= Unit.MAX_HEALTH) {
                unit.setHealth(Unit.MAX_HEALTH);
                unit.setStatus("active");
            }
            return;
        }
        if (unit.getStatus().equals(UnitStatus.SLEEP) || unit.getStatus().equals(UnitStatus.FORTIFY) ||
                unit.getStatus().equals(UnitStatus.GARRISON) || unit.getStatus().equals(UnitStatus.SIEGEPREP))
            return;
        if (unit.getStatus().equals(UnitStatus.ALERT) && noEnemyNearby())
            return;
        if (unit.getStatus().equals(UnitStatus.REPAIR) || unit.getStatus().equals(UnitStatus.BUILD_IMPROVEMENT) ||
                unit.getStatus().equals(UnitStatus.CLEAR_LAND)) return;
        unit.setStatus("active");
    }

    private static boolean noEnemyNearby() {
        for (Tile neighbor : unit.getTile().getNeighbors())
            if (neighbor.getMilitary() != null && !neighbor.getMilitary().getCivilization().equals(civilization))
                return false;
        return true;
    }

    private static String foundCity() {
        Popup popup = new Popup();
        TextField nameField = new TextField();
        nameField.setPromptText("Enter city's name:");
        nameField.setStyle("-fx-font-family: 'Tw Cen MT'; -fx-font-size: 30;" +
                "-fx-background-color: rgba(255,255,255,0.5); -fx-background-radius: 10;" +
                "-fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 10;");
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 5) {
                String copy = nameField.getText().substring(0, 5);
                nameField.setText(copy);
            }
        });
        nameField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                //TODO
                if (!popup.isShowing())
                    popup.show(Main.stage);
                else if (!cityNameAlreadyExists(nameField.getText())) {
                    popup.hide();
                    new City(civilization, unit.getTile(), nameField.getText());
                    unit.kill();
                    GameMenu.getGameMapController().setChosenUnit(null);
                    GameMenu.getGameMapController().showMap();
                }
                else
                    nameField.setStyle("-fx-font-family: 'Tw Cen MT'; -fx-font-size: 30;" +
                            "-fx-border-color: red; -fx-border-width: 3; -fx-border-radius: 10;" +
                            "-fx-background-color: rgba(255,255,255,0.5); -fx-background-radius: 10;");
            }
        });
        popup.getContent().add(nameField);
        popup.show(Main.stage);
        return "";
    }

    private static boolean cityNameAlreadyExists(String cityName) {
        for (User player : Game.getInstance().getPlayers())
            for (City city : player.getCivilization().getCities())
                if (city.getName().equals(cityName)) return true;
        return false;
    }

    private static boolean canAttackTo(Tile tile) {
        if (tile.getCity() != null && tile.isCenterOfCity(tile.getCity()) &&
                !tile.getCity().getCivilization().equals(civilization))
            return true;
        return (tile.getMilitary() != null || tile.getCivilian() != null) &&
                !tile.getMilitary().getCivilization().equals(civilization);
    }

    private static String attack(Tile targetTile) {
        if (targetTile.isCenterOfCity(targetTile.getCity())) {
            if (unit.getType().isRangeCombat())
                return rangedAttackToCity(targetTile.getCity());
            if (unit.getType().isMeleeCombat())
                return meleeAttackToCity(targetTile.getCity());
        }
        else {
            if (unit.getType().isRangeCombat())
                return rangedAttackToUnit(targetTile);
            if (unit.getType().isMeleeCombat())
                return meleeAttackToUnit(targetTile);
        }
        return "couldn't handle command";
    }

    private static String rangedAttackToUnit(Tile targetTile) {
        if (targetTile.getMilitary() == null) {
            Civilization civilization = targetTile.getCivilian().getCivilization();
            targetTile.getCivilian().kill();
            checkIfDefeated(civilization);
            return "done";
        }
        Civilization civilization = targetTile.getMilitary().getCivilization();
        Military me = (Military)unit;
        targetTile.getMilitary().setHealth(targetTile.getMilitary().getHealth() - me.getRangedCombatStrength() / 4);
        if (targetTile.getMilitary().getHealth() <= 0){
            targetTile.getMilitary().kill();
            checkIfDefeated(civilization);
        }
        unit.setMovesInTurn(unit.getMP());
        unit.setStatus("active");
        return "done";
    }

    private static String rangedAttackToCity(City city) {
        if (cityIsOutOfRange(city)) {
            return GameMenu.cityOutOfUnitRange();
        }
        Military military = (Military) unit;
        System.out.println(unit.getStatus());
        System.out.println(unit.getType());
        city.setHP(city.getHP() - military.getRangedCombatStrength() / 4);
        if (city.getHP() < 0) city.setHP(0);
        GameMenu.rangedAttackToCitySuccessfully(city);
        unit.setMovesInTurn(unit.getMP());
        unit.setStatus("active");
        return "done";
    }

    private static boolean cityIsOutOfRange(City city) {
        ArrayList<Tile> unitInRangeTiles = new ArrayList<>(unit.getTile().getNeighbors());
        int beginIndex = 0;
        for (int i = 0; i < unit.getType().getRange(); i++) {
            for (Tile tile : unitInRangeTiles)
                if (tile.isCenterOfCity(city))
                    return false;
            int sizeHolder = unitInRangeTiles.size();
            for (int j = beginIndex; j < sizeHolder; j++)
                unitInRangeTiles.addAll(unitInRangeTiles.get(j).getNeighbors());
            beginIndex = sizeHolder;
        }
        for (Tile tile : unitInRangeTiles)
            if (tile.isCenterOfCity(city))
                return false;
        return true;
    }

    private static String meleeAttackToCity(City city) {
        unit.setHealth(unit.getHealth() - city.getCombatStrength() / 4);
        city.setHP(city.getHP() - ((Military) unit).getCombatStrength());
        unit.setMovesInTurn(unit.getMP());
        unit.setStatus("active");
        if (unit.getHealth() <= 0) unit.kill();
        if (city.getHP() <= 0) {
//            CivilizationController.enterCityAsConqueror(city);
            return GameMenu.cityHPIsZero(city);
        }
        return "done";
    }

    private static String meleeAttackToUnit (Tile targetTile) {
        if (targetTile.getMilitary() == null) {
        Civilization civilization = targetTile.getCivilian().getCivilization();
            targetTile.getCivilian().setCivilization(unit.getCivilization());
            unit.getCivilization().addUnit(targetTile.getCivilian());
            checkIfDefeated(civilization);
            return "done";
        }
        Civilization civilization = targetTile.getMilitary().getCivilization();
        Military enemy = targetTile.getMilitary();
        Military me = (Military)unit;

        me.setHealth(me.getHealth() - enemy.getCombatStrength() / 4);
        enemy.setHealth(enemy.getHealth() - me.getCombatStrength() / 4);
        unit.setMovesInTurn(unit.getMP());
        unit.setStatus("active");
        if (enemy.getHealth() <= 0) {
            int i = enemy.getTile().getIndexInMapI(), j = enemy.getTile().getIndexInMapJ();
            enemy.kill();
            me.setMovesInTurn(0);
            moveUnit(i, j);
            me.setMovesInTurn(me.getMP());
            checkIfDefeated(civilization);
        }

        if (me.getHealth() <= 0)
            me.kill();
        return "done";
    }

    private static void checkIfDefeated(Civilization civilization) {
        if(civilization.getCities().size() ==0 && civilization.getUnits().size() == 0){
            Game.getInstance().getPlayers().removeIf(player -> player.getCivilization().equals(civilization));
        }
        for (User player : Game.getInstance().getPlayers()) {
            //TODO az ehsan pull konam civ hazf shode ro az list enemy civ ha pak konam
        }
        if(Game.getInstance().getPlayers().size() == 1){
            GameMenu.getGameMapController().setEnded(true);
            GameMenu.getGameMapController().showScores();
        }
    }
}
