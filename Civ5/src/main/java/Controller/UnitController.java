package Controller;

import Model.Civilization;
import Model.Game;
import Model.Map.City;
import Model.Map.Path;
import Model.Map.TerrainType;
import Model.Map.Tile;
import Model.TileStatus;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitStatus;
import Model.UnitPackage.UnitType;
import View.GameMenu;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnitController{
    private static Civilization civilization;
    private static Unit unit;

    public static void changeCivilization(Civilization civilization){
        UnitController.civilization = civilization;
    }

    public static void setUnit (Unit unit) {
        UnitController.unit = unit;
    }

    public static void handleUnitOption() {
        Matcher matcher = getUnitDecision();
        unit.setStatus(matcher.pattern().toString());
        //TODO switch case and call the related func
        System.out.println("status = " + unit.getStatus().toString());
        if (unit.getStatus().equals(UnitStatus.MOVE)) {
            if (! matcher.find()) throw new RuntimeException();
            int destCenterX = Integer.parseInt(matcher.group("x")), destCenterY = Integer.parseInt(matcher.group("y"));
            if (isTileEmpty(destCenterX, destCenterY)) {
                if (unit.getMovesInTurn() < unit.getMP()) {
                    moveUnit(destCenterX, destCenterY);
                    //GameMenu.showMap(civilization);
                }else System.out.println("not enough moves"); //TODO... take it to view :)
                return;
            }
            GameMenu.unavailableTile();
        }
        else System.out.println("unit controller, invalid command");
    }

    private static Matcher getUnitDecision() {
        if (unit.getPath().tiles.size() > 0) return Pattern.compile("has path").matcher("has path");

        String regex;

        while (true) {
            String command = GameMenu.nextCommand();
            regex = "move to (--cordinates|-c) (?<x>\\d+) (?<y>\\d+)";
            if (command.matches(regex)) {
                Matcher matcher = Pattern.compile(regex).matcher(command);
                if (! matcher.find()) throw new RuntimeException();
                if (! GameController.invalidPos(Integer.parseInt(matcher.group("x")), Integer.parseInt(matcher.group("y"))))
                    return Pattern.compile(regex).matcher(command);
                else
                    GameMenu.invalidChosenTile();
            }

            if (command.equals("sleep"))
                return Pattern.compile("sleep").matcher(command);

            if (command.equals("wake") && unit.getStatus().equals(UnitStatus.SLEEP))
                return Pattern.compile("wake").matcher(command);

            if (command.equals("delete"))
                return Pattern.compile("delete").matcher(command);

            regex = "attack (?<x>\\d+) (?<y>\\d+)";
            if (command.matches(regex)) {
                Matcher matcher = Pattern.compile(regex).matcher(command);
                if (!matcher.find()) throw new RuntimeException();
                int x = Integer.parseInt(matcher.group("x")), y = Integer.parseInt(matcher.group("y"));
                if (!(unit instanceof Military))
                    GameMenu.unitIsCivilianError();
                else if (GameController.invalidPos(x, y))
                    GameMenu.invalidChosenTile();
                else if (!unit.isSiege() || unit.getStatus().equals(UnitStatus.SIEGEPREP))
                    return Pattern.compile(regex).matcher(command);
                else
                    GameMenu.siegeNotPrepared();
            }

            if (command.equals("alert") || command.equals("fortify")) {
                if (unit instanceof Military)
                    return Pattern.compile(command).matcher(command);
                else
                    GameMenu.unitIsCivilianError();
            }

            if (command.equals("fortify heal")) {
                if (unit instanceof Military) {
                    if (unit.getHealth() < Unit.MAX_HEALTH)
                        return Pattern.compile(command).matcher(command);
                    else
                        GameMenu.unitHasFullHealth();
                }
                else
                    GameMenu.unitIsCivilianError();
            }

            if (command.equals("garrison")) {
                if (unit instanceof Military) {
                    if (militaryIsInCityTiles())
                        return Pattern.compile(command).matcher(command);
                    else
                        GameMenu.cantMakeGarrison();
                }
                else
                    GameMenu.unitIsCivilianError();
            }

            if (command.equals("setup ranged")) {
                if (unit instanceof Military) {
                    if (unit.isSiege())
                        return Pattern.compile(command).matcher(command);
                    else
                        GameMenu.unitIsNotSiege();
                }
                else
                    GameMenu.unitIsCivilianError();
            }

            regex = "build (?<improvement>\\S+)";
            if (command.matches(regex)) {
                Matcher matcher = Pattern.compile(regex).matcher(command);
                if (! matcher.find()) throw new RuntimeException();
                if (unit.getType().equals(UnitType.WORKER))
                    return Pattern.compile(regex).matcher(command); //TODO check validation of improvements in future
                else
                    GameMenu.unitIsNotWorker();
            }

            regex = "remove (?<resource>(jungle|route))";
            if (command.matches(regex)) {
                Matcher matcher = Pattern.compile(regex).matcher(command);
                if (! matcher.find()) throw new RuntimeException();
                if (unit.getType().equals(UnitType.WORKER))
                    return Pattern.compile(regex).matcher(command);
                else
                    GameMenu.unitIsNotWorker();
            }

            if (command.equals("repair")) {
                if (unit.getType().equals(UnitType.WORKER))
                    return Pattern.compile(command).matcher(command);
                else
                    GameMenu.unitIsNotWorker();
            }

            if (command.equals("do nothing"))
                return Pattern.compile(command).matcher(command);
        }
    }

    private static boolean militaryIsInCityTiles() {
        return true;
    }

    private static boolean isTileEmpty (int centerX, int centerY) {
        if (unit instanceof Military) return Game.getTiles()[centerX][centerY].getMilitary() == null;
        else return Game.getTiles()[centerX][centerY].getCivilian() == null;
    }

    private static void moveUnit (int destCenterX, int destCenterY) {
        System.out.println("dest = " + destCenterX + ", " + destCenterY);
        int destIndexI = destCenterX, destIndexJ = destCenterY;
        //if (destIndexJ % 2 == 0) destIndexI /= 2;
        if (!isTileWalkable(Game.getTiles()[destIndexI][destIndexJ])) {
            System.out.println("can't walk on that tile"); //TODO... non walkable unit
            return;
        }
        Path chosenPath = findBestPath(destIndexI, destIndexJ);

        if (chosenPath == null) return;
        moveOnPath(chosenPath);

        if (chosenPath.tiles.size() > 0) unit.setPath(chosenPath);
        System.out.println("the end");
    }

    private static void continuePath() {
        Tile destTile = unit.getPath().tiles.get(unit.getPath().tiles.size() - 1);
        Path chosenPath = findBestPath(destTile.getIndexInMapI(), destTile.getIndexInMapJ());
        if (chosenPath == null) {
            unit.setStatus("active");
            return;
        }
        moveOnPath(chosenPath);
        if (chosenPath.tiles.size() == 0) unit.setStatus("active");
    }

    private static Path findBestPath (int destIndexI, int destIndexJ) {
        ArrayList <Path> paths = new ArrayList<>();
        generateFirstPaths(paths, unit.getTile());
        while (paths.size() > 0) {
            System.out.println("first loop");
            Path path = paths.get(0);
            Tile lastTile = path.tiles.get(path.tiles.size() - 1);
            for (Tile neighborTile : getTileNeighbors(lastTile)) {
                if (! isTileWalkable(neighborTile)) continue;
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
                if (neighborTile.equals(Game.getTiles()[destIndexI][destIndexJ]))
                    return child;
                paths.add(child);
            }
            paths.remove(path);
        }
        return null;
    }

    public static void moveOnPath (Path chosenPath) {
        while (unit.getMovesInTurn() < unit.getMP() && chosenPath.tiles.size() > 0) {
            System.out.println("second loop");
            if (unit instanceof Military) {
                chosenPath.tiles.get(0).setMilitary((Military) unit);
                unit.getTile().setMilitary(null);
            }
            else {
                chosenPath.tiles.get(0).setCivilian(unit);
                unit.getTile().setCivilian(null);
            }
            //changeTileStatus(unit.getTile(), TileStatus.DISCOVERED);
            unit.calcMovesTo(chosenPath.tiles.get(0));
            unit.setTile(chosenPath.tiles.get(0));
            changeTileStatus(unit.getTile(), TileStatus.CLEAR);
            chosenPath.tiles.remove(0);
        }
    }

    private static void changeTileStatus (Tile tile, TileStatus newStatus) {
        ArrayList <Tile> neighbors = getTileNeighbors(tile);
        for (Tile neighbor : neighbors)
            civilization.getTileVisionStatuses()[neighbor.getIndexInMapI()][neighbor.getIndexInMapJ()] = newStatus;
    }

    private static boolean areNeighbors (Tile first, Tile second) {
        if (Math.abs(first.getCenterX() - second.getCenterX()) > 2) return false;
        return Math.abs(first.getCenterY() - second.getCenterY()) <= 1;
    }

    public static boolean isTileWalkable (Tile tile) {
        return !tile.getType().equals(TerrainType.OCEAN) &&
                !tile.getType().equals(TerrainType.MOUNTAIN);
    }

    private static void generateFirstPaths (ArrayList <Path> paths, Tile startingTile) {
        int indexI = startingTile.getIndexInMapI(), indexJ = startingTile.getIndexInMapJ();

        for (int i = indexI - 1; i <= indexI + 1; i += 2) {
            if (GameController.invalidPos(i, indexJ)) continue;
            Path path = new Path(null);
            path.tiles.add(Game.getTiles()[i][indexJ]);
            paths.add(path);
        }

        for (int j = indexJ - 1; j <= indexJ + 1; j += 2) {
            if (GameController.invalidPos(indexI, j)) continue;
            Path path = new Path(null);
            path.tiles.add(Game.getTiles()[indexI][j]);
            paths.add(path);
        }

        if (indexJ % 2 == 0) indexI--;
        else indexI++;

        for (int j = indexJ - 1; j <= indexJ + 1; j += 2) {
            if (GameController.invalidPos(indexI, j)) continue;
            Path path = new Path(null);
            path.tiles.add(Game.getTiles()[indexI][j]);
            paths.add(path);
        }
    }

    public static ArrayList <Tile> getTileNeighbors (Tile startingTile) {
        ArrayList <Tile> neighbors = new ArrayList<>();
        int indexI = startingTile.getIndexInMapI(), indexJ = startingTile.getIndexInMapJ();

        for (int i = indexI - 1; i <= indexI + 1; i += 2) {
            if (GameController.invalidPos(i, indexJ)) continue;
            neighbors.add(Game.getTiles()[i][indexJ]);
        }

        for (int j = indexJ - 1; j <= indexJ + 1; j += 2) {
            if (GameController.invalidPos(indexI, j)) continue;
            neighbors.add(Game.getTiles()[indexI][j]);
        }

        if (indexJ % 2 == 0) indexI--;
        else indexI++;

        for (int j = indexJ - 1; j <= indexJ + 1; j += 2) {
            if (GameController.invalidPos(indexI, j)) continue;
            neighbors.add(Game.getTiles()[indexI][j]);
        }
        return neighbors;
    }

    public static void doRemainingMissions() {
        if (unit.getStatus().equals(UnitStatus.HAS_PATH)) continuePath();
    }

    private static void sleepUnit(){

    }

    private static void alertMilitary(){

    }

    private static void boostMilitary(){

    }

    private static void healUnit(){

    }

    private static void settleMilitary(){

    }//estehkam va hoshdar safhe 26 doc

    private static void siegePrep(){

    }

    private static void rangedAttack(){//may attack to a city or a military or civilian
        Tile tile = GameMenu.showRangedAttackOptions((Military) unit);
        if (tile == null) ; //ke hichi// ya cancle mikone ya eshteba mizane//age eshteba zad while beznim ta odorst bezane
        //ya rangedAttackToUnit
        //ya rangedAttackToCity
        //ya attackCivilian ????//TODO
    }

    private static void raid(){

    }

    private static void foundCity() {
        //TODO check settler.type bare debug
    }

    private static void abortMission(){

    }

    private static void wakeUnit(){

    }

    private static void dissolveUnit (){//monhal kardan

    }




    private static void attack (Unit defendingUnit){//should call by move
        //TODO if defender is civilian
        //TODO call ranged attack to city or unit
        //TODO call close attack to city or unit
    }

    private static void rangedAttackToUnit(Unit defendingUnit){

    }

    private static void rangedAttackToCity(City city){

    }

    private static void closeAttackToCity(City city){

    }

    private static void closeAttackToUnit(Unit defendingUnit){

    }
}
