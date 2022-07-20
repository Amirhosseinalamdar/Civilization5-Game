package Model.UnitPackage;

import Model.Map.TerrainType;
import com.google.gson.annotations.Expose;

public class Military extends Unit {
    @Expose
    private double combatStrength;
    @Expose
    private double rangedCombatStrength;
    @Expose
    private int range;
    @Expose
    private boolean isReady;

    public Military(UnitType militaryType) {
        super(militaryType);
        this.combatStrength = militaryType.getCombatStrength();
        this.rangedCombatStrength = militaryType.getRangedCombatStrength();
        this.range = militaryType.getRange();
        this.isReady = !this.type.isSiege();
    }

    @Override
    public void setStatus(String string) {
        super.setStatus(string);
        if (string.startsWith("attack")) this.status = UnitStatus.ATTACK;
        else if (string.equals("alert")) this.status = UnitStatus.ALERT;
        else if (string.equals("fortify")) this.status = UnitStatus.FORTIFY;
        else if (string.equals("garrison")) this.status = UnitStatus.GARRISON;
        else if (string.equals("setup ranged")) this.status = UnitStatus.SIEGEPREP;
        else if (string.startsWith("pillage")) this.status = UnitStatus.PILLAGE;
        else if (string.equals("heal")) this.status = UnitStatus.HEAL;
    }

    @Override
    public void kill() {
        tile.setMilitary(null);
        civilization.getUnits().remove(this);
    }

    public double getCombatStrength() {
        if ((this.status.equals(UnitStatus.FORTIFY) && this.type.hasDefensiveBonus()) ||
                this.tile.getType().equals(TerrainType.HILL))
            return combatStrength + 2;
        return combatStrength * (1 + tile.getFeature().getBattleEffect() + tile.getType().getBattleEffect());
    }

    public double getRangedCombatStrength() {
        if (this.tile.getType().equals(TerrainType.HILL)) return rangedCombatStrength + 2;
        return rangedCombatStrength;
    }

    public void setCombatStrength(double combatStrength) {
        this.combatStrength = combatStrength;
    }

    public void setRangedCombatStrength(double rangedCombatStrength) {
        this.rangedCombatStrength = rangedCombatStrength;
    }

}
