package View;

import Controller.GameController;
import Controller.UnitController;
import Model.Civilization;
import Model.Game;
import Model.Map.*;
import Model.TileStatus;
import Model.UnitPackage.Military;
import Model.UnitPackage.Unit;
import Model.UnitPackage.UnitStatus;
import Model.UnitPackage.UnitType;
import Model.User;

import javax.sound.midi.Soundbank;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.ArrayList;
import java.util.Scanner;


public class GameMenu {
    static Scanner scanner;
    public static void startGame (ArrayList <User> players, Scanner scanner) {
        Game.generateGame(players);
        GameMenu.scanner = scanner;
        do {
            String command = scanner.nextLine();
            if (command.equals("next turn")) GameController.updateGame();
            else {
                GameController.setCivilizationAndDoMissions();
                GameController.doTurn(command);
            }
        } while (scanner.hasNextLine());
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

    public static void showMap(Civilization civilization){//TODO check if units are in correct tile
                                                            //TODO fogy and ... added but not tested
        for (Unit unit : civilization.getUnits()) {
            civilization.getTileVisionStatuses()[unit.getTile().getIndexInMapI()][unit.getTile().getIndexInMapJ()] = TileStatus.CLEAR;
            for (Tile tileNeighbor : UnitController.getTileNeighbors(unit.getTile()))
                civilization.getTileVisionStatuses()[tileNeighbor.getIndexInMapI()][tileNeighbor.getIndexInMapJ()] = TileStatus.CLEAR;
        }
        for(int j=0;j<10;j++){
            System.out.print("   _____        ");
        }
//        for(int i=0;i<20;i++){
//            for(int j=0;j<20;j++){
//                //System.out.print(civilization.getTileVisionStatuses()[i][j].toString());
//                civilization.setTileVisionStatuses(i,j,TileStatus.DISCOVERED);
//            }
//            //System.out.print('\n');
//        }
        System.out.print('\n');
        for(int i=0;i<123;i++){
            for(int j=0;j<10;j++){
                if(i%6==0){
                    if(i==120){
                        if(j==0) System.out.print("   ");
                        System.out.print("     \\"
                                +Game.getTiles()[19][2*j+1].getTypeForCiv(civilization,19,2*j+1).getColor()
                                +"         "+RESET+"/");
                    }
                    else {
                        showXAndY(i/6,2*j,true,civilization);
                    }
                }else if(i%6==1){
                    if(i==121){
                        if(j==0) System.out.print("  ");
                        System.out.print("       \\");
                        System.out.print(Game.getTiles()[19][2*j+1].getTypeForCiv(civilization,19,2*j+1).getColor());
                        showUnitAndMilitary((i-1)/6-1,2*j+1,false,civilization);
                        System.out.print(RESET);
                        System.out.print("/");

                    }
                    else{
                        showResource((i-1)/6,2*j,true,civilization);
                    }
                }else if(i%6==2){
                    if(i==122){
                        if(j==0) System.out.print(" ");
                        System.out.print("         \\"+
                                Game.getTiles()[19][2*j+1].getTypeForCiv(civilization,19,2*j+1).getColor()
                                +"_____"+RESET+"/");
                    }
                    else{
                        int I=(i-2)/6;
                        int J= j*2;
                        if(civilization.getTileVisionStatuses()[I][J] != TileStatus.FOGGY) {
                            System.out.print("/" + Game.getTiles()[(i - 2) / 6][j * 2].getTypeForCiv(civilization, I, J).getColor()
                                    + "   " + getTypeFirstChar(Game.getTiles()[(i - 2) / 6][2 * j].getTypeForCiv(civilization, I, J)) + "," +
                                    getFeatureFirstChar(Game.getTiles()[(i - 2) / 6][2 * j].getFeature()) + "   " + RESET);//9 wh
                        }else{
                            System.out.print("/         ");
                        }
                        if(I>0) System.out.print("\\"+Game.getTiles()[I-1][J+1].getTypeForCiv(civilization,I-1,J+1).getColor()+"_____"+RESET);
                        else System.out.print("\\_____");
                    }
                }else if(i%6==3){
                    showXAndY((i-3)/6,2*j+1,false,civilization);
                    //System.out.print("\\         /     ");
                }else if(i%6==4){
                    showResource((i-4)/6,j*2+1,false,civilization);
                }else{
                    int I = (i-5)/6;
                    int J = (2*j+1);
                    if(j==0) System.out.print("  ");
                    if(civilization.getTileVisionStatuses()[I][J-1] != TileStatus.FOGGY)
                        System.out.print("\\" + Game.getTiles()[I][J - 1].getTypeForCiv(civilization, I, J-1).getColor() + "_____" + RESET + "/");
                    else System.out.print("\\_____/");
                    if(civilization.getTileVisionStatuses()[I][J] != TileStatus.FOGGY)
                        System.out.print(Game.getTiles()[I][J].getTypeForCiv(civilization, I, J).getColor() + "   "
                                + getTypeFirstChar(Game.getTiles()[(i - 5) / 6][2 * j + 1].getTypeForCiv(civilization, I, J)) + "," +
                                getFeatureFirstChar(Game.getTiles()[(i - 5) / 6][2 * j + 1].getFeature()) + "   " + RESET);//7 wh
                    else System.out.print("         ");
                }
            }
            if(i%6==3) System.out.print("\\");
            else if(i%6==4) System.out.print(
                    Game.getTiles()[(i-4)/6][19].getTypeForCiv(civilization,(i-4)/6,19).getColor()+" "+RESET+"\\");
            else if(i%6==5) System.out.print("\\");
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
    private static char getFeatureFirstChar(TerrainFeature feature){
        if(feature == TerrainFeature.SWAMP) return 'S';
        else if(feature == TerrainFeature.FOREST) return 'F';
        else if(feature == TerrainFeature.JUNGLE) return 'J';
        else if(feature == TerrainFeature.ICE) return 'I';
        else if(feature == TerrainFeature.OASIS) return 'O';
        else if(feature == TerrainFeature.DELTA) return 'D';
        else return ' ';
    }
    private static void showResource(int i,int j,boolean isEven,Civilization civilization){
        if(isEven){
            if(j/2 == 0) System.out.print(" ");
            System.out.print("/"+Game.getTiles()[i][j].getTypeForCiv(civilization,i,j).getColor());
//            if(Game.getTiles()[i][j].getResource() != Resource.JEWELERY &&
//                    Game.getTiles()[i][j].getResource() != Resource.NONE) {
//                System.out.print(Game.getTiles()[i][j].getResource());
//                for(int k=0;k<7-Game.getTiles()[i][j].getResource().toString().length();k++)
//                    System.out.print(' ');
//                System.out.print(RESET+"\\");
//            }else if(Game.getTiles()[i][j].getResource() == Resource.NONE){
//                System.out.print("       "+RESET+"\\");
//            } else{
//                System.out.print("JEWEL  "+RESET+"\\");
//            }
            System.out.print(outputResource(civilization,i,j));
            for(int k=0;k<7-outputResource(civilization,i,j).length();k++) System.out.print(' ');
            System.out.print(RESET+"\\");
            if(i>0) showUnitAndMilitary(i-1,j+1,false,civilization);
            else System.out.print("       ");
        } else{
            if((j-1)/2 == 0) System.out.print(" ");
            if(j>0) showUnitAndMilitary(i,j-1,true,civilization);
            System.out.print(Game.getTiles()[i][j].getTypeForCiv(civilization,i,j).getColor());
//            if(Game.getTiles()[i][j].getResource() != Resource.JEWELERY &&
//                    Game.getTiles()[i][j].getResource() != Resource.NONE) {
//                System.out.print(Game.getTiles()[i][j].getResource());
//                for(int k=0;k<6-Game.getTiles()[i][j].getResource().toString().length();k++)
//                    System.out.print(' ');
//            }else if(Game.getTiles()[i][j].getResource() == Resource.NONE){
//                System.out.print("      ");
//            } else{
//                System.out.print("JEWEL ");
//            }
            System.out.print(outputResource(civilization,i,j));
            for(int k=0;k<6-outputResource(civilization,i,j).length();k++) System.out.print(' ');

            if((j-1)/2 != 9 && !outputResource(civilization,i,j).equals(Resource.BOKHOOR.toString())) System.out.print(" ");
            System.out.print(RESET);
        }
    }

    private static String outputResource(Civilization civilization,int i,int j){
        if(civilization.getTileVisionStatuses()[i][j] == TileStatus.CLEAR){
            if(Game.getTiles()[i][j].getResource() == Resource.JEWELERY) return "JEWEL";
            else if(Game.getTiles()[i][j].getResource() == Resource.NONE) return " ";
            else return Game.getTiles()[i][j].getResource().toString();
        }else{
            return " ";
        }
    }

    private static void showXAndY(int i,int j,boolean isEven,Civilization civilization){
        if(isEven){
            if(j==0) System.out.print("  ");
            System.out.print("/"+Game.getTiles()[i][j].getTypeForCiv(civilization,i,j).getColor());
            String loc =Game.getTiles()[i][j].getCenterX()+","+Game.getTiles()[i][j].getCenterY();
            System.out.print(loc);
            for(int k=0;k<5-loc.length();k++) System.out.print(' ');
            System.out.print(RESET+"\\");
            if(i>0)System.out.print(Game.getTiles()[i-1][j+1].getTypeForCiv(civilization,i-1,j+1).getColor()+"         "+RESET);
            else System.out.print("         ");
        }else {
            System.out.print("\\"+Game.getTiles()[i][j-1].getTypeForCiv(civilization,i,j-1).getColor()+"         "+RESET+"/");
            System.out.print(Game.getTiles()[i][j].getTypeForCiv(civilization,i,j).getColor());
            String loc =Game.getTiles()[i][j].getCenterX()+","+Game.getTiles()[i][j].getCenterY();
            System.out.print(loc);
            for(int k=0;k<5-loc.length();k++) System.out.print(' ');
            System.out.print(RESET);
        }
    }

    private static void showUnitAndMilitary(int i,int j,boolean isEven,Civilization civilization){
        String output1,output2;
        if(isEven){
            System.out.print("\\"+Game.getTiles()[i][j].getTypeForCiv(civilization,i,j).getColor());
            if(Game.getTiles()[i][j].getMilitary() == null) output1 ="   ";
            else output1 = Game.getTiles()[i][j].getMilitary().getType().toString().substring(0,3);
            if(Game.getTiles()[i][j].getCivilian() == null) output2 = "   ";
            else output2 = Game.getTiles()[i][j].getCivilian().getType().toString().substring(0,3);
            if(civilization.getTileVisionStatuses()[i][j] == TileStatus.CLEAR) System.out.print(output1+","+output2);
            else System.out.print("       ");
            System.out.print(RESET+"/");
        } else{
            if(Game.getTiles()[i][j].getMilitary() == null) output1 ="   ";
            else output1 = Game.getTiles()[i][j].getMilitary().getType().toString().substring(0,3);
            if(Game.getTiles()[i][j].getCivilian() == null) output2 = "   ";
            else output2 = Game.getTiles()[i][j].getCivilian().getType().toString().substring(0,3);
            System.out.print(Game.getTiles()[i][j].getTypeForCiv(civilization,i,j).getColor());
            if(civilization.getTileVisionStatuses()[i][j] == TileStatus.CLEAR)
                System.out.print(output1+","+output2+RESET);
            else  System.out.print("       "+RESET);

        }
    }

    public static void notYourUnit() {
        System.out.println("this is not your unit");
    }

    public static void invalidChosenUnit() {
        System.out.println("no unit exists in chosen pos");
    }

    public static void invalidPosForCity() {
        System.out.println("no city exists in chosen pos");
    }

    public static void invalidNameForCity() {
        System.out.println("no city exists with this name");
    }

    public static void siegeNotPrepared() {
        System.out.println("you have to prepare the siege unit first");
    }

    public static void cantMakeGarrison() {
        System.out.println("can't make garrison, no city available");
    }

    public static void unitIsNotSiege() {
        System.out.println("chosen unit is not siege");
    }

    public static void siegeAlreadyPrepared() {
        System.out.println("this unit is already prepared");
    }

    public static void invalidChosenTile() {
        System.out.println("no tile exists in chosen tile");
    }

    public static void unitIsCivilianError() {
        System.out.println("error: this unit is civilian");
    }

    public static void unitIsMilitaryError() {
        System.out.println("error: this unit is military");
    }

    public static void unitHasFullHealth() {
        System.out.println("unit's health is already full");
    }

    public static void unitIsNotWorker() {
        System.out.println("this unit is not worker");
    }
}
