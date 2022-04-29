package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class SerializedBoardAbstractTest {
    List<Player> playerList = null;
    BoardAbstract b2;
    BoardAbstract b3;

    @BeforeEach
    void init() {
        playerList = new ArrayList<>();
        Player p1 = new Player("player one", PlayerColour.BLACK);
        Player p2 = new Player("player two", PlayerColour.WHITE);
        playerList.add(p1);
        playerList.add(p2);

        //create board factory
        BoardFactory bf = new BoardFactory(playerList);
        //test for BoardTwo
        b2 = bf.createBoard();
        //b3 = new BoardThree(playerList);

    }
    @Test
    public void SerializedBoardAbstractTest(){
        SerializedBoardAbstract serializedBoard = new SerializedBoardAbstract(b2.archipelagos, b2.clouds, b2.mn, b2.schools);
        Assertions.assertEquals(b2.archipelagos, serializedBoard.getArchipelagos());
        Assertions.assertEquals(b2.clouds, serializedBoard.getClouds());
        Assertions.assertEquals(b2.mn, serializedBoard.getMn());
        Assertions.assertEquals(b2.schools, serializedBoard.getSchools());
    }
}
