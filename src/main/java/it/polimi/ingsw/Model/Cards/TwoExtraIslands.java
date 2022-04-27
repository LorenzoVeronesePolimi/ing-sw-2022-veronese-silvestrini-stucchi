package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Player;

/**
 * This class represents the card with this effect:
 * the player can move Mother Nature up to two additional archipelagos than is indicated in the assistant card played.
 */
public class TwoExtraIslands extends AbstractCharacterCard{

    /**
     * Constructor of the card. It sets the price.
     */
    public TwoExtraIslands(CharacterCardEnumeration type){
        super(type,1);
    }

    /**
     * This method activates the effect of the card.
     * @param player The current player that has played the card.
     */
    public void useEffect(Player player){
        player.getLastCard().extendMnMovement();
    }
}
