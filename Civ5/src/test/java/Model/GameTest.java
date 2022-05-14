package Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameTest {


    @Mock
    User user;

    @Test
    void nextTurn() {
        //User user = new User("amir","dfhjsjkl","sdf",true,10);
        Game.getPlayers().add(user);
        Game.nextTurn();
    }

    @Test
    void generateGame() {
    }

    @Test
    void generateMap() {
    }

    @Test
    void getTiles() {
    }
}