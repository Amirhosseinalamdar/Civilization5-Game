package Controller;

import Model.Civilization;
import Model.Game;
import Model.Map.City;
import Model.Map.Route;
import Model.Map.Tile;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitStatus;
import View.GameMenu;

import java.util.ArrayList;

public class UnitController{
    private static Civilization civilization;
    private static Unit unit;

    public static void changeCivilization(Civilization civilization){
        UnitController.civilization = civilization;
    }

    public static void handleUnitOption (Unit unit) {
        UnitController.unit = unit;
        unit.setStatus(chooseUnitOption());
        //TODO switch case and call the related func
        if (unit.getStatus().equals(UnitStatus.ACTIVE)) {
            String[] args = GameMenu.nextCommand().split(" ");
            int destCenterX = Integer.parseInt(args[0]), destCenterY = Integer.parseInt(args[1]);
            if (isTileEmpty(destCenterX, destCenterY)) {
                moveUnit(destCenterX, destCenterY);
                return;
            }
            GameMenu.unavailableTile();
        }
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
        ArrayList <Route> routes = new ArrayList<>();
        generateFirstRoutes(routes, unit.getTile());
        Route chosenRoute = null;
        for (Route route : routes) {
            Tile lastTile = route.tiles.get(route.tiles.size() - 1);
            if (lastTile.equals(Game.getTiles()[destCenterX][destCenterY])) {
                chosenRoute = route;
                break;
            }
            for (Tile neighborTile : getTileNeighbors(lastTile)) {
                if (! isTileWalkable(neighborTile)) continue;
                boolean isRouteRepetitive = false;
                for (Tile previous : route.tiles) {
                    if (previous.equals(lastTile)) break;
                    if (areNeighbors(neighborTile, previous)) {
                        isRouteRepetitive = true;
                        break;
                    }
                }
                if (isRouteRepetitive) continue;
                Route child = new Route(route);
                child.tiles.add(neighborTile);
                routes.add(child);
            }
            routes.remove(route);
        }
        unit.setRoute(chosenRoute);
    }

    private static boolean areNeighbors (Tile first, Tile second) {
        if (Math.abs(first.getCenterX() - second.getCenterX()) > 2) return false;
        return Math.abs(first.getCenterY() - second.getCenterY()) <= 1;
    }

    private static boolean isTileWalkable (Tile tile) {
        return true; //TODO... check tile features, then judge
    }

    private static void generateFirstRoutes (ArrayList <Route> routes, Tile startingTile) {
        int centerX = startingTile.getCenterX(), centerY = startingTile.getCenterY();
        for (int i = centerX - 2; i <= centerX + 2; i += 2) {
            if (i < 0 || i > 19) continue;
            for (int j = centerY - 1; j <= centerY + 1; j++) {
                if (j < 0 || j > 19 || (j == centerY && i == centerX)) continue;
                routes.get(routes.size() - 1).tiles.add(Game.getTiles()[i][j]);
            }
        }
    }

    private static ArrayList <Tile> getTileNeighbors (Tile tile) {
        return new ArrayList<>();
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
