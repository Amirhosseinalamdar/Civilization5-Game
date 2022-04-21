package Model.Map;

import java.util.ArrayList;

public class Route {
    public ArrayList <Tile> tiles;

    public Route (Route parent) {
        if (parent != null) this.tiles = new ArrayList<>(parent.tiles);
        else this.tiles = new ArrayList<>();
    }
}
