package Model.UnitPackage;

import Model.Map.Resource;
import Model.Technology;

public enum UnitType {
    ARCHER(70, 2, 4, 6, 2, null, Technology.ARCHERY),
    CHARIOT_ARCHER(60, 4, 3, 6, 2, Resource.HORSE, Technology.WHEEL),
    SCOUT(25, 2, 4, 0, 0, null, Technology.AGRICULTURE),
    SETTLER(89, 2, 0, 0, 0, null, Technology.AGRICULTURE),
    SPEARMAN(50, 2, 7, 0, 0, null, Technology.BRONZE_WORKING),
    WARRIOR(40, 2, 6, 0, 0, null, Technology.AGRICULTURE),
    WORKER(70, 2, 0, 0, 0, null, Technology.AGRICULTURE),
    CATAPULT(100, 2, 4, 14, 2, Resource.IRON, Technology.MATHEMATICS),
    HORSEMAN(80, 4, 12, 0, 0, Resource.HORSE, Technology.HORSEBACK_RIDING),
    SWORDSMAN(80, 2, 11, 0, 0, Resource.IRON, Technology.IRON_WORKING),
    CROSSBOWMAN(120, 2, 6, 12, 2, null, Technology.MACHINERY),
    KNIGHT(150, 3, 18, 0, 0, Resource.HORSE, Technology.CHIVALRY),
    LONGSWORDSMAN(150, 3, 18, 0, 0, Resource.IRON, Technology.STEEL),
    PIKEMAN(100, 2, 10, 0, 0, null, Technology.CIVIL_SERVICE),
    TREBUCHET(170, 2, 6, 20, 2, Resource.IRON, Technology.PHYSICS),
    CANNON(250, 2, 10, 26, 2, null, Technology.CHEMISTRY),
    CAVALRY(250, 3, 25, 0, 0, Resource.HORSE, Technology.MILITARY_SCIENCE),
    LANCER(220, 4, 22, 0, 0, Resource.HORSE, Technology.METALLURGY),
    MUSKETMAN(120, 2, 16, 0, 0, null, Technology.GUNPOWDER),
    RIFLEMAN(200, 2, 25, 0, 0, null, Technology.RIFLING),
    ANTI_TANK_GUN(300, 2, 32, 0, 0, null, Technology.REPLACEABLE_PARTS),
    ARTILLERY(420, 2, 16, 32, 3, null, Technology.DYNAMITE),
    INFANTRY(300, 2, 36, 0, 0, null, Technology.REPLACEABLE_PARTS),
    PANZER(450, 5, 60, 0, 0, null, Technology.COMBUSTION),
    TANK(450, 4, 50, 0, 0, null, Technology.COMBUSTION);

    private final int MP;
    private final int cost;
    private final int combatStrength;
    private final int rangedCombatStrength;
    private final int range;
    private final Resource prerequisiteResource;
    private final Technology prerequisiteTech;

    UnitType (int cost, int MP, int combatStrength, int rangedCombatStrength, int range, Resource resource, Technology technology) {
        this.MP = MP;
        this.cost = cost;
        this.combatStrength = combatStrength;
        this.rangedCombatStrength = rangedCombatStrength;
        this.range = range;
        this.prerequisiteResource = resource;
        this.prerequisiteTech = technology;
    }

    public int getCost() {
        return cost;
    }

    public int getMP() {
        return MP;
    }

    public Resource getPrerequisiteResource() {
        return prerequisiteResource;
    }

    public Technology getPrerequisiteTech() {
        return prerequisiteTech;
    }

    public int getCombatStrength() {
        return combatStrength;
    }

    public int getRangedCombatStrength() {
        return rangedCombatStrength;
    }

    public int getRange() {
        return range;
    }

    public boolean isCivilian() {
        return this.equals(WORKER) || this.equals(SETTLER);
    }
}
