package it.polimi.ingsw.Model.Places.School;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.CoinNotFoundException;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxStudentsDiningRoomException;
import it.polimi.ingsw.Model.Pawns.Coin;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;

public class SchoolAdvanced extends School{
    private List<Coin> coins = new ArrayList<>();

    public SchoolAdvanced(Player player, int numStudentsHall, int numTowers) {
        super(player, numStudentsHall, numTowers);
    }

    public void addCoin(Coin coin){
        coins.add(coin);
    }

    public Coin removeCoin() throws CoinNotFoundException {
        if(coins.size()>0) {
            Coin removed = coins.get(0);
            coins.remove(removed);
            return removed;
        }
        throw  new CoinNotFoundException();
    }

    public int getNumCoins(){
        return coins.size();
    }

}
