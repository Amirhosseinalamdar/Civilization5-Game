package Model.UnitPackage;

import Model.Civilization;
import Model.Map.Path;
import Model.Map.Tile;

import java.util.ArrayList;

public class Unit {
    protected UnitType type;
    protected Tile tile;
    protected ArrayList<Tile> zonesOfControl;
    protected Civilization civilization;
    protected int MP;
    protected int movesInTurn;
    protected double health;
    protected int cost;
    public static double MAX_HEALTH = 10;
    protected Path path;

    public Unit(UnitType unitType) {
        this.type = unitType;
        this.status = UnitStatus.ACTIVE;
        this.path = new Path(null);
        this.MP = unitType.getMP();
        this.health = MAX_HEALTH;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return this.path;
    }

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

    public void setHealth(double health) {
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

    public double getHealth() {
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

    public void kill() {
        tile.setCivilian(null);
        civilization.getUnits().remove(this);
        path = null;
    }

    public void setStatus(String string) {
        if (string.equals("has path")) this.status = UnitStatus.HAS_PATH;
        else if (string.equals("sleep")) this.status = UnitStatus.SLEEP;
        else if (string.equals("found city")) this.status = UnitStatus.FOUND_CITY;
        else if (string.equals("cancel mission")) this.status = UnitStatus.CANCEL_MISSION; //should it be?
        else if (string.equals("wake")) this.status = UnitStatus.WAKE;
        else if (string.equals("delete")) this.delete();
        else if (string.startsWith("repair")) this.status = UnitStatus.REPAIR;
        else if (string.startsWith("build")) this.status = UnitStatus.BUILD_IMPROVEMENT;
        else if (string.startsWith("move")) this.status = UnitStatus.MOVE;
        else if (string.startsWith("clear")) this.status = UnitStatus.CLEAR_LAND;
        else if (string.equals("do nothing")) this.status = UnitStatus.DO_NOTHING;
        else if (string.equals("active")) this.status = UnitStatus.ACTIVE;
    }

    private void delete() {
        this.kill();
        civilization.setTotalGold(civilization.getTotalGold() + 5);
    }

    public void updateMovesInTurn(Tile dest) {
        if (dest.isEnemyZoneOfControl(this.civilization)) {
            this.movesInTurn = MP;
            return;
        }
        if (dest.isRoughTerrain() && type.equals(UnitType.CHARIOT_ARCHER)) {
            this.movesInTurn = MP;
            return;
        }
        this.movesInTurn += dest.getMovementCost();
        if (this.type.equals(UnitType.SCOUT))
            this.movesInTurn -= dest.getFeature().getMovementCost();
    }

    //    public void changeStatus (UnitStatus status) { ------> to controller
//        this.status = status;
//    }

    public boolean hasRemainingMoves() {
        return movesInTurn < MP;
    }
}
