package View;

import Controller.GameController;
import Model.Civilization;
import Model.Game;
import Model.Map.*;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitStatus;
import Model.UnitPackage.UnitType;

import java.util.Scanner;
import java.util.regex.Matcher;


public class GameMenu {
    Scanner scanner;
    public void startGame (String command) {
        Game game = new Game(command);
        GameController gameController = new GameController();
        while (true) {
            //command = scanner.nextLine();
            //if (command == exit) return;
            gameController.doTurn(game, command);
        }
    }

    public static String nextCommand(){
        //return scanner.nextLine();
        return null;
    }


    public static Tile showRangedAttackOptions (Military military) {
        //return a tile based on scanner and inputs and military.tile
        return null;
    }

    public static UnitStatus showUnitOptions (Unit unit) {
        return null;
    }

    public static void showBanner(City city) {

    }

    public static void cityOutput(City city) {

    }

    public static void civilizationOutput(City city) {

    }

    public static void showCity(City city){

    }

    public static void cityShopMenu(City city){

    }

    public static UnitType cityProductionMenu(City city){// should add building in next phase
        return null;
    }

//    public static UnitType showProductionOptions(City city){
//
//    }

    public static void showResearchOptions(Civilization civilization){

    }

    public static Citizen chooseCitizenToLock(){

        return null;
    }

    public static Improvement chooseImprovement(){
        return null;
    }

    public static CityStatus conqueredCityOptions(){
        return null;
    }

//info part ===============================

    private static void researchInfo(){

    }

    private static void unitsInfo(){

    }

    private static void cityInfo(){

    }

    private static void diplomacyInfo(){

    }

    private static void victoryProgressInfo(){

    }

    private static void demographicsInfo(){

    }

    private static void notificationHistory(){

    }

    private static void militaryInfo(){

    }

    private static void economicInfo(){

    }

    private static void tradingHistory(){

    }

    private static void generalMilitaryCheck(){

    }

    private static void generalEconomicCheck(){

    }

    private static void generalDiplomaticCheck(){

    }

}
