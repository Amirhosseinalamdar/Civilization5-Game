package View;

import Client.View.Commands;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class CommandsTest {

    @Test
    void getUsernames1() {
        String input = "play game -p1 hesi";
        ArrayList<String> outputs = new ArrayList<>();
        outputs.add("hesi");
        Assertions.assertTrue(outputs.size() == Commands.getUsernames(input).size() && outputs.equals(Commands.getUsernames(input)));
    }

    @Test
    void getUsernames2() {
        String input = "play game -player1 hesi";
        ArrayList<String> outputs = new ArrayList<>();
        outputs.add("hesi");
        Assertions.assertTrue(outputs.size() == Commands.getUsernames(input).size() && outputs.equals(Commands.getUsernames(input)));
    }
}