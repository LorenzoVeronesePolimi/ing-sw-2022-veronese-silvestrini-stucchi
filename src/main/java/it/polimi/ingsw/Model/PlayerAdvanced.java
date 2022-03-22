package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Pawns.Coin;

public class PlayerAdvanced extends Player{
    private Player player;
    private Coin coin;

    public PlayerAdvanced(Player p){
        super(p.getNickname(), p.getColour());
    }
    public void addCoin(){

    }
}