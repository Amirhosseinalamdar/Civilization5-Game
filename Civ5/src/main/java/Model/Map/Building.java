package Model.Map;

import Model.Technology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public enum Building {

    BARRACKS(80, 1, 0, 0, 0, 0, 0, 0, 0, Technology.BRONZE_WORKING, null),
    GRANARY(100, 1, 2, 0, 0, 0, 0, 0, 0, Technology.POTTERY, null),
    LIBRARY(80, 1, 0, 1, 0, 0, 0, 0, 0, Technology.WRITING, null),
    MONUMENT(60, 1, 0, 0, 0, 0, 0, 0, 0, null, null),
    WALLS(100, 1, 0, 0, 0, 0, 0, 0, 0, Technology.MASONRY, null),
    WATER_MILL(120, 2, 2, 0, 0, 0, 0, 0, 0, Technology.WHEEL, null),
    ARMORY(130, 3, 0, 0, 0, 0, 0, 0, 0, Technology.IRON_WORKING, new ArrayList<>(Collections.singletonList(BARRACKS))),
    BURIAL_TOMB(120, 0, 0, 0, 2, 0, 1, 0, 0, Technology.PHILOSOPHY, null),
    CIRCUS(150, 3, 0, 0, 3, 0, 0, 0, 0, Technology.HORSEBACK_RIDING, null),
    COLOSSEUM(150, 3, 0, 0, 4, 0, 0, 0, 0, Technology.CONSTRUCTION, null),
    COURTHOUSE(200, 5, 0, 0, 0, 0, 0, 0, 0, Technology.MATHEMATICS, null),
    STABLE(100, 1, 0, 0, 0, 0, 0, 0.25, 0, Technology.HORSEBACK_RIDING, null),
    TEMPLE(120, 2, 0, 0, 0, 0, 0, 0, 0, Technology.PHILOSOPHY, new ArrayList<>(Collections.singletonList(MONUMENT))),
    CASTLE(200, 3, 0, 0, 0, 0, 0, 0, 0, Technology.CHIVALRY, new ArrayList<>(Collections.singletonList(WALLS))),
    FORGE(150, 2, 0, 0, 0, 0, 0, 0.15, 0, Technology.METAL_CASTING, null),
    GARDEN(120, 2, 0, 0, 0, 0, 0, 0, 0, Technology.THEOLOGY, null),
    MARKET(120, 0, 0, 0, 0, 0, 0.25, 0, 0, Technology.CURRENCY, null),
    MINT(120, 0, 0, 0, 0, 0, 1, 0, 0, Technology.CURRENCY, null),
    MONASTERY(120, 2, 0, 0, 0, 0, 0, 0, 0, Technology.THEOLOGY, null),
    UNIVERSITY(200, 3, 0, 2, 0, 0, 0, 0, 0.5, Technology.EDUCATION, new ArrayList<>(Collections.singletonList(LIBRARY))),
    WORKSHOP(100, 2, 0, 0, 0, 0, 0, 0.2, 0, Technology.METAL_CASTING, null),
    BANK(220, 0, 0, 0, 0, 0, 0.25, 0, 0, Technology.BANKING, new ArrayList<>(Collections.singletonList(MARKET))),
    MILITARY_ACADEMY(350, 3, 0, 0, 0, 0, 0, 0, 0, Technology.MILITARY_SCIENCE, new ArrayList<>(Collections.singletonList(BARRACKS))),
    OPERA_HOUSE(220, 3, 0, 0, 0, 0, 0, 0, 0, Technology.ACOUSTICS, new ArrayList<>(Arrays.asList(TEMPLE, BURIAL_TOMB))),
    MUSEUM(350, 3, 0, 0, 0, 0, 0, 0, 0, Technology.ARCHAEOLOGY, new ArrayList<>(Collections.singletonList(OPERA_HOUSE))),
    PUBLIC_SCHOOL(350, 3, 0, 0, 0, 0, 0, 0, 0.5, Technology.SCIENTIFIC_THEORY, new ArrayList<>(Collections.singletonList(UNIVERSITY))),
    SATRAPS_COURT(220, 0, 0, 0, 2, 0, 0.25, 0, 0, Technology.BANKING, new ArrayList<>(Collections.singletonList(MARKET))),
    THEATER(300, 5, 0, 0, 4, 0, 0, 0, 0, Technology.PRINTING_PRESS, new ArrayList<>(Collections.singletonList(COLOSSEUM))),
    WINDMILL(180, 2, 0, 0, 0, 0, 0, 0.15, 0, Technology.ECONOMICS, null),
    ARSENAL(350, 3, 0, 0, 0, 0, 0, 0.2, 0, Technology.RAILROAD, new ArrayList<>(Collections.singletonList(MILITARY_ACADEMY))),
    BROADCAST_TOWER(600, 3, 0, 0, 0, 0, 0, 0, 0, Technology.RADIO, new ArrayList<>(Collections.singletonList(MUSEUM))),
    FACTORY(300, 3, 0, 0, 0, 0, 0, 0.5, 0, Technology.STEAM_POWER, null),
    HOSPITAL(400, 2, 0, 0, 0, -0.5, 0, 0, 0, Technology.BIOLOGY, null),
    MILITARY_BASE(450, 4, 0, 0, 0, 0, 0, 0, 0, Technology.TELEGRAPH, new ArrayList<>(Collections.singletonList(CASTLE))),
    STOCK_EXCHANGE(650, 0, 0, 0, 0, 0, 0.33, 0, 0, Technology.ELECTRICITY, new ArrayList<>(Arrays.asList(BANK, SATRAPS_COURT)));

    private final int cost;
    private final int maintenance;
    private final int foodAdder;
    private final int scienceAdder;
    private final int happinessAdder;
    private final double foodMultiplier;
    private final double goldMultiplier;
    private final double productionMultiplier;
    private final double scienceMultiplier;
    private final Technology prerequisiteTech;
    private final ArrayList<Building> prerequisiteBuildings;

    Building(int cost, int maintenance, int foodAdder, int scienceAdder, int happinessAdder,
             double foodMultiplier, double goldMultiplier, double productionMultiplier, double scienceMultiplier,
             Technology prerequisiteTech, ArrayList<Building> prerequisiteBuildings) {
        this.cost = cost;
        this.maintenance = maintenance;
        this.foodAdder = foodAdder;
        this.scienceAdder = scienceAdder;
        this.happinessAdder = happinessAdder;
        this.foodMultiplier = foodMultiplier;
        this.goldMultiplier = goldMultiplier;
        this.productionMultiplier = productionMultiplier;
        this.scienceMultiplier = scienceMultiplier;
        this.prerequisiteTech = prerequisiteTech;
        this.prerequisiteBuildings = prerequisiteBuildings;
    }

    public int getCost() {
        return cost;
    }

    public int getMaintenance() {
        return maintenance;
    }

    public int getFoodAdder() {
        return foodAdder;
    }

    public int getScienceAdder() {
        return scienceAdder;
    }

    public int getHappinessAdder() {
        return happinessAdder;
    }

    public double getFoodMultiplier() {
        return foodMultiplier;
    }

    public double getGoldMultiplier() {
        return goldMultiplier;
    }

    public double getProductionMultiplier() {
        return productionMultiplier;
    }

    public double getScienceMultiplier() {
        return scienceMultiplier;
    }

    public Technology getPrerequisiteTech() {
        return prerequisiteTech;
    }

    public ArrayList<Building> getPrerequisiteBuildings() {
        return prerequisiteBuildings;
    }
}
