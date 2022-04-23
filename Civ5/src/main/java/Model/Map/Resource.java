package Model.Map;

public enum Resource {
    BANANA(1,0,0,Improvement.SEEDING_AND_WORKING),
    COW(1,0,0,Improvement.PASTURE),
    DEER(1,0,0,Improvement.CAMP),
    SHEEP(1,0,0,Improvement.PASTURE),
    WHEAT(1,0,0,Improvement.FARM),
    COAL(0,1,0,Improvement.MINE),//TODO
    HORSE(0,1,0,Improvement.PASTURE),//TODO dar doc tozihate bishtari daran
    IRON(0,1,0,Improvement.MINE),//TODO
    COTTON(0,0,2,Improvement.SEEDING_AND_WORKING),
    COLOR(0,0,2,Improvement.SEEDING_AND_WORKING),
    FUR(0,0,2,Improvement.CAMP),
    JEWELERY(0,0,3,Improvement.MINE),
    GOLD(0,0,2,Improvement.MINE),
    BOKHOOR(0,0,2,Improvement.SEEDING_AND_WORKING),
    TUSK(0,0,2,Improvement.CAMP),
    MARBLE(0,0,2,Improvement.STONE_MINE),
    SILK(0,0,2,Improvement.SEEDING_AND_WORKING),
    SILVER(0,0,2,Improvement.MINE),
    SUGAR(0,0,2,Improvement.SEEDING_AND_WORKING),
    NONE(0,0,0,Improvement.NONE);

    private int food;
    private int production;
    private int gold;
    private Improvement improvement;

    Resource(int food,int production,int gold,Improvement improvement){
        this.food = food;
        this.gold = gold;
        this.production = production;
        this.improvement = improvement;
    }
}
