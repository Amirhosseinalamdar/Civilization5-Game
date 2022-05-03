package Model;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public enum Technology {
    AGRICULTURE(20, null),
    ANIMAL_HUSBANDRY(35, new ArrayList<>(Collections.singletonList(AGRICULTURE))),
    MINING(35, new ArrayList<>(Collections.singletonList(AGRICULTURE))),
    POTTERY(35, new ArrayList<>(Collections.singletonList(AGRICULTURE))),
    ARCHERY(35, new ArrayList<>(Collections.singletonList(AGRICULTURE))),
    BRONZE_WORKING(55, new ArrayList<>(Collections.singletonList(MINING))),
    CALENDER(70, new ArrayList<>(Collections.singletonList(POTTERY))),
    MASONRY(55, new ArrayList<>(Collections.singletonList(MINING))),
    WHEEL(55, new ArrayList<>(Collections.singletonList(ANIMAL_HUSBANDRY))),
    TRAPPING(55, new ArrayList<>(Collections.singletonList(ANIMAL_HUSBANDRY))),
    WRITING(55, new ArrayList<>(Collections.singletonList(POTTERY))),
    CONSTRUCTION(100, new ArrayList<>(Collections.singletonList(MASONRY))),
    HORSE_RIDING(100, new ArrayList<>(Collections.singletonList(WHEEL))),
    IRON_WORKING(150, new ArrayList<>(Collections.singletonList(BRONZE_WORKING))),
    MATH(100, new ArrayList<>(Arrays.asList(WHEEL, ARCHERY))),
    PHILOSOPHY(100, new ArrayList<>(Collections.singletonList(WRITING))),
    CURRENCY(250, new ArrayList<>(Collections.singletonList(MATH))),
    THEOLOGY(250, new ArrayList<>(Arrays.asList(CALENDER, PHILOSOPHY))),
    CIVIL_SERVICE(400, new ArrayList<>(Arrays.asList(PHILOSOPHY, TRAPPING))),
    CHIVALRY(440, new ArrayList<>(Arrays.asList(CIVIL_SERVICE, HORSE_RIDING, CURRENCY))),
    ENGINEERING(250, new ArrayList<>(Arrays.asList(MATH, CONSTRUCTION))),
    MACHINERY(440, new ArrayList<>(Collections.singletonList(ENGINEERING))),
    METAL_CASTING(240, new ArrayList<>(Collections.singletonList(IRON_WORKING))),
    PHYSICS(440, new ArrayList<>(Arrays.asList(ENGINEERING, METAL_CASTING))),
    STEEL(440, new ArrayList<>(Collections.singletonList(METAL_CASTING))),
    EDUCATION(440, new ArrayList<>(Collections.singletonList(THEOLOGY))),
    ACOUSTICS(650, new ArrayList<>(Collections.singletonList(EDUCATION))),
    ARCHAEOLOGY(1300, new ArrayList<>(Collections.singletonList(ACOUSTICS))),
    BANKING(650, new ArrayList<>(Arrays.asList(EDUCATION, CHIVALRY))),
    GUNPOWDER(680, new ArrayList<>(Arrays.asList(PHYSICS, STEEL))),
    CHEMISTRY(900, new ArrayList<>(Collections.singletonList(GUNPOWDER))),
    PRINTING_PRESS(650, new ArrayList<>(Arrays.asList(MACHINERY, CHEMISTRY))),
    FERTILIZER(1300, new ArrayList<>(Collections.singletonList(CHEMISTRY))),
    METALLURGY(900, new ArrayList<>(Collections.singletonList(GUNPOWDER))),
    ECONOMICS(900, new ArrayList<>(Arrays.asList(BANKING, PRINTING_PRESS))),
    MILITARY_SCIENCE(1300, new ArrayList<>(Arrays.asList(ECONOMICS, CHEMISTRY))),
    RIFLING(1425, new ArrayList<>(Collections.singletonList(METALLURGY))),
    SCIENTIFIC_THEORY(1300, new ArrayList<>(Collections.singletonList(ACOUSTICS))),
    BIOLOGY(1680, new ArrayList<>(Arrays.asList(ARCHAEOLOGY, SCIENTIFIC_THEORY))),
    STEAM_POWER(1680, new ArrayList<>(Arrays.asList(SCIENTIFIC_THEORY, MILITARY_SCIENCE))),
    DYNAMITE(1900, new ArrayList<>(Arrays.asList(FERTILIZER, RIFLING))),
    ELECTRICITY(1900, new ArrayList<>(Arrays.asList(BIOLOGY, STEAM_POWER))),
    RADIO(2200, new ArrayList<>(Collections.singletonList(ELECTRICITY))),
    RAILROAD(1900, new ArrayList<>(Collections.singletonList(STEAM_POWER))),
    REPLACEABLE_PARTS(1900, new ArrayList<>(Collections.singletonList(STEAM_POWER))),
    TELEGRAPH(2200, new ArrayList<>(Collections.singletonList(ELECTRICITY))),
    COMBUSTION(2200, new ArrayList<>(Arrays.asList(REPLACEABLE_PARTS, RAILROAD, DYNAMITE)));

    private final int cost;
    private final ArrayList <Technology> parents;

    Technology(int cost, ArrayList <Technology> parents) {
        this.parents = parents;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public ArrayList <Technology> getParents() {
        return parents;
    }
}
