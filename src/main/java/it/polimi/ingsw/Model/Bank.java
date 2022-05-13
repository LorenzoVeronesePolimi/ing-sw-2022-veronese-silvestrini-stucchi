package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Exceptions.EmptyCaveauException;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxNumCoinException;
import it.polimi.ingsw.Model.Pawns.Coin;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as a repository of coins. Every coin of the game is created in (and then taken from)
 * this class.
 */
public class Bank {
    private final List<Coin> caveau;
    private final int MAXNUMCOIN=20;

    /**
     * The constructor creates 20 coins (no other coin can be created elsewhere).
     */
    public Bank(){
        caveau = new ArrayList<>();
        for(int i=0; i<MAXNUMCOIN ;i++){
            caveau.add(new Coin());
        }
    }

    public Bank(Bank toCopy){
        caveau = new ArrayList<>();
        caveau.addAll(toCopy.getCaveau());
    }

    protected List<Coin> getCaveau(){
        return this.caveau;
    }

    /**
     *
     * @param coin represents the coin that the caller wants to re-put in the bank
     * @throws ExceededMaxNumCoinException when the bank is full (caveau already has 20 coins)
     */
    public void addCoin(Coin coin) throws ExceededMaxNumCoinException {
        if(caveau.size()<MAXNUMCOIN) {
            caveau.add(coin);
        }
        else{
            throw  new ExceededMaxNumCoinException();
        }
    }

    /**
     *
     * @return a coin from the caveau to the caller
     * @throws EmptyCaveauException when there are no coins in the caveau
     */
    public Coin getCoin() throws EmptyCaveauException {
        if(caveau.size()>0) {
            return caveau.remove(0);
        }
        throw new EmptyCaveauException();
    }
}
