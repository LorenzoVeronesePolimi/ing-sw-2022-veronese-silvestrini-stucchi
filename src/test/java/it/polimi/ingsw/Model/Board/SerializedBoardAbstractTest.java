package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Exceptions.AssistantCardAlreadyPlayedTurnException;
import it.polimi.ingsw.Model.Exceptions.NoAssistantCardException;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class SerializedBoardAbstractTest {
    List<Player> playerList = null;
    BoardAbstract b2;
    Player p1 = new Player("player one", PlayerColour.BLACK);
    Player p2 = new Player("player two", PlayerColour.WHITE);

    @BeforeEach
    void init() {
        playerList = new ArrayList<>();

        playerList.add(p1);
        playerList.add(p2);
        //create board factory
        BoardFactory bf = new BoardFactory(playerList);
        //test for BoardTwo
        b2 = bf.createBoard();
        //b3 = new BoardThree(playerList);

    }
    @Test
    public void SerializedBoardAbstractTests(){
        SerializedBoardAbstract serializedBoard = new SerializedBoardAbstract(b2.archipelagos, b2.clouds, b2.mn, b2.schools);
        Assertions.assertEquals(b2.archipelagos, serializedBoard.getArchipelagos());
        Assertions.assertEquals(b2.clouds, serializedBoard.getClouds());
        Assertions.assertEquals(b2.mn, serializedBoard.getMn());
        Assertions.assertEquals(b2.schools, serializedBoard.getSchools());
        List<Integer> usedCards = new ArrayList<>();

        serializedBoard.setCurrentState(State.ACTION1);
        Assertions.assertEquals(State.ACTION1, serializedBoard.getCurrentState());
        serializedBoard.setCurrentState(State.ACTION2);
        Assertions.assertEquals(State.ACTION2, serializedBoard.getCurrentState());
        serializedBoard.setCurrentState(State.END);
        Assertions.assertEquals(State.END, serializedBoard.getCurrentState());

        serializedBoard.setCurrentPlayer(playerList.get(0));
        Assertions.assertEquals(playerList.get(0), serializedBoard.getCurrentPlayer());
        serializedBoard.setCurrentPlayer(playerList.get(1));
        Assertions.assertEquals(playerList.get(1), serializedBoard.getCurrentPlayer());

        serializedBoard.setNicknameWinner(playerList.get(0).getNickname());
        Assertions.assertEquals(playerList.get(0).getNickname(), serializedBoard.getNicknameWinner());
        serializedBoard.setNicknameWinner(playerList.get(1).getNickname());
        Assertions.assertEquals(playerList.get(1).getNickname(), serializedBoard.getNicknameWinner());

        serializedBoard.setSitPlayers(playerList);
        Assertions.assertEquals(playerList, serializedBoard.getSitPlayers());

        try {
            b2.useAssistantCard(usedCards, p1, 1);
        } catch (AssistantCardAlreadyPlayedTurnException | NoAssistantCardException e) {
            e.printStackTrace();
        }

        SerializedBoardAbstract serializedBoard2 = new SerializedBoardAbstract(b2.archipelagos, b2.clouds, b2.mn, b2.schools, p2.getNickname());

        if(serializedBoard2.getSchools().get(0).getPlayer().getNickname().equals(p1.getNickname())) {
            Assertions.assertNotEquals(b2.getPlayerSchool(p1), serializedBoard2.getSchools().get(0));
            Assertions.assertEquals(serializedBoard2.getSchools().get(1).getPlayer().getNickname(), p2.getNickname());
        }
        else {
            Assertions.assertNotEquals(b2.getPlayerSchool(p1), serializedBoard2.getSchools().get(1));
            Assertions.assertEquals(serializedBoard2.getSchools().get(0).getPlayer().getNickname(), p2.getNickname());
        }

    }
}
