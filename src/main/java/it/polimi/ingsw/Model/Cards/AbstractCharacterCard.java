package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;

/*
* card called after Mother Nature movement!
 */

public abstract class AbstractCharacterCard {
    private  int basePrice;
    private int numUsed=0;

    protected AbstractCharacterCard(int price) {
        basePrice = price;
    }

    protected void updatePrice(){
        numUsed++;
    }

    public abstract void update(BoardAdvanced boardAdvanced);

    public int getCurrentPrice(){
        return basePrice + numUsed;
    }
}
