package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Exceptions.EmptyCaveauExcepion;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxNumCoinException;
import it.polimi.ingsw.Model.Pawns.Coin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class BankTest {
    @Test
    void EmptyCaveauExceptionTest(){
        Bank bank = new Bank();
        List<Coin> withdrawn = new ArrayList<>();

        for(int i=0; i<20; i++) {
            try {
                withdrawn.add(bank.getCoin());
            } catch (EmptyCaveauExcepion e) {
                e.printStackTrace();
            }
        }

        Assertions.assertThrows(EmptyCaveauExcepion.class, () -> withdrawn.add(bank.getCoin()));
    }

    @Test
    void ExceededMaxNumCoinException(){
        Bank bank = new Bank();
        Coin coin = new Coin();

        Assertions.assertThrows(ExceededMaxNumCoinException.class,() -> bank.addCoin(coin));
    }
}
