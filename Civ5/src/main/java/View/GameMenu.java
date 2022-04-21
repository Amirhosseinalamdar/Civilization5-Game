package View;

import Controller.GameController;
import Model.Civilization;
import Model.Game;
import Model.Map.*;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitStatus;
import Model.UnitPackage.UnitType;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.Scanner;


public class GameMenu {
    Scanner scanner;
//    public void startGame (String command) {
//        Game game = new Game(command);
//        GameController gameController = new GameController();
//        while (true) {
//            //command = scanner.nextLine();
//            //if (command == exit) return;
//            gameController.doTurn(game, command);
//        }
//    }

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

    public static void showMap(Game game){
        for(int j=0;j<10;j++){
            System.out.print("   _____        ");
        }
        System.out.print('\n');
        for(int i=0;i<123;i++){
            for(int j=0;j<10;j++){
                if(i%6==0){
                    if(i==120 && j==0) System.out.print("        \\       ");
                    else System.out.print("  /     \\       ");
                }else if(i%6==1){
                    if(i==121 && j==0) System.out.print("         \\      ");
                    else System.out.print(" /       \\      ");
                }else if(i%6==2){
                    if(i==122 && j==0) System.out.print("          \\_____");
                    else if(i==122) System.out.print("/         \\_____");
                    else{
                        System.out.print("/   "+getTypeFirstChar(game.getTiles()[(i-2)/6][2*j].getType())+","+
                                getFeatureFirstChar(game.getTiles()[(i-2)/6][2*j].getFeature())+"   \\_____");//9 wh
                    }
                }else if(i%6==3){
                    System.out.print("\\         /     ");
                }else if(i%6==4){
                    System.out.print(" \\       /      ");
                }else{
                    System.out.print("  \\_____/   "+getTypeFirstChar(game.getTiles()[(i-5)/6][2*j+1].getType())+","+
                            getFeatureFirstChar(game.getTiles()[(i-2)/6][2*j+1].getFeature())+" ");//7 wh
                }
            }
            if(i%6==3 && i>2) System.out.print("\\");
            else if(i%6==4 && i>2) System.out.print(" \\");
            else if(i%6==5 && i>2) System.out.print("  \\");
            else if(i%6==0 && i>2) System.out.print("  /");
            else if(i%6==1 && i>2) System.out.print(" /");
            else if(i%6==2 && i>2) System.out.print("/");
            System.out.print('\n');
        }
    }

    private static char getTypeFirstChar(TerrainType type){
        if(type == TerrainType.HILL) return 'H';
        else if(type == TerrainType.SNOW) return 'S';
        else if(type == TerrainType.TUNDRA) return 'T';
        else if(type == TerrainType.GRASS) return 'G';
        else if(type == TerrainType.MOUNTAIN) return 'M';
        else if(type == TerrainType.DESERT) return 'D';
        else if(type == TerrainType.OCEAN) return '#';
        else return 'P';
    }
    private static char getFeatureFirstChar(TerrainFeature feature){
        if(feature == TerrainFeature.SWAMP) return 'S';
        else if(feature == TerrainFeature.FOREST) return 'F';
        else if(feature == TerrainFeature.JUNGLE) return 'J';
        else if(feature == TerrainFeature.ICE) return 'I';
        else if(feature == TerrainFeature.OASIS) return 'O';
        else if(feature == TerrainFeature.DELTA) return 'D';
        else return ' ';
    }
}
