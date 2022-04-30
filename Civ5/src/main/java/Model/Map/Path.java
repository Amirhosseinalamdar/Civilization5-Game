package Model.Map;

import java.util.ArrayList;

public class Path {
    public ArrayList<Tile> tiles;

    public Path(Path parent) {
        if (parent != null) this.tiles = new ArrayList<>(parent.tiles);
        else this.tiles = new ArrayList<>();
    }
}
