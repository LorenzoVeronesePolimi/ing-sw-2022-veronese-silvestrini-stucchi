package it.polimi.ingsw.Model.Pawns;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class StudentTest {
    Student testedStudent = null;

    @Test
    void getColour() {
        testedStudent = new Student(SPColour.RED);
        Assertions.assertEquals(SPColour.RED, testedStudent.getColour());

        testedStudent = new Student(SPColour.GREEN);
        Assertions.assertEquals(SPColour.GREEN, testedStudent.getColour());

        testedStudent = new Student(SPColour.BLUE);
        Assertions.assertEquals(SPColour.BLUE, testedStudent.getColour());

        testedStudent = new Student(SPColour.PINK);
        Assertions.assertEquals(SPColour.PINK, testedStudent.getColour());

        testedStudent = new Student(SPColour.YELLOW);
        Assertions.assertEquals(SPColour.YELLOW, testedStudent.getColour());

        //check no modification after some method calls
        testedStudent.getColour();
        testedStudent.toString();
        Assertions.assertEquals(SPColour.YELLOW, testedStudent.getColour());

    }

    @Test
    void toStringTest() {
        testedStudent = new Student(SPColour.YELLOW);

        Assertions.assertEquals(""+ SPColour.YELLOW, testedStudent.toString());

    }
}
