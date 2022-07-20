package Model.Map;

import Model.ImageBase;
import Model.Technology;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public enum Improvement {
    CAMP(6, new ArrayList<>(Arrays.asList(TerrainType.TUNDRA, TerrainType.HILL, TerrainType.PLAIN)),
            new ArrayList<>(Collections.singletonList(TerrainFeature.JUNGLE)), Technology.TRAPPING, ImageBase.CAMP.getImage()),

    FARM(6, new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.GRASS)),
            null, Technology.AGRICULTURE, ImageBase.FARM.getImage()),

    LUMBER_MILL(6, null, new ArrayList<>(Collections.singletonList(TerrainFeature.JUNGLE)),
            Technology.CONSTRUCTION, ImageBase.LUMBER_MILL.getImage()),

    MINE(6, new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.GRASS, TerrainType.TUNDRA, TerrainType.SNOW, TerrainType.HILL)),
            new ArrayList<>(Arrays.asList(TerrainFeature.JUNGLE, TerrainFeature.FOREST, TerrainFeature.MARSH)), Technology.MINING, ImageBase.MINE.getImage()),

    PASTURE(7, new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.TUNDRA,
            TerrainType.GRASS, TerrainType.HILL)), null, Technology.ANIMAL_HUSBANDRY, ImageBase.PASTURE.getImage()),

    PLANTATION(5, new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.GRASS)),
            new ArrayList<>(Arrays.asList(TerrainFeature.JUNGLE, TerrainFeature.FOREST, TerrainFeature.MARSH, TerrainFeature.DELTA)), Technology.CALENDER, ImageBase.PLANTATION.getImage()),

    QUARRY(7, new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.GRASS, TerrainType.TUNDRA, TerrainType.HILL)),
            null, Technology.MASONRY, ImageBase.QUARRY.getImage()),

    TRADING_POST(8, new ArrayList<>(Arrays.asList(TerrainType.PLAIN, TerrainType.DESERT, TerrainType.GRASS, TerrainType.TUNDRA, TerrainType.HILL)),
            null, Technology.TRAPPING, ImageBase.TRADING_POST.getImage()),

    NONE(0, null, null, null, null);

    private final int constructionTime;
    private final Technology prerequisiteTech;
    private final ArrayList<TerrainType> prerequisiteTypes;
    private final ArrayList<TerrainFeature> prerequisiteFeatures;
    private final Image image;

    Improvement(int constructionTime, ArrayList<TerrainType> prerequisiteTypes, ArrayList<TerrainFeature> prerequisiteFeatures, Technology prerequisiteTech, Image image) {
        this.constructionTime = constructionTime;
        this.prerequisiteTech = prerequisiteTech;
        this.prerequisiteTypes = prerequisiteTypes;
        this.prerequisiteFeatures = prerequisiteFeatures;
        this.image = image;
    }

    public Image getImage() {
        return image;
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
