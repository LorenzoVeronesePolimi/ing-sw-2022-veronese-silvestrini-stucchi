package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Messages.Enumerations.INMessageType;
import it.polimi.ingsw.Model.Board.*;
import it.polimi.ingsw.Model.Cards.ExchangeThreeStudents;
import it.polimi.ingsw.Model.Cards.PlaceOneStudent;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControllerIntegrityTest {
    ControllerIntegrity controllerIntegrity = new ControllerIntegrity();
    BoardAbstract board;
    BoardAdvanced boardAdvanced;
    List<Player> players;

    @BeforeEach
    void init() {
        Player p1 = new Player("Mario", PlayerColour.WHITE);
        Player p2 = new Player("Luigi", PlayerColour.BLACK);
        Player p3 = new Player("Luigi", PlayerColour.BLACK);
        Player p4 = new Player("Luigi", PlayerColour.WHITE);
        this.players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);

        try {
            this.board = new BoardFour(players);
            this.boardAdvanced = new BoardAdvanced(board);
            controllerIntegrity.setBoard(board);
            controllerIntegrity.setBoardAdvanced(boardAdvanced);
        } catch (StudentNotFoundException | ExceedingAssistantCardNumberException | EmptyCaveauException | TowerNotFoundException | ExceededMaxStudentsCloudException | ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }
    }


    @Test
    void setBoard() {
        controllerIntegrity.setBoard(board);
        assertEquals(controllerIntegrity.getBoard(), board);
    }

    @Test
    void getBoard() {
        controllerIntegrity.setBoard(board);
        assertEquals(controllerIntegrity.getBoard(), board);
    }

    @Test
    void setBoardAdvanced() {
        controllerIntegrity.setBoardAdvanced(boardAdvanced);
        assertEquals(controllerIntegrity.getBoardAdvanced(), boardAdvanced);
    }

    @Test
    void getBoardAdvanced() {
        controllerIntegrity.setBoardAdvanced(boardAdvanced);
        assertEquals(controllerIntegrity.getBoardAdvanced(), boardAdvanced);
    }

    @Test
    void setAdvanced() {
        controllerIntegrity.setAdvanced(true);
        assertTrue(controllerIntegrity.isAdvanced());
        controllerIntegrity.setAdvanced(false);
        assertFalse(controllerIntegrity.isAdvanced());
    }

    @Test
    void isAdvanced() {
        controllerIntegrity.setAdvanced(true);
        assertTrue(controllerIntegrity.isAdvanced());
        controllerIntegrity.setAdvanced(false);
        assertFalse(controllerIntegrity.isAdvanced());
    }

    @Test
    void checkCreateMatch() {
        assertTrue(controllerIntegrity.checkCreateMatch(2));
        assertTrue(controllerIntegrity.checkCreateMatch(3));
        assertTrue(controllerIntegrity.checkCreateMatch(4));
        assertFalse(controllerIntegrity.checkCreateMatch(5));
    }

    @Test
    void checkAssistantCard() {
        //Case first to play
        assertTrue(controllerIntegrity.checkAssistantCard(players, 0, players.get(0), 3));

        //Case one player played one card
        try {
            this.players.get(0).useAssistantCard(2);
        } catch (NoAssistantCardException e) {
            e.printStackTrace();
        }
        assertFalse(controllerIntegrity.checkAssistantCard(players, 1, players.get(1), 2));
        assertTrue(controllerIntegrity.checkAssistantCard(players, 1, players.get(1), 3));

        try {
            this.players.get(1).useAssistantCard(4);
        } catch (NoAssistantCardException e) {
            e.printStackTrace();
        }

        assertFalse(controllerIntegrity.checkAssistantCard(players, 2, players.get(2), 2));
        assertFalse(controllerIntegrity.checkAssistantCard(players, 2, players.get(2), 4));
        assertTrue(controllerIntegrity.checkAssistantCard(players, 2, players.get(2), 8));

        //Case last card
        try {
            this.players.get(1).useAssistantCard(1);
            this.players.get(1).useAssistantCard(3);
            this.players.get(1).useAssistantCard(5);
            this.players.get(1).useAssistantCard(6);
            this.players.get(1).useAssistantCard(7);
            this.players.get(1).useAssistantCard(8);
            this.players.get(1).useAssistantCard(9);
            this.players.get(1).useAssistantCard(10);
        } catch (NoAssistantCardException e) {
            e.printStackTrace();
        }

        assertTrue(controllerIntegrity.checkAssistantCard(players, 1, players.get(1), 2));
    }

    @Test
    void checkStudentHallToDiningRoom() {
        for(int i = 0; i < 10; i++){
            try {
                board.getPlayerSchool(players.get(0)).addStudentDiningRoom(new Student(SPColour.RED));
            } catch (ExceededMaxStudentsDiningRoomException e) {
                e.printStackTrace();
            }
        }
        controllerIntegrity.setBoard(board);
        assertFalse(controllerIntegrity.checkStudentHallToDiningRoom(players.get(0), SPColour.RED));
    }

    @Test
    void checkStudentToArchipelago() {
        //Case no student of that colour in the Hall
        for(Student s : board.getPlayerSchool(players.get(0)).getStudentsHall()) {
            try {
                board.getPlayerSchool(players.get(0)).moveStudentHallToDiningRoom(s.getColour());
            } catch (StudentNotFoundException e) {
                e.printStackTrace();
            } catch (ExceededMaxStudentsDiningRoomException e) {
                e.printStackTrace();
            }
        }
        controllerIntegrity.setBoard(board);
        assertFalse(controllerIntegrity.checkStudentToArchipelago(players.get(0), SPColour.BLUE, 1));

        //Case archipelago not existing
        SPColour c = board.getPlayerSchool(players.get(1)).getStudentsHall().get(0).getColour();
        assertFalse(controllerIntegrity.checkStudentToArchipelago(players.get(1), c, 13));

        //Case true
        assertTrue(controllerIntegrity.checkStudentToArchipelago(players.get(1), c, 2));
    }

    @Test
    void checkMoveMotherNature() {
        try {
            players.get(0).useAssistantCard(7);
        } catch (NoAssistantCardException e) {
            e.printStackTrace();
        }

        assertFalse(controllerIntegrity.checkMoveMotherNature(players.get(0), 8));
        assertTrue(controllerIntegrity.checkMoveMotherNature(players.get(0), 1));
    }

    @Test
    void checkStudentCloudToSchool() {
        //Case void Cloud chosen
        board.getClouds().get(0).empty();

        controllerIntegrity.setBoard(board);
        assertFalse(controllerIntegrity.checkStudentCloudToSchool(players.get(0), 0));

        //Case not enough space in the School Hall
        assertFalse(controllerIntegrity.checkStudentCloudToSchool(players.get(1), 0));

    }

    @Test
    void checkCCExchangeThreeStudents() {
        //Case not advanced
        controllerIntegrity.setAdvanced(false);
        List colours = new ArrayList();
        colours.add(SPColour.BLUE); colours.add(SPColour.BLUE);
        assertFalse(controllerIntegrity.checkCCExchangeTwoHallDining(players.get(0), colours, colours));

        //Case different sizes of input lists
        controllerIntegrity.setAdvanced(true);
        controllerIntegrity.setBoard(board);
        controllerIntegrity.setBoardAdvanced(boardAdvanced);
        List<SPColour> coloursCard = new ArrayList<>();
        coloursCard.add(SPColour.BLUE); coloursCard.add(SPColour.BLUE); coloursCard.add(SPColour.BLUE);
        List<SPColour> coloursSchool = new ArrayList<>();
        coloursCard.add(SPColour.BLUE); coloursCard.add(SPColour.BLUE);
        try {
            ExchangeThreeStudents chosenCard = new ExchangeThreeStudents(boardAdvanced);
            assertFalse(controllerIntegrity.checkCCExchangeThreeStudents(players.get(0), coloursCard, coloursSchool, chosenCard));
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }

        //Case ok
        Player playerOk = players.get(2);
        List<Student> studentsInSchool = board.getPlayerSchool(playerOk).getStudentsHall();
        List<SPColour> coloursSchoolOk = new ArrayList<>();
        coloursSchoolOk.add(studentsInSchool.get(0).getColour()); coloursSchoolOk.add(studentsInSchool.get(1).getColour());
        List<SPColour> coloursCardOk = new ArrayList<>();
        try {
            ExchangeThreeStudents chosenCardOk = new ExchangeThreeStudents(boardAdvanced);
            coloursCardOk.add(chosenCardOk.getStudents().get(0).getColour()); coloursCardOk.add(chosenCardOk.getStudents().get(1).getColour());
            assertTrue(controllerIntegrity.checkCCExchangeThreeStudents(playerOk, coloursCardOk, coloursSchoolOk, chosenCardOk));
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void checkCCExchangeTwoHallDining() {
        //Case not advanced
        controllerIntegrity.setAdvanced(false);
        List<SPColour> colours = new ArrayList<>();
        colours.add(SPColour.BLUE); colours.add(SPColour.BLUE);
        assertFalse(controllerIntegrity.checkCCExchangeTwoHallDining(players.get(0), colours, colours));
        controllerIntegrity.setAdvanced(true);

        //Case different sizes of input lists
        controllerIntegrity.setBoard(board);
        controllerIntegrity.setBoardAdvanced(boardAdvanced);
        List<SPColour> coloursHall = new ArrayList<>();
        coloursHall.add(SPColour.BLUE);
        List<SPColour> coloursDiningRoom = new ArrayList<>();
        coloursDiningRoom.add(SPColour.BLUE); coloursDiningRoom.add(SPColour.BLUE);
        assertFalse(controllerIntegrity.checkCCExchangeTwoHallDining(players.get(0), coloursHall, coloursDiningRoom));

        // Case all Students required are present
        // case same colours in coloursDiningRoom
        List<SPColour> coloursDiningRoomOkSame = new ArrayList<>();
        coloursDiningRoomOkSame.add(SPColour.RED); coloursDiningRoomOkSame.add(SPColour.RED);
        try {
            board.getPlayerSchool(players.get(1)).addStudentDiningRoom(new Student(SPColour.RED));
            board.getPlayerSchool(players.get(1)).addStudentDiningRoom(new Student(SPColour.RED));
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }
        List<Student> studentsInHallOkSame = board.getPlayerSchool(players.get(1)).getStudentsHall();
        List<SPColour> coloursHallOkSame = new ArrayList<>();
        coloursHallOkSame.add(studentsInHallOkSame.get(0).getColour());
        coloursHallOkSame.add(studentsInHallOkSame.get(1).getColour());
        assertTrue(controllerIntegrity.checkCCExchangeTwoHallDining(players.get(1), coloursHallOkSame, coloursDiningRoomOkSame));
        // case different colours in coloursDiningRoom
        List<SPColour> coloursDiningRoomOkDifferent = new ArrayList<>();
        coloursDiningRoomOkDifferent.add(SPColour.RED); coloursDiningRoomOkDifferent.add(SPColour.YELLOW);
        try {
            board.getPlayerSchool(players.get(2)).addStudentDiningRoom(new Student(SPColour.RED));
            board.getPlayerSchool(players.get(2)).addStudentDiningRoom(new Student(SPColour.YELLOW));
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }
        List<Student> studentsInHallOkDifferent = board.getPlayerSchool(players.get(2)).getStudentsHall();
        List<SPColour> coloursHallOkDifferent = new ArrayList<>();
        coloursHallOkDifferent.add(studentsInHallOkDifferent.get(0).getColour());
        coloursHallOkDifferent.add(studentsInHallOkDifferent.get(1).getColour());
        assertTrue(controllerIntegrity.checkCCExchangeTwoHallDining(players.get(2), coloursHallOkDifferent, coloursDiningRoomOkDifferent));
        // case one colour selected
        List<SPColour> coloursDiningRoomOkOne = new ArrayList<>();
        coloursDiningRoomOkOne.add(SPColour.RED);
        try {
            board.getPlayerSchool(players.get(3)).addStudentDiningRoom(new Student(SPColour.RED));
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }
        List<Student> studentsInHallOkOne = board.getPlayerSchool(players.get(3)).getStudentsHall();
        List<SPColour> coloursHallOkOne = new ArrayList<>();
        coloursHallOkOne.add(studentsInHallOkOne.get(0).getColour());
        assertTrue(controllerIntegrity.checkCCExchangeTwoHallDining(players.get(3), coloursHallOkOne, coloursDiningRoomOkOne));
    }

    @Test
    void checkCCGeneric() {
        controllerIntegrity.setAdvanced(false);
        assertFalse(controllerIntegrity.checkCCGeneric());
        controllerIntegrity.setAdvanced(true);
        assertTrue(controllerIntegrity.checkCCGeneric());
    }

    @Test
    void checkCCFakeMNMovement() {
        // Case not advanced
        controllerIntegrity.setAdvanced(false);
        assertFalse(controllerIntegrity.checkCCFakeMNMovement(1));
        controllerIntegrity.setAdvanced(true);

        // Case index not ok
        assertFalse(controllerIntegrity.checkCCFakeMNMovement(13));

        // Case index ok
        assertTrue(controllerIntegrity.checkCCFakeMNMovement(2));
    }

    @Test
    void checkCCForbidIsland() {
        // Case not advanced
        controllerIntegrity.setAdvanced(false);
        assertFalse(controllerIntegrity.checkCCForbidIsland(1));
        controllerIntegrity.setAdvanced(true);

        // Case index not ok
        assertFalse(controllerIntegrity.checkCCForbidIsland(13));

        // Case index ok
        assertTrue(controllerIntegrity.checkCCForbidIsland(2));
    }

    @Test
    void checkCCPlaceOneStudent() {
        // Case not advanced
        controllerIntegrity.setAdvanced(false);
        assertFalse(controllerIntegrity.checkCCForbidIsland(1));
        controllerIntegrity.setAdvanced(true);

        // Case index not ok
        assertFalse(controllerIntegrity.checkCCForbidIsland(13));

        // Case index ok
        try {
            PlaceOneStudent chosenCard = new PlaceOneStudent(boardAdvanced);
            assertTrue(controllerIntegrity.checkCCPlaceOneStudent(chosenCard.getCardStudents().get(0).getColour(), 1, chosenCard));
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
    }
}