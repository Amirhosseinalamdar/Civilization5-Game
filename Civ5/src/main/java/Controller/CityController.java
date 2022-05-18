package Controller;

import Model.Civilization;
import Model.Game;
import Model.Map.*;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitStatus;
import Model.UnitPackage.UnitType;
import View.Commands;
import View.GameMenu;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;

public class CityController {
    private static Civilization civilization;
    private static City city;

    public static void changeCivilization(Civilization civilization) {
        CityController.civilization = civilization;
    }

    public static void setCity(City city) {
        CityController.city = city;
    }

    public static void handleCityOptions() {
        Matcher matcher = getCityDecision();

        if (matcher.pattern().toString().equals(Commands.CREATE_UNIT.getRegex()) ||
            matcher.pattern().toString().equals(Commands.PURCHASE_UNIT.getRegex())) {
            UnitType unitType = getUnitTypeFromString(matcher.group("unitName"));
            if (unitType == null) {
                GameMenu.invalidUnitType();
                return;
            }
            if (matcher.pattern().toString().equals(Commands.CREATE_UNIT.getRegex())) tryCreateUnit(unitType);
            else tryPurchaseUnit(unitType);
        }

        else if (matcher.pattern().toString().equals(Commands.PURCHASE_TILE.getRegex())) {
            Tile targetTile = Game.getTiles()[Integer.parseInt(matcher.group("x"))][Integer.parseInt(matcher.group("y"))];
            if (tileIsPurchasable(targetTile))
                purchaseTile(targetTile);
        }

        else if (matcher.pattern().toString().equals(Commands.LOCK_CITIZEN.getRegex())) {
            int x = Integer.parseInt(matcher.group("x")), y = Integer.parseInt(matcher.group("y"));
            for (Citizen citizen : city.getCitizens())
                if (citizen.getTile() == null) {
                    lockCitizenOnTile(citizen, Game.getTiles()[x][y]);
                    return;
                }
            GameMenu.noUnemployedCitizenAvailable();
            if (city.getCitizens().size() == 0) return;
            Citizen workingCitizen = getWorkingCitizen();
            if (workingCitizen == null) {
                GameMenu.citizenLockError();
                return;
            }
            lockCitizenOnTile(workingCitizen, Game.getTiles()[x][y]);
        }

        else if (matcher.pattern().toString().equals(Commands.ATTACK.getRegex())) {
            int x = Integer.parseInt(matcher.group("x")), y = Integer.parseInt(matcher.group("y"));
            if (canCityAttackTo(Game.getTiles()[x][y])) {
                rangeAttackToUnit(Game.getTiles()[x][y].getMilitary());
            }
        }

        else if (matcher.pattern().toString().equals(Commands.SHOW_CITY_OUTPUT.getRegex())) {
            updateCityInfos(city);
            GameMenu.showCityOutput(city);
        }

        else System.out.println("city controller, invalid command");
    }

    public static Matcher getCityDecision() {
        Matcher matcher;

        while (true) {
            String command = GameMenu.nextCommand();
            if ((matcher = Commands.getMatcher(command, Commands.CREATE_UNIT)) != null ||
                    (matcher = Commands.getMatcher(command, Commands.PURCHASE_UNIT)) != null) {
                if (unitIsValid(matcher.group("unitName")))
                    return matcher;
                GameMenu.invalidUnitType();
            }
            if ((matcher = Commands.getMatcher(command, Commands.PURCHASE_TILE)) != null) {
                if (!GameController.invalidPos(Integer.parseInt(matcher.group("x")),
                        Integer.parseInt(matcher.group("y"))))
                    return matcher;
                GameMenu.indexOutOfArray();
            }
            if ((matcher = Commands.getMatcher(command, Commands.LOCK_CITIZEN)) != null) {
                if (!GameController.invalidPos(Integer.parseInt(matcher.group("x")),
                        Integer.parseInt(matcher.group("y"))))
                    return matcher;
                GameMenu.indexOutOfArray();
            }

            if ((matcher = Commands.getMatcher(command, Commands.ATTACK)) != null) {
                if (!GameController.invalidPos(Integer.parseInt(matcher.group("x")),
                        Integer.parseInt(matcher.group("y"))))
                    return matcher;
                GameMenu.indexOutOfArray();
            }

            if ((matcher = Commands.getMatcher(command, Commands.SHOW_CITY_OUTPUT)) != null)
                return matcher;

            System.out.println("city decision wasn't valid");
        }
    }

