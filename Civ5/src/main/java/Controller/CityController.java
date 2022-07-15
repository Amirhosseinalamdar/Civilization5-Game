package Controller;

import Model.Map.Building;
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
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;

public class CityController {
    private static Civilization civilization;
    private static City city;
    private static String command;

    public static void changeCivilization(Civilization civilization) {
        CityController.civilization = civilization;
    }

    public static void setCity(City city, String command) {
        CityController.city = city;
        CityController.command = command;
    }

    public static String handleCityOptions() {
        Matcher matcher = getCityDecision();

        if (matcher.pattern().toString().equals(Commands.CREATE_UNIT.getRegex()) ||
                matcher.pattern().toString().equals(Commands.PURCHASE_UNIT.getRegex())) {
            UnitType unitType = getUnitTypeFromString(matcher.group("unitName"));
            if (unitType == null)
                return "unit type is invalid";

            if (matcher.pattern().toString().equals(Commands.CREATE_UNIT.getRegex())) return tryCreateUnit(unitType);
            else return tryPurchaseUnit(unitType);
        }
        else if (matcher.pattern().toString().equals(Commands.PURCHASE_TILE.getRegex())) {
            Tile targetTile = Game.getInstance().getTiles()[Integer.parseInt(matcher.group("x"))][Integer.parseInt(matcher.group("y"))];
            return purchaseTile(targetTile);
        }
        else if (matcher.pattern().toString().equals(Commands.LOCK_CITIZEN.getRegex())) {
            int x = Integer.parseInt(matcher.group("x")), y = Integer.parseInt(matcher.group("y"));
            for (Citizen citizen : city.getCitizens())
                if (citizen.getTile() == null) {
                    lockCitizenOnTile(citizen, Game.getInstance().getTiles()[x][y]);
                    return "";
                }
            if (city.getCitizens().size() == 0) return "city has no citizens at all!";
            Citizen workingCitizen = getWorkingCitizen();
            if (workingCitizen == null)
                return "couldn't lock any citizen";
            lockCitizenOnTile(workingCitizen, Game.getInstance().getTiles()[x][y]);
            return "";
        }
        else if (matcher.pattern().toString().equals(Commands.ATTACK.getRegex())) {
            int x = Integer.parseInt(matcher.group("x")), y = Integer.parseInt(matcher.group("y"));
            //TODO... handle options (canCityAttackTo()) from graphic side
            return rangeAttackToUnit(Game.getInstance().getTiles()[x][y].getMilitary());
        }
        else if (matcher.pattern().toString().equals(Commands.SHOW_CITY_OUTPUT.getRegex())) {
            updateCityInfos(city);
            GameMenu.showCityOutput(city);
        }
        else if (matcher.pattern().toString().equals(Commands.CREATE_BUILDING.getRegex()) ||
                matcher.pattern().toString().equals(Commands.PURCHASE_BUILDING.getRegex())) {
            Building building = getBuildingFromString(matcher.group("buildingName"));
            if (building == null)
                return "building is invalid";

            if (matcher.pattern().toString().equals(Commands.CREATE_BUILDING.getRegex())) return tryCreateBuilding(building);
            else return tryPurchaseBuilding(building);
        }
        return "city controller, invalid command";
    }

