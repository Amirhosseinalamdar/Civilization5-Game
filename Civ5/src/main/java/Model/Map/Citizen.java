package Model.Map;

import com.google.gson.annotations.Expose;

public class Citizen {
    @Expose
    private Tile tile;
    private City city;

    public Citizen(City city, Tile tile) {
        this.city = city;
        this.tile = tile;
    }

    public void changeWorkingTile(Tile newTile) {
        this.tile = newTile;
    }

    public City getCity() {
        return city;
    }

    public Tile getTile() {
        return tile;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
