package it.polimi.ingsw.Model.Places.School;

import it.polimi.ingsw.Model.Exceptions.CoinNotFoundException;
import it.polimi.ingsw.Model.Pawns.Coin;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This class extends the School class, adding methods that manage the coin aspect of the Advanced mode.
 */
public class SchoolAdvanced extends School{
    private final List<Coin> coins = new ArrayList<>();

    /**
     * The constructor created a schoolAdvanced by creating a normal School.
     * @param player is the player that is associated to the school
     * @param numStudentsHall is the maximum number of students that can stay in the hall (depends on the
     *                        number of players of the game)
     * @param numTowers is the number of towers that a player owns (depends on the number of players of the game)
     */
    public SchoolAdvanced(Player player, int numStudentsHall, int numTowers) {
        super(player, numStudentsHall, numTowers);
    }

    /**
     * This method adds the coin that is giver as parameter to the school list of coins
     * @param coin that must be added
     */
    public void addCoin(Coin coin){
        coins.add(coin);
    }

    /**
     * This method removes a coi from the coin list of the school
     * @return the coin that has been removed
     * @throws CoinNotFoundException if the list of coin of the school is already empty
     */
    public Coin removeCoin() throws CoinNotFoundException {
        if(coins.size()>0) {
            Coin removed = coins.get(0);
            coins.remove(removed);
            return removed;
        }
        throw  new CoinNotFoundException();
    }

    /**
     *
     * @return the number of coins that the school owns
     */
    public int getNumCoins(){
        return coins.size();
    }

}
