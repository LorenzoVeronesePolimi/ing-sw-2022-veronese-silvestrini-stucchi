package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Player;

public class TwoExtraIslands extends AbstractCharacterCard{

    public TwoExtraIslands(){
        super(1);
    }

    public void useEffect(Player player){
        player.getLastCard().extendMnMovement();
    }
}
