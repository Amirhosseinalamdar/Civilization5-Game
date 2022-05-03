package Model.Map;

import Model.Technology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public enum Improvement {
    CAMP(new ArrayList<>(Arrays.asList(TerrainType.TUNDRA, TerrainType.HILL, TerrainType.PLAIN)), new ArrayList<>(Collections.singletonList(TerrainFeature.JUNGLE)), Technology.TRAPPING),

    FARM(new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.GRASS)), null, Technology.AGRICULTURE),

    LUMBER_MILL(null, new ArrayList<>(Collections.singletonList(TerrainFeature.JUNGLE)), Technology.CONSTRUCTION),

    MINE(new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.GRASS, TerrainType.TUNDRA, TerrainType.SNOW, TerrainType.HILL)),
            new ArrayList<>(Arrays.asList(TerrainFeature.JUNGLE, TerrainFeature.FOREST, TerrainFeature.SWAMP)), Technology.MINING),

    PASTURE(new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.TUNDRA, TerrainType.GRASS, TerrainType.HILL)), null, Technology.ANIMAL_HUSBANDRY),

    SEEDING_AND_WORKING(new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.GRASS)),
            new ArrayList<>(Arrays.asList(TerrainFeature.JUNGLE, TerrainFeature.FOREST, TerrainFeature.SWAMP, TerrainFeature.DELTA)), Technology.CALENDER),

    STONE_MINE(new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.GRASS, TerrainType.TUNDRA, TerrainType.HILL)), null, Technology.MASONRY),

    TRADING_POST(new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.GRASS, TerrainType.TUNDRA, TerrainType.HILL)), null, Technology.TRAPPING),

    FACTORY(new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.GRASS, TerrainType.TUNDRA, TerrainType.SNOW)), null, Technology.ENGINEERING),

    ROAD(null, null, null),

    NONE(null, null, null);

    private final Technology prerequisiteTech;
    private final ArrayList <TerrainType> prerequisiteTypes;
    private final ArrayList <TerrainFeature> prerequisiteFeatures;


    Improvement (ArrayList <TerrainType> prerequisiteTypes, ArrayList <TerrainFeature> prerequisiteFeatures, Technology prerequisiteTech) {
        this.prerequisiteTech = prerequisiteTech;
        this.prerequisiteTypes = prerequisiteTypes;
        this.prerequisiteFeatures = prerequisiteFeatures;
    }

    public Technology getPrerequisiteTech() {
        return prerequisiteTech;
    }

    public ArrayList<TerrainType> getPrerequisiteTypes() {
        return prerequisiteTypes;
    }

    public ArrayList<TerrainFeature> getPrerequisiteFeatures() {
        return prerequisiteFeatures;
    }
}
