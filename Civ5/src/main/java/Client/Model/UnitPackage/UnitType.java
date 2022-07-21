package Client.Model.UnitPackage;

import Client.Model.ImageBase;
import Client.Model.Map.Resource;
import Client.Model.Technology;
import javafx.scene.image.Image;

public enum UnitType {
    ARCHER(70, 2, 4, 6, 2, null, Technology.ARCHERY, ImageBase.ARCHER.getImage()),
    CHARIOT_ARCHER(60, 4, 3, 6, 2, Resource.HORSE, Technology.WHEEL, ImageBase.CHARIOT_ARCHER.getImage()),
    SCOUT(25, 2, 4, 0, 0, null, Technology.AGRICULTURE, ImageBase.SCOUT.getImage()),
    SETTLER(89, 2, 0, 0, 0, null, Technology.AGRICULTURE, ImageBase.SETTLER.getImage()),
    SPEARMAN(50, 2, 7, 0, 0, null, Technology.BRONZE_WORKING, ImageBase.SPEARS_MAN.getImage()),
    WARRIOR(40, 2, 6, 0, 0, null, Technology.AGRICULTURE, ImageBase.WARRIOR.getImage()),
    WORKER(70, 2, 0, 0, 0, null, Technology.AGRICULTURE, ImageBase.WORKER.getImage()),
    CATAPULT(100, 2, 4, 14, 2, Resource.IRON, Technology.MATHEMATICS, ImageBase.CATAPULT.getImage()),
    HORSEMAN(80, 4, 12, 0, 0, Resource.HORSE, Technology.HORSEBACK_RIDING, ImageBase.HORSEMAN.getImage()),
    SWORDSMAN(80, 2, 11, 0, 0, Resource.IRON, Technology.IRON_WORKING, ImageBase.SWORDSMAN.getImage()),
    CROSSBOWMAN(120, 2, 6, 12, 2, null, Technology.MACHINERY, ImageBase.CROSSBOWMAN.getImage()),
    KNIGHT(150, 3, 18, 0, 0, Resource.HORSE, Technology.CHIVALRY, ImageBase.KNIGHT.getImage()),
    LONGSWORDSMAN(150, 3, 18, 0, 0, Resource.IRON, Technology.STEEL, ImageBase.LONG_SWORDSMAN.getImage()),
    PIKEMAN(100, 2, 10, 0, 0, null, Technology.CIVIL_SERVICE, ImageBase.PIKE_MAN.getImage()),
    TREBUCHET(170, 2, 6, 20, 2, Resource.IRON, Technology.PHYSICS, ImageBase.TREBUCHET.getImage()),
    CANNON(250, 2, 10, 26, 2, null, Technology.CHEMISTRY, ImageBase.CANNON.getImage()),
    CAVALRY(250, 3, 25, 0, 0, Resource.HORSE, Technology.MILITARY_SCIENCE, ImageBase.CAVALRY.getImage()),
    LANCER(220, 4, 22, 0, 0, Resource.HORSE, Technology.METALLURGY, ImageBase.LANCER.getImage()),
    MUSKETMAN(120, 2, 16, 0, 0, null, Technology.GUNPOWDER, ImageBase.MUSKET_MAN.getImage()),
    RIFLEMAN(200, 2, 25, 0, 0, null, Technology.RIFLING, ImageBase.RIFLEMAN.getImage()),
    ANTI_TANK_GUN(300, 2, 32, 0, 0, null, Technology.REPLACEABLE_PARTS, ImageBase.ANTI_TANK_GUN.getImage()),
    ARTILLERY(420, 2, 16, 32, 3, null, Technology.DYNAMITE, ImageBase.ARTILLERY.getImage()),
    INFANTRY(300, 2, 36, 0, 0, null, Technology.REPLACEABLE_PARTS, ImageBase.INFANTRY.getImage()),
    PANZER(450, 5, 60, 0, 0, null, Technology.COMBUSTION, ImageBase.PANZER.getImage()),
    TANK(450, 4, 50, 0, 0, null, Technology.COMBUSTION, ImageBase.TANK.getImage());

    private final int MP;
    private final int cost;
    private final int combatStrength;
    private final int rangedCombatStrength;
    private final int range;
    private final Resource prerequisiteResource;
    private final Technology prerequisiteTech;
    private final Image image;

    UnitType(int cost, int MP, int combatStrength, int rangedCombatStrength, int range, Resource resource, Technology technology, Image image) {
        this.MP = MP;
        this.cost = cost;
        this.combatStrength = combatStrength;
        this.rangedCombatStrength = rangedCombatStrength;
        this.range = range;
        this.prerequisiteResource = resource;
        this.prerequisiteTech = technology;
        this.image = image;
    }

    public Image getImage() {
        return image;
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

    public boolean hasLimitedVisibility() {
        return this.equals(CATAPULT) || this.equals(TREBUCHET) || this.equals(PANZER);
    }

    public boolean hasDefensiveBonus() {
        return !(this.equals(CATAPULT) || this.equals(KNIGHT) || this.equals(TREBUCHET) || this.equals(CANNON) ||
                this.equals(CHARIOT_ARCHER) || this.equals(HORSEMAN) || this.equals(CAVALRY) || this.equals(LANCER) ||
                this.equals(ARTILLERY) || this.equals(PANZER) || this.equals(TANK));
    }

    public boolean isSiege() {
        return this.equals(CATAPULT) || this.equals(CANNON) ||
                this.equals(TREBUCHET) || this.equals(ARTILLERY);
    }

    public boolean isRangeCombat() {
        return rangedCombatStrength > 0;
    }

    public boolean isMeleeCombat() {
        return combatStrength > 0;
    }
}
