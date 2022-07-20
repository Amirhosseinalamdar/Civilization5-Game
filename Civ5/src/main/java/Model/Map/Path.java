package Model.Map;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Path {
    @Expose
    public ArrayList<Tile> tiles;

    public Path(Path parent) {
        if (parent != null) this.tiles = new ArrayList<>(parent.tiles);
        else this.tiles = new ArrayList<>();
    }
}
