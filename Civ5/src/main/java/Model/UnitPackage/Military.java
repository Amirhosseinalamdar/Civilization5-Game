package Model.UnitPackage;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class Military extends Unit {

    private final int combatStrength;
    private int rangedCombatStrength;
    private int range;
    private boolean isReady; //hame ready an joz siege//ke faghat if(isReady)
    private int XP;

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

    public int getCombatStrength() {
        if (this.status.equals(UnitStatus.FORTIFY) && this.type.hasDefensiveBonus())
            return combatStrength + 2; //Combat Strength handled
        return combatStrength;
    }

    public int getRangedCombatStrength() {
        return rangedCombatStrength;
    }

    public int getRange() {
        return range;
    }

}
