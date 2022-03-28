package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;

public class TwoExtraPoints extends AbstractCharacterCard{
    private BoardAdvanced boardAdvanced;

    public TwoExtraPoints(BoardAdvanced boardAdvanced){
        super(2);
        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect() {
        boardAdvanced.setTwoExtraPointsFlag(true);

        boardAdvanced.tryToConquer();
    }
}
