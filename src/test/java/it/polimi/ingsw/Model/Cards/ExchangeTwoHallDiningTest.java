package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAbstract;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Board.BoardFactory;
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

public class ExchangeTwoHallDiningTest {
    @Test
    void ExchangeTwoHallDiningTest() {
        List<Player> playerList = new ArrayList<>();
        Player p1 = new Player("player one", PlayerColour.BLACK);
        Player p2 = new Player("player two", PlayerColour.WHITE);
        playerList.add(p1);
        playerList.add(p2);

        BoardFactory bf = new BoardFactory(playerList);
        BoardAbstract board = bf.createBoard();
        BoardAdvanced boardAdvanced = null;
        try {
            boardAdvanced = new BoardAdvanced(board);
        } catch (ExceededMaxStudentsHallException | EmptyCaveauException | TowerNotFoundException | StudentNotFoundException e) {
            e.printStackTrace();
        }

        ExchangeTwoHallDining card = new ExchangeTwoHallDining(boardAdvanced);

        for (int i = 0; i < 7; i++) {
            try {
                boardAdvanced.getSchools().get(0).removeStudentHall(boardAdvanced.getSchools().get(0).getStudentsHall().get(0).getColour());
            } catch (StudentNotFoundException e) {
                e.printStackTrace();
            }
        }

        Student s1 = new Student(SPColour.BLUE);
        Student s2 = new Student(SPColour.BLUE);
        Student s3 = new Student(SPColour.BLUE);
        Student s4 = new Student(SPColour.BLUE);
        Student s5 = new Student(SPColour.BLUE);
        Student s6 = new Student(SPColour.BLUE);
        Student s7 = new Student(SPColour.BLUE);

        try {
            boardAdvanced.getSchools().get(0).addStudentHall(s1);
            boardAdvanced.getSchools().get(0).addStudentHall(s2);
            boardAdvanced.getSchools().get(0).addStudentHall(s3);
            boardAdvanced.getSchools().get(0).addStudentHall(s4);
            boardAdvanced.getSchools().get(0).addStudentHall(s5);
            boardAdvanced.getSchools().get(0).addStudentHall(s6);
            boardAdvanced.getSchools().get(0).addStudentHall(s7);

        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }
        try {
            boardAdvanced.getSchools().get(0).moveStudentHallToDiningRoom(SPColour.BLUE);
            boardAdvanced.getSchools().get(0).moveStudentHallToDiningRoom(SPColour.BLUE);
        } catch (StudentNotFoundException | ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(2, boardAdvanced.getSchools().get(0).getNumStudentColour(SPColour.BLUE));
        Assertions.assertEquals(0, boardAdvanced.getSchools().get(0).getNumStudentColour(SPColour.PINK));
        Assertions.assertEquals(0, boardAdvanced.getSchools().get(0).getNumStudentColour(SPColour.RED));
        Assertions.assertEquals(0, boardAdvanced.getSchools().get(0).getNumStudentColour(SPColour.GREEN));
        Assertions.assertEquals(0, boardAdvanced.getSchools().get(0).getNumStudentColour(SPColour.YELLOW));

        Student s8 = new Student(SPColour.PINK);
        Student s9 = new Student(SPColour.RED);

        try {
            boardAdvanced.getSchools().get(0).addStudentHall(s8);
            boardAdvanced.getSchools().get(0).addStudentHall(s9);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }

        boardAdvanced.setExtractedCards(card);

        List<SPColour> hallColours = new ArrayList<>();
        hallColours.add(SPColour.PINK);
        hallColours.add(SPColour.RED);

        List<SPColour> diningColour = new ArrayList<>();
        diningColour.add(SPColour.BLUE);
        diningColour.add(SPColour.BLUE);

        try {
            boardAdvanced.useExchangeTwoHallDining(p1,hallColours,diningColour, 0);
        } catch (EmptyCaveauException | StudentNotFoundException | ExceededMaxStudentsHallException | ExceededMaxStudentsDiningRoomException | WrongNumberOfStudentsTransferException | CoinNotFoundException | ExceededMaxNumCoinException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(0, ((SchoolAdvanced)boardAdvanced.getSchools().get(0)).getNumCoins());

        Assertions.assertEquals(0, boardAdvanced.getSchools().get(0).getNumStudentColour(SPColour.BLUE));
        Assertions.assertEquals(1, boardAdvanced.getSchools().get(0).getNumStudentColour(SPColour.PINK));
        Assertions.assertEquals(1, boardAdvanced.getSchools().get(0).getNumStudentColour(SPColour.RED));
        Assertions.assertEquals(0, boardAdvanced.getSchools().get(0).getNumStudentColour(SPColour.GREEN));
        Assertions.assertEquals(0, boardAdvanced.getSchools().get(0).getNumStudentColour(SPColour.YELLOW));

    }
}
