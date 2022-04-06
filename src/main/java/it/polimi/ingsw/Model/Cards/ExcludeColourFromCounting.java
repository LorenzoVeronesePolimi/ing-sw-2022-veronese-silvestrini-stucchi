package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.AnotherTowerException;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxTowersException;
import it.polimi.ingsw.Model.Exceptions.InvalidTowerNumberException;
import it.polimi.ingsw.Model.Exceptions.TowerNotFoundException;
import it.polimi.ingsw.Model.Player;

/**
 * This class represents the card that has this effect:
 * the player chooses a colour and, for that turn, during the influence computation, that
 * colour won't be considered
 */
public class ExcludeColourFromCounting extends AbstractCharacterCard{
    private final BoardAdvanced boardAdvanced;

    /**
     * Constructor of the card.
     * @param boardAdvanced The object modified by the card.
     */
    public ExcludeColourFromCounting(BoardAdvanced boardAdvanced){
        super(3);
        this.boardAdvanced = boardAdvanced;
    }

    /**
     * This method activates the effect of the card.
     * @param colourToExclude the colour tha the player does want to exclude
     *
     */
    public void useEffect(SPColour colourToExclude){

        boardAdvanced.setColourToExclude(colourToExclude);
    }
}
