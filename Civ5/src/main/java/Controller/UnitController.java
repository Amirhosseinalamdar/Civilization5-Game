package Controller;

import Model.Civilization;
import Model.Game;
import Model.Map.City;
import Model.Map.Path;
import Model.Map.TerrainType;
import Model.Map.Tile;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitStatus;
import Model.UnitPackage.UnitType;
import View.GameMenu;
import sun.security.krb5.internal.PAData;

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
        unit.setStatus(matcher);
        //TODO switch case and call the related func
        if (unit.getStatus().equals(UnitStatus.MOVE)) {
            int destCenterX = Integer.parseInt(matcher.group("x")), destCenterY = Integer.parseInt(matcher.group("y"));
            if (isTileEmpty(destCenterX, destCenterY)) {
                moveUnit(destCenterX, destCenterY);
                return;
            }
            GameMenu.unavailableTile();
        }
    }

    private static Matcher getUnitDecision() {
        String regex;

        while (true) {
            String command = GameMenu.nextCommand();
            regex = "^move to (?<x>\\d+) (?<y>\\d+)$";
            if (command.matches(regex)) {
                Matcher matcher = Pattern.compile(regex).matcher(command);
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

            regex = "^attack (?<x>\\d+) (?<y>\\d+)$";
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

            regex = "^build (?<improvement>\\S+)";
            if (command.matches(regex)) {
                if (unit.getType().equals(UnitType.WORKER))
                    return Pattern.compile(regex).matcher(command); //TODO check validation of improvements in future
                else
                    GameMenu.unitIsNotWorker();
            }

            regex = "^remove (?<resource>(jungle|route))$";
            if (command.matches(regex)) {
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
        }
    }

    private static boolean militaryIsInCityTiles() {
        return true;
    }

    private static boolean isTileEmpty (int centerX, int centerY) {
        if (unit instanceof Military) return Game.getTiles()[centerX][centerY].getMilitary() == null;
        else return Game.getTiles()[centerX][centerY].getCivilian() == null;
    }

    private static UnitStatus chooseUnitOption() {
        GameMenu.showUnitOptions(unit);
        String decision = GameMenu.nextCommand();
        if (decision.equals("move")) return UnitStatus.ACTIVE;
        throw new RuntimeException();
    }

    private static void moveUnit (int destCenterX, int destCenterY) {
        ArrayList <Path> paths = new ArrayList<>();
        generateFirstRoutes(paths, unit.getTile());
        Path chosenPath = null;
        for (Path path : paths) {
            Tile lastTile = path.tiles.get(path.tiles.size() - 1);
            if (lastTile.equals(Game.getTiles()[destCenterX][destCenterY])) {
                chosenPath = path;
                break;
            }
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
                paths.add(child);
            }
            paths.remove(path);
        }
        if (chosenPath == null) return;
        chosenPath.tiles.remove(unit.getTile()); //remove current tile from path
        while (unit.getMovesInTurn() < unit.getMP()) {
            unit.setTile(chosenPath.tiles.get(0));
            if (unit instanceof Military) chosenPath.tiles.get(0).setMilitary((Military) unit);
            else chosenPath.tiles.get(0).setCivilian(unit);
            unit.calcMovesTo(chosenPath.tiles.get(0));
            chosenPath.tiles.remove(0);
        }
    }

    private static boolean areNeighbors (Tile first, Tile second) {
        if (Math.abs(first.getCenterX() - second.getCenterX()) > 2) return false;
        return Math.abs(first.getCenterY() - second.getCenterY()) <= 1;
    }

    private static boolean isTileWalkable (Tile tile) {
        return !tile.getType().equals(TerrainType.OCEAN) &&
                !tile.getType().equals(TerrainType.MOUNTAIN);
    }

    private static void generateFirstRoutes (ArrayList <Path> paths, Tile startingTile) {
        int centerX = startingTile.getCenterX(), centerY = startingTile.getCenterY();
        for (int i = centerX - 2; i <= centerX + 2; i += 2) {
            if (i < 0 || i > 19) continue;
            for (int j = centerY - 1; j <= centerY + 1; j++) {
                if (j < 0 || j > 19 || (j == centerY && i == centerX)) continue;
                paths.get(paths.size() - 1).tiles.add(Game.getTiles()[i][j]);
            }
        }
    }

    private static ArrayList <Tile> getTileNeighbors (Tile tile) {
        ArrayList <Tile> neighbors = new ArrayList<>();
        for (int i = tile.getCenterX() - 2; i <= tile.getCenterX() + 2; i += 2) {
            if (i < 0 || i > 19) continue;
            for (int j = tile.getCenterY() - 1; j <= tile.getCenterY() + 1; j++) {
                if (j < 0 || j > 19) continue;
                neighbors.add(Game.getTiles()[i][j]);
            }
        }
        return neighbors;
    }

    private static ArrayList <Tile> findBestRoute (int myX, int myY, int destX, int destY) {
        return null;
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
