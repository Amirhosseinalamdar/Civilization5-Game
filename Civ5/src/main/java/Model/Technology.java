package Model;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public enum Technology {
    AGRICULTURE(20, null, new ArrayList<>(Collections.singletonList("Farm")),ImageBase.AGRICULTURE.getImage()),
    ANIMAL_HUSBANDRY(35, new ArrayList<>(Collections.singletonList(AGRICULTURE)), new ArrayList<>(Arrays.asList("Horses", "Pasture")),ImageBase.ANIMAL_HUSBANDRY.getImage()),
    MINING(35, new ArrayList<>(Collections.singletonList(AGRICULTURE)), new ArrayList<>(Arrays.asList("Mines", "remove Forest")),ImageBase.MINING.getImage()),
    POTTERY(35, new ArrayList<>(Collections.singletonList(AGRICULTURE)), new ArrayList<>(Collections.singletonList("Granary")),ImageBase.POTTERY.getImage()),
    ARCHERY(35, new ArrayList<>(Collections.singletonList(AGRICULTURE)), new ArrayList<>(Collections.singletonList("Archer")),ImageBase.ARCHERY.getImage()),
    BRONZE_WORKING(55, new ArrayList<>(Collections.singletonList(MINING)), new ArrayList<>(Arrays.asList("Spearman", "Barracks", "remove Jungle")),ImageBase.BRONZE_WORKING.getImage()),
    CALENDER(70, new ArrayList<>(Collections.singletonList(POTTERY)), new ArrayList<>(Collections.singletonList("Plantation")),ImageBase.CALENDER.getImage()),
    MASONRY(55, new ArrayList<>(Collections.singletonList(MINING)), new ArrayList<>(Arrays.asList("Walls", "Quarry", "clear a Marsh")),ImageBase.MASONRY.getImage()),
    WHEEL(55, new ArrayList<>(Collections.singletonList(ANIMAL_HUSBANDRY)), new ArrayList<>(Arrays.asList("Chariot Archer", "Water Mill", "build a Road")),ImageBase.THE_WHEEL.getImage()),
    TRAPPING(55, new ArrayList<>(Collections.singletonList(ANIMAL_HUSBANDRY)), new ArrayList<>(Arrays.asList("Trading Post", "Camp")),ImageBase.TRAPPING.getImage()),
    WRITING(55, new ArrayList<>(Collections.singletonList(POTTERY)), new ArrayList<>(Collections.singletonList("Library")),ImageBase.WRITING.getImage()),
    CONSTRUCTION(100, new ArrayList<>(Collections.singletonList(MASONRY)), new ArrayList<>(Arrays.asList("Colosseum", "bridges over rivers")),ImageBase.CONSTRUCTION.getImage()),
    HORSEBACK_RIDING(100, new ArrayList<>(Collections.singletonList(WHEEL)), new ArrayList<>(Arrays.asList("Horseman", "Stable", "Circus")),ImageBase.HORSEBACK_RIDING.getImage()),
    IRON_WORKING(150, new ArrayList<>(Collections.singletonList(BRONZE_WORKING)), new ArrayList<>(Arrays.asList("Swordsman", "Legion", "Armory", "Iron")),ImageBase.IRON_WORKING.getImage()),
    MATHEMATICS(100, new ArrayList<>(Arrays.asList(WHEEL, ARCHERY)), new ArrayList<>(Arrays.asList("Catapult", "Courthouse")),ImageBase.MATHEMATICS.getImage()),
    PHILOSOPHY(100, new ArrayList<>(Collections.singletonList(WRITING)), new ArrayList<>(Arrays.asList("Burial Tomb", "Temple")),ImageBase.PHILOSOPHY.getImage()),
    CURRENCY(250, new ArrayList<>(Collections.singletonList(MATHEMATICS)), new ArrayList<>(Collections.singletonList("Market")),ImageBase.CURRENCY.getImage()),
    THEOLOGY(250, new ArrayList<>(Arrays.asList(CALENDER, PHILOSOPHY)), new ArrayList<>(Arrays.asList("Monastery", "Garden")),ImageBase.THEOLOGY.getImage()),
    CIVIL_SERVICE(400, new ArrayList<>(Arrays.asList(PHILOSOPHY, TRAPPING)), new ArrayList<>(Collections.singletonList("Pikeman")),ImageBase.CIVIL_SERVICE.getImage()),
    CHIVALRY(440, new ArrayList<>(Arrays.asList(CIVIL_SERVICE, HORSEBACK_RIDING, CURRENCY)), new ArrayList<>(Arrays.asList("Knight", "Camel Archer", "Castle")),ImageBase.CHIVALRY.getImage()),
    ENGINEERING(250, new ArrayList<>(Arrays.asList(MATHEMATICS, CONSTRUCTION)), null,ImageBase.ENGINEERING.getImage()),
    MACHINERY(440, new ArrayList<>(Collections.singletonList(ENGINEERING)), new ArrayList<>(Arrays.asList("Crossbowman", "1.2 faster road movement")),ImageBase.MACHINERY.getImage()),
    METAL_CASTING(240, new ArrayList<>(Collections.singletonList(IRON_WORKING)), new ArrayList<>(Arrays.asList("Forge", "Workshop")),ImageBase.METAL_CASTING.getImage()),
    PHYSICS(440, new ArrayList<>(Arrays.asList(ENGINEERING, METAL_CASTING)), new ArrayList<>(Collections.singletonList("Trebuchet")),ImageBase.PHYSICS.getImage()),
    STEEL(440, new ArrayList<>(Collections.singletonList(METAL_CASTING)), new ArrayList<>(Collections.singletonList("Longswordsman")),ImageBase.STEEL.getImage()),
    EDUCATION(440, new ArrayList<>(Collections.singletonList(THEOLOGY)), new ArrayList<>(Collections.singletonList("University")),ImageBase.EDUCATION.getImage()),
    ACOUSTICS(650, new ArrayList<>(Collections.singletonList(EDUCATION)), null,ImageBase.ACOUSTICS.getImage()),
    ARCHAEOLOGY(1300, new ArrayList<>(Collections.singletonList(ACOUSTICS)), new ArrayList<>(Collections.singletonList("Museum")),ImageBase.ARCHAEOLOGY.getImage()),
    BANKING(650, new ArrayList<>(Arrays.asList(EDUCATION, CHIVALRY)), new ArrayList<>(Arrays.asList("Satrap's Court", "Bank")),ImageBase.BANKING.getImage()),
    GUNPOWDER(680, new ArrayList<>(Arrays.asList(PHYSICS, STEEL)), new ArrayList<>(Collections.singletonList("Musketman")),ImageBase.GUNPOWDER.getImage()),
    CHEMISTRY(900, new ArrayList<>(Collections.singletonList(GUNPOWDER)), new ArrayList<>(Collections.singletonList("Ironworks")),ImageBase.CHEMISTRY.getImage()),
    PRINTING_PRESS(650, new ArrayList<>(Arrays.asList(MACHINERY, CHEMISTRY)), new ArrayList<>(Collections.singletonList("Theater")),ImageBase.PRINTING_PRESS.getImage()),
    FERTILIZER(1300, new ArrayList<>(Collections.singletonList(CHEMISTRY)), new ArrayList<>(Collections.singletonList("Farms without Fresh Water yield increased by 1")),ImageBase.FERTILIZER.getImage()),
    METALLURGY(900, new ArrayList<>(Collections.singletonList(GUNPOWDER)), new ArrayList<>(Collections.singletonList("Lancer")),ImageBase.METALLURGY.getImage()),
    ECONOMICS(900, new ArrayList<>(Arrays.asList(BANKING, PRINTING_PRESS)), new ArrayList<>(Collections.singletonList("Windmill")),ImageBase.ECONOMICS.getImage()),
    MILITARY_SCIENCE(1300, new ArrayList<>(Arrays.asList(ECONOMICS, CHEMISTRY)), new ArrayList<>(Arrays.asList("Cavalry", "Military Academy")),ImageBase.MILITARY_SCIENCE.getImage()),
    RIFLING(1425, new ArrayList<>(Collections.singletonList(METALLURGY)), new ArrayList<>(Collections.singletonList("Rifleman")),ImageBase.RIFLING.getImage()),
    SCIENTIFIC_THEORY(1300, new ArrayList<>(Collections.singletonList(ACOUSTICS)), new ArrayList<>(Arrays.asList("Public School", "Coal")),ImageBase.SCIENTIFIC_THEORY.getImage()),
    BIOLOGY(1680, new ArrayList<>(Arrays.asList(ARCHAEOLOGY, SCIENTIFIC_THEORY)), null,ImageBase.BIOLOGY.getImage()),
    STEAM_POWER(1680, new ArrayList<>(Arrays.asList(SCIENTIFIC_THEORY, MILITARY_SCIENCE)), new ArrayList<>(Collections.singletonList("Factory")),ImageBase.STEAM_POWER.getImage()),
    DYNAMITE(1900, new ArrayList<>(Arrays.asList(FERTILIZER, RIFLING)), new ArrayList<>(Collections.singletonList("Artillery")),ImageBase.DYNAMITE.getImage()),
    ELECTRICITY(1900, new ArrayList<>(Arrays.asList(BIOLOGY, STEAM_POWER)), new ArrayList<>(Collections.singletonList("Stock Exchange")),ImageBase.ELECTRICITY.getImage()),
    RADIO(2200, new ArrayList<>(Collections.singletonList(ELECTRICITY)), new ArrayList<>(Collections.singletonList("Broadcast Tower")),ImageBase.RADIO.getImage()),
    RAILROAD(1900, new ArrayList<>(Collections.singletonList(STEAM_POWER)), new ArrayList<>(Arrays.asList("Arsenal", "Railroad")),ImageBase.RAILROAD.getImage()),
    REPLACEABLE_PARTS(1900, new ArrayList<>(Collections.singletonList(STEAM_POWER)), new ArrayList<>(Arrays.asList("Anti-Tank Gun", "Infantry")),ImageBase.REPLACEABLE_PARTS.getImage()),
    TELEGRAPH(2200, new ArrayList<>(Collections.singletonList(ELECTRICITY)), new ArrayList<>(Collections.singletonList("Military Base")),ImageBase.TELEGRAPH.getImage()),
    COMBUSTION(2200, new ArrayList<>(Arrays.asList(REPLACEABLE_PARTS, RAILROAD, DYNAMITE)), new ArrayList<>(Arrays.asList("Tank", "Panzer")),ImageBase.COMBUSTION.getImage());

    private final int cost;
    private final ArrayList<Technology> parents;
    private final ArrayList<String> unlocks;
    private final Image image;
    Technology(int cost, ArrayList<Technology> parents, ArrayList<String> unlocks,Image image) {
        this.parents = parents;
        this.unlocks = unlocks;
        this.cost = cost;
        this.image = image;
    }

    public Image getImage() {
        return image;
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
