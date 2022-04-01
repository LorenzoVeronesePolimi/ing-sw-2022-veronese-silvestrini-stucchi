package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Exceptions.AnotherTowerException;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxTowersException;
import it.polimi.ingsw.Model.Exceptions.InvalidTowerNumberException;
import it.polimi.ingsw.Model.Exceptions.TowerNotFoundException;
import it.polimi.ingsw.Model.Player;

public class FakeMNMovement extends AbstractCharacterCard{
    private final BoardAdvanced boardAdvanced;

    public FakeMNMovement(BoardAdvanced boardAdvanced){
        super(3);

        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect(Player currentPlayer, int fakeMNPosition) throws
            InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException, TowerNotFoundException {

        int mnRealPosition = boardAdvanced.whereIsMotherNature();

        boardAdvanced.moveMotherNature(fakeMNPosition);
        boardAdvanced.tryToConquer(currentPlayer); //if conquerable -> conquer
        boardAdvanced.moveMotherNature(mnRealPosition);  //TODO: assuming normal conquer condition (in mnRealPosition)
    }
}
