package Model;

import Model.Map.TerrainType;
import Model.Map.Tile;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private ArrayList <User> players;
    private int turn;
    private int time;
    private ArrayList <Tile> map;//20*20
    private Tile[][] tiles = new Tile[20][20];

//    public Game (String playerList) {
//        set players from string
//        set other fields
//        generateMap();
//    }

    public void generateMap(){
        //Tile[][] tiles = new Tile[20][20];
        Random random = new Random();
        int centersParameter = 1;//TODO for graphics
        for(int i=0;i<20;i++){
            for(int j=0;j<20;j++){
                tiles[i][j] = new Tile();
                tiles[i][j].setCenterY(j * centersParameter);
                tiles[i][j].setCenterX(i * centersParameter * 2 + centersParameter * (j % 2));
                if(i<1 || j<1 || i>18 || j>18){
                    tiles[i][j].setType(TerrainType.OCEAN);
                } else if((i<2 || j<2 || i>17 || j>17) && random.nextInt(2) == 0) {
                    tiles[i][j].setType(TerrainType.OCEAN);
                }
            }
        }
        for(int i=0;i<20;i++){
            for(int j=0;j<20;j++){
                if(tiles[i][j].getType() != null) continue;
                else {
                    TerrainType type = getType(random.nextInt(7));
                    int probabilityDecrement= setProbabilityDecrement(type);
                    int probability = setProbability(type);
                    generateGroup(i,j,tiles,type,probability,random,probabilityDecrement);
                }
            }
        }
        completeMap(tiles);
    }
    private int setProbability(TerrainType type){
        if(type == TerrainType.MOUNTAIN) return 50;
        else if(type == TerrainType.HILL) return 30;
        else if(type == TerrainType.SNOW) return 70;
        else if(type == TerrainType.TUNDRA) return 70;
        return 90;
    }
    private int setProbabilityDecrement(TerrainType type){
        if(type == TerrainType.MOUNTAIN) return 40;
        else if(type == TerrainType.HILL) return 70;
        else if(type == TerrainType.SNOW) return 30;
        else if(type == TerrainType.TUNDRA) return 30;
        return 10;
    }

    private void completeMap(Tile[][] tiles){
        for(int i=0;i<20;i++){
            for(int j=0;j<20;j++){
                if(tiles[i][j].getType() == null){
                    int[] counter = new int[7];
                    addCounter(counter,tiles[i+1][j].getType());
                    addCounter(counter,tiles[i-1][j].getType());
                    addCounter(counter,tiles[i][j+1].getType());
                    addCounter(counter,tiles[i+1][j-1].getType());
                    if(j%2 == 1) {
                        addCounter(counter, tiles[i + 1][j+1].getType());
                        addCounter(counter, tiles[i + 1][j-1].getType());
                    }else{
                        addCounter(counter, tiles[i - 1][j-1].getType());
                        addCounter(counter, tiles[i - 1][j+1].getType());
                    }
                    int maxValueIndex = 4;
                    for(int k=0;k<7;k++){
                        if(counter[k] > counter[maxValueIndex]) maxValueIndex = k;
                    }
                    tiles[i][j].setType(getType(maxValueIndex));
                }
                this.map = new ArrayList<>();
                map.add(tiles[i][j]);
            }
        }
    }

//check kardane i va j dar v1 niaz nis chon ta 2 laye ocean e va goftim type null bashe unja

    private void addCounter(int[] counter, TerrainType type){
        if(type == TerrainType.DESERT) counter[0]++;
        else if(type == TerrainType.GRASS) counter[1]++;
        else if(type == TerrainType.HILL) counter[2]++;
        else if(type == TerrainType.MOUNTAIN) counter[3]++;
        else if(type == TerrainType.PLAIN) counter[4]++;
        else if(type == TerrainType.TUNDRA) counter[5]++;
        else if(type == TerrainType.SNOW) counter[6]++;
    }

    private TerrainType getType(int key){
        if(key == 0) return TerrainType.DESERT;
        else if(key == 1) return TerrainType.GRASS;
        else if(key == 2) return TerrainType.HILL;
        else if(key == 3) return TerrainType.MOUNTAIN;
        else if(key == 4) return TerrainType.PLAIN;
        else if(key == 5) return TerrainType.TUNDRA;
        else return TerrainType.SNOW;
    }
    private void generateGroup(int i,int j,Tile[][] tiles, TerrainType type, int probability,Random random,int probabilityDecrement){
        tiles[i][j].setType(type);
        int key = random.nextInt(100);
        if(checkIAndJ(i-1,j)&&(probability > key) && tiles[i-1][j].getType() == null){
            generateGroup(i-1,j,tiles,type,probability - probabilityDecrement,random,probabilityDecrement);
        }
        key = random.nextInt(100);
        if(checkIAndJ(i,j+1)&&(probability > key) && tiles[i][j+1].getType() == null){
            generateGroup(i,j+1,tiles,type,probability - probabilityDecrement,random,probabilityDecrement);
        }
        key = random.nextInt(100);
        if(j%2==1&&checkIAndJ(i+1,j+1)&&(probability > key) && tiles[i+1][j+1].getType() == null){
            generateGroup(i+1,j+1,tiles,type,probability - probabilityDecrement,random,probabilityDecrement);
        }
        key = random.nextInt(100);
        if(j%2==0&&checkIAndJ(i-1,j-1)&&(probability > key) && tiles[i-1][j-1].getType() == null){
            generateGroup(i-1,j-1,tiles,type,probability - probabilityDecrement,random,probabilityDecrement);
        }
        key = random.nextInt(100);
        if(checkIAndJ(i+1,j)&&(probability > key) && tiles[i+1][j].getType() == null){
            generateGroup(i+1,j,tiles,type,probability - probabilityDecrement,random,probabilityDecrement);
        }
        key = random.nextInt(100);
        if(j%2==1 && checkIAndJ(i+1,j-1)&&(probability > key) && tiles[i+1][j-1].getType() == null){
            generateGroup(i+1,j-1,tiles,type,probability - probabilityDecrement,random,probabilityDecrement);
        }
        key = random.nextInt(100);
        if(j%2==0&&checkIAndJ(i-1,j+1)&&(probability > key) && tiles[i-1][j+1].getType() == null){
            generateGroup(i-1,j+1,tiles,type,probability - probabilityDecrement,random,probabilityDecrement);
        }
        key = random.nextInt(100);
        if(checkIAndJ(i,j-1)&&(probability > key) && tiles[i][j-1].getType() == null){
            generateGroup(i,j-1,tiles,type,probability - probabilityDecrement,random,probabilityDecrement);
        }
    }

    private boolean checkIAndJ(int i,int j){
        return i <= 19 && i >= 0 && j <= 19 && j >= 0;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
}
