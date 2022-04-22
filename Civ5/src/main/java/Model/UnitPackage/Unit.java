package Model.UnitPackage;

import Model.Civilization;
import Model.Map.Tile;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class Unit {
    protected UnitType type ;
    protected Tile tile;
    protected ArrayList<Tile> zonesOfControl;
    protected Civilization civilization;
    protected int MP;
    protected int movesInTurn;
    protected int health;
    protected int cost;
    protected int busyTurns; //holds number of turns that the unit is busy

    public void setType(UnitType type) {
        this.type = type;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void setZonesOfControl(ArrayList<Tile> zonesOfControl) {
        this.zonesOfControl = zonesOfControl;
    }

    public void setCivilization(Civilization civilization) {
        this.civilization = civilization;
    }

    public void setMP(int MP) {
        this.MP = MP;
    }

    public void setMovesInTurn(int movesInTurn) {
        this.movesInTurn = movesInTurn;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setMaintenance(int maintenance) {
        this.maintenance = maintenance;
    }

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

    public void setStatus(Matcher matcher) {
        if (matcher.toString().equals("sleep")) this.status = UnitStatus.SLEEP;
        else if (matcher.toString().equals("found city")) this.status = UnitStatus.FOUNDCITY;
        else if (matcher.toString().equals("cancel mission")) this.status = UnitStatus.ACTIVE; //should it be?
        else if (matcher.toString().equals("wake")) this.status = UnitStatus.ACTIVE;
        else if (matcher.toString().equals("delete")) this.kill();
        else if (matcher.toString().equals("repair")) this.status = UnitStatus.HEAL;
        else if (matcher.toString().startsWith("build")) this.status = UnitStatus.BUILD;
        else if (matcher.toString().startsWith("move")) this.status = UnitStatus.MOVE;
        else if (matcher.toString().startsWith("remove")) this.status = UnitStatus.REMOVE_RESOURCE;
    }

    public void calcMovesTo (Tile dest) {
        this.movesInTurn += dest.getMovementCost();
    }

    //    public void changeStatus (UnitStatus status) { ------> to controller
//        this.status = status;
//    }
}
