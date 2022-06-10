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

public class ExtraStudentInDiningTest {
    @Test
    void ExtraStudentInDining(){
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

        ExtraStudentInDining card = null;
        try {
            card = new ExtraStudentInDining(boardAdvanced);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals("ExtraStudentInDining [c. " + 2 + "]", card.toString());

        assert boardAdvanced != null;
        boardAdvanced.setExtractedCards(card);

        for(int i=0; i<7; i++) {
            try {
                boardAdvanced.getSchools().get(0).removeStudentHall(boardAdvanced.getSchools().get(0).getStudentsHall().get(0).getColour());
            } catch (StudentNotFoundException e) {
                e.printStackTrace();
            }
        }
        Student s1 = new Student(SPColour.BLUE);
        Student s2 = new Student(SPColour.BLUE);
        Student s3 = new Student(SPColour.BLUE);
        try {
            boardAdvanced.getSchools().get(0).addStudentHall(s1);
            boardAdvanced.getSchools().get(0).addStudentHall(s2);
            boardAdvanced.getSchools().get(0).addStudentHall(s3);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }
        try {
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.BLUE);
        } catch (StudentNotFoundException | ExceededMaxStudentsDiningRoomException | EmptyCaveauException | ProfessorNotFoundException | NoProfessorBagException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(2,((SchoolAdvanced)boardAdvanced.getSchools().get(0)).getNumCoins());

        assert card != null;
        if(card.getStudentsOnCard().stream().anyMatch(x -> x.getColour().equals(SPColour.PINK))){
            try {
                boardAdvanced.useExtraStudentInDining(p1,SPColour.PINK, 0);
                Assertions.assertEquals(1, boardAdvanced.getSchools().get(0).getNumStudentColour(SPColour.PINK));
            } catch (EmptyCaveauException | ExceededMaxNumCoinException | CoinNotFoundException | StudentNotFoundException | ExceededMaxStudentsDiningRoomException e) {
                e.printStackTrace();
            }
        }else{
            BoardAdvanced finalBoardAdvanced = boardAdvanced;
            Assertions.assertThrows(StudentNotFoundException.class, () -> finalBoardAdvanced.useExtraStudentInDining(p1, SPColour.PINK,0));
        }

    }
}
