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

public class ExchangeThreeStudentsTest {
    @Test
    void ExchangeThreeStudentTest() {
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

        ExchangeThreeStudents card = null;
        try {
            card = new ExchangeThreeStudents(boardAdvanced);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }

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

        long numBlueBefore = boardAdvanced.getSchools().get(0).getStudentsHall().stream().filter(x -> x.getColour().equals(SPColour.BLUE)).count();
        long numPinkBefore = boardAdvanced.getSchools().get(0).getStudentsHall().stream().filter(x -> x.getColour().equals(SPColour.PINK)).count();
        long numRedBefore = boardAdvanced.getSchools().get(0).getStudentsHall().stream().filter(x -> x.getColour().equals(SPColour.RED)).count();
        long numGreenBefore = boardAdvanced.getSchools().get(0).getStudentsHall().stream().filter(x -> x.getColour().equals(SPColour.GREEN)).count();
        long numYellowBefore = boardAdvanced.getSchools().get(0).getStudentsHall().stream().filter(x -> x.getColour().equals(SPColour.YELLOW)).count();

        Assertions.assertEquals(7, numBlueBefore);
        Assertions.assertEquals(0, numPinkBefore);
        Assertions.assertEquals(0, numRedBefore);
        Assertions.assertEquals(0, numGreenBefore);
        Assertions.assertEquals(0, numYellowBefore);

        List<SPColour> exchangeColours = new ArrayList<>();
        exchangeColours.add(card.getStudents().get(0).getColour());
        exchangeColours.add(card.getStudents().get(1).getColour());
        exchangeColours.add(card.getStudents().get(2).getColour());

        long numBlueToBeAdded = exchangeColours.stream().filter(x -> x.equals(SPColour.BLUE)).count();
        long numPinkToBeAdded = exchangeColours.stream().filter(x -> x.equals(SPColour.PINK)).count();
        long numRedToBeAdded = exchangeColours.stream().filter(x -> x.equals(SPColour.RED)).count();
        long numGreenToBeAdded = exchangeColours.stream().filter(x -> x.equals(SPColour.GREEN)).count();
        long numYellowToBeAdded = exchangeColours.stream().filter(x -> x.equals(SPColour.YELLOW)).count();


        List<SPColour> hallColours = new ArrayList<>();
        hallColours.add(SPColour.BLUE);
        hallColours.add(SPColour.BLUE);
        hallColours.add(SPColour.BLUE);

        long numBlueToBeRemoved = hallColours.stream().filter(x -> x.equals(SPColour.BLUE)).count();
        long numPinkToBeRemoved = hallColours.stream().filter(x -> x.equals(SPColour.PINK)).count();
        long numRedToBeRemoved = hallColours.stream().filter(x -> x.equals(SPColour.RED)).count();
        long numGreenToBeRemoved = hallColours.stream().filter(x -> x.equals(SPColour.GREEN)).count();
        long numYellowToBeRemoved = hallColours.stream().filter(x -> x.equals(SPColour.YELLOW)).count();


        boardAdvanced.setExtractedCards(card);

        try {
            boardAdvanced.useExchangeThreeStudents(p1, hallColours, exchangeColours, 0);
        } catch (EmptyCaveauException | ExceededMaxStudentsHallException | StudentNotFoundException | WrongNumberOfStudentsTransferException | CoinNotFoundException | ExceededMaxNumCoinException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(0, ((SchoolAdvanced) boardAdvanced.getSchools().get(0)).getNumCoins());

        long numBlueAfter = boardAdvanced.getSchools().get(0).getStudentsHall().stream().filter(x -> x.getColour().equals(SPColour.BLUE)).count();
        long numPinkAfter = boardAdvanced.getSchools().get(0).getStudentsHall().stream().filter(x -> x.getColour().equals(SPColour.PINK)).count();
        long numRedAfter = boardAdvanced.getSchools().get(0).getStudentsHall().stream().filter(x -> x.getColour().equals(SPColour.RED)).count();
        long numGreenAfter = boardAdvanced.getSchools().get(0).getStudentsHall().stream().filter(x -> x.getColour().equals(SPColour.GREEN)).count();
        long numYellowAfter = boardAdvanced.getSchools().get(0).getStudentsHall().stream().filter(x -> x.getColour().equals(SPColour.YELLOW)).count();

        Assertions.assertEquals(numBlueBefore - numBlueToBeRemoved + numBlueToBeAdded, numBlueAfter);
        Assertions.assertEquals(numPinkBefore - numPinkToBeRemoved + numPinkToBeAdded, numPinkAfter);
        Assertions.assertEquals(numRedBefore - numRedToBeRemoved + numRedToBeAdded, numRedAfter);
        Assertions.assertEquals(numGreenBefore - numGreenToBeRemoved + numGreenToBeAdded, numGreenAfter);
        Assertions.assertEquals(numYellowBefore - numYellowToBeRemoved + numYellowToBeAdded, numYellowAfter);

        BoardAdvanced finalBoardAdvance = boardAdvanced;
        Assertions.assertThrows(CoinNotFoundException.class, () -> finalBoardAdvance.useExchangeThreeStudents(p1, hallColours, exchangeColours, 0));
    }

    @Test
    void ExchangeThreeStudentExceptionTest() {
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
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        } catch (TowerNotFoundException e) {
            e.printStackTrace();
        } catch (EmptyCaveauException e) {
            e.printStackTrace();
        }

        ExchangeThreeStudents card = null;
        try {
            card = new ExchangeThreeStudents(boardAdvanced);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
        boardAdvanced.setExtractedCards(card);


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

        long numBlueBefore = boardAdvanced.getSchools().get(0).getStudentsHall().stream().filter(x -> x.getColour().equals(SPColour.BLUE)).count();
        long numPinkBefore = boardAdvanced.getSchools().get(0).getStudentsHall().stream().filter(x -> x.getColour().equals(SPColour.PINK)).count();
        long numRedBefore = boardAdvanced.getSchools().get(0).getStudentsHall().stream().filter(x -> x.getColour().equals(SPColour.RED)).count();
        long numGreenBefore = boardAdvanced.getSchools().get(0).getStudentsHall().stream().filter(x -> x.getColour().equals(SPColour.GREEN)).count();
        long numYellowBefore = boardAdvanced.getSchools().get(0).getStudentsHall().stream().filter(x -> x.getColour().equals(SPColour.YELLOW)).count();

        Assertions.assertEquals(7, numBlueBefore);
        Assertions.assertEquals(0, numPinkBefore);
        Assertions.assertEquals(0, numRedBefore);
        Assertions.assertEquals(0, numGreenBefore);
        Assertions.assertEquals(0, numYellowBefore);

        List<SPColour> exchangeColours = new ArrayList<>();
        exchangeColours.add(card.getStudents().get(0).getColour());
        exchangeColours.add(card.getStudents().get(1).getColour());

        List<SPColour> hallColours = new ArrayList<>();
        hallColours.add(SPColour.BLUE);
        hallColours.add(SPColour.BLUE);
        hallColours.add(SPColour.BLUE);

        BoardAdvanced finalBoard = boardAdvanced;
        Assertions.assertThrows(WrongNumberOfStudentsTransferException.class, () -> finalBoard.useExchangeThreeStudents(p1, hallColours, exchangeColours, 0));
    }
}