    private static String tryCreateBuilding(Building building) {
        if (!hasReachedTechForBuilding(building)) {
            return "you haven't reached " + building.getPrerequisiteTech() + " yet";
        }
        if (hasNotBuiltBuildingForBuilding(building)) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Building b : building.getPrerequisiteBuildings())
                if (!city.getBuildings().containsKey(b) || city.getBuildings().get(b) > 0)
                    stringBuilder.append(b.toString()).append(", ");
            stringBuilder.setCharAt(stringBuilder.length() - 1, ' ');
            return "you haven't reached " + stringBuilder + " yet";
        }
        if (checkResource(building))
            return "you can't build this building here";

        if (!city.getBuildings().containsKey(building))
            city.getBuildings().put(building, building.getCost());

        city.setInProgressBuilding(building);
        return "";
    }

    public static boolean canConstructBuilding (Building building) {
        if (!hasReachedTechForBuilding(building)) {
            return false;
        }
        if (hasNotBuiltBuildingForBuilding(building)) {
            return false;
        }
        if (checkResource(building)) {
            return false;
        }
        try {
            int remainingCost = city.getBuildings().get(building);
            if (remainingCost == 0) System.out.println("this building is already built");
            else System.out.println("already in progress... remaining cost: " + remainingCost);
            return false;
        }
        catch (Exception e) {
            return true;
        }
    }

    private static boolean hasNotBuiltBuildingForBuilding(Building building) {
        for (Building prerequisiteBuilding : building.getPrerequisiteBuildings()) {
            try {
                if (city.getBuildings().get(prerequisiteBuilding) <= 0) return false;
            } catch (Exception ignored) {
            }
        }
        return true;
    }

    private static boolean checkResource(Building building) {
        if (building.equals(Building.WATER_MILL) || building.equals(Building.GARDEN)) {
            for (Tile tile : city.getTiles()) {
                if (tile.isRiverAtLeft()) return false;
            }
            return true;
        } else if (building.equals(Building.CIRCUS) || building.equals(Building.STABLE)) {
            for (Tile tile : city.getTiles()) {
                if (tile.getResource().equals(Resource.HORSE)) return false;
            }
            return true;
        } else if (building.equals(Building.FORGE)) {
            for (Tile tile : city.getTiles()) {
                if (tile.getResource().equals(Resource.IRON)) return false;
            }
            return true;
        } else if (building.equals(Building.WINDMILL)) {
            for (Tile tile : city.getTiles()) {
                if (tile.getType().equals(TerrainType.HILL)) return true;
            }
            return false;
        } else if (building.equals(Building.FACTORY)) {
            for (Resource resource : civilization.getResources()) {
                if (resource.equals(Resource.COAL)) return false;
            }
            return true;
        }
        return false;
    }

    private static String tryPurchaseBuilding(Building building) {
        int buildingGoldCost = 2 * building.getCost();
        if (buildingGoldCost > civilization.getTotalGold())
            return "cant purchase " + building + "; not enough gold";

        if (city.getBuildings().get(building) != null) {
            int remainingCost = city.getBuildings().get(building);
            if (remainingCost <= 0) System.out.println("this building is already built");
            else {
                civilization.setTotalGold(civilization.getTotalGold() - buildingGoldCost);
                city.getBuildings().replace(building, 0);
                civilization.getNotifications().add(city.getInProgressBuilding() + " is built in city: "
                        + city.getName() + ".    time: " + Game.getInstance().getTime());
                if (city.getInProgressBuilding().equals(building)) city.setInProgressBuilding(null);
            }
        } else {
            civilization.setTotalGold(civilization.getTotalGold() - buildingGoldCost);
            city.getBuildings().put(building, 0);
            civilization.getNotifications().add(city.getInProgressBuilding() + " is built in city: "
                    + city.getName() + ".    time: " + Game.getInstance().getTime());
        }
        return "";
    }


    public static Matcher getCityDecision() {
        Matcher matcher;

        while (true) {
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

    private static boolean canCityAttackTo(Tile targetTile) {
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

    private static String tryPurchaseUnit(UnitType unitType) {
        int unitGoldCost = 100;
        if (!civilization.hasReachedTech(unitType.getPrerequisiteTech()))
            return "you haven't reached " + unitType.getPrerequisiteTech() + " yet";

        if (unitGoldCost > civilization.getTotalGold())
            return "not enough gold";

        if ((unitType.isCivilian() && city.getTiles().get(0).getCivilian() != null) ||
                (city.getTiles().get(0).getMilitary() != null && !unitType.isCivilian()))
            return "city is already occupied by another unit. move the unit and try again";

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
        return "";
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
            Citizen citizen = Game.getInstance().getTiles()[x][y].getWorkingCitizen();
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

    private static Building getBuildingFromString(String string) {
        try {
            return Building.valueOf(string);
        } catch (IllegalArgumentException i) {
            return null;
        }
    }

    private static String purchaseTile(Tile targetTile) {
        int necessaryAmountOfGoldForPurchase = targetTile.getCost();
        if (civilization.getTotalGold() >= necessaryAmountOfGoldForPurchase) {
            civilization.setTotalGold(civilization.getTotalGold() - necessaryAmountOfGoldForPurchase);
            city.getTiles().add(targetTile);
            targetTile.setCity(city);
            civilization.getNotifications().add("new tile is purchased. tile x: "
                    + targetTile.getIndexInMapI() + " y: " + targetTile.getIndexInMapJ() + "    time: " + Game.getInstance().getTime());
            return "";
        }
        else
            return "can't purchase tile : not enough gold";
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
        updateCityBuildingsEffects(city);
        city.updateStoredFood();
    }

    private static void updateCityBuildingsEffects(City city) {
        for (Map.Entry<Building, Integer> set : city.getBuildings().entrySet()) {
            if (set.getValue() <= 0) {
                city.setFoodPerTurn(city.getFoodPerTurn() + set.getKey().getFoodAdder() + (int)(set.getKey().getFoodMultiplier() * city.getFoodPerTurn()));
                city.setSciencePerTurn(city.getSciencePerTurn() + set.getKey().getScienceAdder() + (int)(set.getKey().getScienceMultiplier() * city.getSciencePerTurn()));
                city.setGoldPerTurn(city.getGoldPerTurn() + (int)(set.getKey().getGoldMultiplier() * city.getGoldPerTurn()));
                city.setProductionPerTurn(city.getProductionPerTurn() + (int)(set.getKey().getProductionMultiplier() * city.getProductionPerTurn()));
            }
        }
    }

    public static void updateCity(City city) {
        if (city.getHP() < 20) city.setHP(city.getHP() + 1);
        updateCityInfos(city);
        handlePopulation(city);
        updateBorder(city);
        updateProduction(city);
        updateBuildBuildings(city);
        updateBuildingImprovement(city);
        updateRemovingProgress();
        updateRoads();
    }

    private static void updateBuildBuildings(City city) {
        if (city.getInProgressBuilding() != null) {
            int i = city.getBuildings().get(city.getInProgressBuilding());
            i -= city.getProductionPerTurn();
            city.getBuildings().replace(city.getInProgressBuilding(), i);
            if (i <= 0) {
                city.getBuildings().replace(city.getInProgressBuilding(), 0);
                civilization.getNotifications().add(city.getInProgressBuilding() + " is built in city: "
                        + city.getName() + ".    time: " + Game.getInstance().getTime());
                if (city.getInProgressBuilding().equals(Building.WALLS)) city.setHP(city.getHP() + 5);
                else if (city.getInProgressBuilding().equals(Building.CASTLE)) city.setHP(city.getHP() + 8);
                else if (city.getInProgressBuilding().equals(Building.MILITARY_BASE)) city.setHP(city.getHP() + 12);
                city.setInProgressBuilding(null);
            }
        }
    }

    private static void updateRoads() {
        for (Unit unit : civilization.getUnits()) {
            if (unit.getTile().getRouteInProgress() != null
                    && (unit.getTile().getRouteInProgress().getKey().equals("road") || unit.getTile().getRouteInProgress().getKey().equals("railroad"))
                    && unit.getTile().getCivilian() != null && unit.getTile().getCivilian().getType().equals(UnitType.WORKER)
                    && (unit.getTile().getCivilian().getStatus().equals(UnitStatus.BUILD_IMPROVEMENT) || unit.getTile().getCivilian().getStatus().equals(UnitStatus.REPAIR))) {
                int turn = unit.getTile().getRouteInProgress().getValue();
                turn--;
                unit.getTile().setRouteInProgress(new Pair<>(unit.getTile().getRouteInProgress().getKey(), turn));
                if (unit.getTile().getRouteInProgress().getValue() == 0) {
                    unit.getTile().getCivilian().setStatus("active");
                }
            }
        }
    }

    private static void updateRemovingProgress() {
        for (Unit unit : civilization.getUnits()) {
            if (unit.getTile().getRemoveInProgress() != null && unit.getTile().getCivilian() != null
                    && unit.getTile().getCivilian().getType().equals(UnitType.WORKER)
                    && unit.getTile().getCivilian().getStatus().equals(UnitStatus.CLEAR_LAND)) {
                int turn = unit.getTile().getRemoveInProgress().getValue();
                turn--;
                unit.getTile().setRemoveInProgress(new Pair<>(unit.getTile().getRemoveInProgress().getKey(), turn));
                if (turn == 0) {
                    unit.getTile().getCivilian().setStatus("active");
                    if (unit.getTile().getRemoveInProgress().getKey().equals("forest") || unit.getTile().getRemoveInProgress().getKey().equals("jungle"))
                        unit.getTile().setFeature(TerrainFeature.NONE);
                    else unit.getTile().setRouteInProgress(null);
                    unit.getTile().setRemoveInProgress(null);
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
                            + tile.getIndexInMapI() + " y: " + tile.getIndexInMapJ() + ".    time: " + Game.getInstance().getTime());
                    if (tile.getResource() != null) {
                        civilization.getNotifications().add(tile.getResource() + " is achieved.    time: " + Game.getInstance().getTime());
                    }
                }
            }
        }
    }

    private static void updateProduction(City city) {
        if (city.getInProgressUnit() != null) {
            System.out.println("debugging; " + city.getLastCostsUntilNewProductions().containsKey(city.getInProgressUnit()));
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
                civilization.getNotifications().add("The " + city.getName() + "'s population is increased     time: " + Game.getInstance().getTime());
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
                civilization.getNotifications().add("The " + city.getName() + "'s population is decreased     time: " + Game.getInstance().getTime());
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
            civilization.getNotifications().add("The " + city.getName() + "'s border is expanded     time: " + Game.getInstance().getTime());
        }
        if ((city.getCitizens().size() + city.getStoredFood()) == 0) city.setTurnsUntilGrowthBorder(0);
        else
            city.setTurnsUntilGrowthBorder(city.getBorderLastCost() / (city.getCitizens().size() + city.getStoredFood()));
    }

    public static void expandCity(City city) {
        Tile tile = findAppropriateTile(city);
        if (tile == null) return;
        city.getTiles().add(tile);
        tile.setCity(city);
    }

    private static Tile findAppropriateTile(City city) {
        Random random = new Random();
        int n;
        ArrayList<Tile> tiles = new ArrayList<>(city.getTiles());
        while (tiles.size() > 0) {
            n = random.nextInt(tiles.size());
            for (Tile tileNeighbor : tiles.get(n).getNeighbors()) {
                if (tileNeighbor.getCity() == null && (tileNeighbor.getCivilian() == null || tileNeighbor.getCivilian().getCivilization() != city.getCivilization()) &&
                        (tileNeighbor.getMilitary() == null || tileNeighbor.getMilitary().getCivilization() != city.getCivilization()))
                    return tileNeighbor;
            }
            tiles.remove(n);
        }
        return null;
    }

    private void cityAttackToUnit(City city, Unit unit) {

    }

    private static String tryCreateUnit (UnitType unitType) {
        if (!hasReachedTechForUnit(unitType))
            return "haven't reached the necessary tech yet";

        if (!hasEnoughResources(unitType))
            return "you don't have enough resource";

        if (unitType.isCivilian())
            if (city.getTiles().get(0).getCivilian() != null)
                return "city is already occupied by a " +
                        city.getTiles().get(0).getCivilian().getType() +
                        " unit. move the unit and try again";
        else
            if (city.getTiles().get(0).getMilitary() != null)
                return "city is already occupied by a " +
                        city.getTiles().get(0).getMilitary().getType() +
                        " unit. move the unit and try again";

        if (!city.getLastCostsUntilNewProductions().containsKey(unitType))
            city.getLastCostsUntilNewProductions().put(unitType, unitType.getCost());
        city.setInProgressUnit(unitType);
        return "";
    }

    public static boolean canCreateUnit (UnitType unitType) {
        if (!hasReachedTechForUnit(unitType))
            return false;
        if (!hasEnoughResources(unitType))
            return false;
        if (unitType.isCivilian())
            if (city.getTiles().get(0).getCivilian() != null)
                return false;
        else
            if (city.getTiles().get(0).getMilitary() != null)
                return false;
        try {
            int remainingCost = city.getLastCostsUntilNewProductions().get(unitType);
            return remainingCost > 0;
        }
        catch (Exception e) {
            return true;
        }
    }

    private static boolean hasReachedTechForUnit(UnitType unitType) {
        try {
            return civilization.getLastCostUntilNewTechnologies().get(unitType.getPrerequisiteTech()) <= 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean hasReachedTechForBuilding(Building building) {
        try {
            return civilization.getLastCostUntilNewTechnologies().get(building.getPrerequisiteTech()) <= 0;
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

    private static String rangeAttackToUnit(Military targetUnit) {
        targetUnit.setHealth(targetUnit.getHealth() - (city.getCombatStrength() - targetUnit.getCombatStrength()) / 3);
        if (targetUnit.getHealth() <= 0) targetUnit.kill();
        return "";
    }
}
