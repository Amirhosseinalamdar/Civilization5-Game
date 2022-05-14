package Controller;

import Model.Civilization;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class CityControllerTest {

    @Mock
    Civilization civilization;



    @Test
    void changeCivilization() {
        CityController.changeCivilization(civilization);
    }

    @Test
    void setCity() {
    }

    @Test
    void handleCityOptions() {
    }

    @Test
    void getCityDecision() {
    }

    @Test
    void lockCitizenOnTile() {
    }

    @Test
    void updateCityInfos() {
    }

    @Test
    void updateCity() {
    }

    @Test
    void turnsForNewUnit() {
    }

    @Test
    void expandCity() {
    }
}