package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Player;

public class TwoExtraPoints extends AbstractCharacterCard{
    private BoardAdvanced boardAdvanced;

    public TwoExtraPoints(BoardAdvanced boardAdvanced){
        super(2);
        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect(Player currentPlayer) {
        boardAdvanced.setTwoExtraPointsFlag(true);

        boardAdvanced.tryToConquer(currentPlayer); //TODO: added Player parameter
    }
}
