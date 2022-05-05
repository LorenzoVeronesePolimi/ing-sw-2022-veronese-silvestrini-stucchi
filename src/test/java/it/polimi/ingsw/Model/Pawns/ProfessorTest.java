package it.polimi.ingsw.Model.Pawns;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class ProfessorTest {
    Professor testedProfessor = null;

    @Test
    void getColour() {
        testedProfessor = new Professor(SPColour.RED);
        assertEquals(SPColour.RED, testedProfessor.getColour());

        testedProfessor = new Professor(SPColour.GREEN);
        assertEquals(SPColour.GREEN, testedProfessor.getColour());

        testedProfessor = new Professor(SPColour.BLUE);
        assertEquals(SPColour.BLUE, testedProfessor.getColour());

        testedProfessor = new Professor(SPColour.PINK);
        assertEquals(SPColour.PINK, testedProfessor.getColour());

        testedProfessor = new Professor(SPColour.YELLOW);
        assertEquals(SPColour.YELLOW, testedProfessor.getColour());

        //check no modofication after some method calls
        testedProfessor.getColour();
        testedProfessor.toString();
        assertEquals(SPColour.YELLOW, testedProfessor.getColour());

    }

    @Test
    void toStringTest() {
        testedProfessor = new Professor(SPColour.YELLOW);

        assertEquals(""+SPColour.YELLOW, testedProfessor.toString());

        assertEquals(""+testedProfessor.getColour(), testedProfessor.toString());

    }
}
