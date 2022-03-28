package it.polimi.ingsw.Model.Exceptions;

public class CoinNotFoundException extends Exception{
    public CoinNotFoundException(){
        System.out.println("Coin not found");
    }
}
