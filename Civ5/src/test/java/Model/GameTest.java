package Model;

import Model.Map.Tile;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

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
        ArrayList<User> users = new ArrayList<>();
        User user2 = new User("f", "df", "df", true, 10);
        User user3 = new User("ff", "dff", "dff", true, 10);
        users.add(user2);
        users.add(user3);
        Game.generateGame(users);
    }

    /*@Test
    void completeMap(){
        Tile[][] tiles = new Tile[20][20];
        for(int i=0;i<20;i++){
            for(int j=0;j<20;j++){
                tiles[i][j] =new Tile();
            }
        }
        Game.completeMap(tiles);
    }*/

}