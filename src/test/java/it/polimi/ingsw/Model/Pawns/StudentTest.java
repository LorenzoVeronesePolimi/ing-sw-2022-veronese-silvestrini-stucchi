package it.polimi.ingsw.Model.Pawns;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class StudentTest {
    Student testedStudent = null;

    @Test
    void getColour() {
        testedStudent = new Student(SPColour.RED);
        assertEquals(SPColour.RED, testedStudent.getColour());

        testedStudent = new Student(SPColour.GREEN);
        assertEquals(SPColour.GREEN, testedStudent.getColour());

        testedStudent = new Student(SPColour.BLUE);
        assertEquals(SPColour.BLUE, testedStudent.getColour());

        testedStudent = new Student(SPColour.PINK);
        assertEquals(SPColour.PINK, testedStudent.getColour());

        testedStudent = new Student(SPColour.YELLOW);
        assertEquals(SPColour.YELLOW, testedStudent.getColour());

        //check no modofication after some method calls
        testedStudent.getColour();
        testedStudent.toString();
        assertEquals(SPColour.YELLOW, testedStudent.getColour());

    }

    @Test
    void toStringTest() {
        testedStudent = new Student(SPColour.YELLOW);

        assertEquals("Student{" +
                "colour=" + SPColour.YELLOW +
                '}', testedStudent.toString());

        assertEquals("Student{" +
                "colour=" + testedStudent.getColour() +
                '}', testedStudent.toString());

    }
}
