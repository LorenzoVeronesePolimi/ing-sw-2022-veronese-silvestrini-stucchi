package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAbstract;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Board.BoardFactory;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TwoExtraIslandsTest {
    @Test
    void TwoExtraIslandTest() {
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
        } catch (ExceededMaxStudentsHallException | StudentNotFoundException | TowerNotFoundException | EmptyCaveauExcepion e) {
            e.printStackTrace();
        }

        TwoExtraIslands card = new TwoExtraIslands(boardAdvanced);
        boardAdvanced.setExtractedCards(card);

        try {
            boardAdvanced.useAssistantCard(p1,3);
            boardAdvanced.useAssistantCard(p1,5);
        } catch (AssistantCardAlreadyPlayedTurnException e) {
            e.printStackTrace();
        } catch (NoAssistantCardException e) {
            e.printStackTrace();
        }

        //TODO: I can't really test this card... is much  more a controller task to assure that the MN movement respect the assistant card constraint
        try {
            p1.useAssistantCard(3);
        } catch (NoAssistantCardException e) {
            e.printStackTrace();
        }


    }
}
