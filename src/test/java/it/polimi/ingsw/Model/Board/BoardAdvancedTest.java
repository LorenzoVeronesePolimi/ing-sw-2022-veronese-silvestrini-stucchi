package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Places.School.SchoolAdvanced;
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

        BoardFactory bf = new BoardFactory(playerList);
        BoardAbstract board = bf.createBoard();

        BoardAdvanced boardAdvanced = null;
        try {
            boardAdvanced = new BoardAdvanced(board);
        } catch (ExceededMaxStudentsHallException | StudentNotFoundException | TowerNotFoundException | EmptyCaveauException e) {
            e.printStackTrace();
        }

        List<Integer> usedCards = new ArrayList<>();

        try {
            boardAdvanced.useAssistantCard(usedCards, board.players.get(0), 1);
            usedCards.add(1);
            Assertions.assertEquals(1, board.players.get(0).getLastCard().getTurnPriority());
            Assertions.assertEquals(boardAdvanced.computeWinner(board.players.get(0), board.players.get(0), board.archipelagos.get(0)), null);
            Assertions.assertEquals(boardAdvanced.computeInfluenceOfPlayer(board.players.get(0), board.archipelagos.get(0)), 0);
            Assertions.assertEquals(1, ((SchoolAdvanced)boardAdvanced.getPlayerSchool(p2)).getNumCoins());

        } catch (AssistantCardAlreadyPlayedTurnException | NoAssistantCardException e) {
            e.printStackTrace();
        }

        boardAdvanced.moveMotherNature(1);
        boardAdvanced.placeMotherNatureInitialBoard();
        Assertions.assertEquals(0, boardAdvanced.whereIsMotherNature());
        Assertions.assertEquals(board.bag , boardAdvanced.getBag());

        Assertions.assertEquals(board.archipelagos, boardAdvanced.getArchiList());

        Assertions.assertEquals(board.schools, boardAdvanced.getSchools());
        Assertions.assertEquals(3, boardAdvanced.getSchools().size());

        Assertions.assertEquals(9, boardAdvanced.getSchools().get(0).getStudentsHall().size());
        for(int i=0; i<12; i++) {
            Assertions.assertEquals(board.archipelagos.get(i), boardAdvanced.getArchipelago(i));
        }
        Assertions.assertEquals(3, boardAdvanced.getClouds().size());

        // remove all students from all schools
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
        Assertions.assertFalse(boardAdvanced.isStudentInSchoolHall(p1,SPColour.GREEN));
        Assertions.assertFalse(boardAdvanced.isStudentInSchoolHall(p1,SPColour.RED));
        Assertions.assertFalse(boardAdvanced.isStudentInSchoolHall(p1,SPColour.YELLOW));
        Assertions.assertFalse(boardAdvanced.isStudentInSchoolHall(p1,SPColour.PINK));

        Assertions.assertFalse(boardAdvanced.isStudentInSchoolHall(p2,SPColour.BLUE));
        Assertions.assertFalse(boardAdvanced.isStudentInSchoolHall(p2,SPColour.GREEN));
        Assertions.assertFalse(boardAdvanced.isStudentInSchoolHall(p2,SPColour.RED));
        Assertions.assertFalse(boardAdvanced.isStudentInSchoolHall(p2,SPColour.YELLOW));
        Assertions.assertFalse(boardAdvanced.isStudentInSchoolHall(p2,SPColour.PINK));

        Assertions.assertFalse(boardAdvanced.isStudentInSchoolHall(p3,SPColour.BLUE));
        Assertions.assertFalse(boardAdvanced.isStudentInSchoolHall(p3,SPColour.GREEN));
        Assertions.assertFalse(boardAdvanced.isStudentInSchoolHall(p3,SPColour.RED));
        Assertions.assertFalse(boardAdvanced.isStudentInSchoolHall(p3,SPColour.YELLOW));
        Assertions.assertFalse(boardAdvanced.isStudentInSchoolHall(p3,SPColour.PINK));

        BoardAdvanced finalBoardAdvanced1 = boardAdvanced;
        Assertions.assertThrows(StudentNotFoundException.class, () -> finalBoardAdvanced1.moveStudentHallToDiningRoom(p1, SPColour.BLUE));

        Student s = new Student(SPColour.BLUE);
        try {
            boardAdvanced.getSchools().get(0).addStudentHall(s);
            boardAdvanced.moveStudentHallToDiningRoom(p1, SPColour.BLUE);
            // professor BLUE is conquered
        } catch (ExceededMaxStudentsHallException | StudentNotFoundException | ExceededMaxStudentsDiningRoomException | EmptyCaveauException | ProfessorNotFoundException | NoProfessorBagException e) {
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
        Assertions.assertEquals(0, boardAdvanced.getArchipelago(0).howManyStudents().get(SPColour.BLUE));
        Assertions.assertEquals(0, boardAdvanced.getArchipelago(0).howManyStudents().get(SPColour.GREEN));
        Assertions.assertEquals(0, boardAdvanced.getArchipelago(0).howManyStudents().get(SPColour.YELLOW));
        Assertions.assertEquals(0, boardAdvanced.getArchipelago(0).howManyStudents().get(SPColour.RED));

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
        } catch (StudentNotFoundException | ExceededMaxStudentsCloudException e) {
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
            // empty player 3 school
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
        } catch (StudentNotFoundException | ExceededMaxStudentsDiningRoomException | EmptyCaveauException | ProfessorNotFoundException | NoProfessorBagException e) {
            e.printStackTrace();
        }

        Assertions.assertTrue(boardAdvanced.isProfessorInSchool(SPColour.GREEN));
        Assertions.assertEquals(boardAdvanced.getPlayerSchool(p3), boardAdvanced.whereIsProfessor(SPColour.GREEN));
        Assertions.assertFalse(boardAdvanced.isProfessorInSchool(SPColour.YELLOW));
        try {
            boardAdvanced.moveProfessor(p3, SPColour.YELLOW);
        } catch (ProfessorNotFoundException | NoProfessorBagException e) {
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
        } catch (InvalidTowerNumberException | AnotherTowerException | ExceededMaxTowersException | TowerNotFoundException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(PlayerColour.GRAY, boardAdvanced.getArchipelago(1).getOwner().getColour());
        Assertions.assertEquals(p3, boardAdvanced.getArchipelago(1).getOwner());

        // check coin needed

        try {
            Assertions.assertEquals(1, ((SchoolAdvanced)boardAdvanced.getPlayerSchool(p2)).getNumCoins());
            _1 = new Student(SPColour.BLUE);
            _2 = new Student(SPColour.BLUE);
            _3 = new Student(SPColour.BLUE);

            boardAdvanced.getPlayerSchool(p2).addStudentHall(_1);
            boardAdvanced.getPlayerSchool(p2).addStudentHall(_2);
            boardAdvanced.getPlayerSchool(p2).addStudentHall(_3);
            boardAdvanced.moveStudentHallToDiningRoom(p2, SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p2, SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p2, SPColour.BLUE);
            Assertions.assertEquals(2, ((SchoolAdvanced)boardAdvanced.getPlayerSchool(p2)).getNumCoins());
            Assertions.assertEquals(boardAdvanced.getPlayerSchool(p2), boardAdvanced.whereIsProfessor(SPColour.BLUE));


            _1 = new Student(SPColour.RED);
            _2 = new Student(SPColour.RED);
            _3 = new Student(SPColour.RED);
            boardAdvanced.getPlayerSchool(p2).addStudentHall(_1);
            boardAdvanced.getPlayerSchool(p2).addStudentHall(_2);
            boardAdvanced.getPlayerSchool(p2).addStudentHall(_3);
            boardAdvanced.moveStudentHallToDiningRoom(p2, SPColour.RED);
            boardAdvanced.moveStudentHallToDiningRoom(p2, SPColour.RED);
            boardAdvanced.moveStudentHallToDiningRoom(p2, SPColour.RED);
            Assertions.assertEquals(3, ((SchoolAdvanced)boardAdvanced.getPlayerSchool(p2)).getNumCoins());
            Assertions.assertEquals(boardAdvanced.getPlayerSchool(p2), boardAdvanced.whereIsProfessor(SPColour.RED));



            _1 = new Student(SPColour.YELLOW);
            _2 = new Student(SPColour.YELLOW);
            _3 = new Student(SPColour.YELLOW);
            boardAdvanced.getPlayerSchool(p2).addStudentHall(_1);
            boardAdvanced.getPlayerSchool(p2).addStudentHall(_2);
            boardAdvanced.getPlayerSchool(p2).addStudentHall(_3);
            boardAdvanced.moveStudentHallToDiningRoom(p2, SPColour.YELLOW);
            boardAdvanced.moveStudentHallToDiningRoom(p2, SPColour.YELLOW);
            boardAdvanced.moveStudentHallToDiningRoom(p2, SPColour.YELLOW);
            Assertions.assertEquals(4, ((SchoolAdvanced)boardAdvanced.getPlayerSchool(p2)).getNumCoins());
            Assertions.assertEquals(boardAdvanced.getPlayerSchool(p2), boardAdvanced.whereIsProfessor(SPColour.YELLOW));



            _1 = new Student(SPColour.PINK);
            _2 = new Student(SPColour.PINK);
            _3 = new Student(SPColour.PINK);
            boardAdvanced.getPlayerSchool(p2).addStudentHall(_1);
            boardAdvanced.getPlayerSchool(p2).addStudentHall(_2);
            boardAdvanced.getPlayerSchool(p2).addStudentHall(_3);
            boardAdvanced.moveStudentHallToDiningRoom(p2, SPColour.PINK);
            boardAdvanced.moveStudentHallToDiningRoom(p2, SPColour.PINK);
            boardAdvanced.moveStudentHallToDiningRoom(p2, SPColour.PINK);
            Assertions.assertEquals(5, ((SchoolAdvanced)boardAdvanced.getPlayerSchool(p2)).getNumCoins());
            Assertions.assertEquals(boardAdvanced.getPlayerSchool(p2), boardAdvanced.whereIsProfessor(SPColour.PINK));


        } catch (ExceededMaxStudentsHallException | ExceededMaxStudentsDiningRoomException | ProfessorNotFoundException | StudentNotFoundException | EmptyCaveauException | NoProfessorBagException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(1, boardAdvanced.whereIsMotherNature());
        _1 = new Student(SPColour.BLUE);
        _2 = new Student(SPColour.BLUE);
        _3 = new Student(SPColour.BLUE);
        _4 = new Student(SPColour.BLUE);
        _5 = new Student(SPColour.BLUE);
        board.archipelagos.get(1).addStudent(_1);
        board.archipelagos.get(1).addStudent(_2);
        board.archipelagos.get(1).addStudent(_3);
        board.archipelagos.get(1).addStudent(_4);
        board.archipelagos.get(1).addStudent(_5);
        try {
            boardAdvanced.tryToConquer(p2);
            Assertions.assertEquals(p2, boardAdvanced.getArchipelago(1).getOwner());
        } catch (InvalidTowerNumberException | AnotherTowerException | ExceededMaxTowersException | TowerNotFoundException e) {
            e.printStackTrace();
        }
        _1 = new Student(SPColour.GREEN);
        _2 = new Student(SPColour.GREEN);
        _3 = new Student(SPColour.GREEN);
        _4 = new Student(SPColour.GREEN);
        _5 = new Student(SPColour.GREEN);
        _6 = new Student(SPColour.GREEN);
        board.archipelagos.get(1).addStudent(_1);
        board.archipelagos.get(1).addStudent(_2);
        board.archipelagos.get(1).addStudent(_3);
        board.archipelagos.get(1).addStudent(_4);
        board.archipelagos.get(1).addStudent(_5);
        board.archipelagos.get(1).addStudent(_6);
        try {
            boardAdvanced.tryToConquer(p3);
            Assertions.assertEquals(p3, boardAdvanced.getArchipelago(1).getOwner());
        } catch (InvalidTowerNumberException | AnotherTowerException | ExceededMaxTowersException | TowerNotFoundException e) {
            e.printStackTrace();
        }

    }
}
