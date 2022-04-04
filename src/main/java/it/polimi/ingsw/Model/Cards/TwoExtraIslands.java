package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Player;

public class TwoExtraIslands extends AbstractCharacterCard{
    private final BoardAdvanced boardAdvanced;

    public TwoExtraIslands(BoardAdvanced boardAdvanced){
        super(1);

        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect(Player player){
        player.getLastCard().extendMnMovement();
    }
}