    private static boolean canCityAttackTo (Tile targetTile) {
        boolean tileWithin2radius = false;
        for (Tile tile : city.getTiles()) {
            if (targetTile.equals(tile)) {
                tileWithin2radius = true;
                break;
            }
            for (Tile neighbor : tile.getNeighbors()) {
                if (targetTile.equals(neighbor)) {
                    tileWithin2radius = true;
                    break;
                }
                for (Tile neighbor2 : neighbor.getNeighbors()) {
                    if (targetTile.equals(neighbor2)) {
                        tileWithin2radius = true;
                        break;
                    }
                }
                if (tileWithin2radius) break;
            }
            if (tileWithin2radius) break;
        }
        if (!tileWithin2radius) return false;
        return targetTile.getMilitary() != null &&
                !targetTile.getMilitary().getCivilization().equals(civilization);
    }

    private static void tryPurchaseUnit (UnitType unitType) {
        int unitGoldCost = 100;
        if (!civilization.hasReachedTech(unitType.getPrerequisiteTech())) {
            GameMenu.unreachedTech(unitType.getPrerequisiteTech());
            return;
        }
        if (unitGoldCost > civilization.getTotalGold()) {
            GameMenu.notEnoughGoldForUnit(unitType);
            return;
        }
        if ((unitType.isCivilian() && city.getTiles().get(0).getCivilian() != null) ||
                city.getTiles().get(0).getMilitary() != null){
            GameMenu.cityIsOccupied(unitType.toString());
            return;
        }
        civilization.setTotalGold(civilization.getTotalGold() - unitGoldCost);
        if (unitType.isCivilian()) {
            Unit civilian = new Unit(unitType);
            civilization.addUnit(civilian);
            city.getTiles().get(0).setCivilian(civilian);
            civilian.setTile(city.getTiles().get(0));
            civilian.setCivilization(civilization);
        }
        else {
            Military military = new Military(unitType);
            civilization.addUnit(military);
            city.getTiles().get(0).setMilitary(military);
            military.setTile(city.getTiles().get(0));
            military.setCivilization(civilization);
        }
    }

    private static boolean unitIsValid(String unitName) {
        try {
            System.out.println("you have chosen: " + UnitType.valueOf(unitName) + " to create");
            return true;
        } catch (IllegalArgumentException i) {
            return false;
        }
    }

    private static Citizen getWorkingCitizen() {
        String[] args = GameMenu.nextCommand().split(" ");
        try {
            int x = Integer.parseInt(args[0]), y = Integer.parseInt(args[1]);
            if (GameController.invalidPos(x, y)) {
                GameMenu.indexOutOfArray();
                return null;
            }
            Citizen citizen = Game.getTiles()[x][y].getWorkingCitizen();
            if (citizen == null) return null;
            if (citizen.getCity().equals(city)) return citizen;
            GameMenu.citizenNotYours();
            return null;
        } catch (Exception e) {
            GameMenu.invalidPosForCitizen();
            return null;
        }
    }

    public static void lockCitizenOnTile(Citizen citizen, Tile tile) {
        citizen.changeWorkingTile(tile);
        tile.setWorkingCitizen(citizen);
    }

    private static UnitType getUnitTypeFromString(String string) {
        try {
            return UnitType.valueOf(string);
        } catch (IllegalArgumentException i) {
            return null;
        }
    }

    private static boolean tileIsPurchasable(Tile targetTile) {
        boolean isNeighbor = false;
        for (Tile tile : city.getTiles()) {
            if (tile.equals(targetTile)) {
                GameMenu.cityAlreadyHasTile();
                return false;
            }
            if (!UnitController.areNeighbors(tile, targetTile)) {
                isNeighbor = true;
                break;
            }
        }
        if (!isNeighbor) GameMenu.unreachableTileForCity();
        return isNeighbor;
    }

