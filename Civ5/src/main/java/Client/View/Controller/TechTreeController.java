package Client.View.Controller;

import Client.Model.Civilization;
import Client.Model.Technology;
import Client.Controller.CivilizationController;
import Client.Controller.GameController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class TechTreeController {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label invalidTech;
    @FXML
    private TextField techName;
    @FXML
    private javafx.scene.shape.Rectangle agriculture;
    @FXML
    private javafx.scene.shape.Rectangle pottery;
    @FXML
    private javafx.scene.shape.Rectangle animalHusbandry;
    @FXML
    private javafx.scene.shape.Rectangle archery;
    @FXML
    private javafx.scene.shape.Rectangle mining;
    @FXML
    private javafx.scene.shape.Rectangle calendar;
    @FXML
    private javafx.scene.shape.Rectangle writing;
    @FXML
    private javafx.scene.shape.Rectangle trapping;
    @FXML
    private javafx.scene.shape.Rectangle theWheel;
    @FXML
    private javafx.scene.shape.Rectangle masonry;
    @FXML
    private javafx.scene.shape.Rectangle bronzeWorking;
    @FXML
    private javafx.scene.shape.Rectangle philosophy;
    @FXML
    private javafx.scene.shape.Rectangle horsebackRiding;
    @FXML
    private javafx.scene.shape.Rectangle mathematics;
    @FXML
    private javafx.scene.shape.Rectangle construction;
    @FXML
    private javafx.scene.shape.Rectangle ironWorking;
    @FXML
    private javafx.scene.shape.Rectangle theology;
    @FXML
    private javafx.scene.shape.Rectangle civilService;
    @FXML
    private javafx.scene.shape.Rectangle currency;
    @FXML
    private javafx.scene.shape.Rectangle engineering;
    @FXML
    private javafx.scene.shape.Rectangle metalCasting;
    @FXML
    private javafx.scene.shape.Rectangle education;
    @FXML
    private javafx.scene.shape.Rectangle chivalry;
    @FXML
    private javafx.scene.shape.Rectangle machinery;
    @FXML
    private javafx.scene.shape.Rectangle physics;
    @FXML
    private javafx.scene.shape.Rectangle steel;
    @FXML
    private javafx.scene.shape.Rectangle acoustics;
    @FXML
    private javafx.scene.shape.Rectangle banking;
    @FXML
    private javafx.scene.shape.Rectangle printingPress;
    @FXML
    private javafx.scene.shape.Rectangle gunpowder;
    @FXML
    private javafx.scene.shape.Rectangle economics;
    @FXML
    private javafx.scene.shape.Rectangle chemistry;
    @FXML
    private javafx.scene.shape.Rectangle metallurgy;
    @FXML
    private javafx.scene.shape.Rectangle scientificTheory;
    @FXML
    private javafx.scene.shape.Rectangle militaryScience;
    @FXML
    private javafx.scene.shape.Rectangle fertilizer;
    @FXML
    private javafx.scene.shape.Rectangle rifling;
    @FXML
    private javafx.scene.shape.Rectangle biology;
    @FXML
    private javafx.scene.shape.Rectangle steamPower;
    @FXML
    private javafx.scene.shape.Rectangle electricity;
    @FXML
    private javafx.scene.shape.Rectangle replaceableParts;
    @FXML
    private javafx.scene.shape.Rectangle railroad;
    @FXML
    private javafx.scene.shape.Rectangle dynamite;
    @FXML
    private javafx.scene.shape.Rectangle telegraph;
    @FXML
    private javafx.scene.shape.Rectangle radio;
    @FXML
    private javafx.scene.shape.Rectangle combustion;

    public void initialize() {
        paintAvailable();
        paintCurrentlyResearching();
        paintResearched();
        chooseResearch();
    }

    private void chooseResearch() {
        for (Node child : anchorPane.getChildren()) {
            if (!(child instanceof ImageView)) continue;
            ImageView imageView = (ImageView) child;
            if (imageView.getFitWidth() != 80 || imageView.getFitHeight() != 80) continue;
            imageView.setStyle("-fx-cursor: hand");
            Technology technology = getTechFromImageView(imageView);
            if (technology == null) continue;
            imageView.setOnMouseClicked(mouseEvent -> {
                Civilization civilization = GameController.getCivilization();
                if (civilization.hasPrerequisitesOf(technology) && !civilization.hasReachedTech(technology)) {
                    if (!civilization.getLastCostUntilNewTechnologies().containsKey(technology))
                        civilization.getLastCostUntilNewTechnologies().put(technology, technology.getCost());
                    civilization.setInProgressTech(technology);
                    paintAvailable();
                    paintCurrentlyResearching();
                    paintResearched();
                }
            });
        }
    }

    private Technology getTechFromImageView(ImageView imageView) {
        for (Technology tech : Technology.values())
            if (imagesAreSame(imageView.getImage(), tech.getImage()))
                return tech;
        return null;
    }

    private boolean imagesAreSame(Image i1, Image i2) {
        for (int i = 0; i < i1.getWidth(); i++)
            for (int j = 0; j < i1.getHeight(); j++)
                if (!colorsAreTheSame(i1.getPixelReader().getColor(i, j), i2.getPixelReader().getColor(i, j)))
                    return false;
        return true;
    }

    private boolean colorsAreTheSame(Color c1, Color c2) {
        return c1.getRed() == c2.getRed() && c1.getGreen() == c2.getGreen() && c1.getBlue() == c2.getBlue();
    }

    public void searchTech(MouseEvent mouseEvent) {
        invalidTech.setVisible(false);
        double x = searchTechPosition(techName.getText());
        scrollPane.setHvalue(x * 1.32);
        if (x == 100) {
            invalidTech.setVisible(true);
            techName.setStyle("-fx-border-color: red");
        }
    }

    public void changeVisibility(KeyEvent keyEvent) {
        invalidTech.setVisible(false);
        techName.setStyle("-fx-border-color: #11313d");
    }

    private double searchTechPosition(String techName) {
        if (techName.equals("Agriculture")) return agriculture.getLayoutX();
        else if (techName.equals("Pottery")) return pottery.getLayoutX();
        else if (techName.equals("Animal Husbandry")) return animalHusbandry.getLayoutX();
        else if (techName.equals("Archery")) return archery.getLayoutX();
        else if (techName.equals("Mining")) return mining.getLayoutX();
        else if (techName.equals("Calendar")) return calendar.getLayoutX();
        else if (techName.equals("Writing")) return writing.getLayoutX();
        else if (techName.equals("Trapping")) return trapping.getLayoutX();
        else if (techName.equals("The Wheel")) return theWheel.getLayoutX();
        else if (techName.equals("Masonry")) return masonry.getLayoutX();
        else if (techName.equals("Bronze Working")) return bronzeWorking.getLayoutX();
        else if (techName.equals("Philosophy")) return philosophy.getLayoutX();
        else if (techName.equals("Horseback Riding")) return horsebackRiding.getLayoutX();
        else if (techName.equals("Mathematics")) return mathematics.getLayoutX();
        else if (techName.equals("Construction")) return construction.getLayoutX();
        else if (techName.equals("Iron Working")) return ironWorking.getLayoutX();
        else if (techName.equals("Theology")) return theology.getLayoutX();
        else if (techName.equals("Civil Service")) return civilService.getLayoutX();
        else if (techName.equals("Currency")) return currency.getLayoutX();
        else if (techName.equals("Engineering")) return engineering.getLayoutX();
        else if (techName.equals("Metal Casting")) return metalCasting.getLayoutX();
        else if (techName.equals("Education")) return education.getLayoutX();
        else if (techName.equals("Chivalry")) return chivalry.getLayoutX();
        else if (techName.equals("Machinery")) return machinery.getLayoutX();
        else if (techName.equals("Physics")) return physics.getLayoutX();
        else if (techName.equals("Steel")) return steel.getLayoutX();
        else if (techName.equals("Acoustics")) return acoustics.getLayoutX();
        else if (techName.equals("Banking")) return banking.getLayoutX();
        else if (techName.equals("Printing Press")) return printingPress.getLayoutX();
        else if (techName.equals("Gunpowder")) return gunpowder.getLayoutX();
        else if (techName.equals("Economics")) return economics.getLayoutX();
        else if (techName.equals("Chemistry")) return chemistry.getLayoutX();
        else if (techName.equals("Metallurgy")) return metallurgy.getLayoutX();
        else if (techName.equals("Scientific Theory")) return scientificTheory.getLayoutX();
        else if (techName.equals("Military Science")) return militaryScience.getLayoutX();
        else if (techName.equals("Fertilizer")) return fertilizer.getLayoutX();
        else if (techName.equals("Rifling")) return rifling.getLayoutX();
        else if (techName.equals("Biology")) return biology.getLayoutX();
        else if (techName.equals("Steam Power")) return steamPower.getLayoutX();
        else if (techName.equals("Electricity")) return electricity.getLayoutX();
        else if (techName.equals("Replaceable Parts")) return replaceableParts.getLayoutX();
        else if (techName.equals("Railroad")) return railroad.getLayoutX();
        else if (techName.equals("Dynamite")) return dynamite.getLayoutX();
        else if (techName.equals("Telegraph")) return telegraph.getLayoutX();
        else if (techName.equals("Radio")) return radio.getLayoutX();
        else if (techName.equals("Combustion")) return combustion.getLayoutX();
        else return 100;
    }

    private void paintAvailable() {
        if (CivilizationController.canAskForTech(Technology.AGRICULTURE)) agriculture.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.POTTERY)) pottery.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.ANIMAL_HUSBANDRY))
            animalHusbandry.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.ARCHERY)) archery.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.MINING)) mining.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.CALENDER)) calendar.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.WRITING)) writing.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.TRAPPING)) trapping.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.WHEEL)) theWheel.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.MASONRY)) masonry.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.BRONZE_WORKING))
            bronzeWorking.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.PHILOSOPHY)) philosophy.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.HORSEBACK_RIDING))
            horsebackRiding.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.MATHEMATICS)) mathematics.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.CONSTRUCTION))
            construction.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.IRON_WORKING)) ironWorking.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.THEOLOGY)) theology.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.CIVIL_SERVICE))
            civilService.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.CURRENCY)) currency.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.ENGINEERING)) engineering.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.METAL_CASTING))
            metalCasting.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.EDUCATION)) education.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.CHIVALRY)) chivalry.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.MACHINERY)) machinery.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.PHYSICS)) physics.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.STEEL)) steel.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.ACOUSTICS)) acoustics.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.BANKING)) banking.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.PRINTING_PRESS))
            printingPress.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.GUNPOWDER)) gunpowder.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.ECONOMICS)) economics.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.CHEMISTRY)) chemistry.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.METALLURGY)) metallurgy.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.SCIENTIFIC_THEORY))
            scientificTheory.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.MILITARY_SCIENCE))
            militaryScience.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.FERTILIZER)) fertilizer.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.RIFLING)) rifling.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.BIOLOGY)) biology.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.STEAM_POWER)) steamPower.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.ELECTRICITY)) electricity.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.REPLACEABLE_PARTS))
            replaceableParts.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.RAILROAD)) railroad.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.DYNAMITE)) dynamite.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.TELEGRAPH)) telegraph.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.RADIO)) radio.setFill(Paint.valueOf("19831e"));
        if (CivilizationController.canAskForTech(Technology.COMBUSTION)) combustion.setFill(Paint.valueOf("19831e"));

    }

    private void paintCurrentlyResearching() {
        if (GameController.getCivilization().getInProgressTech() == null) return;
        if (GameController.getCivilization().getInProgressTech().equals(Technology.AGRICULTURE))
            agriculture.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.POTTERY))
            pottery.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.ANIMAL_HUSBANDRY))
            animalHusbandry.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.ARCHERY))
            archery.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.MINING))
            mining.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.CALENDER))
            calendar.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.WRITING))
            writing.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.TRAPPING))
            trapping.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.WHEEL))
            theWheel.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.MASONRY))
            masonry.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.BRONZE_WORKING))
            bronzeWorking.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.PHILOSOPHY))
            philosophy.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.HORSEBACK_RIDING))
            horsebackRiding.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.MATHEMATICS))
            mathematics.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.CONSTRUCTION))
            construction.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.IRON_WORKING))
            ironWorking.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.THEOLOGY))
            theology.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.CIVIL_SERVICE))
            civilService.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.CURRENCY))
            currency.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.ENGINEERING))
            engineering.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.METAL_CASTING))
            metalCasting.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.EDUCATION))
            education.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.CHIVALRY))
            chivalry.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.MACHINERY))
            machinery.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.PHYSICS))
            physics.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.STEEL))
            steel.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.ACOUSTICS))
            acoustics.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.BANKING))
            banking.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.PRINTING_PRESS))
            printingPress.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.GUNPOWDER))
            gunpowder.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.ECONOMICS))
            economics.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.CHEMISTRY))
            chemistry.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.METALLURGY))
            metallurgy.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.SCIENTIFIC_THEORY))
            scientificTheory.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.MILITARY_SCIENCE))
            militaryScience.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.FERTILIZER))
            fertilizer.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.RIFLING))
            rifling.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.BIOLOGY))
            biology.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.STEAM_POWER))
            steamPower.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.ELECTRICITY))
            electricity.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.REPLACEABLE_PARTS))
            replaceableParts.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.RAILROAD))
            railroad.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.DYNAMITE))
            dynamite.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.TELEGRAPH))
            telegraph.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.RADIO))
            radio.setFill(Paint.valueOf("005eab"));
        else if (GameController.getCivilization().getInProgressTech().equals(Technology.COMBUSTION))
            combustion.setFill(Paint.valueOf("005eab"));
    }

    private void paintResearched() {
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.AGRICULTURE) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.AGRICULTURE) <= 0)
            agriculture.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.POTTERY) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.POTTERY) <= 0)
            pottery.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.ANIMAL_HUSBANDRY) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.ANIMAL_HUSBANDRY) <= 0)
            animalHusbandry.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.ARCHERY) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.ARCHERY) <= 0)
            archery.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.MINING) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.MINING) <= 0)
            mining.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.CALENDER) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.CALENDER) <= 0)
            calendar.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.WRITING) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.WRITING) <= 0)
            writing.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.TRAPPING) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.TRAPPING) <= 0)
            trapping.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.WHEEL) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.WHEEL) <= 0)
            theWheel.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.MASONRY) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.MASONRY) <= 0)
            masonry.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.BRONZE_WORKING) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.BRONZE_WORKING) <= 0)
            bronzeWorking.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.PHILOSOPHY) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.PHILOSOPHY) <= 0)
            philosophy.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.HORSEBACK_RIDING) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.HORSEBACK_RIDING) <= 0)
            horsebackRiding.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.MATHEMATICS) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.MATHEMATICS) <= 0)
            mathematics.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.CONSTRUCTION) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.CONSTRUCTION) <= 0)
            construction.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.IRON_WORKING) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.IRON_WORKING) <= 0)
            ironWorking.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.THEOLOGY) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.THEOLOGY) <= 0)
            theology.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.CIVIL_SERVICE) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.CIVIL_SERVICE) <= 0)
            civilService.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.CURRENCY) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.CURRENCY) <= 0)
            currency.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.ENGINEERING) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.ENGINEERING) <= 0)
            engineering.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.METAL_CASTING) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.METAL_CASTING) <= 0)
            metalCasting.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.EDUCATION) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.EDUCATION) <= 0)
            education.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.CHIVALRY) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.CHIVALRY) <= 0)
            chivalry.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.MACHINERY) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.MACHINERY) <= 0)
            machinery.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.PHYSICS) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.PHYSICS) <= 0)
            physics.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.STEEL) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.STEEL) <= 0)
            steel.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.ACOUSTICS) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.ACOUSTICS) <= 0)
            acoustics.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.BANKING) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.BANKING) <= 0)
            banking.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.PRINTING_PRESS) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.PRINTING_PRESS) <= 0)
            printingPress.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.GUNPOWDER) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.GUNPOWDER) <= 0)
            gunpowder.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.ECONOMICS) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.ECONOMICS) <= 0)
            economics.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.CHEMISTRY) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.CHEMISTRY) <= 0)
            chemistry.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.METALLURGY) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.METALLURGY) <= 0)
            metallurgy.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.SCIENTIFIC_THEORY) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.SCIENTIFIC_THEORY) <= 0)
            scientificTheory.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.MILITARY_SCIENCE) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.MILITARY_SCIENCE) <= 0)
            militaryScience.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.FERTILIZER) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.FERTILIZER) <= 0)
            fertilizer.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.RIFLING) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.RIFLING) <= 0)
            rifling.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.BIOLOGY) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.BIOLOGY) <= 0)
            biology.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.STEAM_POWER) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.STEAM_POWER) <= 0)
            steamPower.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.ELECTRICITY) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.ELECTRICITY) <= 0)
            electricity.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.REPLACEABLE_PARTS) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.REPLACEABLE_PARTS) <= 0)
            replaceableParts.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.RAILROAD) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.RAILROAD) <= 0)
            railroad.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.DYNAMITE) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.DYNAMITE) <= 0)
            dynamite.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.TELEGRAPH) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.TELEGRAPH) <= 0)
            telegraph.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.RADIO) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.RADIO) <= 0)
            radio.setFill(Paint.valueOf("a3bf00"));
        if (GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.COMBUSTION) != null &&
                GameController.getCivilization().getLastCostUntilNewTechnologies().get(Technology.COMBUSTION) <= 0)
            combustion.setFill(Paint.valueOf("a3bf00"));
    }
}
