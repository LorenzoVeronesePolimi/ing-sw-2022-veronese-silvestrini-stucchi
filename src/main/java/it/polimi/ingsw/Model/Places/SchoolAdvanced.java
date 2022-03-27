package it.polimi.ingsw.Model.Places;

import it.polimi.ingsw.Model.Pawns.Coin;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;

public class SchoolAdvanced extends School{
    private School school;
    private List<Coin> coins = new ArrayList<>();

    public SchoolAdvanced(Player player, int numStudentsHall, int numTowers) {
        super(player, numStudentsHall, numTowers);
        Coin coin = new Coin();
        coins.add(coin);
    }

    public void addCoin(Coin coin){
        coins.add(coin);
    }
    public Coin removeCoin(){
        Coin removed=coins.get(0);
        coins.remove(removed);
        return removed;
    }
    public int getNumCoins(){
        return coins.size();
    }
}
