package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAbstract;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Board.BoardFactory;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Places.School.SchoolAdvanced;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Assertions;
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
        } catch (ExceededMaxStudentsHallException | StudentNotFoundException | TowerNotFoundException | EmptyCaveauException e) {
            e.printStackTrace();
        }

        List<Integer> usedCards = new ArrayList<>();

        TwoExtraIslands card = new TwoExtraIslands();
        Assertions.assertEquals("TwoExtraIslands [c. " + 1 + "]", card.toString());

        assert boardAdvanced != null;
        boardAdvanced.setExtractedCards(card);

        Assertions.assertEquals(card, boardAdvanced.getExtractedCards().get(0));

        try {
            boardAdvanced.useAssistantCard(usedCards, p1,3);
        } catch (AssistantCardAlreadyPlayedTurnException | NoAssistantCardException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(2, p1.getLastCard().getMotherNatureMovement());

        try {
            boardAdvanced.useTwoExtraIslands(p1, 0);
        } catch (EmptyCaveauException | ExceededMaxNumCoinException | CoinNotFoundException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(0,((SchoolAdvanced)boardAdvanced.getPlayerSchool(p1)).getNumCoins());
        Assertions.assertEquals(4,p1.getLastCard().getMotherNatureMovement());

    }
}
