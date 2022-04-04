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

public class TakeProfessorOnEquityTest {
    @Test
    void TakeProfessorOnEquityTest(){
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
        } catch (ExceededMaxStudentsHallException | StudentNotFoundException | TowerNotFoundException | EmptyCaveauExcepion e) {
            e.printStackTrace();
        }

        TakeProfessorOnEquity card = new TakeProfessorOnEquity(boardAdvanced);
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
        Student s5 = new Student(SPColour.BLUE);
        Student s6 = new Student(SPColour.BLUE);

        try {
            boardAdvanced.getSchools().get(0).addStudentHall(s1);
            boardAdvanced.getSchools().get(0).addStudentHall(s2);
            boardAdvanced.getSchools().get(0).addStudentHall(s3);
            boardAdvanced.getSchools().get(1).addStudentHall(s4);
            boardAdvanced.getSchools().get(1).addStudentHall(s5);
            boardAdvanced.getSchools().get(1).addStudentHall(s6);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }

        try {
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.BLUE);
        } catch (StudentNotFoundException | ExceededMaxStudentsDiningRoomException | EmptyCaveauExcepion | ProfessorNotFoundException | NoProfessorBagException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(boardAdvanced.getPlayerSchool(p1),boardAdvanced.whereIsProfessor(SPColour.BLUE));

        try {
            boardAdvanced.moveStudentHallToDiningRoom(p2,SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p2,SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p2,SPColour.BLUE);
        } catch (StudentNotFoundException | ExceededMaxStudentsDiningRoomException | EmptyCaveauExcepion | ProfessorNotFoundException | NoProfessorBagException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(2, ((SchoolAdvanced)boardAdvanced.getPlayerSchool(p2)).getNumCoins());

        try {
            boardAdvanced.useTakeProfessorOnEquity(p2);
        } catch (EmptyCaveauExcepion | TowerNotFoundException | ExceededMaxTowersException | NoProfessorBagException | ProfessorNotFoundException | AnotherTowerException | InvalidTowerNumberException | CoinNotFoundException | ExceededMaxNumCoinException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(boardAdvanced.getPlayerSchool(p2),boardAdvanced.whereIsProfessor(SPColour.BLUE));
        Assertions.assertEquals(0, ((SchoolAdvanced)boardAdvanced.getPlayerSchool(p2)).getNumCoins());
    }
}
