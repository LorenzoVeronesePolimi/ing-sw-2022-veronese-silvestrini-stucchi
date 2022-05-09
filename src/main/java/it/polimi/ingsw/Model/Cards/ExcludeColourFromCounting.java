package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import it.polimi.ingsw.Model.Enumerations.SPColour;

import java.io.Serializable;

/**
 * This class represents the card that has this effect:
 * the player chooses a colour and, for that turn, during the influence computation, that
 * colour won't be considered
 */
public class ExcludeColourFromCounting extends AbstractCharacterCard implements Serializable {

    /**
     * Constructor of the card.
     * @param boardAdvanced The object modified by the card.
     */
    public ExcludeColourFromCounting(BoardAdvanced boardAdvanced){
        super(CharacterCardEnumeration.EXCLUDE_COLOUR_FROM_COUNTING, boardAdvanced, 3);
    }

    /**
     * This method activates the effect of the card.
     * @param colourToExclude the colour tha the player does want to exclude
     *
     */
    public void useEffect(SPColour colourToExclude){
        boardAdvanced.setColourToExclude(colourToExclude);
    }

    @Override
    public String toString() {
        return "Exclude Colour From Counting";
    }
}
