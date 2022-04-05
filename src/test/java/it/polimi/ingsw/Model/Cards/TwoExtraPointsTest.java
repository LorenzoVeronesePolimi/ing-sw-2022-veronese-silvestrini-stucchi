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

public class TwoExtraPointsTest {
    @Test
    void ReduceColourInDiningTest() {
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
        } catch (ExceededMaxStudentsHallException | StudentNotFoundException | TowerNotFoundException | EmptyCaveauException e) {
            e.printStackTrace();
        }

        TwoExtraPoints card = new TwoExtraPoints(boardAdvanced);
        boardAdvanced.setExtractedCards(card);

        for (int i = 0; i < 7; i++) {
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
        Student s4 = new Student(SPColour.PINK);
        Student s5 = new Student(SPColour.PINK);

        try {
            boardAdvanced.getSchools().get(0).addStudentHall(s1);
            boardAdvanced.getSchools().get(0).addStudentHall(s2);
            boardAdvanced.getSchools().get(0).addStudentHall(s3);
            boardAdvanced.getSchools().get(1).addStudentHall(s4);
            boardAdvanced.getSchools().get(1).addStudentHall(s5);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }

        try {
            boardAdvanced.moveStudentHallToDiningRoom(p1, SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p1, SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p1, SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p2, SPColour.PINK);
            boardAdvanced.moveStudentHallToDiningRoom(p2, SPColour.PINK);
        } catch (StudentNotFoundException | ExceededMaxStudentsDiningRoomException | EmptyCaveauExcepion | ProfessorNotFoundException | NoProfessorBagException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(boardAdvanced.getPlayerSchool(p1), boardAdvanced.whereIsProfessor(SPColour.BLUE));
        Assertions.assertEquals(boardAdvanced.getPlayerSchool(p2), boardAdvanced.whereIsProfessor(SPColour.PINK));

        Student _1 = new Student(SPColour.PINK);
        Student _2 = new Student(SPColour.PINK);
        Student _3 = new Student(SPColour.BLUE);
        boardAdvanced.getArchipelago(0).addStudent(_1);
        boardAdvanced.getArchipelago(0).addStudent(_2);
        boardAdvanced.getArchipelago(0).addStudent(_3);

        try {
            boardAdvanced.useTwoExtraPoints(p1);
        } catch (EmptyCaveauExcepion | TowerNotFoundException | ExceededMaxTowersException | AnotherTowerException | InvalidTowerNumberException | CoinNotFoundException | ExceededMaxNumCoinException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(p1, boardAdvanced.getArchipelago(0).getOwner());

    }
}
