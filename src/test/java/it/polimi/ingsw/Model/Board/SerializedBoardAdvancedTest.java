package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Exceptions.EmptyCaveauException;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxStudentsHallException;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Exceptions.TowerNotFoundException;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class SerializedBoardAdvancedTest {
    List<Player> playerList = null;
    BoardAbstract b2;

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
        BoardAdvanced boardAdvanced = null;
        try {
            boardAdvanced = new BoardAdvanced(b2);
        } catch (ExceededMaxStudentsHallException | StudentNotFoundException | TowerNotFoundException | EmptyCaveauException e) {
            e.printStackTrace();
        }

        assert boardAdvanced != null;
        SerializedBoardAdvanced serializedBoard = new SerializedBoardAdvanced(b2.archipelagos, b2.clouds, b2.mn, b2.schools, boardAdvanced.getColourToExclude(), "", boardAdvanced.getExtractedCards());
        Assertions.assertEquals(b2.archipelagos, serializedBoard.getArchipelagos());
        Assertions.assertEquals(b2.clouds, serializedBoard.getClouds());
        Assertions.assertEquals(b2.mn, serializedBoard.getMn());
        Assertions.assertEquals(b2.schools, serializedBoard.getSchools());
        Assertions.assertEquals(boardAdvanced.getColourToExclude(),serializedBoard.getColourToExclude());
        Assertions.assertEquals(boardAdvanced.getExtractedCards(), serializedBoard.getExtractedCards());
    }
}
