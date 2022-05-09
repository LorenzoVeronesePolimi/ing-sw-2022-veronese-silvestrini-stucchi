package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import it.polimi.ingsw.Model.Exceptions.ExceededNumberForbidFlagException;

import java.io.Serializable;

/**
 * This class represents the card with this effect:
 * in setup, 4 no Entry tiles are placed on the card.
 */
public class ForbidIsland extends AbstractCharacterCard implements Serializable {

    /**
     * Constructor of the card. It sets the price.
     * @param boardAdvanced The object modified by the card.
     */
    public ForbidIsland(BoardAdvanced boardAdvanced){
        super(CharacterCardEnumeration.FORBID_ISLAND, boardAdvanced,2);
    }

    /**
     * This method activates the effect of the card.
     * @param archipelago The archipelago where to put the No Entry tiles.
     * @throws ExceededNumberForbidFlagException When there are already 4 No Entry tiles on an archipelago.
     */
    public void useEffect(int archipelago) throws ExceededNumberForbidFlagException {
        if(boardAdvanced.getArchipelago(archipelago).getForbidFlag() < 4)
            boardAdvanced.getArchipelago(archipelago).addForbidFlag();
        else
            throw new ExceededNumberForbidFlagException();
    }

    @Override
    public String toString() {
        return "Forbid Island";
    }
}
