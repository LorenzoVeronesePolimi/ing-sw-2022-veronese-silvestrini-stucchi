package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Player;

public class FakeMNMovement extends AbstractCharacterCard{
    private BoardAdvanced boardAdvanced;

    public FakeMNMovement(BoardAdvanced boardAdvanced){
        super(3);

        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect(Player currentPlayer, int fakeMNPosition) {
        int mnRealPosition = boardAdvanced.whereIsMotherNature();

        boardAdvanced.moveMotherNature(fakeMNPosition);
        boardAdvanced.tryToConquer(currentPlayer); //if conquerable -> conquer //TODO: added Player parameter
        boardAdvanced.moveMotherNature(mnRealPosition);  //TODO: assuming normal conquer condition (in mnRealPosition)
    }
}
