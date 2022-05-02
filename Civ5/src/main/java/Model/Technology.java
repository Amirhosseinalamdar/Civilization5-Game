package Model;

public enum Technology {
    AGRICULTURE(20, null, null, null),
    ANIMAL_HUSBANDRY(35, AGRICULTURE, null, null),
    MINING(35, AGRICULTURE, null, null),
    POTTERY(35, AGRICULTURE, null, null),
    ARCHERY(35, AGRICULTURE, null, null),
    BRONZE_WORKING(55, MINING, null, null),
    CALENDER(70, POTTERY, null, null),
    MASONRY(55, MINING, null, null),
    WHEEL(55, ANIMAL_HUSBANDRY, null, null),
    TRAPPING(55, ANIMAL_HUSBANDRY, null, null),
    WRITING(55, POTTERY, null, null),
    CONSTRUCTION(100, MASONRY, null, null),
    HORSE_RIDING(100, WHEEL, null, null),
    IRON_WORKING(150, BRONZE_WORKING, null, null),
    MATH(100, WHEEL, ARCHERY, null),
    PHILOSOPHY(100, WRITING, null, null),
    CURRENCY(250, MATH, null, null),
    THEOLOGY(250, CALENDER, PHILOSOPHY, null),
    CIVIL_SERVICE(400, PHILOSOPHY, TRAPPING, null),
    CHIVALRY(440, CIVIL_SERVICE, HORSE_RIDING, CURRENCY),
    ENGINEERING(250, MATH, CONSTRUCTION, null),
    MACHINERY(440, ENGINEERING, null, null),
    METAL_CASTING(240, IRON_WORKING, null, null),
    PHYSICS(440, ENGINEERING, METAL_CASTING, null),
    STEEL(440, METAL_CASTING, null, null),
    EDUCATION(440, THEOLOGY, null, null),
    ACOUSTICS(650, EDUCATION, null, null),
    ARCHAEOLOGY(1300, ACOUSTICS, null, null),
    BANKING(650, EDUCATION, CHIVALRY, null),
    GUNPOWDER(680, PHYSICS, STEEL, null),
    CHEMISTRY(900, GUNPOWDER, null, null),
    PRINTING_PRESS(650, MACHINERY, CHEMISTRY, null),
    FERTILIZER(1300, CHEMISTRY, null, null),
    METALLURGY(900, GUNPOWDER, null, null),
    ECONOMICS(900, BANKING, PRINTING_PRESS, null),
    MILITARY_SCIENCE(1300, ECONOMICS, CHEMISTRY, null),
    RIFLING(1425, METALLURGY, null, null),
    SCIENTIFIC_THEORY(1300, ACOUSTICS, null, null),
    BIOLOGY(1680, ARCHAEOLOGY, SCIENTIFIC_THEORY, null),
    STEAM_POWER(1680, SCIENTIFIC_THEORY, MILITARY_SCIENCE, null),
    DYNAMITE(1900, FERTILIZER, RIFLING, null),
    ELECTRICITY(1900, BIOLOGY, STEAM_POWER, null),
    RADIO(2200, ELECTRICITY, null, null),
    RAILROAD(1900, STEAM_POWER, null, null),
    REPLACEABLE_PARTS(1900, STEAM_POWER, null, null),
    TELEGRAPH(2200, ELECTRICITY, null, null),
    COMBUSTION(2200, REPLACEABLE_PARTS, RAILROAD, DYNAMITE);

    private final Technology parent1, parent2, parent3;
    private final int cost;

    Technology(int cost, Technology parent1, Technology parent2, Technology parent3) {
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.parent3 = parent3;
        this.cost = cost;
    }

    public Technology getParent1() {
        return parent1;
    }

    public Technology getParent2() {
        return parent2;
    }

    public Technology getParent3() {
        return parent3;
    }

    public int getCost() {
        return cost;
    }
}
