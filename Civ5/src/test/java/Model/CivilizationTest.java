package Model;

import Model.Map.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CivilizationTest {

    @Mock
    Civilization civilization = new Civilization();

    @Test
    void setHappiness() {
        civilization.setHappiness(10);
        Assertions.assertEquals(civilization.getHappiness(),10);
    }

    @Test
    void getNotifications() {
        civilization.getNotifications().add("salam");
        Assertions.assertEquals("salam",civilization.getNotifications().
                get(civilization.getNotifications().size()-1));
    }

    @Test
    void getScore() {
        int a = civilization.getScore();
        civilization.increaseScore(10);
        Assertions.assertEquals(a + 10,civilization.getScore());
    }

    @Test
    void getLuxuryResources() {
        HashMap<Resource,Integer> H = civilization.getLuxuryResources();
        Assertions.assertEquals(H,civilization.getLuxuryResources());
    }


    @Test
    void setTotalGold() {
        int a = civilization.getTotalGold();
        civilization.setTotalGold(a+10);
        Assertions.assertEquals(a+10,civilization.getTotalGold());
    }

    @Test
    void getInProgressTech() {
        civilization.setInProgressTech(Technology.AGRICULTURE);
        Technology t = civilization.getInProgressTech();
        Assertions.assertEquals(t,Technology.AGRICULTURE);
    }


    @Test
    void testCenters() {
        int x = civilization.getShowingCenterI();
        int y = civilization.getShowingCenterI();
        civilization.setShowingCenterI(x+10);
        civilization.setShowingCenterJ(y+10);
        Assertions.assertEquals(x+10,civilization.getShowingCenterI());
        Assertions.assertEquals(y+10,civilization.getShowingCenterJ());
    }

    @Test
    void getCivColor() {
    }

    @Test
    void createSettlerAndWarriorOnTile() {
    }

    @Test
    void addCity() {
    }

    @Test
    void setTileVisionStatuses() {
    }

    @Test
    void getTotalGold() {
    }

    @Test
    void getCities() {
    }

    @Test
    void getUnits() {
    }

    @Test
    void addUnit() {
    }

    @Test
    void getTileVisionStatuses() {
    }

    @Test
    void getLastCostUntilNewTechnologies() {
    }

    @Test
    void getScience() {
    }

    @Test
    void getHappiness() {
    }

    @Test
    void setTotalScience() {
    }

    @Test
    void getResources() {
    }

    @Test
    void hasReachedTech() {
    }
}