    private static void purchaseTile(Tile targetTile) {
        int necessaryAmountOfGoldForPurchase = targetTile.getGoldPerTurn() * 3 + targetTile.getProductionPerTurn() +
                targetTile.getFoodPerTurn() * 2;
        if (civilization.getTotalGold() >= necessaryAmountOfGoldForPurchase) {
            civilization.setTotalGold(civilization.getTotalGold() - necessaryAmountOfGoldForPurchase);
            city.getTiles().add(targetTile);
            targetTile.setCity(city);
            civilization.getNotifications().add("new tile is purchased. tile x: "
                    + targetTile.getIndexInMapI() + " y: " + targetTile.getIndexInMapJ() + "    time: " + Game.getTime());
        } else
            GameMenu.notEnoughGoldForTilePurchase();
    }

    public static void updateCityInfos(City city) {
        int food = 1;
        int production = 1;
        int gold = 0;
        int science = 0;
        for (Citizen citizen : city.getCitizens()) {
            if (citizen.getTile() == null) production++;
            else {
                food += citizen.getTile().getFoodPerTurn();
                production += citizen.getTile().getProductionPerTurn();
                gold += citizen.getTile().getGoldPerTurn();
                if (citizen.getTile().canUseItsResource()) {
                    food += citizen.getTile().getResource().getFood();
                    production += citizen.getTile().getResource().getProduction();
                    gold += citizen.getTile().getResource().getGold();
                }
            }
        }
        if (city.getCityStatus() == CityStatus.CAPITAL) science += 3;
        science += city.getCitizens().size();
        city.setFoodPerTurn(food);
        city.setProductionPerTurn(production);
        city.setGoldPerTurn(gold);
        city.setSciencePerTurn(science);
        city.updateStoredFood();
    }


    public static void updateCity(City city) {
        if (city.getHP() < 20) city.setHP(city.getHP() + 1);
        updateCityInfos(city);
        handlePopulation(city);
        updateBorder(city);
        updateProduction(city);
        updateBuildingImprovement(city);
        updateRemovingImprovement(city);
        updateRoads(city);

    }

    private static void updateRoads(City city) {
        for (Tile tile : city.getTiles()) {
            if (tile.getRouteInProgress() != null
                    && (tile.getRouteInProgress().getKey().equals("road") || tile.getRouteInProgress().getKey().equals("railroad"))
                    && tile.getCivilian() != null && tile.getCivilian().getType().equals(UnitType.WORKER)
                    && (tile.getCivilian().getStatus().equals(UnitStatus.BUILD_IMPROVEMENT) || tile.getCivilian().getStatus().equals(UnitStatus.REPAIR))) {
                if (tile.getRouteInProgress().getValue() <= 0) continue;
                int turn = tile.getRouteInProgress().getValue();
                turn--;
                tile.setImprovementInProgress(new Pair<>(tile.getImprovementInProgress().getKey(), turn));
            }
        }
    }

    private static void updateRemovingImprovement(City city) {
        for (Tile tile : city.getTiles()) {
            if (tile.getRemoveInProgress() != null && tile.getCivilian() != null
                    && tile.getCivilian().getType().equals(UnitType.WORKER)
                    && tile.getCivilian().getStatus().equals(UnitStatus.CLEAR_LAND)) {
                int turn = tile.getRemoveInProgress().getValue();
                turn--;
                tile.setRemoveInProgress(new Pair<>(tile.getRouteInProgress().getKey(), turn));
                if (turn == 0) {
                    tile.getCivilian().setStatus("active");
                    tile.setFeature(TerrainFeature.NONE);
                }
            }
        }
    }

