package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public enum Technology {
    AGRICULTURE(20, null, new ArrayList<>(Collections.singletonList("Farm"))),
    ANIMAL_HUSBANDRY(35, new ArrayList<>(Collections.singletonList(AGRICULTURE)), new ArrayList<>(Arrays.asList("Horses", "Pasture"))),
    MINING(35, new ArrayList<>(Collections.singletonList(AGRICULTURE)), new ArrayList<>(Arrays.asList("Mines", "remove Forest"))),
    POTTERY(35, new ArrayList<>(Collections.singletonList(AGRICULTURE)), new ArrayList<>(Collections.singletonList("Granary"))),
    ARCHERY(35, new ArrayList<>(Collections.singletonList(AGRICULTURE)), new ArrayList<>(Collections.singletonList("Archer"))),
    BRONZE_WORKING(55, new ArrayList<>(Collections.singletonList(MINING)), new ArrayList<>(Arrays.asList("Spearman", "Barracks", "remove Jungle"))),
    CALENDER(70, new ArrayList<>(Collections.singletonList(POTTERY)), new ArrayList<>(Collections.singletonList("Plantation"))),
    MASONRY(55, new ArrayList<>(Collections.singletonList(MINING)), new ArrayList<>(Arrays.asList("Walls", "Quarry", "clear a Marsh"))),
    WHEEL(55, new ArrayList<>(Collections.singletonList(ANIMAL_HUSBANDRY)), new ArrayList<>(Arrays.asList("Chariot Archer", "Water Mill", "build a Road"))),
    TRAPPING(55, new ArrayList<>(Collections.singletonList(ANIMAL_HUSBANDRY)), new ArrayList<>(Arrays.asList("Trading Post", "Camp"))),
    WRITING(55, new ArrayList<>(Collections.singletonList(POTTERY)), new ArrayList<>(Collections.singletonList("Library"))),
    CONSTRUCTION(100, new ArrayList<>(Collections.singletonList(MASONRY)), new ArrayList<>(Arrays.asList("Colosseum", "bridges over rivers"))),
    HORSEBACK_RIDING(100, new ArrayList<>(Collections.singletonList(WHEEL)), new ArrayList<>(Arrays.asList("Horseman", "Stable", "Circus"))),
    IRON_WORKING(150, new ArrayList<>(Collections.singletonList(BRONZE_WORKING)), new ArrayList<>(Arrays.asList("Swordsman", "Legion", "Armory", "Iron"))),
    MATHEMATICS(100, new ArrayList<>(Arrays.asList(WHEEL, ARCHERY)), new ArrayList<>(Arrays.asList("Catapult", "Courthouse"))),
    PHILOSOPHY(100, new ArrayList<>(Collections.singletonList(WRITING)), new ArrayList<>(Arrays.asList("Burial Tomb", "Temple"))),
    CURRENCY(250, new ArrayList<>(Collections.singletonList(MATHEMATICS)), new ArrayList<>(Collections.singletonList("Market"))),
    THEOLOGY(250, new ArrayList<>(Arrays.asList(CALENDER, PHILOSOPHY)), new ArrayList<>(Arrays.asList("Monastery", "Garden"))),
    CIVIL_SERVICE(400, new ArrayList<>(Arrays.asList(PHILOSOPHY, TRAPPING)), new ArrayList<>(Collections.singletonList("Pikeman"))),
    CHIVALRY(440, new ArrayList<>(Arrays.asList(CIVIL_SERVICE, HORSEBACK_RIDING, CURRENCY)), new ArrayList<>(Arrays.asList("Knight", "Camel Archer", "Castle"))),
    ENGINEERING(250, new ArrayList<>(Arrays.asList(MATHEMATICS, CONSTRUCTION)), null),
    MACHINERY(440, new ArrayList<>(Collections.singletonList(ENGINEERING)), new ArrayList<>(Arrays.asList("Crossbowman", "1.2 faster road movement"))),
    METAL_CASTING(240, new ArrayList<>(Collections.singletonList(IRON_WORKING)), new ArrayList<>(Arrays.asList("Forge", "Workshop"))),
    PHYSICS(440, new ArrayList<>(Arrays.asList(ENGINEERING, METAL_CASTING)), new ArrayList<>(Collections.singletonList("Trebuchet"))),
    STEEL(440, new ArrayList<>(Collections.singletonList(METAL_CASTING)), new ArrayList<>(Collections.singletonList("Longswordsman"))),
    EDUCATION(440, new ArrayList<>(Collections.singletonList(THEOLOGY)), new ArrayList<>(Collections.singletonList("University"))),
    ACOUSTICS(650, new ArrayList<>(Collections.singletonList(EDUCATION)), null),
    ARCHAEOLOGY(1300, new ArrayList<>(Collections.singletonList(ACOUSTICS)), new ArrayList<>(Collections.singletonList("Museum"))),
    BANKING(650, new ArrayList<>(Arrays.asList(EDUCATION, CHIVALRY)), new ArrayList<>(Arrays.asList("Satrap's Court", "Bank"))),
    GUNPOWDER(680, new ArrayList<>(Arrays.asList(PHYSICS, STEEL)), new ArrayList<>(Collections.singletonList("Musketman"))),
    CHEMISTRY(900, new ArrayList<>(Collections.singletonList(GUNPOWDER)), new ArrayList<>(Collections.singletonList("Ironworks"))),
    PRINTING_PRESS(650, new ArrayList<>(Arrays.asList(MACHINERY, CHEMISTRY)), new ArrayList<>(Collections.singletonList("Theater"))),
    FERTILIZER(1300, new ArrayList<>(Collections.singletonList(CHEMISTRY)), new ArrayList<>(Collections.singletonList("Farms without Fresh Water yield increased by 1"))),
    METALLURGY(900, new ArrayList<>(Collections.singletonList(GUNPOWDER)), new ArrayList<>(Collections.singletonList("Lancer"))),
    ECONOMICS(900, new ArrayList<>(Arrays.asList(BANKING, PRINTING_PRESS)), new ArrayList<>(Collections.singletonList("Windmill"))),
    MILITARY_SCIENCE(1300, new ArrayList<>(Arrays.asList(ECONOMICS, CHEMISTRY)), new ArrayList<>(Arrays.asList("Cavalry", "Military Academy"))),
    RIFLING(1425, new ArrayList<>(Collections.singletonList(METALLURGY)), new ArrayList<>(Collections.singletonList("Rifleman"))),
    SCIENTIFIC_THEORY(1300, new ArrayList<>(Collections.singletonList(ACOUSTICS)), new ArrayList<>(Arrays.asList("Public School", "Coal"))),
    BIOLOGY(1680, new ArrayList<>(Arrays.asList(ARCHAEOLOGY, SCIENTIFIC_THEORY)), null),
    STEAM_POWER(1680, new ArrayList<>(Arrays.asList(SCIENTIFIC_THEORY, MILITARY_SCIENCE)), new ArrayList<>(Collections.singletonList("Factory"))),
    DYNAMITE(1900, new ArrayList<>(Arrays.asList(FERTILIZER, RIFLING)), new ArrayList<>(Collections.singletonList("Artillery"))),
    ELECTRICITY(1900, new ArrayList<>(Arrays.asList(BIOLOGY, STEAM_POWER)), new ArrayList<>(Collections.singletonList("Stock Exchange"))),
    RADIO(2200, new ArrayList<>(Collections.singletonList(ELECTRICITY)), new ArrayList<>(Collections.singletonList("Broadcast Tower"))),
    RAILROAD(1900, new ArrayList<>(Collections.singletonList(STEAM_POWER)), new ArrayList<>(Arrays.asList("Arsenal", "Railroad"))),
    REPLACEABLE_PARTS(1900, new ArrayList<>(Collections.singletonList(STEAM_POWER)), new ArrayList<>(Arrays.asList("Anti-Tank Gun", "Infantry"))),
    TELEGRAPH(2200, new ArrayList<>(Collections.singletonList(ELECTRICITY)), new ArrayList<>(Collections.singletonList("Military Base"))),
    COMBUSTION(2200, new ArrayList<>(Arrays.asList(REPLACEABLE_PARTS, RAILROAD, DYNAMITE)), new ArrayList<>(Arrays.asList("Tank", "Panzer")));

    private final int cost;
    private final ArrayList<Technology> parents;
    private final ArrayList<String> unlocks;

    Technology(int cost, ArrayList<Technology> parents, ArrayList<String> unlocks) {
        this.parents = parents;
        this.unlocks = unlocks;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public ArrayList<Technology> getParents() {
        return parents;
    }

    public ArrayList<String> getUnlocks() {
        return unlocks;
    }
}
