package it.polimi.ingsw.Model.Board;

import static org.junit.Assert.*;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardAbstractTest {
    List<Player> playerList = null;
    BoardAbstract b2;
    BoardAbstract b3;

    @BeforeEach
    void init() {
        playerList = new ArrayList<>();
        Player p1 = new Player("player one", PlayerColour.BLACK);
        Player p2 = new Player("player two", PlayerColour.WHITE);
        Player p3 = new Player("player three", PlayerColour.GRAY);
        playerList.add(p1);
        playerList.add(p2);
        playerList.add(p3);

        //test for BoardTwo
        try {
            b2 = new BoardTwo(playerList);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        } catch (ExceededMaxStudentsCloudException e) {
            e.printStackTrace();
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        } catch (ExceedingAssistantCardNumberException e) {
            e.printStackTrace();
        } catch (NullContentException e) {
            e.printStackTrace();
        }
        //b3 = new BoardThree(playerList);

    }

    @Test
    void boardTest() {
        Assertions.assertEquals(2, this.b2.players.size());
        //check if there are 12 archipelagos
        Assertions.assertEquals(12, b2.archipelagos.size());

        //check each archipelago
        Assertions.assertEquals(this.b2.archipelagos.get(0), this.b2.getArchipelago(0));
        Assertions.assertEquals(this.b2.archipelagos.get(1), this.b2.getArchipelago(1));
        Assertions.assertEquals(this.b2.archipelagos.get(2), this.b2.getArchipelago(2));
        Assertions.assertEquals(this.b2.archipelagos.get(3), this.b2.getArchipelago(3));
        Assertions.assertEquals(this.b2.archipelagos.get(4), this.b2.getArchipelago(4));
        Assertions.assertEquals(this.b2.archipelagos.get(5), this.b2.getArchipelago(5));
        Assertions.assertEquals(this.b2.archipelagos.get(6), this.b2.getArchipelago(6));
        Assertions.assertEquals(this.b2.archipelagos.get(7), this.b2.getArchipelago(7));
        Assertions.assertEquals(this.b2.archipelagos.get(8), this.b2.getArchipelago(8));
        Assertions.assertEquals(this.b2.archipelagos.get(9), this.b2.getArchipelago(9));
        Assertions.assertEquals(this.b2.archipelagos.get(10), this.b2.getArchipelago(10));
        Assertions.assertEquals(this.b2.archipelagos.get(11), this.b2.getArchipelago(11));

        //--------------------- initialize conquer sequence (left merge)
        Student s1 = new Student(SPColour.RED);
        Student s2 = new Student(SPColour.RED);
        Student s3 = new Student(SPColour.RED);
        Student s4 = new Student(SPColour.RED);

        try {
            this.b2.getPlayerSchool(playerList.get(0)).addStudentDiningRoom(s4);
            this.b2.conquerProfessor(playerList.get(0), SPColour.RED);
        } catch (ExceededMaxStudentsDiningRoomException | NoProfessorBagException | ProfessorNotFoundException e) {
            e.printStackTrace();
        }
        //TODO:check try to conquer
        this.b2.archipelagos.get(0).addStudent(s1);
        this.b2.archipelagos.get(1).addStudent(s2);
        this.b2.archipelagos.get(1).addStudent(s3);
        //conquer
        try {
            this.b2.tryToConquer(playerList.get(0));
        } catch (InvalidTowerNumberException e) {
            e.printStackTrace();
        } catch (AnotherTowerException e) {
            e.printStackTrace();
        } catch (ExceededMaxTowersException e) {
            e.printStackTrace();
        }
        //check correct conquer
        Assertions.assertEquals(playerList.get(0), this.b2.getArchipelago(0).getOwner());
        this.b2.moveMotherNature(1);
        try {
            this.b2.tryToConquer(playerList.get(0));
        } catch (InvalidTowerNumberException e) {
            e.printStackTrace();
        } catch (AnotherTowerException e) {
            e.printStackTrace();
        } catch (ExceededMaxTowersException e) {
            e.printStackTrace();
        }
        //check correct conquer
        Assertions.assertEquals(playerList.get(0), this.b2.getArchipelago(0).getOwner());
        //check if there are 11 archipelagos
        assertEquals(11, b2.archipelagos.size());

        //--------------------- (right merge)
        Student s5 = new Student(SPColour.RED);
        Student s6 = new Student(SPColour.RED);
        this.b2.archipelagos.get(10).addStudent(s5);
        this.b2.archipelagos.get(10).addStudent(s6);
        this.b2.moveMotherNature(10);
        try {
            this.b2.tryToConquer(playerList.get(0));
        } catch (InvalidTowerNumberException e) {
            e.printStackTrace();
        } catch (AnotherTowerException e) {
            e.printStackTrace();
        } catch (ExceededMaxTowersException e) {
            e.printStackTrace();
        }
        //check correct conquer
        Assertions.assertEquals(playerList.get(0), this.b2.getArchipelago(9).getOwner());
        //check if there are 10 archipelagos
        assertEquals(10, b2.archipelagos.size());

        //check each archipelago
        Assertions.assertEquals(this.b2.archipelagos.get(0), this.b2.getArchipelago(0));
        Assertions.assertEquals(this.b2.archipelagos.get(1), this.b2.getArchipelago(1));
        Assertions.assertEquals(this.b2.archipelagos.get(2), this.b2.getArchipelago(2));
        Assertions.assertEquals(this.b2.archipelagos.get(3), this.b2.getArchipelago(3));
        Assertions.assertEquals(this.b2.archipelagos.get(4), this.b2.getArchipelago(4));
        Assertions.assertEquals(this.b2.archipelagos.get(5), this.b2.getArchipelago(5));
        Assertions.assertEquals(this.b2.archipelagos.get(6), this.b2.getArchipelago(6));
        Assertions.assertEquals(this.b2.archipelagos.get(7), this.b2.getArchipelago(7));
        Assertions.assertEquals(this.b2.archipelagos.get(8), this.b2.getArchipelago(8));
        Assertions.assertEquals(this.b2.archipelagos.get(9), this.b2.getArchipelago(9));

        //-------------------- Professors
        //check professor position
        Assertions.assertEquals(this.b2.getPlayerSchool(playerList.get(0)), this.b2.whereIsProfessor(SPColour.RED));

        //move students from hall to archipelago
        try {
            this.b2.moveStudentSchoolToArchipelagos(playerList.get(0), this.b2.getPlayerSchool(playerList.get(0)).getStudentsHall().get(0).getColour(), 4);
            this.b2.moveStudentSchoolToArchipelagos(playerList.get(0), this.b2.getPlayerSchool(playerList.get(0)).getStudentsHall().get(0).getColour(), 5);
            this.b2.moveStudentSchoolToArchipelagos(playerList.get(1), this.b2.getPlayerSchool(playerList.get(1)).getStudentsHall().get(0).getColour(), 3);
            this.b2.moveStudentSchoolToArchipelagos(playerList.get(1), this.b2.getPlayerSchool(playerList.get(1)).getStudentsHall().get(0).getColour(), 4);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }

        Student s7 = new Student(SPColour.GREEN);
        Student s8 = new Student(SPColour.GREEN);
        Student s9 = new Student(SPColour.PINK);
        Student s10 = new Student(SPColour.PINK);

        try {
            this.b2.getPlayerSchool(playerList.get(1)).addStudentHall(s7);
            this.b2.getPlayerSchool(playerList.get(1)).addStudentHall(s8);
            this.b2.getPlayerSchool(playerList.get(0)).addStudentHall(s9);
            this.b2.getPlayerSchool(playerList.get(0)).addStudentHall(s10);
            //assert the presence of PINK students in hall
            Assertions.assertTrue(this.b2.isStudentInSchoolHall(playerList.get(0), SPColour.PINK));
            //if needed, take professor from bag
            try {
                this.b2.moveStudentHallToDiningRoom(playerList.get(1), SPColour.GREEN);
                this.b2.moveStudentHallToDiningRoom(playerList.get(1), SPColour.GREEN);
                this.b2.moveStudentHallToDiningRoom(playerList.get(0), SPColour.PINK);
                this.b2.moveStudentHallToDiningRoom(playerList.get(0), SPColour.PINK);
            } catch (StudentNotFoundException e) {
                e.printStackTrace();
            } catch (ExceededMaxStudentsDiningRoomException e) {
                e.printStackTrace();
            } catch (ProfessorNotFoundException e) {
                e.printStackTrace();
            } catch (NoProfessorBagException e) {
                e.printStackTrace();
            }
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }

        //check professor position
        Assertions.assertEquals(this.b2.getPlayerSchool(playerList.get(0)), this.b2.whereIsProfessor(SPColour.RED));
        Assertions.assertEquals(this.b2.getPlayerSchool(playerList.get(1)), this.b2.whereIsProfessor(SPColour.GREEN));
        Assertions.assertEquals(this.b2.getPlayerSchool(playerList.get(0)), this.b2.whereIsProfessor(SPColour.PINK));

        //one player takes professor from another player
        Student s11 = new Student(SPColour.PINK);
        Student s12 = new Student(SPColour.PINK);

        try {
            this.b2.getPlayerSchool(playerList.get(1)).addStudentHall(s11);
            this.b2.getPlayerSchool(playerList.get(1)).addStudentHall(s12);
            //assert the presence of PINK students in hall
            Assertions.assertTrue(this.b2.isStudentInSchoolHall(playerList.get(1), SPColour.PINK));
            try {
                this.b2.moveStudentHallToDiningRoom(playerList.get(1), SPColour.PINK);
                this.b2.moveStudentHallToDiningRoom(playerList.get(1), SPColour.PINK);
            } catch (StudentNotFoundException e) {
                e.printStackTrace();
            } catch (ExceededMaxStudentsDiningRoomException e) {
                e.printStackTrace();
            } catch (ProfessorNotFoundException e) {
                e.printStackTrace();
            } catch (NoProfessorBagException e) {
                e.printStackTrace();
            }
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }

        //assert nothing changed
        Assertions.assertEquals(this.b2.getPlayerSchool(playerList.get(0)), this.b2.whereIsProfessor(SPColour.PINK));

        //one more PINK students
        Student s13 = new Student(SPColour.PINK);

        try {
            this.b2.getPlayerSchool(playerList.get(1)).addStudentHall(s13);
            this.b2.moveStudentHallToDiningRoom(playerList.get(1), SPColour.PINK);
        } catch (ExceededMaxStudentsHallException | StudentNotFoundException | ExceededMaxStudentsDiningRoomException | ProfessorNotFoundException | NoProfessorBagException e) {
            e.printStackTrace();
        }

        //assert change of professor dominance
        Assertions.assertEquals(this.b2.getPlayerSchool(playerList.get(1)), this.b2.whereIsProfessor(SPColour.PINK));

        //move students from hall to archi in order to free the space for students from cloud
        try {
            this.b2.moveStudentSchoolToArchipelagos(playerList.get(0), this.b2.getPlayerSchool(playerList.get(0)).getStudentsHall().get(0).getColour(), 4);
            this.b2.moveStudentSchoolToArchipelagos(playerList.get(0), this.b2.getPlayerSchool(playerList.get(0)).getStudentsHall().get(0).getColour(), 5);
            this.b2.moveStudentSchoolToArchipelagos(playerList.get(0), this.b2.getPlayerSchool(playerList.get(0)).getStudentsHall().get(0).getColour(), 3);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }

        List<Student> coloursHallBefore = new ArrayList<>();
        List<Student> coloursCloud = new ArrayList<>();
        SPColour[] availableColours = {SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};

        coloursHallBefore.addAll(this.b2.getPlayerSchool(playerList.get(0)).getStudentsHall());
        coloursCloud.addAll(this.b2.clouds.get(0).getStudents());

        Assertions.assertEquals(2, this.b2.getPlayerSchool(playerList.get(0)).getStudentsHall().size());
        //students from cloud to hall
        try {
            this.b2.moveStudentCloudToSchool(playerList.get(0), 0);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }

        for(SPColour c: availableColours) {
            Assertions.assertEquals(coloursHallBefore.stream().filter(x -> x.getColour().equals(c)).count() + coloursCloud.stream().filter(x -> x.getColour().equals(c)).count(),
                    this.b2.getPlayerSchool(playerList.get(0)).getStudentsHall().stream().filter(x -> x.getColour().equals(c)).count());
        }

        Assertions.assertEquals(5, this.b2.getPlayerSchool(playerList.get(0)).getStudentsHall().size());
        //empty cloud
        Assertions.assertTrue(this.b2.clouds.get(0).getStudents().size() == 0);
    }

}