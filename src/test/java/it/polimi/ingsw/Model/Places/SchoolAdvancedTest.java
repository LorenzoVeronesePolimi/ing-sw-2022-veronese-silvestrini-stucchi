package it.polimi.ingsw.Model.Places;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Pawns.Coin;
import it.polimi.ingsw.Model.Places.School.SchoolAdvanced;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;


public class SchoolAdvancedTest {

    @Test
    void getNumCoins(){
        Player p = new Player("owner", PlayerColour.WHITE);
        SchoolAdvanced schoolAdvanced = new SchoolAdvanced(p,7,8);
        assertEquals(0,schoolAdvanced.getNumCoins());

        Coin coin = new Coin();
        schoolAdvanced.addCoin(coin);
        assertEquals(1, schoolAdvanced.getNumCoins());
    }
}
