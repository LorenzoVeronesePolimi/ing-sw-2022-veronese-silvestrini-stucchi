package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Player;

public class ExcludeColourFromCounting extends AbstractCharacterCard{
    private BoardAdvanced boardAdvanced;

    public ExcludeColourFromCounting(BoardAdvanced boardAdvanced){
        super(3);
        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect(Player currentPlayer, SPColour colourToExclude){ //TODO: added Player parameter
        boardAdvanced.setColourToExclude(colourToExclude);
        boardAdvanced.tryToConquer(currentPlayer);
    }
}
