package Model.Map;

import Model.ImageBase;
import Model.Technology;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public enum Building {

    BARRACKS(80, 1, 0, 0, 0, 0, 0, 0, 0, Technology.BRONZE_WORKING, null, ImageBase.BARRACKS.getImage()),
    GRANARY(100, 1, 2, 0, 0, 0, 0, 0, 0, Technology.POTTERY, null, ImageBase.GRANARY.getImage()),
    LIBRARY(80, 1, 0, 1, 0, 0, 0, 0, 0, Technology.WRITING, null, ImageBase.LIBRARY.getImage()),
    MONUMENT(60, 1, 0, 0, 0, 0, 0, 0, 0, null, null, ImageBase.MONUMENT.getImage()),
    WALLS(100, 1, 0, 0, 0, 0, 0, 0, 0, Technology.MASONRY, null, ImageBase.WALLS.getImage()),
    WATER_MILL(120, 2, 2, 0, 0, 0, 0, 0, 0, Technology.WHEEL, null, ImageBase.WATER_MILL.getImage()),
    ARMORY(130, 3, 0, 0, 0, 0, 0, 0, 0, Technology.IRON_WORKING, new ArrayList<>(Collections.singletonList(BARRACKS)), ImageBase.ARMORY.getImage()),
    BURIAL_TOMB(120, 0, 0, 0, 2, 0, 1, 0, 0, Technology.PHILOSOPHY, null, ImageBase.BURIAL_TOMB.getImage()),
    CIRCUS(150, 3, 0, 0, 3, 0, 0, 0, 0, Technology.HORSEBACK_RIDING, null, ImageBase.CIRCUS.getImage()),
    COLOSSEUM(150, 3, 0, 0, 4, 0, 0, 0, 0, Technology.CONSTRUCTION, null, ImageBase.COLOSSEUM.getImage()),
    COURTHOUSE(200, 5, 0, 0, 0, 0, 0, 0, 0, Technology.MATHEMATICS, null, ImageBase.COURTHOUSE.getImage()),
    STABLE(100, 1, 0, 0, 0, 0, 0, 0.25, 0, Technology.HORSEBACK_RIDING, null, ImageBase.STABLE.getImage()),
    TEMPLE(120, 2, 0, 0, 0, 0, 0, 0, 0, Technology.PHILOSOPHY, new ArrayList<>(Collections.singletonList(MONUMENT)), ImageBase.TEMPLE.getImage()),
    CASTLE(200, 3, 0, 0, 0, 0, 0, 0, 0, Technology.CHIVALRY, new ArrayList<>(Collections.singletonList(WALLS)), ImageBase.CASTLE.getImage()),
    FORGE(150, 2, 0, 0, 0, 0, 0, 0.15, 0, Technology.METAL_CASTING, null, ImageBase.FORGE.getImage()),
    GARDEN(120, 2, 0, 0, 0, 0, 0, 0, 0, Technology.THEOLOGY, null, ImageBase.GARDEN.getImage()),
    MARKET(120, 0, 0, 0, 0, 0, 0.25, 0, 0, Technology.CURRENCY, null, ImageBase.MARKET.getImage()),
    MINT(120, 0, 0, 0, 0, 0, 1, 0, 0, Technology.CURRENCY, null, ImageBase.MINT.getImage()),
    MONASTERY(120, 2, 0, 0, 0, 0, 0, 0, 0, Technology.THEOLOGY, null, ImageBase.MONASTERY.getImage()),
    UNIVERSITY(200, 3, 0, 2, 0, 0, 0, 0, 0.5, Technology.EDUCATION, new ArrayList<>(Collections.singletonList(LIBRARY)), ImageBase.UNIVERSITY.getImage()),
    WORKSHOP(100, 2, 0, 0, 0, 0, 0, 0.2, 0, Technology.METAL_CASTING, null, ImageBase.WORKSHOP.getImage()),
    BANK(220, 0, 0, 0, 0, 0, 0.25, 0, 0, Technology.BANKING, new ArrayList<>(Collections.singletonList(MARKET)), ImageBase.BANK.getImage()),
    MILITARY_ACADEMY(350, 3, 0, 0, 0, 0, 0, 0, 0, Technology.MILITARY_SCIENCE, new ArrayList<>(Collections.singletonList(BARRACKS)), ImageBase.MILITARY_ACADEMY.getImage()),
    OPERA_HOUSE(220, 3, 0, 0, 0, 0, 0, 0, 0, Technology.ACOUSTICS, new ArrayList<>(Arrays.asList(TEMPLE, BURIAL_TOMB)), ImageBase.OPERA_HOUSE.getImage()),
    MUSEUM(350, 3, 0, 0, 0, 0, 0, 0, 0, Technology.ARCHAEOLOGY, new ArrayList<>(Collections.singletonList(OPERA_HOUSE)), ImageBase.MUSEUM.getImage()),
    PUBLIC_SCHOOL(350, 3, 0, 0, 0, 0, 0, 0, 0.5, Technology.SCIENTIFIC_THEORY, new ArrayList<>(Collections.singletonList(UNIVERSITY)), ImageBase.PUBLIC_SCHOOL.getImage()),
    SATRAPS_COURT(220, 0, 0, 0, 2, 0, 0.25, 0, 0, Technology.BANKING, new ArrayList<>(Collections.singletonList(MARKET)), ImageBase.SATRAPS_COURT.getImage()),
    THEATER(300, 5, 0, 0, 4, 0, 0, 0, 0, Technology.PRINTING_PRESS, new ArrayList<>(Collections.singletonList(COLOSSEUM)), ImageBase.THEATER.getImage()),
    WINDMILL(180, 2, 0, 0, 0, 0, 0, 0.15, 0, Technology.ECONOMICS, null, ImageBase.WINDMILL.getImage()),
    ARSENAL(350, 3, 0, 0, 0, 0, 0, 0.2, 0, Technology.RAILROAD, new ArrayList<>(Collections.singletonList(MILITARY_ACADEMY)), ImageBase.ARSENAL.getImage()),
    BROADCAST_TOWER(600, 3, 0, 0, 0, 0, 0, 0, 0, Technology.RADIO, new ArrayList<>(Collections.singletonList(MUSEUM)), ImageBase.BROADCAST_TOWER.getImage()),
    FACTORY(300, 3, 0, 0, 0, 0, 0, 0.5, 0, Technology.STEAM_POWER, null, ImageBase.FACTORY.getImage()),
    HOSPITAL(400, 2, 0, 0, 0, -0.5, 0, 0, 0, Technology.BIOLOGY, null, ImageBase.HOSPITAL.getImage()),
    MILITARY_BASE(450, 4, 0, 0, 0, 0, 0, 0, 0, Technology.TELEGRAPH, new ArrayList<>(Collections.singletonList(CASTLE)), ImageBase.MILITARY_BASE.getImage()),
    STOCK_EXCHANGE(650, 0, 0, 0, 0, 0, 0.33, 0, 0, Technology.ELECTRICITY, new ArrayList<>(Arrays.asList(BANK, SATRAPS_COURT)), ImageBase.STOCK_EXCHANGE.getImage());

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
    private final Image image;

    Building(int cost, int maintenance, int foodAdder, int scienceAdder, int happinessAdder,
             double foodMultiplier, double goldMultiplier, double productionMultiplier, double scienceMultiplier,
             Technology prerequisiteTech, ArrayList<Building> prerequisiteBuildings, Image image) {
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
        this.image = image;
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

    public Image getImage() {
        return image;
    }
}
