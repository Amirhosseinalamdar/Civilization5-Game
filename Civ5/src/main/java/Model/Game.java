package Model;

import Model.Map.Resource;
import Model.Map.TerrainFeature;
import Model.Map.TerrainType;
import Model.Map.Tile;
import Model.UnitPackage.UnitType;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private static ArrayList <User> players;
    private static int turn;
    private static int time;
    private static ArrayList <Tile> map;//20*20
    private static Tile[][] tiles = new Tile[20][20];

    public static ArrayList<User> getPlayers() {
        return players;
    }

    public static int getTurn() {
        return turn;
    }

    public static int getTime() {
        return time;
    }

    public static ArrayList<Tile> getMap() {
        return map;
    }

    public Game (String playerList) {
//        set players from string
//        set other fields
//        generateMap();
    }

    public static void generateGame(ArrayList<User> users) {
        players = users;
        turn = 0;
        time = 1;
        generateMap();
        ArrayList <Tile> tiles = new ArrayList<>(Game.getMap());
        for (User player : players) {
            player.newCivilization();
            int random = (int)Math.floor(Math.random() * (tiles.size() + 1));
            player.getCivilization().createCityOnTile(tiles.get(random));
            tiles.remove(random);
            Tile myTile = player.getCivilization().getCities().get(0).getTiles().get(0);
            System.out.println("i am " + player.getUsername() + ", my capital is on " +
                    myTile.getCenterX() + ", " + myTile.getCenterY());
        }
    }

    public static void generateMap(){
        map = new ArrayList<>();
//        Tile[][] tiles = new Tile[20][20];
        Random random = new Random();
        int centersParameter = 1;//TODO for graphics
        for(int i=0;i<20;i++){
            for(int j=0;j<20;j++){
                tiles[i][j] = new Tile();
                tiles[i][j].setIndexInMapI(i);
                tiles[i][j].setIndexInMapJ(j);
                tiles[i][j].setCenterY(j * centersParameter);
                tiles[i][j].setCenterX(i * centersParameter * 2 + centersParameter * (j % 2));
                if(i<1 || j<1 || i>18 || j>18) {
                    tiles[i][j].setType(TerrainType.OCEAN);
                } else if((i<2 || j<2 || i>17 || j>17) && random.nextInt(2) == 0) {
                    tiles[i][j].setType(TerrainType.OCEAN);
                }
            }
        }
        for(int i=0;i<20;i++){
            for(int j=0;j<20;j++){
                if (tiles[i][j].getType() != null) continue;
                TerrainType type = getType(random.nextInt(7));
                int probabilityDecrement= setProbabilityDecrement(type);
                int probability = setProbability(type);
                generateGroup(i,j,tiles,type,probability,random,probabilityDecrement);
            }
        }
        completeMap(tiles);
    }
    private static int setProbability(TerrainType type){
        if(type == TerrainType.MOUNTAIN) return 50;
        else if(type == TerrainType.HILL) return 30;
        else if(type == TerrainType.SNOW) return 70;
        else if(type == TerrainType.TUNDRA) return 70;
        return 90;
    }
    private static int setProbabilityDecrement(TerrainType type){
        if(type == TerrainType.MOUNTAIN) return 40;
        else if(type == TerrainType.HILL) return 70;
        else if(type == TerrainType.SNOW) return 30;
        else if(type == TerrainType.TUNDRA) return 30;
        return 10;
    }

    private static void completeMap(Tile[][] tiles){
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
                if(tiles[i][j].getFeature() == null) tiles[i][j].setFeature(TerrainFeature.NONE);
                if(tiles[i][j].getResource() == null) tiles[i][j].setResource(Resource.NONE);
                map.add(tiles[i][j]);
                tiles[i][j].initializeTile(tiles[i][j].getType(),tiles[i][j].getFeature());
            }
        }
        //TODO add river
        addResource();
    }

    private static void addResource(){
        Random random = new Random();
        int key;
        for(int i=0;i<20;i++){
            for(int j=0;j<20;j++){
                key = random.nextInt(100);
                if(tiles[i][j].getFeature() == TerrainFeature.FOREST){
                    if(key<10) tiles[i][j].setResource(Resource.BANANA);
                    else if(key<20) tiles[i][j].setResource(Resource.COLOR);
                    else if(key<30) tiles[i][j].setResource(Resource.JEWELERY);
                }else if(tiles[i][j].getFeature() == TerrainFeature.JUNGLE){
                    if(key<10) tiles[i][j].setResource(Resource.DEER);
                    else if(key<20) tiles[i][j].setResource(Resource.COLOR);
                    else if(key<30) tiles[i][j].setResource(Resource.FUR);
                    else if(key<40) tiles[i][j].setResource(Resource.SILK);
                } else if(tiles[i][j].getFeature() == TerrainFeature.SWAMP){
                    if(key<20) tiles[i][j].setResource(Resource.SUGAR);
                } else if(tiles[i][j].getFeature() == TerrainFeature.DELTA){
                    if(key<10) tiles[i][j].setResource(Resource.WHEAT);
                    else if(key<20) tiles[i][j].setResource(Resource.SUGAR);
                } else if(tiles[i][j].getType() == TerrainType.GRASS){
                    if(key<10) tiles[i][j].setResource(Resource.COW);
                    else if(key<20) tiles[i][j].setResource(Resource.SHEEP);
                    else if(key<30) tiles[i][j].setResource(Resource.COTTON);
                    else if(key<40) tiles[i][j].setResource(Resource.GOLD);
                    else if(key<50) tiles[i][j].setResource(Resource.MARBLE);
                    else if(key<60) tiles[i][j].setResource(Resource.COAL);
                    else if(key<70) tiles[i][j].setResource(Resource.HORSE);
                    else if(key<80) tiles[i][j].setResource(Resource.IRON);
                    else if(key<90) tiles[i][j].setResource(Resource.JEWELERY);
                } else if(tiles[i][j].getType() == TerrainType.PLAIN){
                    if(key<5) tiles[i][j].setResource(Resource.SHEEP);
                    else if(key<10) tiles[i][j].setResource(Resource.WHEAT);
                    else if(key<15) tiles[i][j].setResource(Resource.COTTON);
                    else if(key<20) tiles[i][j].setResource(Resource.JEWELERY);
                    else if(key<25) tiles[i][j].setResource(Resource.GOLD);
                    else if(key<30) tiles[i][j].setResource(Resource.BOKHOOR);
                    else if(key<35) tiles[i][j].setResource(Resource.TUSK);
                    else if(key<40) tiles[i][j].setResource(Resource.MARBLE);
                    else if(key<45) tiles[i][j].setResource(Resource.COAL);
                    else if(key<50) tiles[i][j].setResource(Resource.HORSE);
                    else if(key<55) tiles[i][j].setResource(Resource.IRON);
                } else if(tiles[i][j].getType() == TerrainType.TUNDRA){
                    if(key<10) tiles[i][j].setResource(Resource.DEER);
                    else if(key<20) tiles[i][j].setResource(Resource.FUR);
                    else if(key<30) tiles[i][j].setResource(Resource.JEWELERY);
                    else if(key<40) tiles[i][j].setResource(Resource.MARBLE);
                    else if(key<50) tiles[i][j].setResource(Resource.SILVER);
                    else if(key<60) tiles[i][j].setResource(Resource.HORSE);
                    else if(key<70) tiles[i][j].setResource(Resource.IRON);
                } else if(tiles[i][j].getType() == TerrainType.HILL){
                    if(key<10) tiles[i][j].setResource(Resource.DEER);
                    else if(key<20) tiles[i][j].setResource(Resource.SHEEP);
                    else if(key<30) tiles[i][j].setResource(Resource.JEWELERY);
                    else if(key<40) tiles[i][j].setResource(Resource.GOLD);
                    else if(key<50) tiles[i][j].setResource(Resource.MARBLE);
                    else if(key<60) tiles[i][j].setResource(Resource.SILVER);
                    else if(key<70) tiles[i][j].setResource(Resource.COAL);
                    else if(key<80) tiles[i][j].setResource(Resource.IRON);
                }else if(tiles[i][j].getType() == TerrainType.DESERT){
                    if(key<10) tiles[i][j].setResource(Resource.SHEEP);
                    else if(key<20) tiles[i][j].setResource(Resource.COTTON);
                    else if(key<30) tiles[i][j].setResource(Resource.JEWELERY);
                    else if(key<40) tiles[i][j].setResource(Resource.GOLD);
                    else if(key<50) tiles[i][j].setResource(Resource.BOKHOOR);
                    else if(key<60) tiles[i][j].setResource(Resource.MARBLE);
                    else if(key<70) tiles[i][j].setResource(Resource.SILVER);
                    else if(key<80) tiles[i][j].setResource(Resource.IRON);
                } else if(tiles[i][j].getType() == TerrainType.SNOW){
                    if(key<10) tiles[i][j].setResource(Resource.IRON);
                }
            }
        }
    }

