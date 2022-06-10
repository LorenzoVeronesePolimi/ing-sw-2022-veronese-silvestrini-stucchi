package it.polimi.ingsw.Model.Pawns;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class TowerTest {
    Tower testedTower = null;

    @Test
    void getPlayer() {
        Player player = new Player("nickname", PlayerColour.BLACK);

        testedTower = new Tower(player);

        Assertions.assertEquals(player, testedTower.getPlayer());
    }

    @Test
    void toStringTest() {
        Player player = new Player("nickname", PlayerColour.BLACK);

        testedTower = new Tower(player);

        Assertions.assertEquals("Tower{" +
                "player=" + testedTower.getPlayer() +
                '}', testedTower.toString());
    }
}
