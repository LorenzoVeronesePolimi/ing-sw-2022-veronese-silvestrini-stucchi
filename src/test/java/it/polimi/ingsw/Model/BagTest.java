package it.polimi.ingsw.Model;

import static org.junit.Assert.*;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.NoProfessorBagException;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BagTest {
    Bag bag;

    @BeforeEach
    public void init() { bag = new Bag(); }

    // ---------------STUDENTS
    @Test
    public void getInitialStudents() {
        List<Student> students = null;
        students = bag.getInitialStudents();

        //check if initial students are always 10
        Assertions.assertEquals(students.size(), 10);

        //check if initial students are removed from the list in bag after one call of getInitialStudents
        Assertions.assertEquals(0, bag.getInitialStudents().size());
    }

    @Test
    public void extractStudents() {
        List<Student> students = null;

        //extract all students
        try {
            students = bag.extractStudents(120);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
        assert students != null;
        Assertions.assertEquals(students.size(), 120);

        //check if no student remaining
        assertThrows(StudentNotFoundException.class, () -> bag.extractStudents(1));
    }

    @Test
    public void putStudent() {
        Student s = new Student(SPColour.RED);
        List<Student> students = null;

        //extract all students
        try {
            students = bag.extractStudents(120);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }

        //add student to the bag
        bag.putStudent(s);

        //extract only student
        try {
            students = bag.extractStudents(1);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
        //check if it's the same students that was put in the bag
        assert students != null;
        Assertions.assertEquals(s, students.get(0));

        //check if no other student left
        assertThrows(StudentNotFoundException.class, () -> bag.extractStudents(1));
    }
    //---------------PROFESSORS
    @Test
    void takeProfessor() {
        Professor p1 = null;
        Professor p2 = null;
        Professor p3 = null;
        Professor p4 = null;
        Professor p5 = null;

        //Get all professors
        try {
            p1 = bag.takeProfessor(SPColour.BLUE);
            p2 = bag.takeProfessor(SPColour.RED);
            p3 = bag.takeProfessor(SPColour.GREEN);
            p4 = bag.takeProfessor(SPColour.YELLOW);
            p5 = bag.takeProfessor(SPColour.PINK);
        } catch (NoProfessorBagException e) {
            e.printStackTrace();
        }

        //check correct return statement
        assert p1 != null;
        assert p2 != null;
        assert p3 != null;
        assert p4 != null;
        assert p5 != null;
        Assertions.assertEquals(p1.getColour(), SPColour.BLUE);
        Assertions.assertEquals(p2.getColour(), SPColour.RED);
        Assertions.assertEquals(p3.getColour(), SPColour.GREEN);
        Assertions.assertEquals(p4.getColour(), SPColour.YELLOW);
        Assertions.assertEquals(p5.getColour(), SPColour.PINK);

        //check no professors left in the bag
        assertThrows(NoProfessorBagException.class, () -> bag.takeProfessor(SPColour.BLUE));
        assertThrows(NoProfessorBagException.class, () -> bag.takeProfessor(SPColour.RED));
        assertThrows(NoProfessorBagException.class, () -> bag.takeProfessor(SPColour.GREEN));
        assertThrows(NoProfessorBagException.class, () -> bag.takeProfessor(SPColour.YELLOW));
        assertThrows(NoProfessorBagException.class, () -> bag.takeProfessor(SPColour.PINK));
    }

}
