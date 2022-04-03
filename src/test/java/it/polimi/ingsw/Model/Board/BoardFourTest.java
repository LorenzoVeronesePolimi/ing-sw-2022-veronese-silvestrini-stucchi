package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class BoardFourTest {
    BoardFactory boardFactory;
    Player _1 = new Player("player one", PlayerColour.WHITE);
    Player _2 = new Player("player two", PlayerColour.WHITE);
    Player _3 = new Player("player three", PlayerColour.BLACK);
    Player _4 = new Player("player four", PlayerColour.BLACK);
    List<Player> playerList = new ArrayList<>();

    @BeforeEach
    void init() {
        playerList.add(_1);
        playerList.add(_2);
        playerList.add(_3);
        playerList.add(_4);
        boardFactory = new BoardFactory(playerList);
    }

    @Test
    void BoardFourStandardTest() {
        Board boardFour = boardFactory.createBoard();

        // Schools
        Assertions.assertEquals(_1, boardFour.getPlayerSchool(_1).getPlayer());
    }

    @Test
    void BoardTestAdvancedTest() {

    }
}
