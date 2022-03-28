package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Pawns.Coin;

import java.util.ArrayList;
import java.util.List;

/*
* card called after Mother Nature movement!
 */

public abstract class AbstractCharacterCard {
    private int basePrice;
    private List<Coin> addedPrice;

    protected AbstractCharacterCard(int price) {
        basePrice = price;
        addedPrice = new ArrayList<>();
    }

    public void updatePrice(Coin coin){
        addedPrice.add(coin);
    }

    public int getCurrentPrice(){
        return basePrice + addedPrice.size();
    }
}
