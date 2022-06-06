package Model;

import Controller.UnitController;
import Model.Map.Resource;
import Model.Map.TerrainFeature;
import Model.Map.TerrainType;
import Model.Map.Tile;


import java.util.ArrayList;
import java.util.Random;

public class Game {
    private static Game instance;

    public static Game getInstance() {
        if (instance == null) instance = new Game();
        return instance;
    }

    private ArrayList<User> players = new ArrayList<>();
    private int turn;
    private int time;
    private ArrayList<Tile> map;
    private Tile[][] tiles = new Tile[20][20];

    public ArrayList<User> getPlayers() {
        return players;
    }

    public int getTurn() {
        return turn;
    }

    public int getTime() {
        return time;
    }

    public void nextTurn() {
        turn++;
        turn %= players.size();
        if (turn == 0) time++;
    }

    public void generateGame(ArrayList<User> users) {
        players = users;
        turn = 0;
        time = 1;
        generateMap();
        for (User player : players) {
            player.newCivilization();
            Random random = new Random(players.indexOf(player));
            int randomX, randomY;
            do {
                randomX = random.nextInt(20);
                randomY = random.nextInt(20);
            } while (UnitController.tileIsImpassable(tiles[randomX][randomY], null));
            player.getCivilization().createSettlerAndWarriorOnTile(tiles[randomX][randomY]);
            Tile settlerTile = player.getCivilization().getUnits().get(0).getTile();
            System.out.println("i am " + player.getUsername() + ", my first unit is on " +
                    settlerTile.getIndexInMapI() + ", " + settlerTile.getIndexInMapJ());
            Tile warriorTile = player.getCivilization().getUnits().get(1).getTile();
            System.out.println("my second unit is on " +
                    warriorTile.getIndexInMapI() + ", " + warriorTile.getIndexInMapJ());
            makeFirstTilesVisible(player.getCivilization(), settlerTile, warriorTile);
        }
    }

    private void makeFirstTilesVisible(Civilization civilization, Tile settlerTile, Tile warriorTile) {
        ArrayList<Tile> visibleTiles = settlerTile.getNeighbors();
        visibleTiles.addAll(warriorTile.getNeighbors());
        for (Tile tile : visibleTiles)
            civilization.getTileVisionStatuses()[tile.getIndexInMapI()][tile.getIndexInMapJ()] = TileStatus.CLEAR;
    }

