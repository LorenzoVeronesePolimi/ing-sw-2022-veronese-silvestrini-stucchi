package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;

import java.io.Serializable;

/**
 * This class represents the card with this effect:
 * during the influence calculation, the current player has two additional point given.
 */
public class TwoExtraPoints extends AbstractCharacterCard implements Serializable {
    private final BoardAdvanced boardAdvanced;

    /**
     * Constructor of the card. It sets the price.
     * @param boardAdvanced The object modified by the card.
     */
    public TwoExtraPoints(CharacterCardEnumeration type, BoardAdvanced boardAdvanced){
        super(type,2);
        this.boardAdvanced = boardAdvanced;
    }

    /**
     * This method applies the effect to the board, giving two additional points to the player.
     */
    public void useEffect() {
        boardAdvanced.setTwoExtraPointsFlag(true);
    }
}
