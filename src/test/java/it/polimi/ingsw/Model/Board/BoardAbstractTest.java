package it.polimi.ingsw.Model.Board;

import static org.junit.Assert.*;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardAbstractTest {
    List<Player> playerList = null;
    BoardAbstract b2;
    BoardAbstract b3;

    @BeforeEach
    void init() {
        playerList = new ArrayList<>();
        Player p1 = new Player("player one", PlayerColour.BLACK);
        Player p2 = new Player("player two", PlayerColour.WHITE);
        Player p3 = new Player("player three", PlayerColour.GRAY);
        playerList.add(p1);
        playerList.add(p2);
        playerList.add(p3);

        //test for BoardTwo
        BoardAbstract b2 = new BoardTwo(playerList);

    }

    @Test
    void changeTurnOrder() {
    }

    @Test
    void getArchipelago() {


        //check if there are 12 archipelagos
        assertEquals(12, b2.archipelagos.size());
    }

    @Test
    void whereIsProfessor() {

    }

    @Test
    void whereIsMotherNature() {
    }

    @Test
    void getPlayerSchool() {
    }

    @Test
    void isStudentInSchoolHall() {
    }

    @Test
    void moveMotherNature() {
    }

    @Test
    void moveStudentSchoolToArchipelagos() {
    }

    @Test
    void moveStudentCloudToSchool() {
    }

    @Test
    void moveStudentHallToDiningRoom() {
    }

    @Test
    void moveStudentBagToCloud() {
    }

    @Test
    void moveStudentBagToSchool() {
    }

    @Test
    void moveProfessor() {
    }

    @Test
    void isProfessorInSchool() {
    }

    @Test
    void conquerProfessor() {
    }

    @Test
    void makeTurn() {
    }

    @Test
    void tryToConquer() {
    }

    @Test
    void checkIfConquerable() {
    }

    @Test
    void computeWinner() {
    }

    @Test
    void computeInfluenceOfPlayer() {
    }

    @Test
    void conquerArchipelago() {
    }

    @Test
    void mergeArchipelagos() {
    }
}