package View;

import Controller.GameController;
import Model.Civilization;
import Model.Game;
import Model.Map.*;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitStatus;
import Model.UnitPackage.UnitType;

import javax.sound.midi.Soundbank;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.Scanner;


public class GameMenu {
    static Scanner scanner;
    public static void setScanner (Scanner sc) {
        scanner = sc;
    }
    public static void startGame () {
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            if (command.equals("next turn")) GameController.updateGame();
            else GameController.doTurn(command);
        }
    }

    public static String nextCommand(){
        return scanner.nextLine();
        //return null;
    }


    public static Tile showRangedAttackOptions (Military military) {
        //return a tile based on scanner and inputs and military.tile
        return null;
    }

    public static void showUnitOptions (Unit unit) {
        System.out.println("one option for now... please enter \"move\"");
    }


    public static void notEnoughMoves() {
        System.out.println("not enough moves");
    }

    public static void unavailableTile () {
        System.out.println("Tile is unavailable");
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

    public static final String RESET = "\u001B[0m";
    public static final String BLUE = "\u001B[44m";

    public static void showMap(Game game){//TODO check if units are in correct tile
        for(int j=0;j<10;j++){
            System.out.print("   _____        ");
        }
        System.out.print('\n');
        for(int i=0;i<123;i++){
            for(int j=0;j<10;j++){
                if(i%6==0){
                    if(i==120){
                        if(j==0) System.out.print("   ");
                        System.out.print("     \\"+BLUE+"         "+RESET+"/");
                    }
                    else {
                        showXAndY(i/6,2*j,true,game);
                    }
                }else if(i%6==1){
                    if(i==121){
                        if(j==0) System.out.print("  ");
                        System.out.print("       \\");
                        System.out.print(BLUE);
                        showUnitAndMilitary((i-1)/6-1,2*j+1,false,game);
                        System.out.print(RESET);
                        System.out.print("/");

                    }
                    else{
                        showResource((i-1)/6,2*j,true,game);
                    }
                }else if(i%6==2){
                    if(i==122){
                        if(j==0) System.out.print(" ");
                        System.out.print("         \\"+BLUE+"_____"+RESET+"/");
                    }
                    else{
                        System.out.print("/"+game.getTiles()[(i-2)/6][j*2].getType().getColor()
                                +"   "+getTypeFirstChar(game.getTiles()[(i-2)/6][2*j].getType())+","+
                                getFeatureFirstChar(game.getTiles()[(i-2)/6][2*j].getFeature())+"   "+RESET);//9 wh
                        int I=(i-2)/6;
                        int J= j*2;
                        if(I>0) System.out.print("\\"+game.getTiles()[I-1][J+1].getType().getColor()+"_____"+RESET);
                        else System.out.print("\\_____");
                    }
                }else if(i%6==3){
                    showXAndY((i-3)/6,2*j+1,false,game);
                    //System.out.print("\\         /     ");
                }else if(i%6==4){
                    showResource((i-4)/6,j*2+1,false,game);
                }else{
                    int I = (i-5)/6;
                    int J = (2*j+1);
                    if(j==0) System.out.print("  ");
                    System.out.print("\\"+game.getTiles()[I][J-1].getType().getColor()+"_____"+RESET+"/"+
                            game.getTiles()[I][J].getType().getColor()+"   "
                            +getTypeFirstChar(game.getTiles()[(i-5)/6][2*j+1].getType())+","+
                            getFeatureFirstChar(game.getTiles()[(i-5)/6][2*j+1].getFeature())+"   "+RESET);//7 wh
                }
            }
            if(i%6==3 && i>2) System.out.print("\\");
            else if(i%6==4 && i>2) System.out.print(" \\");
            else if(i%6==5 && i>2) System.out.print("\\");
            else if(i%6==0 && i>2 && i!=120) System.out.print("/");
            else if(i%6==1 && i>2 && i!=121) System.out.print("/");
            else if(i%6==2 && i>2 && i!=122) System.out.print("/");
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
}
