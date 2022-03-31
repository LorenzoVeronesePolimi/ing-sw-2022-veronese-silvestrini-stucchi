package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.AnotherTowerException;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxTowersException;
import it.polimi.ingsw.Model.Exceptions.InvalidTowerNumberException;
import it.polimi.ingsw.Model.Player;

public class ExcludeColourFromCounting extends AbstractCharacterCard{
    private final BoardAdvanced boardAdvanced;

    public ExcludeColourFromCounting(BoardAdvanced boardAdvanced){
        super(3);
        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect(Player currentPlayer, SPColour colourToExclude) throws
            InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException {

        boardAdvanced.setColourToExclude(colourToExclude);
        boardAdvanced.tryToConquer(currentPlayer);
    }
}
