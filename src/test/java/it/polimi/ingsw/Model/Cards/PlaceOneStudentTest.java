package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAbstract;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Board.BoardFactory;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PlaceOneStudentTest {
    @Test
    void PlaceOneStudentTest(){
        List<Player> playerList = new ArrayList<>();
        Player p1 = new Player("player one", PlayerColour.BLACK);
        Player p2 = new Player("player two", PlayerColour.WHITE);
        playerList.add(p1);
        playerList.add(p2);

        BoardFactory bf = new BoardFactory(playerList);
        BoardAbstract board =  bf.createBoard();
        BoardAdvanced boardAdvanced = null;
        try {
            boardAdvanced = new BoardAdvanced(board);
        } catch (ExceededMaxStudentsHallException | StudentNotFoundException | TowerNotFoundException | EmptyCaveauException e) {
            e.printStackTrace();
        }

        PlaceOneStudent card = null;
        try {
            card = new PlaceOneStudent(boardAdvanced);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
        boardAdvanced.setExtractedCards(card);

        Assertions.assertTrue(boardAdvanced.getArchipelago(6).howManyStudents().get(SPColour.BLUE) == 0);
        Assertions.assertTrue(boardAdvanced.getArchipelago(6).howManyStudents().get(SPColour.PINK) == 0);
        Assertions.assertTrue(boardAdvanced.getArchipelago(6).howManyStudents().get(SPColour.RED) == 0);
        Assertions.assertTrue(boardAdvanced.getArchipelago(6).howManyStudents().get(SPColour.GREEN) == 0);
        Assertions.assertTrue(boardAdvanced.getArchipelago(6).howManyStudents().get(SPColour.YELLOW) == 0);

        if(card.getCardStudents().stream().anyMatch(x -> x.getColour().equals(SPColour.BLUE))){
            try {
                boardAdvanced.usePlaceOneStudent(p1,SPColour.BLUE,6);
                Assertions.assertTrue(boardAdvanced.getArchipelago(6).howManyStudents().get(SPColour.BLUE) == 1);
                Assertions.assertTrue(boardAdvanced.getArchipelago(6).howManyStudents().get(SPColour.PINK) == 0);
                Assertions.assertTrue(boardAdvanced.getArchipelago(6).howManyStudents().get(SPColour.RED) == 0);
                Assertions.assertTrue(boardAdvanced.getArchipelago(6).howManyStudents().get(SPColour.GREEN) == 0);
                Assertions.assertTrue(boardAdvanced.getArchipelago(6).howManyStudents().get(SPColour.YELLOW) == 0);

            } catch (StudentNotFoundException | CoinNotFoundException | ExceededMaxNumCoinException | EmptyCaveauException e) {
                e.printStackTrace();
            }
        }else{
            BoardAdvanced finalBoardAdvanced = boardAdvanced;
            Assertions.assertThrows(StudentNotFoundException.class, () -> finalBoardAdvanced.usePlaceOneStudent(p1,SPColour.BLUE,6));
        }

        for(int i=0; i<10;i++) {
            try {
                boardAdvanced.getPlayerSchool(p1).addStudentDiningRoom(new Student(SPColour.BLUE));
            } catch (ExceededMaxStudentsDiningRoomException e) {
                e.printStackTrace();
            }
        }

        BoardAdvanced finalBoardAdvanced = boardAdvanced;
        Assertions.assertThrows(ExceededMaxStudentsDiningRoomException.class, () -> finalBoardAdvanced.getPlayerSchool(p1).addStudentDiningRoom(new Student(SPColour.BLUE)));
    }
}
