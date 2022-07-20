package Model.UnitPackage;

import Controller.GameController;
import Model.Civilization;
import Model.ImageBase;
import Model.Map.Path;
import Model.Map.Tile;
import View.Controller.MapController;
import View.GameMenu;
import com.google.gson.annotations.Expose;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Unit extends ImageView {
    @Expose
    protected UnitType type;
    @Expose
    protected Tile tile;
    @Expose(deserialize = false, serialize = false)
    protected Civilization civilization;
    @Expose
    protected int MP;
    @Expose
    protected int movesInTurn;
    @Expose
    protected double health;
    @Expose
    protected int cost;
    @Expose
    public static double MAX_HEALTH = 10;
    @Expose
    protected Path path;
    @Expose
    protected UnitStatus status;
    @Expose
    protected int maintenance;
    @Expose
    public static final double healRate = 1.5;

    public Unit(UnitType unitType) {
        this.type = unitType;
        this.status = UnitStatus.ACTIVE;
        this.path = new Path(null);
        this.MP = unitType.getMP();
        this.health = MAX_HEALTH;
        this.setImage(ImageBase.valueOf(unitType.toString()).getImage());
        this.setFitWidth(50);
        this.setFitHeight(50);
        setOnMouseEntered(event -> {
            setStyle("-fx-cursor: hand;");
        });
        setOnMouseClicked(event -> {
            System.out.println("clicked duh");
            MapController mapController = GameMenu.getGameMapController();
            if(!(mapController.getChosenUnit() != null && mapController.getChosenUnit().getStatus() == UnitStatus.ATTACK)) {
                try {
                    System.out.println("my type = " + type + ", chosen type = " + mapController.getChosenUnit().getType());
                } catch (Exception e) {
                    System.out.println("my type only = " + type);
                }
                if (mapController.getChosenUnit() == null) {
                    mapController.setChosenUnit(this);
                    if (!civilization.equals(GameController.getCivilization()))
                        mapController.setChosenUnit(null);

                    if (mapController.getChosenUnit() != null) {
                        mapController.showUserPanelDownLeft();
                        mapController.showUnitAvatar();
                        if (type.isCivilian()) mapController.showCivilianOptions();
                        else mapController.showMilitaryOptions();
                    }
                } else {
                    if (mapController.getChosenUnit().equals(this)) {
                        mapController.setChosenUnit(null);
                        mapController.hideUnitAvatar();
                        mapController.hideUnitOptions();
                        mapController.showMap();
                    }
                    else if (civilization.equals(GameController.getCivilization())) {
                        mapController.setChosenUnit(this);
                        mapController.hideUnitAvatar();
                        mapController.hideUnitOptions();
                        mapController.showMap();
                        mapController.showUserPanelDownLeft();
                        mapController.showUnitAvatar();
                        if (type.isCivilian()) mapController.showCivilianOptions();
                        else mapController.showMilitaryOptions();
                    }
                }
            }
        });
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return this.path;
    }

    public void setTile (Tile tile) {
        this.tile = tile;
        System.out.println("ok tile is set, " + tile.getX() + ", " + tile.getY());
        if (type.isCivilian()) {
            this.setX(tile.getX() + 65);
            this.setY(tile.getY() + 40);
        }
        else {
            this.setX(tile.getX() + 10);
            this.setY(tile.getY() + 40);
        }
    }

    public void initTile (Tile tile) {
        this.tile = tile;
        System.out.println("tile initialized");
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

    public void realSetStatus(UnitStatus unitStatus){
        this.status = unitStatus;
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
