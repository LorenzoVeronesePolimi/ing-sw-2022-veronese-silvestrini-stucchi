package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;

import java.io.Serializable;

/**
 * This class represents the card with this effect:
 * when resolving a conquering on an archipelago, towers do not count towards influence.
 */
public class TowerNoValue extends AbstractCharacterCard implements Serializable {
    private final BoardAdvanced boardAdvanced;

    /**
     * Constructor of the card. It sets the price.
     * @param boardAdvanced The object modified by the card.
     */
    public TowerNoValue(CharacterCardEnumeration type, BoardAdvanced boardAdvanced) {
        super(type,3);

        this.boardAdvanced = boardAdvanced;
    }

    /**
     * This method activates the effect of the card.
     */
    public void useEffect() {
        boardAdvanced.getArchiList().get(boardAdvanced.whereIsMotherNature()).setTowerNoValueFlag(true);
    }
}
