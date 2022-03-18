package it.polimi.ingsw.Model.Cards;

public abstract class AbstractCharacterCard {
    private  int basePrice;
    private int numUsed=0;

    protected AbstractCharacterCard(int price) {
        basePrice = price;
    }

    public int getCurrentPrice(){
        return basePrice + numUsed;
    }
}
