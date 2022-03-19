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
        int mnRealPosition = boardAdvanced.getMotherNaturePosition();

        boardAdvanced.moveMotherNature(fakeMNPosition);
        boardAdvanced.checkIfConquerable(currentPlayer); //if conquerable -> conquer
        boardAdvanced.moveMotherNature(mnRealPosition);  //TODO: assuming normal conquer condition (in mnRealPosition)

        updatePrice();
    }

    @Override
    public void update(BoardAdvanced boardAdvanced) {
        this.boardAdvanced = boardAdvanced;
    }
}
