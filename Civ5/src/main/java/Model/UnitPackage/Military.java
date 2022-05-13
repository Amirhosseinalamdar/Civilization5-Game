package Model.UnitPackage;

import Model.Map.TerrainType;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class Military extends Unit {

    private final double combatStrength;
    private double rangedCombatStrength;
    private int range;
    private boolean isReady; //hame ready an joz siege//ke faghat if(isReady)

    public Military (UnitType militaryType) {
        super(militaryType);
        this.combatStrength = militaryType.getCombatStrength();
        this.rangedCombatStrength = militaryType.getRangedCombatStrength();
        this.range = militaryType.getRange();
        this.isReady = !this.type.isSiege();
    }

    @Override
    public void setStatus(String string) {
        super.setStatus(string);
        if (string.equals("alert")) this.status = UnitStatus.ALERT;
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

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public void upgrade() {
        //TODO... change type (costs gold)
    }

    public double getCombatStrength() {
        if ((this.status.equals(UnitStatus.FORTIFY) && this.type.hasDefensiveBonus()) ||
                this.tile.getType().equals(TerrainType.HILL))
            return combatStrength + 2; //Combat Strength handled
        return combatStrength;
    }

    public double getRangedCombatStrength() {
        if (this.tile.getType().equals(TerrainType.HILL)) return rangedCombatStrength + 2;
        return rangedCombatStrength;
    }

    public int getRange() {
        return range;
    }

}
