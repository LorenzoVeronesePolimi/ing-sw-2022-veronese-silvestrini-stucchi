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

public class BoardFourTest {
    BoardFactory boardFactory;
    Player _1 = new Player("player one", PlayerColour.WHITE);
    Player _2 = new Player("player two", PlayerColour.WHITE);
    Player _3 = new Player("player three", PlayerColour.BLACK);
    Player _4 = new Player("player four", PlayerColour.BLACK);
    List<Player> playerList = new ArrayList<>();

    @BeforeEach
    void init() {
        playerList.add(_1);
        playerList.add(_2);
        playerList.add(_3);
        playerList.add(_4);
        boardFactory = new BoardFactory(playerList);
    }

    @Test
    void BoardFourStandardTest() {
        Board boardFour = boardFactory.createBoard();

        // Schools
        Assertions.assertEquals(_1, boardFour.getPlayerSchool(_1).getPlayer());
        Assertions.assertEquals(_2, boardFour.getPlayerSchool(_2).getPlayer());
        Assertions.assertEquals(_3, boardFour.getPlayerSchool(_3).getPlayer());
        Assertions.assertEquals(_4, boardFour.getPlayerSchool(_4).getPlayer());

        Assertions.assertEquals(7, boardFour.getPlayerSchool(_1).getStudentsHall().size());
        Assertions.assertEquals(8, boardFour.getPlayerSchool(_1).getTowers().size());

        Assertions.assertEquals(7, boardFour.getPlayerSchool(_2).getStudentsHall().size());
        Assertions.assertEquals(0, boardFour.getPlayerSchool(_2).getTowers().size());

        Assertions.assertEquals(7, boardFour.getPlayerSchool(_3).getStudentsHall().size());
        Assertions.assertEquals(8, boardFour.getPlayerSchool(_3).getTowers().size());

        Assertions.assertEquals(7, boardFour.getPlayerSchool(_4).getStudentsHall().size());
        Assertions.assertEquals(0, boardFour.getPlayerSchool(_4).getTowers().size());

        // Cloud
        Assertions.assertEquals(4, boardFour.getClouds().size());
        Assertions.assertEquals(3, boardFour.getClouds().get(0).getStudents().size());
        Assertions.assertEquals(3, boardFour.getClouds().get(1).getStudents().size());
        Assertions.assertEquals(3, boardFour.getClouds().get(2).getStudents().size());
        Assertions.assertEquals(3, boardFour.getClouds().get(3).getStudents().size());

        // Mother Nature
        Assertions.assertEquals(0, boardFour.whereIsMotherNature());

        // Moving student conquering professor and archipelago 0
        SPColour[] availableColours = {SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};
        SPColour choosenColour = null;
        for(SPColour c : availableColours) {
            if(boardFour.isStudentInSchoolHall(_1, c)) {
                try {
                    boardFour.moveStudentHallToDiningRoom(_1, c);
                    Assertions.assertTrue(boardFour.isProfessorInSchool(c));
                    Assertions.assertEquals(boardFour.getPlayerSchool(_1), boardFour.whereIsProfessor(c));
                } catch (StudentNotFoundException | ExceededMaxStudentsDiningRoomException | EmptyCaveauExcepion | ProfessorNotFoundException | NoProfessorBagException e) {
                    e.printStackTrace();
                }

                if(boardFour.isStudentInSchoolHall(_1, c)) {
                    choosenColour = c;
                    try {
                        boardFour.moveStudentSchoolToArchipelagos(_1, c, 0);
                        Assertions.assertEquals(1, boardFour.getNumStudentsInArchipelago(0).get(c));
                    } catch (StudentNotFoundException e) {
                        e.printStackTrace();
                    }

                    try {
                        boardFour.tryToConquer(_1);
                        Assertions.assertEquals(_1, boardFour.getArchipelago(0).getOwner());
                        Assertions.assertEquals(7, boardFour.getPlayerSchool(_1).getNumTowers());

                        // test if teammate tries to conquer same archipelago
                        boardFour.tryToConquer(_2);
                        Assertions.assertEquals(_1, boardFour.getArchipelago(0).getOwner());
                        Assertions.assertEquals(7, boardFour.getPlayerSchool(_1).getNumTowers());
                    } catch (InvalidTowerNumberException | AnotherTowerException | ExceededMaxTowersException | TowerNotFoundException e) {
                        e.printStackTrace();
                    }

                    break;
                }
            }
        }

        // compute change of dominance
        try {
            boardFour.getPlayerSchool(_4).removeStudentHall(boardFour.getPlayerSchool(_4).getStudentsHall().get(0).getColour());
            boardFour.getPlayerSchool(_4).removeStudentHall(boardFour.getPlayerSchool(_4).getStudentsHall().get(0).getColour());
            boardFour.getPlayerSchool(_4).removeStudentHall(boardFour.getPlayerSchool(_4).getStudentsHall().get(0).getColour());
            boardFour.getPlayerSchool(_4).removeStudentHall(boardFour.getPlayerSchool(_4).getStudentsHall().get(0).getColour());
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }

        Student s1 = new Student(choosenColour);
        Student s2 = new Student(choosenColour);
        Student s3 = new Student(choosenColour);
        Student s4 = new Student(choosenColour);

        try {
            boardFour.getPlayerSchool(_4).addStudentHall(s1);
            boardFour.getPlayerSchool(_4).addStudentHall(s2);
            boardFour.getPlayerSchool(_4).addStudentHall(s3);
            boardFour.getPlayerSchool(_4).addStudentHall(s4);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }

        try {
            boardFour.moveStudentHallToDiningRoom(_4, choosenColour);
            boardFour.moveStudentHallToDiningRoom(_4, choosenColour);
            Assertions.assertTrue(boardFour.isProfessorInSchool(choosenColour));
            Assertions.assertEquals(boardFour.getPlayerSchool(_4), boardFour.whereIsProfessor(choosenColour));
        } catch (StudentNotFoundException | ExceededMaxStudentsDiningRoomException | EmptyCaveauExcepion | ProfessorNotFoundException | NoProfessorBagException e) {
            e.printStackTrace();
        }

        try {
            boardFour.moveStudentSchoolToArchipelagos(_4, choosenColour, 0);
            boardFour.moveStudentSchoolToArchipelagos(_4, choosenColour, 0);
            Assertions.assertEquals(3, boardFour.getNumStudentsInArchipelago(0).get(choosenColour));
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }

        // conquering (after setup of needed students and professors positions)
        try {
            boardFour.tryToConquer(_4);
            // the owner is the one with the towers
            Assertions.assertEquals(_3, boardFour.getArchipelago(0).getOwner());
            Assertions.assertEquals(7, boardFour.getPlayerSchool(_3).getNumTowers());

            // test if teammate tries to conquer same archipelago
            boardFour.tryToConquer(_3);
            Assertions.assertEquals(_3, boardFour.getArchipelago(0).getOwner());
            Assertions.assertEquals(7, boardFour.getPlayerSchool(_3).getNumTowers());
        } catch (InvalidTowerNumberException | AnotherTowerException | ExceededMaxTowersException | TowerNotFoundException e) {
            e.printStackTrace();
        }

        // try to conquer with lower influence

        try {
            boardFour.tryToConquer(_2);
            Assertions.assertEquals(_3, boardFour.getArchipelago(0).getOwner());
        } catch (InvalidTowerNumberException | AnotherTowerException | ExceededMaxTowersException | TowerNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Test
    void BoardFourAdvancedTest() {
        Board boardFour = boardFactory.createBoard();
        BoardAdvanced boardAdvanced = null;

        try {
            boardAdvanced = new BoardAdvanced((BoardAbstract) boardFour);
        } catch (ExceededMaxStudentsHallException | StudentNotFoundException | TowerNotFoundException | EmptyCaveauExcepion e) {
            e.printStackTrace();
        }

        // Schools
        Assertions.assertEquals(_1, boardAdvanced.getPlayerSchool(_1).getPlayer());
        Assertions.assertEquals(_2, boardAdvanced.getPlayerSchool(_2).getPlayer());
        Assertions.assertEquals(_3, boardAdvanced.getPlayerSchool(_3).getPlayer());
        Assertions.assertEquals(_4, boardAdvanced.getPlayerSchool(_4).getPlayer());

        Assertions.assertEquals(7, boardAdvanced.getPlayerSchool(_1).getStudentsHall().size());
        Assertions.assertEquals(8, boardAdvanced.getPlayerSchool(_1).getTowers().size());

        Assertions.assertEquals(7, boardAdvanced.getPlayerSchool(_2).getStudentsHall().size());
        Assertions.assertEquals(0, boardAdvanced.getPlayerSchool(_2).getTowers().size());

        Assertions.assertEquals(7, boardAdvanced.getPlayerSchool(_3).getStudentsHall().size());
        Assertions.assertEquals(8, boardAdvanced.getPlayerSchool(_3).getTowers().size());

        Assertions.assertEquals(7, boardAdvanced.getPlayerSchool(_4).getStudentsHall().size());
        Assertions.assertEquals(0, boardAdvanced.getPlayerSchool(_4).getTowers().size());

        // Cloud
        Assertions.assertEquals(4, boardAdvanced.getClouds().size());
        Assertions.assertEquals(3, boardAdvanced.getClouds().get(0).getStudents().size());
        Assertions.assertEquals(3, boardAdvanced.getClouds().get(1).getStudents().size());
        Assertions.assertEquals(3, boardAdvanced.getClouds().get(2).getStudents().size());
        Assertions.assertEquals(3, boardAdvanced.getClouds().get(3).getStudents().size());

        // Mother Nature
        Assertions.assertEquals(0, boardAdvanced.whereIsMotherNature());

        // Moving student conquering professor and archipelago 0
        SPColour[] availableColours = {SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};
        SPColour choosenColour = null;
        for(SPColour c : availableColours) {
            if(boardAdvanced.isStudentInSchoolHall(_1, c)) {
                try {
                    boardAdvanced.moveStudentHallToDiningRoom(_1, c);
                    Assertions.assertTrue(boardAdvanced.isProfessorInSchool(c));
                    Assertions.assertEquals(boardAdvanced.getPlayerSchool(_1), boardAdvanced.whereIsProfessor(c));
                } catch (StudentNotFoundException | ExceededMaxStudentsDiningRoomException | EmptyCaveauExcepion | ProfessorNotFoundException | NoProfessorBagException e) {
                    e.printStackTrace();
                }

                if(boardAdvanced.isStudentInSchoolHall(_1, c)) {
                    choosenColour = c;
                    try {
                        boardAdvanced.moveStudentSchoolToArchipelagos(_1, c, 0);
                        Assertions.assertEquals(1, boardAdvanced.getNumStudentsInArchipelago(0).get(c));
                    } catch (StudentNotFoundException e) {
                        e.printStackTrace();
                    }

                    try {
                        boardAdvanced.tryToConquer(_1);
                        Assertions.assertEquals(_1, boardAdvanced.getArchipelago(0).getOwner());
                        Assertions.assertEquals(7, boardAdvanced.getPlayerSchool(_1).getNumTowers());

                        // test if teammate tries to conquer same archipelago
                        boardAdvanced.tryToConquer(_2);
                        Assertions.assertEquals(_1, boardAdvanced.getArchipelago(0).getOwner());
                        Assertions.assertEquals(7, boardAdvanced.getPlayerSchool(_1).getNumTowers());
                    } catch (InvalidTowerNumberException | AnotherTowerException | ExceededMaxTowersException | TowerNotFoundException e) {
                        e.printStackTrace();
                    }

                    break;
                }
            }
        }

        // compute change of dominance
        try {
            boardAdvanced.getPlayerSchool(_4).removeStudentHall(boardFour.getPlayerSchool(_4).getStudentsHall().get(0).getColour());
            boardAdvanced.getPlayerSchool(_4).removeStudentHall(boardFour.getPlayerSchool(_4).getStudentsHall().get(0).getColour());
            boardAdvanced.getPlayerSchool(_4).removeStudentHall(boardFour.getPlayerSchool(_4).getStudentsHall().get(0).getColour());
            boardAdvanced.getPlayerSchool(_4).removeStudentHall(boardFour.getPlayerSchool(_4).getStudentsHall().get(0).getColour());
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }

        Student s1 = new Student(choosenColour);
        Student s2 = new Student(choosenColour);
        Student s3 = new Student(choosenColour);
        Student s4 = new Student(choosenColour);

        try {
            boardAdvanced.getPlayerSchool(_4).addStudentHall(s1);
            boardAdvanced.getPlayerSchool(_4).addStudentHall(s2);
            boardAdvanced.getPlayerSchool(_4).addStudentHall(s3);
            boardAdvanced.getPlayerSchool(_4).addStudentHall(s4);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }

        try {
            boardAdvanced.moveStudentHallToDiningRoom(_4, choosenColour);
            boardAdvanced.moveStudentHallToDiningRoom(_4, choosenColour);
            Assertions.assertTrue(boardAdvanced.isProfessorInSchool(choosenColour));
            Assertions.assertEquals(boardAdvanced.getPlayerSchool(_4), boardAdvanced.whereIsProfessor(choosenColour));
        } catch (StudentNotFoundException | ExceededMaxStudentsDiningRoomException | EmptyCaveauExcepion | ProfessorNotFoundException | NoProfessorBagException e) {
            e.printStackTrace();
        }

        try {
            boardAdvanced.moveStudentSchoolToArchipelagos(_4, choosenColour, 0);
            boardAdvanced.moveStudentSchoolToArchipelagos(_4, choosenColour, 0);
            Assertions.assertEquals(3, boardAdvanced.getNumStudentsInArchipelago(0).get(choosenColour));
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }

        // conquering (after setup of needed students and professors positions)
        try {
            boardAdvanced.tryToConquer(_4);
            // the owner is the one with the towers
            Assertions.assertEquals(_3, boardAdvanced.getArchipelago(0).getOwner());
            Assertions.assertEquals(7, boardAdvanced.getPlayerSchool(_3).getNumTowers());

            // test if teammate tries to conquer same archipelago
            boardAdvanced.tryToConquer(_3);
            Assertions.assertEquals(_3, boardAdvanced.getArchipelago(0).getOwner());
            Assertions.assertEquals(7, boardAdvanced.getPlayerSchool(_3).getNumTowers());
        } catch (InvalidTowerNumberException | AnotherTowerException | ExceededMaxTowersException | TowerNotFoundException e) {
            e.printStackTrace();
        }

        // try to conquer with lower influence

        try {
            boardAdvanced.tryToConquer(_2);
            Assertions.assertEquals(_3, boardAdvanced.getArchipelago(0).getOwner());
        } catch (InvalidTowerNumberException | AnotherTowerException | ExceededMaxTowersException | TowerNotFoundException e) {
            e.printStackTrace();
        }
    }
}
