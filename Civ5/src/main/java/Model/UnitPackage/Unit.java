package Model.UnitPackage;

import Model.Civilization;
import Model.Map.Tile;

import java.util.ArrayList;

public class Unit {
    protected UnitType type ;
    protected Tile tile;
    protected ArrayList<Tile> zonesOfControl;
    protected Civilization civilization;
    protected int MP;
    protected int movesInTurn;
    protected int health;
    protected int cost;
    protected
    public int getMovesInTurn() {
        return movesInTurn;
    }

    public UnitType getType() {
        return type;
    }

    public Tile getTile() {
        return tile;
    }

    public ArrayList<Tile> getZonesOfControl() {
        return zonesOfControl;
    }

    public Civilization getCivilization() {
        return civilization;
    }

    public int getMP() {
        return MP;
    }

    public int getHealth() {
        return health;
    }

    public int getCost() {
        return cost;
    }

    public UnitStatus getStatus() {
        return status;
    }

    public int getMaintenance() {
        return maintenance;
    }

    protected UnitStatus status;
    protected int maintenance;

    public void kill(){
        //TODO remove from everywhere
    }

    //TODO change civilization -> controller

    public void init(){
        //this.tile.setCivilian(this);
        //this.civilization.units.add(this);
    }

    public void setStatus(UnitStatus status) {
        this.status = status;
    }

    public void doMove() {
        movesInTurn++;
    }

    //    public void changeStatus (UnitStatus status) { ------> to controller
//        this.status = status;
//    }
}
