package Controller;

import Model.*;
import Model.Map.*;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitStatus;
import Model.UnitPackage.UnitType;
import View.Commands;
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

        if (matcher.pattern().toString().equals("back")) return;

        unit.setStatus(matcher.pattern().toString());

        if (unit.getStatus().equals(UnitStatus.MOVE)) {
            int destCenterX = Integer.parseInt(matcher.group("x")), destCenterY = Integer.parseInt(matcher.group("y"));

            if (unit.hasRemainingMoves()) moveUnit(destCenterX, destCenterY);
            else GameMenu.notEnoughMoves();
        }

        else if (unit.getStatus().equals(UnitStatus.ATTACK)) {
            int x = Integer.parseInt(matcher.group("x")), y = Integer.parseInt(matcher.group("y"));

            if (unit.hasRemainingMoves()) {
                if (canAttackTo(Game.getTiles()[x][y])) attack(Game.getTiles()[x][y]);
                else GameMenu.invalidTileForAttack();
            }
            else GameMenu.notEnoughMoves();
        }

        else if (unit.getStatus().equals(UnitStatus.FOUND_CITY)) {
            if (canFoundCityHere()) {
                if (unit.hasRemainingMoves()) foundCity();
                else GameMenu.notEnoughMoves();
                return;
            }
            GameMenu.cantFoundCityHere();
        }

        else if (unit.getStatus().equals(UnitStatus.BUILD_IMPROVEMENT)) {
            try {
                Improvement improvement = Improvement.valueOf(matcher.group("improvement"));
                if (canBuildImprovementHere(improvement)) {
                    if (unit.hasRemainingMoves()) buildImprovement(improvement);
                    else GameMenu.notEnoughMoves();
                }
            }
            catch (IllegalArgumentException i) {
                if (matcher.group("improvement").equals("ROAD")) {
                    if (canBuildRoadHere()) {
                        if (unit.hasRemainingMoves()) buildRoute("road");
                        else GameMenu.notEnoughMoves();
                    }
                    else GameMenu.cantBuildRoadHere();
                }
                if (matcher.group("improvement").equals("RAILROAD") && canBuildRailroadHere()) {
                    if (canBuildRailroadHere()) {
                        if (unit.hasRemainingMoves()) buildRoute("railroad");
                        else GameMenu.notEnoughMoves();
                    }
                    else if (civilization.hasReachedTech(Technology.RAILROAD)) GameMenu.cantBuildRailroadHere();
                    else GameMenu.unreachedTech(Technology.RAILROAD);
                }
            }
            catch (Exception e) {
                GameMenu.noSuchImprovement();
            }
        }

        else if (unit.getStatus().equals(UnitStatus.PILLAGE)) {
            if (matcher.group("improvement").equals("road") || matcher.group("improvement").equals("railroad")) {
                if (canPillageRoute(matcher.group("improvement"))) {
                    if (unit.hasRemainingMoves()) pillageRoute(matcher.group("improvement"));
                    else GameMenu.notEnoughMoves();
                }
            }
            else {
                try {
                    Improvement improvement = Improvement.valueOf(matcher.group("improvement"));
                    if (canPillageImprovement(improvement)) {
                        if (unit.hasRemainingMoves()) pillageImprovement();
                        else GameMenu.notEnoughMoves();
                    }
                }
                catch (Exception e) {
                    GameMenu.noSuchImprovement();
                }
            }
        }

        else if (unit.getStatus().equals(UnitStatus.REPAIR)) {
            if (matcher.group("improvement").equals("road") || matcher.group("improvement").equals("railroad")) {
                if (canRepairRoute(matcher.group("improvement"))) {
                    if (unit.hasRemainingMoves()) repairRoute();
                    else GameMenu.notEnoughMoves();
                }
            }
            else {
                try {
                    Improvement improvement = Improvement.valueOf(matcher.group("improvement"));
                    if (canRepairImprovement(improvement)) {
                        if (unit.hasRemainingMoves()) repairImprovement();
                        else GameMenu.notEnoughMoves();
                    }
                }
                catch (Exception e) {
                    GameMenu.noSuchImprovement();
                }
            }
        }

        else if (unit.getStatus().equals(UnitStatus.GARRISON)) {
            if (canGarrison()) {
                if (unit.hasRemainingMoves()) garrison();
                else GameMenu.notEnoughMoves();
            }
        }

        else if (unit.getStatus().equals(UnitStatus.CLEAR_LAND)) {
            String clearingTarget = matcher.group("clearable");
            if (unit.getTile().hasClearable(clearingTarget))
                clearTileFrom(clearingTarget);
        }

        else if (unit.getStatus().equals(UnitStatus.CANCEL_MISSION))
            unit.setStatus("active");

        else if (unit.getStatus().equals(UnitStatus.DO_NOTHING))
            System.out.println("doing nothing"); //TODO... add else_if for other statuses

        else System.out.println("unit controller, status: " + unit.getStatus());
    }

    private static Matcher getUnitDecision() {
        Matcher matcher;

        while (true) {
            String command = GameMenu.nextCommand();

            if (command.equals("back")) return Pattern.compile(command).matcher(command);

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
                else
                    return matcher;
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
                    if (militaryIsInCityTiles())
                        return matcher;
                    else
                        GameMenu.cantMakeGarrison();
                }
                else
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

    private static boolean militaryIsInCityTiles() {
        for (City city : civilization.getCities())
            for (Tile tile : city.getTiles())
                if (tile.equals(unit.getTile()))
                    return true;
        return false;
    }

    private static void clearTileFrom (String clearingTarget) {
        //TODO
    }

    private static boolean canGarrison() {
        return unit.getTile().getCity() != null && unit.getTile().getCity().getTiles().get(0).equals(unit.getTile());
    }

    private static void garrison() {
        unit.setStatus("garrison");
    }

    private static boolean canPillageRoute (String routeType) {
        return unit.getTile().getRouteInProgress() != null &&
                unit.getTile().getRouteInProgress().getKey().equals(routeType) &&
                    unit.getTile().getRouteInProgress().getValue() == 0;
    }

    private static void pillageRoute (String routeType) {
        //TODO... add gold and stuff BLA BLA
        Pair <String, Integer> pair =
                new Pair<>(unit.getTile().getRouteInProgress().getKey(), -3);
        unit.getTile().setRouteInProgress(pair);
        GameMenu.pillageSuccessful(routeType);
    }

    private static boolean canPillageImprovement (Improvement improvement) {
        return unit.getTile().getImprovementInProgress() != null &&
                unit.getTile().getImprovementInProgress().getValue() == 0 &&
                    unit.getTile().getImprovementInProgress().getKey().equals(improvement);
    }

    private static void pillageImprovement() {
        //TODO... add gold to attackers' civilization BLA BLA
        Pair <Improvement, Integer> pair =
                new Pair<>(unit.getTile().getImprovementInProgress().getKey(), -3);
        unit.getTile().setImprovementInProgress(pair);
        GameMenu.pillageSuccessful(pair.getKey().toString());
    }

    private static boolean canRepairRoute (String routeType) {
        try {
            return unit.getTile().getRouteInProgress().getKey().equals(routeType) &&
                    unit.getTile().getRouteInProgress().getValue() < 0;
        }
        catch (Exception e) {
            return false;
        }
    }

    private static void repairRoute() {
        Pair <String, Integer> pair =
                new Pair<>(unit.getTile().getRouteInProgress().getKey(), unit.getTile().getRouteInProgress().getValue() * -1);
        unit.getTile().setRouteInProgress(pair);
        GameMenu.repairStarted(unit.getTile().getRouteInProgress().getKey());
    }

    private static boolean canRepairImprovement (Improvement improvement) {
        try {
            return unit.getTile().getImprovementInProgress().getKey().equals(improvement) &&
                    unit.getTile().getImprovementInProgress().getValue() < 0;
        }
        catch (Exception e) {
            return false;
        }
    }

    private static void repairImprovement() {
        Pair <Improvement, Integer> pair =
                new Pair<>(unit.getTile().getImprovementInProgress().getKey(), unit.getTile().getImprovementInProgress().getValue() * -1);
        unit.getTile().setImprovementInProgress(pair);
    }

    private static boolean canFoundCityHere() {
        ArrayList<Tile> beginningTiles = new ArrayList<>();
        beginningTiles.add(unit.getTile());
        beginningTiles.addAll(unit.getTile().getNeighbors());
        for (Tile tile : beginningTiles)
            if (tile.getCity() != null) return false;
        return !unit.getTile().getFeature().equals(TerrainFeature.ICE);
    }

    private static boolean canBuildImprovementHere(Improvement improvement) {
        if (! civilization.hasReachedTech(improvement.getPrerequisiteTech())) {
            GameMenu.unreachedTech(improvement.getPrerequisiteTech());
            return false;
        }
        if (! tileIsValidForImprovement(unit.getTile(), improvement)) {
            GameMenu.cantBuildImprovementOnTile();
            return false;
        }
        return true;
    }

    private static boolean canBuildRoadHere() {
        return civilization.hasReachedTech(Technology.WHEEL) && unit.getTile().getFeature().equals(TerrainFeature.ICE); //TODO
    }

    private static boolean canBuildRailroadHere() {
        return civilization.hasReachedTech(Technology.RAILROAD);
    }

    private static boolean tileIsValidForImprovement (Tile tile, Improvement improvement) {
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

    private static void buildRoute (String routeType) {
        if (tileAlreadyHas(routeType)) {
            GameMenu.tileAlreadyHas(routeType);
            return;
        }
        else if (routeType.equals("road") && tileAlreadyHas("railroad")) { //Railroad is more advanced so user can not build road on it
            GameMenu.tileAlreadyHas("railroad");
            return;
        }
        Pair <String, Integer> pair = new Pair<>(routeType, calcTurnsFor(routeType));
        unit.getTile().setRouteInProgress(pair);
        GameMenu.buildRouteSuccessfully(routeType);
    }

    private static boolean tileAlreadyHas (String routeType) {
        try {
            return unit.getTile().getRouteInProgress().getKey().equals(routeType) &&
                    unit.getTile().getRouteInProgress().getValue() <= 0;
        }
        catch (Exception e) {
            return false;
        }
    }

    private static int calcTurnsFor (String routeType) {
        if (unit.getTile().getRouteInProgress() != null &&
                unit.getTile().getRouteInProgress().getKey().equals(routeType))
            return unit.getTile().getRouteInProgress().getValue();
        return 3; //Page 87 of manual doc
    }

    private static void buildImprovement (Improvement improvement) {
        if (tileAlreadyHasImprovement(improvement)) {
            GameMenu.tileAlreadyHas(improvement.toString());
            return;
        }
        if (tileOutOfCityLimits()) {
            GameMenu.tileIsNotInTerritory(improvement);
            return;
        }
        Pair <Improvement, Integer> pair = new Pair<>(improvement, calcTurnsForImprovement(improvement));
        unit.getTile().setImprovementInProgress(pair);
    }

    private static boolean tileAlreadyHasImprovement (Improvement improvement) {
        try {
            return unit.getTile().getImprovementInProgress().getKey().equals(improvement) &&
                    unit.getTile().getImprovementInProgress().getValue() <= 0;
        }
        catch (Exception e) {
            return false;
        }
    }

    private static boolean tileOutOfCityLimits() {
        return unit.getTile().getCity() == null || !unit.getTile().getCity().getCivilization().equals(civilization);
    }

    private static int calcTurnsForImprovement (Improvement improvement) {
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

    private static void moveUnit(int destIndexI, int destIndexJ) {

        if (tileIsImpassable(Game.getTiles()[destIndexI][destIndexJ], unit)) {
            GameMenu.impassableTile();
            return;
        }
        Path chosenPath = findBestPath(destIndexI, destIndexJ);

        if (chosenPath == null) return;
        moveOnPath(unit, chosenPath);

        if (chosenPath.tiles.size() > 0) unit.setPath(chosenPath);
    }

    private static void continuePath() {
        Tile destTile = unit.getPath().tiles.get(unit.getPath().tiles.size() - 1);
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
            if (path.tiles.get(0).equals(Game.getTiles()[destIndexI][destIndexJ]))
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
                if (neighborTile.equals(Game.getTiles()[destIndexI][destIndexJ]))
                    return child;
                paths.add(child);
            }
            paths.remove(path);
        }
        return null;
    }

    public static void moveOnPath (Unit unit, Path chosenPath) {
        while (unit.getMovesInTurn() < unit.getMP() && chosenPath.tiles.size() > 0) {
            if (unit instanceof Military) {
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
        }
        if (chosenPath.tiles.size() > 0) unit.setStatus("has path");
        else unit.setStatus("active");
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
            if (!unit.getType().isCivilian() && tile.getMilitary() != null) return true;
            if (unit.getType().isCivilian() && tile.getCivilian() != null) return true;
        }
        return tile.getType().equals(TerrainType.OCEAN) ||
                tile.getType().equals(TerrainType.MOUNTAIN);
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

        paths.removeIf(path -> tileIsImpassable(path.tiles.get(0), unit));
    }

    public static void doRemainingMissions() {
        if (unit.getStatus().equals(UnitStatus.HAS_PATH))
            continuePath();
        if (unit.getStatus().equals(UnitStatus.SLEEP) || unit.getStatus().equals(UnitStatus.FORTIFY) ||
                (unit.getStatus().equals(UnitStatus.HEAL) && unit.getHealth() < 20) ||
                    unit.getStatus().equals(UnitStatus.GARRISON) || unit.getStatus().equals(UnitStatus.SIEGEPREP)) //TODO fortify heal
            return;
        if (unit.getStatus().equals(UnitStatus.ALERT) && noEnemyNearby())
            return;
        unit.setStatus("active");
    }

    private static boolean noEnemyNearby() {
        for (Tile neighbor : unit.getTile().getNeighbors())
            if (neighbor.getMilitary() != null && ! neighbor.getMilitary().getCivilization().equals(civilization))
                return false;
        return true;
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

    private static boolean canAttackTo (Tile tile) {
        if (tile.getCity() != null && tile.isCenterOfCity(tile.getCity()) &&
                !tile.getCity().getCivilization().equals(civilization))
            return true;
        return tile.getMilitary() != null && !tile.getMilitary().getCivilization().equals(civilization);
    }

    private static void attack (Tile targetTile) {
        //TODO if defender is civilian
        //TODO call ranged attack to unit
        //TODO call close attack to unit
        if (targetTile.isCenterOfCity(targetTile.getCity())) {
            if (unit.getType().isRangeCombat())
                rangedAttackToCity(targetTile.getCity());
            if (unit.getType().isMeleeCombat())
                meleeAttackToCity(targetTile.getCity());
        }
        else {
            System.out.println("next phase ;)"); //TODO next phase
        }
    }

    private static void rangedAttackToUnit(Unit defendingUnit) {

    }

    private static void rangedAttackToCity (City city) {
        if (cityIsOutOfRange(city)) {
            GameMenu.cityOutOfUnitRange();
            return;
        }
        Military military = (Military) unit;
        if (unit.getType().isSiege() && !unit.getStatus().equals(UnitStatus.SIEGEPREP)) {
            GameMenu.siegeNotPrepared();
            return;
        }
        city.setHP(city.getHP() - military.getRangedCombatStrength() / 4);
        if (city.getHP() < 0) city.setHP(0);
        GameMenu.rangedAttackToCitySuccessfully(city);
    }

    private static boolean cityIsOutOfRange (City city) {
        //TODO... add mountain
        ArrayList<Tile> unitInRangeTiles = new ArrayList<>(unit.getTile().getNeighbors());
        int beginIndex = 0;
        for (int i = 0; i < unit.getType().getRange(); i++) {
            for (Tile tile : unitInRangeTiles)
                if (tile.isCenterOfCity(city))
                    return true;
            int sizeHolder = unitInRangeTiles.size();
            for (int j = beginIndex; j < sizeHolder; j++)
                unitInRangeTiles.addAll(unitInRangeTiles.get(j).getNeighbors());
            beginIndex = sizeHolder;
        }
        for (Tile tile : unitInRangeTiles)
            if (tile.isCenterOfCity(city))
                return true;
        return false;
    }

    private static void meleeAttackToCity (City city) {
        Path bestPath = findBestPath(city.getTiles().get(0).getIndexInMapI(), city.getTiles().get(0).getIndexInMapJ());
        Military test = new Military(unit.getType());
        moveOnPath(test, bestPath);
        if (bestPath == null || test.getStatus().equals(UnitStatus.HAS_PATH)) {
            GameMenu.cityCenterOutOfMeleeRange(city);
            return;
        }
        Tile destTile = bestPath.tiles.get(bestPath.tiles.size() - 1);
        bestPath = findBestPath(destTile.getIndexInMapI(), destTile.getIndexInMapJ());
        moveOnPath(unit, bestPath);
        unit.setHealth(unit.getHealth() - city.getCombatStrength() / 4);
        city.setHP(city.getHP() - ((Military)unit).getCombatStrength() / 10);
        if (unit.getHealth() <= 0) unit.kill();
        if (city.getHP() <= 0) {
            GameMenu.cityHPIsZero(city);
            CivilizationController.enterCityAsConqueror(city);
        }
    }



    private static void meleeAttackToUnit (Unit defendingUnit) {

    }


}
