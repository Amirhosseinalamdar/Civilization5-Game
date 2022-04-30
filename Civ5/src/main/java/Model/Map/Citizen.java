package Model.Map;

public class Citizen {
    private Tile tile;
    private final City city;

    public Citizen (City city, Tile tile) {
        this.city = city;
        this.tile = tile;
    }

    public void changeWorkingTile (Tile newTile) {
        this.tile = newTile;
    }

    public City getCity() {
        return city;
    }

    public Tile getTile() {
        return tile;
    }
}
