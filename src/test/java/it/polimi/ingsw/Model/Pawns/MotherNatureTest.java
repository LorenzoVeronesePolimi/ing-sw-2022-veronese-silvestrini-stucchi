package it.polimi.ingsw.Model.Pawns;

import it.polimi.ingsw.Model.Places.Archipelago;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;


public class MotherNatureTest {
    @Test
    void putInPosition(){
        MotherNature mn = new MotherNature();
        Archipelago dest = new Archipelago();
        mn.putInPosition(dest);
        assertEquals(dest, mn.getCurrentPosition());
    }
}
