package Controller;

import Model.Civilization;
import Model.Game;
import Model.Map.*;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitType;
import View.Commands;
import View.GameMenu;

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

        if (matcher.pattern().toString().startsWith("create")) {
            UnitType type = getUnitTypeFromString(matcher.group("unitName"));
            if (type == null) {
                GameMenu.noSuchUnitType();
                return;
            }
            tryCreateUnit(type);
        }

        if (matcher.pattern().toString().startsWith("purchase tile")) {
            Tile targetTile = Game.getTiles()[Integer.parseInt(matcher.group("x"))][Integer.parseInt(matcher.group("y"))];
            if (tileIsPurchasable(targetTile))
                purchaseTile(targetTile);
        }

        if (matcher.pattern().toString().startsWith("lock citizen on tile")) {
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
        if (matcher.pattern().toString().startsWith("show")) GameMenu.showCityOutput(city);
    }

    public static Matcher getCityDecision() {
        Matcher matcher;

        while (true) {
            String command = GameMenu.nextCommand();
            if ((matcher = Commands.getMatcher(command, Commands.CREATE_UNIT)) != null) {
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
            if ((matcher = Commands.getMatcher(command, Commands.SHOW_OUTPUT)) != null) {
                return matcher;
            }
            System.out.println("city decision wasn't valid");
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
        //view show options and check enough tiles //TODO
        //purchase that option
        int necessaryAmountOfGoldForPurchase = targetTile.getGoldPerTurn() * 3 + targetTile.getProductionPerTurn() +
                targetTile.getFoodPerTurn() * 2;
        if (civilization.getTotalGold() >= necessaryAmountOfGoldForPurchase) {
            civilization.setTotalGold(civilization.getTotalGold() - necessaryAmountOfGoldForPurchase);
            city.getTiles().add(targetTile);
            targetTile.setCity(city);
        } else
            GameMenu.notEnoughGoldForTilePurchase();
    }

    private void askForNewProduction(City city) {

    }

    public static void updateCityInfos(City city) {
        int food = 0;
        int production = 0;
        int gold = 0;
        int science = 0;
        for (Citizen citizen : city.getCitizens()) {
            if (citizen.getTile() == null) production++;
            else {
                food += citizen.getTile().getFoodPerTurn();
                production += citizen.getTile().getProductionPerTurn();
                gold += citizen.getTile().getGoldPerTurn();
            }
        }
        if (city.getCityStatus() == CityStatus.CAPITAL) science += 3;
        science += city.getCitizens().size();
        city.setFoodPerTurn(food);
        city.setProductionPerTurn(production);
        city.setGoldPerTurn(gold);
        city.setSciencePerTurn(science);
        city.updateStoredFood();
        handlePopulation(city);
        updateBorder(city);
        //TODO production
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
        Random random = new Random();
        int n;
        ArrayList<Tile> tiles = new ArrayList<>(city.getTiles());
        while (tiles.size() > 0) {
            n = random.nextInt(tiles.size());
            for (Tile tileNeighbor : GameController.getTileNeighbors(tiles.get(n))) {
                if (tileNeighbor.getCity() == null && (tileNeighbor.getCivilian() == null || tileNeighbor.getCivilian().getCivilization() != city.getCivilization()) &&
                        (tileNeighbor.getMilitary() == null || tileNeighbor.getMilitary().getCivilization() != city.getCivilization())) return tileNeighbor;
            }
            tiles.remove(n);
        }
        return null;
    }

    private void cityAttackToUnit(City city, Unit unit) {

    }

    private static void tryCreateUnit(UnitType unitType) {
        if (!hasReachedTechForUnit(unitType)) {
            GameMenu.unreachedTech();
            return;
        }
        if (!hasEnoughResources(unitType)) {
            GameMenu.notEnoughResource();
            return;
        }
        try {
            city.getTurnsUntilNewProductions().get(unitType);
            System.out.println("already in progress"); //TODO... view
        }
        catch (Exception e) {
            city.getTurnsUntilNewProductions().put(unitType, calcTurnsForNewUnit(unitType));
        }
        city.getTurnsUntilNewProductions().put(unitType, calcTurnsForNewUnit(unitType));
        city.setInProgressUnit(unitType);
    }

    private static int calcTurnsForNewUnit(UnitType unitType) {
        return unitType.getCost() / city.getProductionPerTurn();
    }

    private static boolean hasReachedTechForUnit(UnitType unitType) {
        try {
            return civilization.getLastCostUntilNewTechnologies().get(unitType.getTechnology()) <= 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean hasEnoughResources(UnitType unitType) {
        for (Resource r : civilization.getResources())
            if (r.equals(unitType.getResource()))
                return true;
        return false;
        //TODO...
    }
}
