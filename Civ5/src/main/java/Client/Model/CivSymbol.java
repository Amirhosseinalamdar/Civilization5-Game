package Client.Model;

import javafx.scene.paint.Color;

public enum CivSymbol {
    BLUE(null, Color.rgb(0, 0, 255)),
    RED(null, Color.rgb(255, 0, 0)),
    BLACK(null, Color.rgb(0, 0, 0)),
    WHITE(null, Color.rgb(255, 255, 255)),
    GREEN(null, Color.rgb(0, 255, 0));

    public Color getColor() {
        return color;
    }

    private String symbol;
    private boolean isTaken;
    private Color color;

    CivSymbol(String symbol, Color color) {
        this.symbol = symbol;
        this.isTaken = false;
        this.color = color;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void setTaken(boolean taken) {
        isTaken = taken;
    }
}
