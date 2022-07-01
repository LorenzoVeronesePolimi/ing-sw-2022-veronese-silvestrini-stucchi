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

public class ForbidIslandTest {
    @Test
    void ForbidIslandTests(){
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

        ForbidIsland card = new ForbidIsland(boardAdvanced);
        Assertions.assertEquals("ForbidIsland [c. " + 2 + "]", card.toString());

        Assertions.assertEquals(4, card.getForbidIconsRemained());
        try {
            card.useEffect(0);
            Assertions.assertEquals(3, card.getForbidIconsRemained());
            card.useEffect(1);
            Assertions.assertEquals(2, card.getForbidIconsRemained());
            card.useEffect(2);
            Assertions.assertEquals(1, card.getForbidIconsRemained());
            card.useEffect(3);
            Assertions.assertEquals(0, card.getForbidIconsRemained());
            Assertions.assertThrows(ExceededNumberForbidFlagException.class, () -> card.useEffect(4));
        } catch (ExceededNumberForbidFlagException e) {
            throw new RuntimeException(e);
        }

        card.addForbidIconsRemained();
        Assertions.assertEquals(1, card.getForbidIconsRemained());
        card.addForbidIconsRemained();
        Assertions.assertEquals(2, card.getForbidIconsRemained());
        card.addForbidIconsRemained();
        Assertions.assertEquals(3, card.getForbidIconsRemained());
        card.addForbidIconsRemained();
        Assertions.assertEquals(4, card.getForbidIconsRemained());
        card.addForbidIconsRemained();
        Assertions.assertEquals(4, card.getForbidIconsRemained());

        assert boardAdvanced != null;
        boardAdvanced.setExtractedCards(card);

        //remove all students from the hall
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
        Student s5 = new Student(SPColour.PINK);
        Student s6 = new Student(SPColour.PINK);

        // add new students
        try {
            boardAdvanced.getSchools().get(0).addStudentHall(s1);
            boardAdvanced.getSchools().get(0).addStudentHall(s2);
            boardAdvanced.getSchools().get(0).addStudentHall(s3);
            boardAdvanced.getSchools().get(1).addStudentHall(s5);
            boardAdvanced.getSchools().get(1).addStudentHall(s6);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }

        // move some students to diningroom
        try {
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p1,SPColour.BLUE);
            boardAdvanced.moveStudentHallToDiningRoom(p2,SPColour.PINK);
            boardAdvanced.moveStudentHallToDiningRoom(p2,SPColour.PINK);
        } catch (StudentNotFoundException | ExceededMaxStudentsDiningRoomException | EmptyCaveauException | ProfessorNotFoundException | NoProfessorBagException e) {
            e.printStackTrace();
        }

        // check blue professor and pink professor
        Assertions.assertEquals(boardAdvanced.getPlayerSchool(p1), boardAdvanced.whereIsProfessor(SPColour.BLUE));
        Assertions.assertEquals(boardAdvanced.getPlayerSchool(p2), boardAdvanced.whereIsProfessor(SPColour.PINK));
        Assertions.assertEquals(2,((SchoolAdvanced)boardAdvanced.getPlayerSchool(p1)).getNumCoins());
        Assertions.assertEquals(1,((SchoolAdvanced)boardAdvanced.getPlayerSchool(p2)).getNumCoins());

        // move some students to archipelago 5
        Student s7 = new Student(SPColour.PINK);
        Student s8 = new Student(SPColour.PINK);
        boardAdvanced.getArchiList().get(5).addStudent(s7);
        boardAdvanced.getArchiList().get(5).addStudent(s8);

        // use card Forbid Island
        try {
            boardAdvanced.useForbidIsland(p1, 5, 0);
        } catch (EmptyCaveauException | CoinNotFoundException | ExceededMaxNumCoinException | ExceededNumberForbidFlagException e) {
            e.printStackTrace();
        }

        // check removed coins
        Assertions.assertEquals(0, ((SchoolAdvanced)boardAdvanced.getPlayerSchool(p1)).getNumCoins());

        // move mother nature where is impossible to conquer
        boardAdvanced.moveMotherNature(5);
        try {
            boardAdvanced.tryToConquer(p2);
        } catch (InvalidTowerNumberException | TowerNotFoundException | ExceededMaxTowersException | AnotherTowerException e) {
            e.printStackTrace();
        }

        Assertions.assertNull(boardAdvanced.getArchipelago(5).getOwner());

        Student s9 = new Student(SPColour.BLUE);
        Student s10 = new Student(SPColour.BLUE);
        Student s11 = new Student(SPColour.BLUE);
        Student s12 = new Student(SPColour.BLUE);

        boardAdvanced.getArchipelago(5).addStudent(s9);
        boardAdvanced.getArchipelago(5).addStudent(s10);
        boardAdvanced.getArchipelago(5).addStudent(s11);
        boardAdvanced.getArchipelago(5).addStudent(s12);

        try {
            boardAdvanced.tryToConquer(p1);
        } catch (InvalidTowerNumberException | TowerNotFoundException | ExceededMaxTowersException | AnotherTowerException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(p1, boardAdvanced.getArchipelago(5).getOwner());

        //This needs to be modified, but it's too difficult to obtain (20 coins in a school!)
        try {
            throw new ExceededNumberForbidFlagException();
        } catch (ExceededNumberForbidFlagException e) {
            System.out.println("ExceededNumberForbidFlag");
        }
    }
}
