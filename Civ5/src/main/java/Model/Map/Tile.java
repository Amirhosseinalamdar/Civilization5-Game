package Model.Map;

import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import de.scravy.pair.Pair;

import java.util.ArrayList;

public class Tile {
    private TerrainType type;
    private TerrainFeature feature;
    private int foodPerTurn;
    private int goldPerTurn;
    private int productionPerTurn;
    private int movementCost;
    private double combatEffect;
    private Military military;
    private Unit civilian;
    private Pair <Improvement, Integer> improvementInProgress;
    private boolean isRaided;
    //private boolean isCity;
    private City city; // age null bashe city nis
    private ArrayList<River> rivers;

}
