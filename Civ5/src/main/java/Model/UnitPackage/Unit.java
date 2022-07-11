package Model.UnitPackage;

import Model.Civilization;
import Model.Map.Path;
import Model.Map.Tile;
import com.google.gson.annotations.Expose;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Unit extends ImageView {
    @Expose(serialize = true, deserialize = true)
    protected UnitType type;
    @Expose(serialize = true, deserialize = true)
    protected Tile tile;
    @Expose(deserialize = false, serialize = false)
    protected Civilization civilization;
    @Expose(serialize = true, deserialize = true)
    protected int MP;
    @Expose(serialize = true, deserialize = true)
    protected int movesInTurn;
    @Expose(serialize = true, deserialize = true)
    protected double health;
    @Expose(serialize = true, deserialize = true)
    protected int cost;
    @Expose(serialize = true, deserialize = true)
    public static double MAX_HEALTH = 10;
    @Expose(serialize = true, deserialize = true)
    protected Path path;
    @Expose(serialize = true, deserialize = true)
    protected UnitStatus status;
    @Expose(serialize = true, deserialize = true)
    protected int maintenance;

    public Unit(UnitType unitType) {
        this.type = unitType;
        this.status = UnitStatus.ACTIVE;
        this.path = new Path(null);
        this.MP = unitType.getMP();
        this.health = MAX_HEALTH;
        System.out.println("/Images/" + unitType);
        this.setImage(new Image(this.getClass().getResource("/Images/units/" + unitType + ".png").toExternalForm()));
        this.setFitWidth(50);
        this.setFitHeight(50);
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return this.path;
    }

    public void setTile(Tile tile) {
        System.out.println("ok tile is set, " + tile.getX() + ", " + tile.getY());
        this.tile = tile;
        if (type.isCivilian()) {
            this.setX(tile.getX() + 65);
            this.setY(tile.getY() + 40);
        }
        else {
            this.setX(tile.getX() + 10);
            this.setY(tile.getY() + 40);
        }
    }

    public void setCivilization(Civilization civilization) {
        if (this.civilization != null)
            this.civilization.getUnits().remove(this);
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

    public int getMovesInTurn() {
        return movesInTurn;
    }

    public UnitType getType() {
        return type;
    }

    public Tile getTile() {
        return tile;
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

    public UnitStatus getStatus() {
        return status;
    }

    public void kill() {
        tile.setCivilian(null);
        civilization.getUnits().remove(this);
        path = null;
    }

    public void setStatus(String string) {
        if (string.equals("has path")) this.status = UnitStatus.HAS_PATH;
        else if (string.equals("sleep")) this.status = UnitStatus.SLEEP;
        else if (string.equals("found city")) this.status = UnitStatus.FOUND_CITY;
        else if (string.equals("cancel mission")) this.status = UnitStatus.CANCEL_MISSION;
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
        if (dest.isEnemyZoneOfControl(this.civilization)
                || (tile.getIndexInMapJ() < dest.getIndexInMapJ() && dest.isRiverAtLeft())
                || (tile.getIndexInMapJ() > dest.getIndexInMapJ() && tile.isRiverAtLeft())
        ) {
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

    public boolean hasRemainingMoves() {
        if (movesInTurn >= MP) this.setStatus("active");
        return movesInTurn < MP;
    }
}
