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

public class ReduceColourInDiningTest {
    @Test
    void ReduceColourInDiningTests(){
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

        ReduceColourInDining card = new ReduceColourInDining(boardAdvanced);
        Assertions.assertEquals("ReduceColourInDining [c. " + 3 + "]", card.toString());

        assert boardAdvanced != null;
        boardAdvanced.setExtractedCards(card);

        for(int i=0; i<7; i++) {
            try {
                boardAdvanced.getSchools().get(0).removeStudentHall(boardAdvanced.getSchools().get(0).getStudentsHall().get(0).getColour());
                boardAdvanced.getSchools().get(1).removeStudentHall(boardAdvanced.getSchools().get(1).getStudentsHall().get(0).getColour());
            } catch (StudentNotFoundException e) {
                e.printStackTrace();
            }
        }

        Student s1 = new Student(SPColour.BLUE);
        Student s2 = new Student(SPColour.BLUE);
        Student s3 = new Student(SPColour.BLUE);
        Student s4 = new Student(SPColour.BLUE);
        Student s5 = new Student(SPColour.PINK);
        Student s6 = new Student(SPColour.PINK);
        Student s7 = new Student(SPColour.PINK);
        Student s8 = new Student(SPColour.RED);
        Student s9 = new Student(SPColour.RED);
        Student s10 = new Student(SPColour.RED);
        Student s11 = new Student(SPColour.RED);
        Student s12 = new Student(SPColour.RED);

        try {
            boardAdvanced.getSchools().get(0).addStudentHall(s1);
            boardAdvanced.getSchools().get(0).addStudentHall(s2);
            boardAdvanced.getSchools().get(0).addStudentHall(s3);
            boardAdvanced.getSchools().get(1).addStudentHall(s4);
            boardAdvanced.getSchools().get(0).addStudentHall(s5);
            boardAdvanced.getSchools().get(0).addStudentHall(s6);
            boardAdvanced.getSchools().get(0).addStudentHall(s7);
            boardAdvanced.getSchools().get(1).addStudentHall(s8);
            boardAdvanced.getSchools().get(1).addStudentHall(s9);
            boardAdvanced.getSchools().get(0).addStudentHall(s10);
            boardAdvanced.getSchools().get(1).addStudentHall(s11);
            boardAdvanced.getSchools().get(1).addStudentHall(s12);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }

        try {
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p2,SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.PINK);
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.PINK);
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.PINK);
            boardAdvanced.moveStudentHallToDiningRoom(p2,SPColour.RED);
            boardAdvanced.moveStudentHallToDiningRoom(p2,SPColour.RED);
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.RED);
            boardAdvanced.moveStudentHallToDiningRoom(p2,SPColour.RED);
            boardAdvanced.moveStudentHallToDiningRoom(p2,SPColour.RED);
        } catch (StudentNotFoundException | ExceededMaxStudentsDiningRoomException | EmptyCaveauException | ProfessorNotFoundException | NoProfessorBagException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(boardAdvanced.getPlayerSchool(p1), boardAdvanced.whereIsProfessor(SPColour.BLUE));
        Assertions.assertEquals(boardAdvanced.getPlayerSchool(p1), boardAdvanced.whereIsProfessor(SPColour.PINK));
        Assertions.assertEquals(boardAdvanced.getPlayerSchool(p2), boardAdvanced.whereIsProfessor(SPColour.RED));
        Assertions.assertEquals(3, ((SchoolAdvanced)boardAdvanced.getPlayerSchool(p1)).getNumCoins());
        Assertions.assertEquals(2, ((SchoolAdvanced)boardAdvanced.getPlayerSchool(p2)).getNumCoins());

        try {
            boardAdvanced.useReduceColourInDining(p1,SPColour.RED, 0);
        } catch (EmptyCaveauException | StudentNotFoundException | CoinNotFoundException | ExceededMaxNumCoinException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(0,boardAdvanced.getPlayerSchool(p1).getNumStudentColour(SPColour.RED));
        Assertions.assertEquals(1,boardAdvanced.getPlayerSchool(p2).getNumStudentColour(SPColour.RED));
        Assertions.assertEquals(0, ((SchoolAdvanced)boardAdvanced.getPlayerSchool(p1)).getNumCoins());
        Assertions.assertEquals(2, ((SchoolAdvanced)boardAdvanced.getPlayerSchool(p2)).getNumCoins());
    }
}
