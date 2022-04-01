package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class BoardAdvancedTest {
    @Test
    void boardAdvancedTest(){
        List<Player> playerList = new ArrayList<>();
        Player p1 = new Player("player one", PlayerColour.BLACK);
        Player p2 = new Player("player two", PlayerColour.WHITE);
        Player p3 = new Player("player three", PlayerColour.GRAY);
        playerList.add(p1);
        playerList.add(p2);
        playerList.add(p3);

        BoardAbstract board = null;
        try {
            board = new BoardThree(playerList);
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
        BoardAdvanced boardAdvanced = null;
        try {
            boardAdvanced = new BoardAdvanced(board);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        } catch (TowerNotFoundException e) {
            e.printStackTrace();
        } catch (EmptyCaveauExcepion e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(board.bag , boardAdvanced.getBag());

        Assertions.assertEquals(board.archipelagos, boardAdvanced.getArchiList());

        Assertions.assertEquals(board.schools, boardAdvanced.getSchools());
        Assertions.assertEquals(3, boardAdvanced.getSchools().size());

        Assertions.assertEquals(9, boardAdvanced.getSchools().get(0).getStudentsHall().size());
        for(int i=0; i<12; i++) {
            Assertions.assertEquals(board.archipelagos.get(i), boardAdvanced.getArchipelago(i));
        }


        try {
            for(int i=0; i<9; i++) {
                boardAdvanced.getSchools().get(0).removeStudentHall(boardAdvanced.getSchools().get(0).getStudentsHall().get(0).getColour());
                boardAdvanced.getSchools().get(1).removeStudentHall(boardAdvanced.getSchools().get(1).getStudentsHall().get(0).getColour());
                boardAdvanced.getSchools().get(2).removeStudentHall(boardAdvanced.getSchools().get(2).getStudentsHall().get(0).getColour());
            }
        } catch (StudentNotFoundException e) {
        e.printStackTrace();
        }
        Assertions.assertFalse(boardAdvanced.isStudentInSchoolHall(p1,SPColour.BLUE));

        BoardAdvanced finalBoardAdvanced1 = boardAdvanced;
        Assertions.assertThrows(StudentNotFoundException.class, () -> finalBoardAdvanced1.moveStudentHallToDiningRoom(p1, SPColour.BLUE));

        Student s = new Student(SPColour.BLUE);
        try {
            boardAdvanced.getSchools().get(0).addStudentHall(s);
            boardAdvanced.moveStudentHallToDiningRoom(p1, SPColour.BLUE);
        } catch (ExceededMaxStudentsHallException | StudentNotFoundException | ExceededMaxStudentsDiningRoomException | EmptyCaveauExcepion | ProfessorNotFoundException | NoProfessorBagException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(boardAdvanced.isProfessorInSchool(SPColour.BLUE));

        BoardAdvanced finalBoardAdvanced = boardAdvanced;
        Assertions.assertThrows(StudentNotFoundException.class, () -> finalBoardAdvanced.moveStudentSchoolToArchipelagos(p1, SPColour.BLUE, 0));

        Student s1 = new Student(SPColour.PINK);
        try {
            boardAdvanced.getSchools().get(0).addStudentHall(s1);
            boardAdvanced.moveStudentSchoolToArchipelagos(p1, SPColour.PINK, 0);
        } catch (ExceededMaxStudentsHallException | StudentNotFoundException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(1, boardAdvanced.getArchipelago(0).howManyStudents().get(SPColour.PINK));

        Assertions.assertThrows(StudentNotFoundException.class, () -> finalBoardAdvanced.moveStudentSchoolToArchipelagos(p1, SPColour.RED, 0));

        try {
            boardAdvanced.moveStudentCloudToSchool(p1, 0);
            boardAdvanced.moveStudentCloudToSchool(p2, 1);
            boardAdvanced.moveStudentCloudToSchool(p3, 2);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }

        try {
            boardAdvanced.moveStudentBagToCloud();
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        } catch (ExceededMaxStudentsCloudException e) {
            e.printStackTrace();
        }

        Student _1 = new Student(SPColour.GREEN);
        Student _2 = new Student(SPColour.GREEN);
        Student _3 = new Student(SPColour.GREEN);
        Student _4 = new Student(SPColour.GREEN);
        Student _5 = new Student(SPColour.GREEN);
        Student _6 = new Student(SPColour.GREEN);
        Student _7 = new Student(SPColour.GREEN);
        Student _8 = new Student(SPColour.GREEN);
        Student _9 = new Student(SPColour.GREEN);
        Student _10 = new Student(SPColour.GREEN);


        try {
            int dim = boardAdvanced.getPlayerSchool(p3).getStudentsHall().size();
            for(int i=0; i<dim; i++) {
                boardAdvanced.getPlayerSchool(p3).removeStudentHall(boardAdvanced.getPlayerSchool(p3).getStudentsHall().get(0).getColour());
            }
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }


        try {
            boardAdvanced.getPlayerSchool(p3).addStudentHall(_1);
            boardAdvanced.getPlayerSchool(p3).addStudentHall(_2);
            boardAdvanced.getPlayerSchool(p3).addStudentHall(_3);
            boardAdvanced.getPlayerSchool(p3).addStudentHall(_4);
            boardAdvanced.getPlayerSchool(p3).addStudentHall(_5);
            boardAdvanced.getPlayerSchool(p3).addStudentHall(_6);
            boardAdvanced.getPlayerSchool(p3).addStudentHall(_7);
            boardAdvanced.getPlayerSchool(p3).addStudentHall(_8);
            boardAdvanced.getPlayerSchool(p3).addStudentHall(_9);

            Assertions.assertThrows(ExceededMaxStudentsHallException.class, () -> finalBoardAdvanced.getPlayerSchool(p3).addStudentHall(_10));
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }

        Assertions.assertThrows(ExceededMaxStudentsHallException.class, () -> finalBoardAdvanced.moveStudentBagToSchool(1));

        boardAdvanced.moveMotherNature(1);
        Assertions.assertEquals(1, boardAdvanced.whereIsMotherNature());

        try {
            boardAdvanced.moveStudentHallToDiningRoom(p3, SPColour.GREEN);
            boardAdvanced.moveStudentHallToDiningRoom(p3, SPColour.GREEN);
            boardAdvanced.moveStudentHallToDiningRoom(p3, SPColour.GREEN);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        } catch (EmptyCaveauExcepion e) {
            e.printStackTrace();
        } catch (ProfessorNotFoundException e) {
            e.printStackTrace();
        } catch (NoProfessorBagException e) {
            e.printStackTrace();
        }

        Assertions.assertTrue(boardAdvanced.isProfessorInSchool(SPColour.GREEN));
        Assertions.assertEquals(boardAdvanced.getPlayerSchool(p3), boardAdvanced.whereIsProfessor(SPColour.GREEN));
        Assertions.assertFalse(boardAdvanced.isProfessorInSchool(SPColour.YELLOW));
        try {
            boardAdvanced.moveProfessor(p3, SPColour.YELLOW);
        } catch (ProfessorNotFoundException e) {
            e.printStackTrace();
        } catch (NoProfessorBagException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(boardAdvanced.isProfessorInSchool(SPColour.YELLOW));
        Assertions.assertEquals(boardAdvanced.getPlayerSchool(p3), boardAdvanced.whereIsProfessor(SPColour.YELLOW));

        try {
            boardAdvanced.moveStudentSchoolToArchipelagos(p3, SPColour.GREEN, 1);
            boardAdvanced.moveStudentSchoolToArchipelagos(p3, SPColour.GREEN, 1);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }

        try {
            boardAdvanced.tryToConquer(p3);
        } catch (InvalidTowerNumberException e) {
            e.printStackTrace();
        } catch (AnotherTowerException e) {
            e.printStackTrace();
        } catch (ExceededMaxTowersException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(PlayerColour.GRAY, boardAdvanced.getArchipelago(1).getOwner().getColour());

    }
}
