package Model.Map;

import com.google.gson.annotations.Expose;

public class Citizen {
    @Expose(serialize = false, deserialize = false)
    private Tile tile;
    @Expose(serialize = false, deserialize = false)
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
