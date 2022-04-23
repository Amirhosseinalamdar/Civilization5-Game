package Model.UnitPackage;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class Military extends Unit{

    private int combatStrength;
    private int rangedCombatStrength;
    private int range;
    private boolean isReady; //hame ready an joz siege//ke faghat if(isReady)
    private int XP;

    @Override
    public void setStatus(Matcher matcher) {
        if (matcher.toString().equals("alert")) this.status = UnitStatus.ALERT;
        else if (matcher.toString().equals("fortify")) this.status = UnitStatus.FORTIFY;
        else if (matcher.toString().equals("garrison")) this.status = UnitStatus.FORTIFY;
        else if (matcher.toString().equals("setup ranged")) this.status = UnitStatus.SIEGEPREP;
    }

    public void create(){
        //this.tile.setMilitary(this);
//        this.civilization.units.add(this);
    }
    public void upgrade() {
        //TODO... change type (costs gold)
    }


}
