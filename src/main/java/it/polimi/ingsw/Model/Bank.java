package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Exceptions.EmptyCaveauExcepion;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxNumCoinException;
import it.polimi.ingsw.Model.Pawns.Coin;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Coin> caveau;
    private final int MAXNUMCOIN=20;

    public Bank(){
        caveau = new ArrayList<>();
        for(int i=0; i<MAXNUMCOIN ;i++){
            caveau.add(new Coin());
        }
    }
    public void addCoin(Coin coin) throws ExceededMaxNumCoinException {
        if(caveau.size()<MAXNUMCOIN) {
            caveau.add(coin);
        }
        else{
            throw  new ExceededMaxNumCoinException();
        }
    }
    public Coin getCoin() throws EmptyCaveauExcepion {
        if(caveau.size()>0) {
            return caveau.remove(0);
        }
        throw new EmptyCaveauExcepion();
    }
}
