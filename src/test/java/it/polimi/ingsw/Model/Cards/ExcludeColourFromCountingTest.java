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

public class ExcludeColourFromCountingTest {
    @Test
    void ExcludeColourFromCountingTests(){
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

        ExcludeColourFromCounting card = new ExcludeColourFromCounting(boardAdvanced);
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

        try {
            boardAdvanced.getSchools().get(0).addStudentHall(s1);
            boardAdvanced.getSchools().get(0).addStudentHall(s2);
            boardAdvanced.getSchools().get(0).addStudentHall(s3);
            boardAdvanced.getSchools().get(0).addStudentHall(s4);
            boardAdvanced.getSchools().get(0).addStudentHall(s5);
            boardAdvanced.getSchools().get(0).addStudentHall(s6);
            boardAdvanced.getSchools().get(0).addStudentHall(s7);
            boardAdvanced.getSchools().get(1).addStudentHall(s8);
            boardAdvanced.getSchools().get(1).addStudentHall(s9);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }

        try {
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.PINK);
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.PINK);
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.PINK);
            boardAdvanced.moveStudentHallToDiningRoom(p2,SPColour.RED);
            boardAdvanced.moveStudentHallToDiningRoom(p2,SPColour.RED);
        } catch (StudentNotFoundException | ExceededMaxStudentsDiningRoomException | EmptyCaveauExcepion | ProfessorNotFoundException | NoProfessorBagException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(boardAdvanced.getSchools().get(0),boardAdvanced.whereIsProfessor(SPColour.BLUE));
        Assertions.assertEquals(boardAdvanced.getSchools().get(0),boardAdvanced.whereIsProfessor(SPColour.PINK));
        Assertions.assertEquals(boardAdvanced.getSchools().get(1),boardAdvanced.whereIsProfessor(SPColour.RED));
        Assertions.assertEquals(3,((SchoolAdvanced)boardAdvanced.getSchools().get(0)).getNumCoins());
        Assertions.assertEquals(1,((SchoolAdvanced)boardAdvanced.getSchools().get(1)).getNumCoins());

        Student s10 = new Student(SPColour.BLUE);
        Student s11 = new Student(SPColour.RED);
        boardAdvanced.getArchiList().get(0).addStudent(s10);
        boardAdvanced.getArchiList().get(0).addStudent(s11);

        try {
            boardAdvanced.useExcludeColourFromCounting(p1,SPColour.RED);
            boardAdvanced.tryToConquer(p1);
        } catch (EmptyCaveauExcepion | ExceededMaxNumCoinException | CoinNotFoundException | InvalidTowerNumberException | AnotherTowerException | ExceededMaxTowersException | TowerNotFoundException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(0,((SchoolAdvanced)boardAdvanced.getSchools().get(0)).getNumCoins());
        Assertions.assertEquals(p1, boardAdvanced.getArchiList().get(0).getOwner());
    }
}