    public void generateMap() {
        map = new ArrayList<>();
        Random random = new Random(0);
        int centersParameter = 1;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                tiles[i][j] = new Tile();
                tiles[i][j].setIndexInMapI(i);
                tiles[i][j].setIndexInMapJ(j);
                tiles[i][j].setCenterY(j * centersParameter);
                tiles[i][j].setCenterX(i * centersParameter * 2 + centersParameter * (j % 2));
                if (i < 1 || j < 1 || i > 18 || j > 18) {
                    tiles[i][j].setType(TerrainType.OCEAN);
                } else if ((i < 2 || j < 2 || i > 17 || j > 17) && random.nextInt(2) == 0) {
                    tiles[i][j].setType(TerrainType.OCEAN);
                }
            }
        }
        generateRivers();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (tiles[i][j].getType() != null) continue;
                TerrainType type = getType(random.nextInt(7));
                int probabilityDecrement = setProbabilityDecrement(type);
                int probability = setProbability(type);
                generateGroup(i, j, tiles, type, probability, random, probabilityDecrement);
            }
        }
        completeMap(tiles);
    }

    private int setProbability(TerrainType type) {
        if (type == TerrainType.MOUNTAIN) return 50;
        else if (type == TerrainType.HILL) return 30;
        else if (type == TerrainType.SNOW) return 70;
        else if (type == TerrainType.TUNDRA) return 70;
        return 90;
    }

    private int setProbabilityDecrement(TerrainType type) {
        if (type == TerrainType.MOUNTAIN) return 40;
        else if (type == TerrainType.HILL) return 70;
        else if (type == TerrainType.SNOW) return 30;
        else if (type == TerrainType.TUNDRA) return 30;
        return 17;
    }

    public void completeMap(Tile[][] tiles) {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (tiles[i][j].getType() == null) {
                    int[] counter = new int[7];
                    addCounter(counter, tiles[i + 1][j].getType());
                    addCounter(counter, tiles[i - 1][j].getType());
                    addCounter(counter, tiles[i][j + 1].getType());
                    addCounter(counter, tiles[i + 1][j - 1].getType());
                    if (j % 2 == 1) {
                        addCounter(counter, tiles[i + 1][j + 1].getType());
                        addCounter(counter, tiles[i + 1][j - 1].getType());
                    } else {
                        addCounter(counter, tiles[i - 1][j - 1].getType());
                        addCounter(counter, tiles[i - 1][j + 1].getType());
                    }
                    int maxValueIndex = 4;
                    for (int k = 0; k < 7; k++) {
                        if (counter[k] > counter[maxValueIndex]) maxValueIndex = k;
                    }
                    tiles[i][j].setType(getType(maxValueIndex));
                }
                if (tiles[i][j].getFeature() == null) tiles[i][j].setFeature(TerrainFeature.NONE);
                if (tiles[i][j].getResource() == null) tiles[i][j].setResource(Resource.NONE);
                map.add(tiles[i][j]);
                tiles[i][j].initializeTile(tiles[i][j].getType(), tiles[i][j].getFeature());
            }
        }
        addResource();
    }

    private void addResource() {
        Random random = new Random(0);
        int key;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                key = random.nextInt(100);
                if (tiles[i][j].getFeature() == TerrainFeature.FOREST) {
                    if (key < 10) tiles[i][j].setResource(Resource.BANANA);
                    else if (key < 20) tiles[i][j].setResource(Resource.COLOR);
                    else if (key < 30) tiles[i][j].setResource(Resource.JEWELERY);
                } else if (tiles[i][j].getFeature() == TerrainFeature.JUNGLE) {
                    if (key < 10) tiles[i][j].setResource(Resource.DEER);
                    else if (key < 20) tiles[i][j].setResource(Resource.COLOR);
                    else if (key < 30) tiles[i][j].setResource(Resource.FUR);
                    else if (key < 40) tiles[i][j].setResource(Resource.SILK);
                } else if (tiles[i][j].getFeature() == TerrainFeature.MARSH) {
                    if (key < 20) tiles[i][j].setResource(Resource.SUGAR);
                } else if (tiles[i][j].getFeature() == TerrainFeature.DELTA) {
                    if (key < 10) tiles[i][j].setResource(Resource.WHEAT);
                    else if (key < 20) tiles[i][j].setResource(Resource.SUGAR);
                } else if (tiles[i][j].getType() == TerrainType.GRASS) {
                    if (key < 10) tiles[i][j].setResource(Resource.COW);
                    else if (key < 20) tiles[i][j].setResource(Resource.SHEEP);
                    else if (key < 30) tiles[i][j].setResource(Resource.COTTON);
                    else if (key < 40) tiles[i][j].setResource(Resource.GOLD);
                    else if (key < 50) tiles[i][j].setResource(Resource.MARBLE);
                    else if (key < 60) tiles[i][j].setResource(Resource.COAL);
                    else if (key < 70) tiles[i][j].setResource(Resource.HORSE);
                    else if (key < 80) tiles[i][j].setResource(Resource.IRON);
                    else if (key < 90) tiles[i][j].setResource(Resource.JEWELERY);
                } else if (tiles[i][j].getType() == TerrainType.PLAIN) {
                    if (key < 5) tiles[i][j].setResource(Resource.SHEEP);
                    else if (key < 10) tiles[i][j].setResource(Resource.WHEAT);
                    else if (key < 15) tiles[i][j].setResource(Resource.COTTON);
                    else if (key < 20) tiles[i][j].setResource(Resource.JEWELERY);
                    else if (key < 25) tiles[i][j].setResource(Resource.GOLD);
                    else if (key < 30) tiles[i][j].setResource(Resource.BOKHOOR);
                    else if (key < 35) tiles[i][j].setResource(Resource.TUSK);
                    else if (key < 40) tiles[i][j].setResource(Resource.MARBLE);
                    else if (key < 45) tiles[i][j].setResource(Resource.COAL);
                    else if (key < 50) tiles[i][j].setResource(Resource.HORSE);
                    else if (key < 55) tiles[i][j].setResource(Resource.IRON);
                } else if (tiles[i][j].getType() == TerrainType.TUNDRA) {
                    if (key < 10) tiles[i][j].setResource(Resource.DEER);
                    else if (key < 20) tiles[i][j].setResource(Resource.FUR);
                    else if (key < 30) tiles[i][j].setResource(Resource.JEWELERY);
                    else if (key < 40) tiles[i][j].setResource(Resource.MARBLE);
                    else if (key < 50) tiles[i][j].setResource(Resource.SILVER);
                    else if (key < 60) tiles[i][j].setResource(Resource.HORSE);
                    else if (key < 70) tiles[i][j].setResource(Resource.IRON);
                } else if (tiles[i][j].getType() == TerrainType.HILL) {
                    if (key < 10) tiles[i][j].setResource(Resource.DEER);
                    else if (key < 20) tiles[i][j].setResource(Resource.SHEEP);
                    else if (key < 30) tiles[i][j].setResource(Resource.JEWELERY);
                    else if (key < 40) tiles[i][j].setResource(Resource.GOLD);
                    else if (key < 50) tiles[i][j].setResource(Resource.MARBLE);
                    else if (key < 60) tiles[i][j].setResource(Resource.SILVER);
                    else if (key < 70) tiles[i][j].setResource(Resource.COAL);
                    else if (key < 80) tiles[i][j].setResource(Resource.IRON);
                } else if (tiles[i][j].getType() == TerrainType.DESERT) {
                    if (key < 10) tiles[i][j].setResource(Resource.SHEEP);
                    else if (key < 20) tiles[i][j].setResource(Resource.COTTON);
                    else if (key < 30) tiles[i][j].setResource(Resource.JEWELERY);
                    else if (key < 40) tiles[i][j].setResource(Resource.GOLD);
                    else if (key < 50) tiles[i][j].setResource(Resource.BOKHOOR);
                    else if (key < 60) tiles[i][j].setResource(Resource.MARBLE);
                    else if (key < 70) tiles[i][j].setResource(Resource.SILVER);
                    else if (key < 80) tiles[i][j].setResource(Resource.IRON);
                } else if (tiles[i][j].getType() == TerrainType.SNOW) {
                    if (key < 10) tiles[i][j].setResource(Resource.IRON);
                }
            }
        }
    }

    private void addCounter(int[] counter, TerrainType type) {
        if (type == TerrainType.DESERT) counter[0]++;
        else if (type == TerrainType.GRASS) counter[1]++;
        else if (type == TerrainType.HILL) counter[2]++;
        else if (type == TerrainType.MOUNTAIN) counter[3]++;
        else if (type == TerrainType.PLAIN) counter[4]++;
        else if (type == TerrainType.TUNDRA) counter[5]++;
        else if (type == TerrainType.SNOW) counter[6]++;
        else counter[0]++;
    }

    private TerrainType getType(int key) {
        if (key == 0) return TerrainType.DESERT;
        else if (key == 1) return TerrainType.GRASS;
        else if (key == 2) return TerrainType.HILL;
        else if (key == 3) return TerrainType.MOUNTAIN;
        else if (key == 4) return TerrainType.PLAIN;
        else if (key == 5) return TerrainType.TUNDRA;
        else return TerrainType.SNOW;
    }

    private void generateGroup(int i, int j, Tile[][] tiles, TerrainType type, int probability, Random random, int probabilityDecrement) {
        tiles[i][j].setType(type);
        int key = random.nextInt(100);
        if (key < 50 && tiles[i][j].getFeature() == null) {
            if (type == TerrainType.GRASS) {
                tiles[i][j].setFeature(TerrainFeature.FOREST);
                if (key < 5) tiles[i][j].setFeature(TerrainFeature.MARSH);
            } else if (type == TerrainType.PLAIN) {
                tiles[i][j].setFeature(TerrainFeature.JUNGLE);
                if (key < 5) tiles[i][j].setFeature(TerrainFeature.MARSH);
            } else if (type == TerrainType.SNOW) {
                tiles[i][j].setFeature(TerrainFeature.ICE);
            } else if (type == TerrainType.DESERT && key < 20) {
                tiles[i][j].setFeature(TerrainFeature.OASIS);
            } else if(tiles[i][j].isRiverAtLeft() && key<20){
                tiles[i][j].setFeature(TerrainFeature.DELTA);
            }
        }
        if (checkIAndJ(i - 1, j) && (probability > key) && tiles[i - 1][j].getType() == null) {
            generateGroup(i - 1, j, tiles, type, probability - probabilityDecrement, random, probabilityDecrement);
        }
        key = random.nextInt(100);
        if (checkIAndJ(i, j + 1) && (probability > key) && tiles[i][j + 1].getType() == null) {
            generateGroup(i, j + 1, tiles, type, probability - probabilityDecrement, random, probabilityDecrement);
        }
        key = random.nextInt(100);
        if (j % 2 == 1 && checkIAndJ(i + 1, j + 1) && (probability > key) && tiles[i + 1][j + 1].getType() == null) {
            generateGroup(i + 1, j + 1, tiles, type, probability - probabilityDecrement, random, probabilityDecrement);
        }
        key = random.nextInt(100);
        if (j % 2 == 0 && checkIAndJ(i - 1, j - 1) && (probability > key) && tiles[i - 1][j - 1].getType() == null) {
            generateGroup(i - 1, j - 1, tiles, type, probability - probabilityDecrement, random, probabilityDecrement);
        }
        key = random.nextInt(100);
        if (checkIAndJ(i + 1, j) && (probability > key) && tiles[i + 1][j].getType() == null) {
            generateGroup(i + 1, j, tiles, type, probability - probabilityDecrement, random, probabilityDecrement);
        }
        key = random.nextInt(100);
        if (j % 2 == 1 && checkIAndJ(i + 1, j - 1) && (probability > key) && tiles[i + 1][j - 1].getType() == null) {
            generateGroup(i + 1, j - 1, tiles, type, probability - probabilityDecrement, random, probabilityDecrement);
        }
        key = random.nextInt(100);
        if (j % 2 == 0 && checkIAndJ(i - 1, j + 1) && (probability > key) && tiles[i - 1][j + 1].getType() == null) {
            generateGroup(i - 1, j + 1, tiles, type, probability - probabilityDecrement, random, probabilityDecrement);
        }
        key = random.nextInt(100);
        if (checkIAndJ(i, j - 1) && (probability > key) && tiles[i][j - 1].getType() == null) {
            generateGroup(i, j - 1, tiles, type, probability - probabilityDecrement, random, probabilityDecrement);
        }
    }

    private void generateRivers() {
        Random random = new Random(0);
        int howManyRivers = random.nextInt(4) + 1;
        for (int k = 0; k < howManyRivers; k++) {
            int i = random.nextInt(7) + 2;
            int j = random.nextInt(15) + 3;
            int targetI = random.nextInt(7) + i + 4;
            for (int l = i; l < targetI + 1; l++) {
                tiles[l][j].setRiverAtLeft(true);
            }
        }
    }

    private boolean checkIAndJ(int i, int j) {
        return i <= 19 && i >= 0 && j <= 19 && j >= 0;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
}
