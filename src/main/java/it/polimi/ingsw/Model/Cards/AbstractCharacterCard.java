package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Pawns.Coin;

import java.util.ArrayList;
import java.util.List;

/**
 * this class models the Character Card entity, for games in advanced mode
 */

public abstract class AbstractCharacterCard {
    private final int basePrice;
    private final List<Coin> addedPrice;

    /**
     * Constructor
     * @param price is the initial cost of the card
     */
    protected AbstractCharacterCard(int price) {
        basePrice = price;
        addedPrice = new ArrayList<>();
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
}