    private static void updateBuildingImprovement(City city) {
        for (Tile tile : city.getTiles()) {
            if (tile.getImprovementInProgress() != null && tile.getCivilian() != null
                    && tile.getCivilian().getType().equals(UnitType.WORKER)
                    && (tile.getCivilian().getStatus().equals(UnitStatus.BUILD_IMPROVEMENT) || tile.getCivilian().getStatus().equals(UnitStatus.REPAIR))) {
                if (tile.getImprovementInProgress().getValue() <= 0) continue;
                int turn = tile.getImprovementInProgress().getValue();
                turn--;
                tile.setImprovementInProgress(new Pair<>(tile.getImprovementInProgress().getKey(), turn));
                if (turn == 0) {
                    tile.getCivilian().setStatus("active");
                    if (tile.getResource() != null && tile.getResource().getType().equals("luxury")) {
                        if (civilization.getLuxuryResources().containsKey(tile.getResource())) {
                            int count = civilization.getLuxuryResources().get(tile.getResource());
                            count++;
                            civilization.getLuxuryResources().replace(tile.getResource(), count);
                        } else civilization.getLuxuryResources().put(tile.getResource(), 1);
                    }
                    civilization.getNotifications().add(tile.getImprovementInProgress().getKey().name() + " is built in tile x: "
                            + tile.getIndexInMapI() + " y: " + tile.getIndexInMapJ() + ".    time: " + Game.getTime());
                    if (tile.getResource() != null) {
                    civilization.getNotifications().add(tile.getResource() + " is achieved.    time: " + Game.getTime());
                    }
                }
            }
        }
    }

    private static void updateProduction(City city) {
        if (city.getInProgressUnit() != null) {
            int i = city.getLastCostsUntilNewProductions().get(city.getInProgressUnit());
            i -= city.getProductionPerTurn();
            city.getLastCostsUntilNewProductions().replace(city.getInProgressUnit(), i);
            if (i <= 0) {
                city.getLastCostsUntilNewProductions().remove(city.getInProgressUnit());
                produceUnit(city);
                city.setInProgressUnit(null);
            }
        }
    }

    private static void produceUnit(City city) {
        if (city.getInProgressUnit().equals(UnitType.WORKER) || city.getInProgressUnit().equals(UnitType.SETTLER)) {
            Unit civilian = new Unit(city.getInProgressUnit());
            civilian.setCivilization(civilization);
            civilization.addUnit(civilian);
            civilian.setTile(city.getTiles().get(0));
            city.getTiles().get(0).setCivilian(civilian);
        } else {
            Military military = new Military(city.getInProgressUnit());
            military.setCivilization(civilization);
            civilization.addUnit(military);
            military.setTile(city.getTiles().get(0));
            city.getTiles().get(0).setMilitary(military);
        }
    }

    public static String turnsForNewUnit(City city) {
        String output;
        int turn;
        if (city.getInProgressUnit() == null) output = "there is no unit in progress";
        else {
            if (city.getProductionPerTurn() != 0) {
                turn = city.getLastCostsUntilNewProductions().get(city.getInProgressUnit()) / city.getProductionPerTurn();
                output = "turns until producing " + city.getInProgressUnit().name() + " : " + turn;
            } else output = "turns until producing " + city.getInProgressUnit().name() + " : " + "-";
        }
        return output;
    }

    private static void handlePopulation(City city) {
        if (city.getStoredFood() > 0) {
            city.setLostCitizenLastFood(city.getCitizenNecessityFood());
            city.setGainCitizenLastFood(city.getGainCitizenLastFood() - city.getStoredFood());
            if (city.getGainCitizenLastFood() <= 0) {
                Citizen citizen = new Citizen(city, null);
                city.getCitizens().add(citizen);
                city.setCitizenNecessityFood((int) (city.getCitizenNecessityFood() * 1.5));
                city.setGainCitizenLastFood(city.getCitizenNecessityFood());
                civilization.getNotifications().add("The " + city.getName() + "'s population is increased     time: " + Game.getTime());
            }
            city.setTurnsUntilBirthCitizen(city.getGainCitizenLastFood() / city.getStoredFood());
        } else if (city.getStoredFood() == 0) city.setTurnsUntilBirthCitizen(0);
        else if (city.getCitizens().size() > 1) {
            city.setGainCitizenLastFood(city.getCitizenNecessityFood());
            city.setLostCitizenLastFood(city.getLostCitizenLastFood() + city.getStoredFood());
            if (city.getLostCitizenLastFood() <= 0) {
                city.getCitizens().remove(city.getCitizens().size() - 1);
                city.setCitizenNecessityFood((int) (city.getCitizenNecessityFood() * 0.66));
                city.setLostCitizenLastFood(city.getCitizenNecessityFood());
                civilization.getNotifications().add("The " + city.getName() + "'s population is decreased     time: " + Game.getTime());
            }
            city.setTurnsUntilDeathCitizen(city.getLostCitizenLastFood() / city.getStoredFood());
        }
    }

