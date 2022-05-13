package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class BoardAbstractTest {
    List<Player> playerList = null;
    BoardAbstract b2;
    BoardAbstract b3;

    @BeforeEach
    void init() {
        playerList = new ArrayList<>();
        Player p1 = new Player("player one", PlayerColour.BLACK);
        Player p2 = new Player("player two", PlayerColour.WHITE);
        playerList.add(p1);
        playerList.add(p2);

        //create board factory
        BoardFactory bf = new BoardFactory(playerList);
        //test for BoardTwo
        b2 = bf.createBoard();
        //b3 = new BoardThree(playerList);

    }

    @Test
    void boardTest() {
        //--------------------- side checks
        //Player
        Assertions.assertEquals(2, this.b2.players.size());

        //Archipelago
        //check if there are 12 archipelagos
        Assertions.assertEquals(12, b2.archipelagos.size());

        Assertions.assertEquals(10, b2.players.get(1).getPlayerHand().size());

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

        // Cloud
        Assertions.assertEquals(2, this.b2.getClouds().size());

        Assertions.assertEquals(3, this.b2.clouds.get(0).getStudents().size());
        Assertions.assertEquals(3, this.b2.clouds.get(1).getStudents().size());

        // Schools
        Assertions.assertEquals(2, this.b2.schools.size());
        Assertions.assertEquals(7, this.b2.schools.get(0).getStudentsHall().size());
        Assertions.assertEquals(7, this.b2.schools.get(1).getStudentsHall().size());
        Assertions.assertEquals(8, this.b2.schools.get(0).getNumTowers());
        Assertions.assertEquals(8, this.b2.schools.get(1).getNumTowers());
        Assertions.assertEquals(8, this.b2.schools.get(0).getTowers().size());
        Assertions.assertEquals(8, this.b2.schools.get(1).getTowers().size());
        //TODO: check empty dining room

        // Map
        Assertions.assertEquals(this.b2.schools.get(0), this.b2.playerSchool.get(this.b2.players.get(0)));
        Assertions.assertEquals(this.b2.schools.get(1), this.b2.playerSchool.get(this.b2.players.get(1)));

        // MotherNature
        Assertions.assertEquals(this.b2.archipelagos.get(0), this.b2.mn.getCurrentPosition());

        // Bag
        Assertions.assertEquals(0, this.b2.bag.getInitialStudents().size());

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
        // check correct conquer professor
        Assertions.assertTrue(this.b2.whereIsProfessor(SPColour.RED) == this.b2.schools.get(0));
        // check owner equals owner
        Assertions.assertFalse(this.b2.checkIfConquerable(this.b2.players.get(0)));

        //TODO:check try to conquer
        this.b2.archipelagos.get(0).addStudent(s1);
        this.b2.archipelagos.get(1).addStudent(s2);
        this.b2.archipelagos.get(1).addStudent(s3);
        //conquer
        try {
            this.b2.tryToConquer(playerList.get(0));
        } catch (InvalidTowerNumberException | TowerNotFoundException | ExceededMaxTowersException | AnotherTowerException e) {
            e.printStackTrace();
        }
        //check correct conquer
        Assertions.assertEquals(playerList.get(0), this.b2.getArchipelago(0).getOwner());

        // move mother nature
        this.b2.moveMotherNature(1);
        // check correct MN position
        Assertions.assertEquals(this.b2.archipelagos.get(1), this.b2.mn.getCurrentPosition());

        // conquer
        try {
            this.b2.tryToConquer(playerList.get(0));
        } catch (InvalidTowerNumberException | TowerNotFoundException | ExceededMaxTowersException | AnotherTowerException e) {
            e.printStackTrace();
        }

        //check correct conquer (after merge they become one archipelago in index 0)
        Assertions.assertEquals(playerList.get(0), this.b2.getArchipelago(0).getOwner());
        //check if there are 11 archipelagos
        Assertions.assertEquals(11, b2.archipelagos.size());

        //--------------------- (left merge - 0 to conquer 10)
        Student s5 = new Student(SPColour.RED);
        Student s6 = new Student(SPColour.RED);
        this.b2.archipelagos.get(10).addStudent(s5);
        this.b2.archipelagos.get(10).addStudent(s6);
        this.b2.moveMotherNature(10);
        try {
            this.b2.tryToConquer(playerList.get(0));
        } catch (InvalidTowerNumberException | TowerNotFoundException | ExceededMaxTowersException | AnotherTowerException e) {
            e.printStackTrace();
        }
        //check correct conquer (after merge, one archipelago in index 9)
        Assertions.assertEquals(playerList.get(0), this.b2.getArchipelago(9).getOwner());
        //check if there are 10 archipelagos
        Assertions.assertEquals(10, b2.archipelagos.size());

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
        //check professor position (red)
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
                // after this
                // Player[0] has pink professor
                // Player[1] has green professor
            } catch (StudentNotFoundException | NoProfessorBagException | ProfessorNotFoundException | ExceededMaxStudentsDiningRoomException e) {
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
            } catch (StudentNotFoundException | NoProfessorBagException | ProfessorNotFoundException | ExceededMaxStudentsDiningRoomException e) {
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

        //assert change of professor dominance (pink)
        Assertions.assertEquals(this.b2.getPlayerSchool(playerList.get(1)), this.b2.whereIsProfessor(SPColour.PINK));

        //move students from hall to archipelago in order to free the space for students from cloud
        try {
            this.b2.moveStudentSchoolToArchipelagos(playerList.get(0), this.b2.getPlayerSchool(playerList.get(0)).getStudentsHall().get(0).getColour(), 4);
            this.b2.moveStudentSchoolToArchipelagos(playerList.get(0), this.b2.getPlayerSchool(playerList.get(0)).getStudentsHall().get(0).getColour(), 5);
            this.b2.moveStudentSchoolToArchipelagos(playerList.get(0), this.b2.getPlayerSchool(playerList.get(0)).getStudentsHall().get(0).getColour(), 3);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }

        SPColour[] availableColours = {SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};

        List<Student> coloursHallBefore = new ArrayList<>(this.b2.getPlayerSchool(playerList.get(0)).getStudentsHall());
        List<Student> coloursCloud = new ArrayList<>(this.b2.clouds.get(0).getStudents());

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

        // conquer archipelago 7
        Student _1 = new Student(SPColour.RED);
        Student _2 = new Student(SPColour.RED);
        Student _3 = new Student(SPColour.RED);

        this.b2.archipelagos.get(7).addStudent(_1);
        this.b2.archipelagos.get(7).addStudent(_2);
        this.b2.archipelagos.get(7).addStudent(_3);

        this.b2.moveMotherNature(8);
        Assertions.assertEquals(7, this.b2.whereIsMotherNature());

        try {
            this.b2.tryToConquer(this.b2.players.get(0));
        } catch (InvalidTowerNumberException | TowerNotFoundException | ExceededMaxTowersException | AnotherTowerException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(playerList.get(0), this.b2.getArchipelago(7).getOwner());

        Student _4 = new Student(SPColour.GREEN);
        Student _5 = new Student(SPColour.GREEN);
        Student _6 = new Student(SPColour.GREEN);
        Student _7 = new Student(SPColour.GREEN);
        Student _8 = new Student(SPColour.GREEN);

        this.b2.archipelagos.get(7).addStudent(_4);
        this.b2.archipelagos.get(7).addStudent(_5);
        this.b2.archipelagos.get(7).addStudent(_6);
        this.b2.archipelagos.get(7).addStudent(_7);
        this.b2.archipelagos.get(7).addStudent(_8);

        try {
            this.b2.tryToConquer(this.b2.players.get(1));
        } catch (InvalidTowerNumberException | TowerNotFoundException | ExceededMaxTowersException | AnotherTowerException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(playerList.get(1), this.b2.getArchipelago(7).getOwner());

        List<Integer> usedCards = new ArrayList<>();
        try {
            this.b2.useAssistantCard(usedCards, this.b2.players.get(0), 1);
            usedCards.add(1);
            Assertions.assertThrows(AssistantCardAlreadyPlayedTurnException.class, () -> this.b2.useAssistantCard(usedCards, this.b2.players.get(1), 1));
        } catch (AssistantCardAlreadyPlayedTurnException | NoAssistantCardException e) {
            e.printStackTrace();
        }

    }
}