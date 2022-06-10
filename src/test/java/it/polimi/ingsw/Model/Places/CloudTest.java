package it.polimi.ingsw.Model.Places;

import static org.junit.Assert.*;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxStudentsCloudException;
import it.polimi.ingsw.Model.Pawns.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CloudTest {
    Cloud testedCloud;

    @BeforeEach
    void init() {
        testedCloud = new Cloud(4);
    }

    @Test
    void getStudents() {
        //no students before inserting
        Assertions.assertEquals(testedCloud.getStudents().size(), 0);

        List<Student> students = new ArrayList<>();
        students.add(new Student(SPColour.RED));
        students.add(new Student(SPColour.RED));
        students.add(new Student(SPColour.GREEN));
        students.add(new Student(SPColour.BLUE));

        //normal call for fill method
        try {
            testedCloud.fill(students);
        } catch (ExceededMaxStudentsCloudException e) {
            e.printStackTrace();
        }

        //check for correct filling
        Assertions.assertEquals(students, testedCloud.getStudents());

        //students should not be removed
        Assertions.assertEquals(testedCloud.getStudents().size(), 4);
    }

    @Test
    void toStringTest() {
        System.out.println(testedCloud.toString());

        Assertions.assertEquals("students="+testedCloud.getStudents(), testedCloud.toString());

        List<Student> students = new ArrayList<>();
        students.add(new Student(SPColour.RED));
        students.add(new Student(SPColour.RED));
        students.add(new Student(SPColour.GREEN));
        students.add(new Student(SPColour.BLUE));

        //normal call for fill method
        try {
            testedCloud.fill(students);
        } catch (ExceededMaxStudentsCloudException e) {
            e.printStackTrace();
        }

        System.out.println(testedCloud.toString());

        Assertions.assertEquals("students=" + testedCloud.getStudents(), testedCloud.toString());
    }

    @Test
    void fill() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(SPColour.RED));
        students.add(new Student(SPColour.RED));
        students.add(new Student(SPColour.GREEN));
        students.add(new Student(SPColour.BLUE));

        //normal call for fill method
        try {
            testedCloud.fill(students);
        } catch (ExceededMaxStudentsCloudException e) {
            e.printStackTrace();
        }

        //check for correct filling
        Assertions.assertEquals(students, testedCloud.getStudents());

        //new student
        students.add(new Student(SPColour.PINK));
        //too many students
        assertThrows(ExceededMaxStudentsCloudException.class, () -> testedCloud.fill(students));
    }

    @Test
    void empty() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(SPColour.RED));
        students.add(new Student(SPColour.RED));
        students.add(new Student(SPColour.GREEN));
        students.add(new Student(SPColour.BLUE));

        //normal call for fill method
        try {
            testedCloud.fill(students);
        } catch (ExceededMaxStudentsCloudException e) {
            e.printStackTrace();
        }

        List<Student> returnedStudents = new ArrayList<>(testedCloud.empty());

        //no students should be left
        Assertions.assertEquals(testedCloud.getStudents().size(), 0);
    }
}