    private static void updateBorder(City city) {
        city.setBorderLastCost(city.getBorderLastCost() - (city.getCitizens().size() + city.getStoredFood()));
        if (city.getBorderLastCost() <= 0) {
            expandCity(city);
            city.setBorderExpansionCost((int) (city.getBorderExpansionCost() * 1.5));
            city.setBorderLastCost(city.getBorderExpansionCost());
            civilization.getNotifications().add("The " + city.getName() + "'s border is expanded     time: " + Game.getTime());
        }
        if ((city.getCitizens().size() + city.getStoredFood()) == 0) city.setTurnsUntilGrowthBorder(0);
        else city.setTurnsUntilGrowthBorder(city.getBorderLastCost() / (city.getCitizens().size() + city.getStoredFood()));
    }

    public static void expandCity(City city) {
        Tile tile = findAppropriateTile(city);
        if (tile == null) return;
        city.getTiles().add(tile);
        tile.setCity(city);
    }

    private static Tile findAppropriateTile(City city) {
        Random random = new Random(0);
        int n;
        ArrayList<Tile> tiles = new ArrayList<>(city.getTiles());
        while (tiles.size() > 0) {
            n = random.nextInt(tiles.size());
            for (Tile tileNeighbor : tiles.get(n).getNeighbors()) {
                if (tileNeighbor.getCity() == null && (tileNeighbor.getCivilian() == null || tileNeighbor.getCivilian().getCivilization() != city.getCivilization()) &&
                        (tileNeighbor.getMilitary() == null || tileNeighbor.getMilitary().getCivilization() != city.getCivilization())) return tileNeighbor;
            }
            tiles.remove(n);
        }
        return null;
    }

    private void cityAttackToUnit(City city, Unit unit) {

    }

    private static void tryCreateUnit (UnitType unitType) {
        if (!hasReachedTechForUnit(unitType)) {
            GameMenu.unreachedTech(unitType.getPrerequisiteTech());
            return;
        }
        if (!hasEnoughResources(unitType)) {
            GameMenu.notEnoughResource();
            return;
        }
        if (unitType.isCivilian()) {
            if (city.getTiles().get(0).getCivilian() != null) {
                GameMenu.cityIsOccupied(city.getTiles().get(0).getCivilian().getType().toString());
                return;
            }
        }
        else {
            if (city.getTiles().get(0).getMilitary() != null) {
                GameMenu.cityIsOccupied(city.getTiles().get(0).getMilitary().getType().toString());
                return;
            }
        }
        try {
            int remainingCost = city.getLastCostsUntilNewProductions().get(unitType);
            System.out.println("already in progress... remaining cost: " + remainingCost);
        }
        catch (Exception e) {
            city.getLastCostsUntilNewProductions().put(unitType, unitType.getCost());
        }
        city.setInProgressUnit(unitType);
    }

    private static boolean hasReachedTechForUnit(UnitType unitType) {
        try {
            return civilization.getLastCostUntilNewTechnologies().get(unitType.getPrerequisiteTech()) <= 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean hasEnoughResources(UnitType unitType) {
        if (unitType.getPrerequisiteResource() == null) return true;
        for (Resource r : civilization.getResources())
            if (r.equals(unitType.getPrerequisiteResource()))
                return true;
        return false;
    }

    private static void rangeAttackToUnit (Military targetUnit) {
        targetUnit.setHealth(targetUnit.getHealth() - (city.getCombatStrength() - targetUnit.getCombatStrength()) / 3);
        if (targetUnit.getHealth() <= 0) targetUnit.kill();
    }
}
