package Model.Map;

import Model.Technology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public enum Improvement {
    CAMP(6, new ArrayList<>(Arrays.asList(TerrainType.TUNDRA, TerrainType.HILL, TerrainType.PLAIN)), new ArrayList<>(Collections.singletonList(TerrainFeature.JUNGLE)), Technology.TRAPPING),

    FARM(6, new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.GRASS)), null, Technology.AGRICULTURE),

    LUMBER_MILL(6, null, new ArrayList<>(Collections.singletonList(TerrainFeature.JUNGLE)), Technology.CONSTRUCTION),

    MINE(6, new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.GRASS, TerrainType.TUNDRA, TerrainType.SNOW, TerrainType.HILL)),
            new ArrayList<>(Arrays.asList(TerrainFeature.JUNGLE, TerrainFeature.FOREST, TerrainFeature.MARSH)), Technology.MINING),

    PASTURE(7, new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.TUNDRA, TerrainType.GRASS, TerrainType.HILL)), null, Technology.ANIMAL_HUSBANDRY),

    PLANTATION(5, new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.GRASS)),
            new ArrayList<>(Arrays.asList(TerrainFeature.JUNGLE, TerrainFeature.FOREST, TerrainFeature.MARSH, TerrainFeature.DELTA)), Technology.CALENDER),

    QUARRY(7, new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.GRASS, TerrainType.TUNDRA, TerrainType.HILL)), null, Technology.MASONRY),

    TRADING_POST(8, new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.GRASS, TerrainType.TUNDRA, TerrainType.HILL)), null, Technology.TRAPPING),

//    MANUFACTORY(5, new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.GRASS, TerrainType.TUNDRA, TerrainType.SNOW)), null, Technology.ENGINEERING),

//    ROAD(null, null, null),

    NONE(0, null, null, null);

    private final int constructionTime;
    private final Technology prerequisiteTech;
    private final ArrayList <TerrainType> prerequisiteTypes;
    private final ArrayList <TerrainFeature> prerequisiteFeatures;


    Improvement (int constructionTime, ArrayList <TerrainType> prerequisiteTypes, ArrayList <TerrainFeature> prerequisiteFeatures, Technology prerequisiteTech) {
        this.constructionTime = constructionTime;
        this.prerequisiteTech = prerequisiteTech;
        this.prerequisiteTypes = prerequisiteTypes;
        this.prerequisiteFeatures = prerequisiteFeatures;
    }

    public int getConstructionTime() {
        return constructionTime;
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
