package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enumerations.MessageType;
import it.polimi.ingsw.Model.Board.*;
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
    }

    @Test
    void checkCCExchangeThreeStudents() {
    }

    @Test
    void checkCCExchangeTwoHallDining() {
    }

    @Test
    void checkCCGeneric() {
    }

    @Test
    void checkCCFakeMNMovement() {
    }

    @Test
    void checkCCForbidIsland() {
    }

    @Test
    void checkCCPlaceOneStudent() {
    }
}