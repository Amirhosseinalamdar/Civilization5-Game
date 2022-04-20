package Controller;

import Model.Civilization;
import Model.Map.City;
import Model.Map.Tile;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import View.GameMenu;

public class UnitController{
    private static Civilization civilization;

    public static void changeCivilization(Civilization civilization){
        UnitController.civilization = civilization;
    }

    public static void handleUnitOption(Unit unit){
        unit.setStatus(GameMenu.showUnitOptions(unit));
        //TODO switch case and call the related func
    }

    private static void moveUnit(Unit unit, Tile target){

    }

    private static void sleepUnit(Unit unit){

    }

    private static void alertMilitary(Military military){

    }

    private static void boostMilitary(Military military){

    }

    private static void healUnit(Unit unit){

    }

    private static void settleMilitary(Military military){

    }//estehkam va hoshdar safhe 26 doc

    private static void siegePrep(Military military){

    }

    private static void rangedAttack(Military military){//may attack to a city or a military or civilian
        Tile tile = GameMenu.showRangedAttackOptions(military);
        if (tile == null) ; //ke hichi// ya cancle mikone ya eshteba mizane//age eshteba zad while beznim ta odorst bezane
        //ya rangedAttackToUnit
        //ya rangedAttackToCity
        //ya attackCivilian ????//TODO
    }

    private static void raid(Military military){

    }

    private static void foundCity (Unit settler) {
        //TODO check settler.type bare debug
    }

    private static void abortMission(Unit unit){

    }

    private static void wakeUnit(Unit unit){

    }

    private static void dissolveUnit(Unit unit){//monhal kardan

    }




    private static void attack(Military attackerMilitary,Unit defendingUnit){//should call by move
        //TODO if defender is civilian
        //TODO call ranged attack to city or unit
        //TODO call close attack to city or unit
    }

    private static void rangedAttackToUnit(Military attackerMilitary, Unit defendingUnit){

    }

    private static void rangedAttackToCity(Military military, City city){

    }

    private static void closeAttackToCity(Military military, City city){

    }

    private static void closeAttackToUnit(Military attackerMilitary, Unit defendingUnit){

    }
}
