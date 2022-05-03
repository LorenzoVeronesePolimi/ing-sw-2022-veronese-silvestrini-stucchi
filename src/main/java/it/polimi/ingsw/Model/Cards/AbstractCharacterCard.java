package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import it.polimi.ingsw.Model.Pawns.Coin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * this class models the Character Card entity, for games in advanced mode
 */

public abstract class AbstractCharacterCard implements Serializable {
    private final CharacterCardEnumeration type;
    private final int basePrice;
    private final List<Coin> addedPrice;
    protected final BoardAdvanced boardAdvanced;

    /**
     * Constructor
     * @param type
     * @param boardAdvanced
     * @param price is the initial cost of the card
     */
    protected AbstractCharacterCard(CharacterCardEnumeration type, BoardAdvanced boardAdvanced, int price) {
        this.type = type;
        this.boardAdvanced = boardAdvanced;
        this.basePrice = price;
        this.addedPrice = new ArrayList<>();
    }

    /**
     * This method will add a coin on the card every time it is used
     * @param coin
     */
    public void updatePrice(Coin coin){
        addedPrice.add(coin);
    }

    /**
     *
     * @return the current price of the card.
     */
    public int getCurrentPrice(){
        return basePrice + addedPrice.size();
    }

    /**
     * @return The enumeration type of the card.
     */
    public CharacterCardEnumeration getType() {
        return type;
    }
}
