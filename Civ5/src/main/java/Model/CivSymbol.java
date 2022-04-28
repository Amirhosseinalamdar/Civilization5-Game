package Model;

public enum CivSymbol {
    WHITE("\033[1;97m"),
    PURPLE("\033[1;95m"),
    BLUE("\033[1;94m"),
    RED("\033[1;91m"),
    BLACK("\033[1;90m");


    private String symbol;
    private boolean isTaken;

    CivSymbol(String symbol) {
        this.symbol = symbol;
        this.isTaken = false;
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
