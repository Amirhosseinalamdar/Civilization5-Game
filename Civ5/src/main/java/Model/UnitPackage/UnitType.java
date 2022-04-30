package Model.UnitPackage;

public enum UnitType {
    ARCHER(70, 2), CHARIOT_ARCHER(60, 4), SCOUT(25, 2),
    SETTLER(89, 2), SPEARMAN(50, 2), WARRIOR(40, 2),
    WORKER(70, 2), CATAPULT(100, 2), HORSEMAN(80, 4),
    SWORDSMAN(80, 2), CROSSBOWMAN(120, 2), KNIGHT(150, 3),
    LONGSWORDSMAN(150, 3), PIKEMAN(100, 2), TREBUCHET(170, 2),
    CANNON(250, 2), CAVALRY(250, 3), LANCER(220, 4),
    MUSKETMAN(120, 2), RIFLEMAN(200, 2), ANTI_TANK_GUN(300, 2),
    ARTILLERY(420, 2), INFANTRY(300, 2), PANZER(450, 5),
    TANK(450, 4);
    private final int MP;
    private final int cost;

    public boolean isPrimary() {
        return this.equals(SETTLER) || this.equals(WORKER) || this.equals(WARRIOR) || this.equals(SCOUT);
    }

    UnitType (int cost, int MP) {
        this.MP = MP;
        this.cost = cost;
    }
    public int getCost() {
        return cost;
    }
    public int getMP() {
        return MP;
    }
}
