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
    protected int health;
    protected int cost;
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

    //    public void changeStatus (UnitStatus status) { ------> to controller
//        this.status = status;
//    }


    public UnitType getType() {
        return type;
    }
}