//check kardane i va j dar v1 niaz nis chon ta 2 laye ocean e va goftim type null bashe unja

    private static void addCounter(int[] counter, TerrainType type){
        if(type == TerrainType.DESERT) counter[0]++;
        else if(type == TerrainType.GRASS) counter[1]++;
        else if(type == TerrainType.HILL) counter[2]++;
        else if(type == TerrainType.MOUNTAIN) counter[3]++;
        else if(type == TerrainType.PLAIN) counter[4]++;
        else if(type == TerrainType.TUNDRA) counter[5]++;
        else if(type == TerrainType.SNOW) counter[6]++;
    }

    private static TerrainType getType(int key){
        if(key == 0) return TerrainType.DESERT;
        else if(key == 1) return TerrainType.GRASS;
        else if(key == 2) return TerrainType.HILL;
        else if(key == 3) return TerrainType.MOUNTAIN;
        else if(key == 4) return TerrainType.PLAIN;
        else if(key == 5) return TerrainType.TUNDRA;
        else return TerrainType.SNOW;
    }
    private static void generateGroup(int i,int j,Tile[][] tiles, TerrainType type, int probability,Random random,int probabilityDecrement){
        tiles[i][j].setType(type);
        int key = random.nextInt(100);
        if(key < 50 && tiles[i][j].getFeature() == null){
            if(type == TerrainType.GRASS){
                tiles[i][j].setFeature(TerrainFeature.FOREST);
                if(key<5) tiles[i][j].setFeature(TerrainFeature.SWAMP);
            }else if(type == TerrainType.PLAIN){
                tiles[i][j].setFeature(TerrainFeature.JUNGLE);
                if(key<5) tiles[i][j].setFeature(TerrainFeature.SWAMP);
            }else if(type == TerrainType.SNOW){
                tiles[i][j].setFeature(TerrainFeature.ICE);
            }else if(type == TerrainType.DESERT && key<20){
                tiles[i][j].setFeature(TerrainFeature.OASIS);
            }
        }
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

    private static boolean checkIAndJ(int i,int j){
        return i <= 19 && i >= 0 && j <= 19 && j >= 0;
    }

    public static Tile[][] getTiles() {
        return tiles;
    }
}
