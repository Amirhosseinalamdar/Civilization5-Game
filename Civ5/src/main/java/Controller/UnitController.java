package Controller;

import Model.Civilization;
import Model.Game;
import Model.Map.*;
import Model.TileStatus;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitStatus;
import Model.UnitPackage.UnitType;
import Model.User;
import View.GameMenu;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnitController {
    private static Civilization civilization;
    private static Unit unit;

    public static void changeCivilization(Civilization civilization) {
        UnitController.civilization = civilization;
    }

    public static void setUnit(Unit unit) {
        UnitController.unit = unit;
    }

    public static void handleUnitOptions() {
        Matcher matcher = getUnitDecision();
        if (!matcher.find()) throw new RuntimeException();
        unit.setStatus(matcher.pattern().toString());
        //TODO switch case and call the related func
        if (unit.getStatus().equals(UnitStatus.MOVE)) {
            int destCenterX = Integer.parseInt(matcher.group("x")), destCenterY = Integer.parseInt(matcher.group("y"));
            if (isTileEmpty(destCenterX, destCenterY)) {
                if (unit.getMovesInTurn() < unit.getMP()) moveUnit(destCenterX, destCenterY);
                else GameMenu.notEnoughMoves();
                return;
            }
            GameMenu.unavailableTile();
        }
        else if (unit.getStatus().equals(UnitStatus.FOUND_CITY)) {
            if (canFoundCityHere()) {
                if (unit.getMovesInTurn() < unit.getMP()) foundCity();
                else GameMenu.notEnoughMoves();
                return;
            }
            GameMenu.cantFoundCityHere();
        }
        else if (unit.getStatus().equals(UnitStatus.BUILD_IMPROVEMENT)) {
            try {
                Improvement improvement = Improvement.valueOf(matcher.group("improvement"));
                if (canBuildImprovementHere(improvement)) {
                    if (unit.getMovesInTurn() < unit.getMP()) buildImprovement(improvement);
                    else GameMenu.notEnoughMoves();
                }
            }
            catch (Exception e) {
                GameMenu.noSuchImprovement();
            }
        }
        else if (unit.getStatus().equals(UnitStatus.DO_NOTHING))
            System.out.println("unit controller, invalid command"); //TODO... add else_if for other statuses
    }

    private static Matcher getUnitDecision() {

        String regex;

        while (true) {
            String command = GameMenu.nextCommand();
            regex = "move to (--coordinates|-c) (?<x>\\d+) (?<y>\\d+)";
            if (command.matches(regex)) {
                Matcher matcher = Pattern.compile(regex).matcher(command);
                if (!matcher.find()) throw new RuntimeException();
                if (!GameController.invalidPos(Integer.parseInt(matcher.group("x")), Integer.parseInt(matcher.group("y"))))
                    return Pattern.compile(regex).matcher(command);
                else
                    GameMenu.indexOutOfArray();
            }

            if (command.equals("sleep"))
                return Pattern.compile("sleep").matcher(command);

            if (command.equals("wake") && unit.getStatus().equals(UnitStatus.SLEEP))
                return Pattern.compile("wake").matcher(command);

            if (command.equals("delete"))
                return Pattern.compile("delete").matcher(command);

            regex = "attack to (-c|--coordinates) (?<x>\\d+) (?<y>\\d+)";
            if (command.matches(regex)) {
                Matcher matcher = Pattern.compile(regex).matcher(command);
                if (!matcher.find()) throw new RuntimeException();
                int x = Integer.parseInt(matcher.group("x")), y = Integer.parseInt(matcher.group("y"));
                if (!(unit instanceof Military))
                    GameMenu.unitIsCivilianError();
                else if (GameController.invalidPos(x, y))
                    GameMenu.indexOutOfArray();
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
                } else
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
                } else
                    GameMenu.unitIsCivilianError();
            }

            regex = "build improvement (-t|--type) (?<improvement>\\S+)";
            if (command.matches(regex)) {
                Matcher matcher = Pattern.compile(regex).matcher(command);
                if (!matcher.find()) throw new RuntimeException();
                if (unit.getType().equals(UnitType.WORKER))
                    return Pattern.compile(regex).matcher(command);
                GameMenu.unitIsNotWorker();
            }

            regex = "remove (?<resource>(jungle|route))";
            if (command.matches(regex)) {
                Matcher matcher = Pattern.compile(regex).matcher(command);
                if (!matcher.find()) throw new RuntimeException();
                if (unit.getType().equals(UnitType.WORKER))
                    return Pattern.compile(regex).matcher(command);
                GameMenu.unitIsNotWorker();
            }

            if (command.equals("repair")) {
                if (unit.getType().equals(UnitType.WORKER))
                    return Pattern.compile(command).matcher(command);
                GameMenu.unitIsNotWorker();
            }

            if (command.equals("do nothing"))
                return Pattern.compile(command).matcher(command);

            if (command.equals("found city")) {
                if (unit.getType().equals(UnitType.SETTLER))
                    return Pattern.compile(command).matcher(command);
                else
                    GameMenu.unitIsNotSettler();
            }

            System.out.println("unit decision wasn't valid");
        }
    }

    private static boolean militaryIsInCityTiles() {
        for (City city : civilization.getCities())
            for (Tile tile : city.getTiles())
                if (tile.equals(unit.getTile()))
                    return true;
        return false;
    }

    private static boolean isTileEmpty(int centerX, int centerY) {
        if (unit instanceof Military) return Game.getTiles()[centerX][centerY].getMilitary() == null;
        else return Game.getTiles()[centerX][centerY].getCivilian() == null;
    }

    private static boolean canFoundCityHere() {
        ArrayList<Tile> beginningTiles = new ArrayList<>();
        beginningTiles.add(unit.getTile());
        beginningTiles.addAll(GameController.getTileNeighbors(unit.getTile()));
        for (Tile tile : beginningTiles)
            if (tile.getCity() != null) return false;
        return !unit.getTile().getFeature().equals(TerrainFeature.ICE);
    }

    private static boolean canBuildImprovementHere(Improvement improvement) {
        if (! civilization.hasReachedTech(improvement.getPrerequisiteTech())) {
            GameMenu.unreachedTech();
            return false;
        }
        if (! tileIsValidForImprovement(unit.getTile(), improvement)) {
            GameMenu.cantBuildImprovementOnTile();
            return false;
        }
        return true;
    }

    private static boolean tileIsValidForImprovement (Tile tile, Improvement improvement) {
        if (improvement.getPrerequisiteTypes() != null)
            for (TerrainType type : improvement.getPrerequisiteTypes())
                if (type.equals(tile.getType())) return true;
        if (improvement.getPrerequisiteFeatures() != null)
            for (TerrainFeature feature : improvement.getPrerequisiteFeatures())
                if (feature.equals(tile.getFeature()))
                    return true;
        return false;
    }

    private static void buildImprovement (Improvement improvement) {
        if (!unit.getTile().getResource().getPrerequisiteImprovement().equals(improvement)) return;
        Pair <Improvement, Integer> pair = new Pair<Improvement, Integer>(improvement, calcTurnsForImprovement(improvement));
        unit.getTile().setImprovementInProgress(pair);
    }

    private static int calcTurnsForImprovement (Improvement improvement) {
        return 1; //TODO
    }

    private static void moveUnit(int destIndexI, int destIndexJ) {

        if (!isTileWalkable(Game.getTiles()[destIndexI][destIndexJ], unit)) {
            System.out.println("can't walk on that tile"); //TODO... non walkable tile
            return;
        }
        Path chosenPath = findBestPath(destIndexI, destIndexJ);

        if (chosenPath == null) return;
        moveOnPath(chosenPath);

        if (chosenPath.tiles.size() > 0) unit.setPath(chosenPath);
    }

    private static void continuePath() {
        Tile destTile = unit.getPath().tiles.get(unit.getPath().tiles.size() - 1);
        unit.setPath(findBestPath(destTile.getIndexInMapI(), destTile.getIndexInMapJ()));
        if (unit.getPath() == null) {
            unit.setStatus("active");
            return;
        }
        moveOnPath(unit.getPath());
        if (unit.getPath().tiles.size() == 0) unit.setStatus("active");
    }

    private static Path findBestPath(int destIndexI, int destIndexJ) {
        ArrayList<Path> paths = new ArrayList<>();
        generateFirstPaths(paths, unit.getTile());
        for (Path path : paths)
            if (path.tiles.get(0).equals(Game.getTiles()[destIndexI][destIndexJ]))
                return path;
        while (paths.size() > 0) {
            Path path = paths.get(0);
            Tile lastTile = path.tiles.get(path.tiles.size() - 1);
            for (Tile neighborTile : GameController.getTileNeighbors(lastTile)) {
                if (!isTileWalkable(neighborTile, unit)) continue;
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

    public static void moveOnPath(Path chosenPath) {
        while (unit.getMovesInTurn() < unit.getMP() && chosenPath.tiles.size() > 0) {
            if (unit instanceof Military) {
                chosenPath.tiles.get(0).setMilitary((Military) unit);
                unit.getTile().setMilitary(null);
            } else {
                chosenPath.tiles.get(0).setCivilian(unit);
                unit.getTile().setCivilian(null);
            }
            //changeTileStatus(unit.getTile(), TileStatus.DISCOVERED);
            unit.calcMovesTo(chosenPath.tiles.get(0));
            unit.setTile(chosenPath.tiles.get(0));
            changeTileStatus(unit.getTile(), TileStatus.CLEAR);
            chosenPath.tiles.remove(0);
        }
        if (chosenPath.tiles.size() > 0) unit.setStatus("has path");
        else unit.setStatus("active");
    }

    private static void changeTileStatus(Tile tile, TileStatus newStatus) {
        ArrayList<Tile> neighbors = GameController.getTileNeighbors(tile);
        neighbors.add(tile);
        for (Tile neighbor : neighbors)
            civilization.getTileVisionStatuses()[neighbor.getIndexInMapI()][neighbor.getIndexInMapJ()] = newStatus;
    }

    public static boolean areNeighbors(Tile first, Tile second) {
        ArrayList<Tile> neighborsOfFirst = GameController.getTileNeighbors(first);
        for (Tile tile : neighborsOfFirst)
            if (tile.equals(second)) return true;
        return false;
    }

    public static boolean isTileWalkable(Tile tile, Unit unit) {
        if (unit != null) {
            if (unit instanceof Military && tile.getMilitary() != null) return false;
            if (!(unit instanceof Military) && tile.getCivilian() != null) return false;
        }
        return !tile.getType().equals(TerrainType.OCEAN) &&
                !tile.getType().equals(TerrainType.MOUNTAIN);
    }

    private static void generateFirstPaths(ArrayList<Path> paths, Tile startingTile) {
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

        paths.removeIf(path -> !isTileWalkable(path.tiles.get(0), unit));
    }

    public static void doRemainingMissions() {
        if (unit.getStatus().equals(UnitStatus.HAS_PATH))
            continuePath();
        if (unit.getStatus().equals(UnitStatus.SLEEP) || unit.getStatus().equals(UnitStatus.FORTIFY)) //TODO fortify heal
            return;
        unit.setStatus("active");
    }

    private static void sleepUnit() {

    }

    private static void alertMilitary() {

    }

    private static void boostMilitary() {

    }

    private static void healUnit() {

    }

    private static void settleMilitary() {

    }//estehkam va hoshdar safhe 26 doc

    private static void siegePrep() {

    }

    private static void rangedAttack() {//may attack to a city or a military or civilian
        Tile tile = GameMenu.showRangedAttackOptions((Military) unit);
        if (tile == null)
            ; //ke hichi// ya cancle mikone ya eshteba mizane//age eshteba zad while beznim ta odorst bezane
        //ya rangedAttackToUnit
        //ya rangedAttackToCity
        //ya attackCivilian ????//TODO
    }

    private static void raid() {

    }

    private static void foundCity() {
        System.out.println("please choose name: "); //TODO... move it to menu :)
        String cityName = GameMenu.nextCommand();
        while (cityNameAlreadyExists(cityName)) {
            GameMenu.cityNameAlreadyExists();
            cityName = GameMenu.nextCommand();
        }
        new City(civilization, unit.getTile(), cityName);
        unit.kill();
    }

    private static boolean cityNameAlreadyExists (String cityName) {
        for (User player : Game.getPlayers())
            for (City city : player.getCivilization().getCities())
                if (city.getName().equals(cityName)) return true;
        return false;
    }

    private static void abortMission() {

    }

    private static void wakeUnit() {

    }

    private static void dissolveUnit() {//monhal kardan

    }


    private static void attack(Unit defendingUnit) {//should call by move
        //TODO if defender is civilian
        //TODO call ranged attack to city or unit
        //TODO call close attack to city or unit
    }

    private static void rangedAttackToUnit(Unit defendingUnit) {

    }

    private static void rangedAttackToCity(City city) {

    }

    private static void closeAttackToCity(City city) {

    }

    private static void closeAttackToUnit(Unit defendingUnit) {

    }


}
