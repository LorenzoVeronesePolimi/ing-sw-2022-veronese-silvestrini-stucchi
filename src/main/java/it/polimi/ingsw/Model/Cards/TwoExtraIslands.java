package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Exceptions.AnotherTowerException;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxTowersException;
import it.polimi.ingsw.Model.Exceptions.ImpossibleMNMove;
import it.polimi.ingsw.Model.Exceptions.InvalidTowerNumberException;
import it.polimi.ingsw.Model.Player;

public class TwoExtraIslands extends AbstractCharacterCard{
    private final BoardAdvanced boardAdvanced;

    public TwoExtraIslands(BoardAdvanced boardAdvanced){
        super(1);

        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect(Player currentPlayer, int archipelago) throws ImpossibleMNMove, InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException {
        //MN initial position
        int startingPoint = boardAdvanced.whereIsMotherNature();
        boolean hasChanged = false;

        for(int i = 0; i <= 2; i++) {
            startingPoint += i;
            startingPoint = checkMovement(startingPoint);

            if(archipelago == startingPoint) {
                boardAdvanced.moveMotherNature(archipelago);
                hasChanged = true;
            }
        }

        if(hasChanged)
            boardAdvanced.tryToConquer(currentPlayer);
        else
            throw new ImpossibleMNMove();
    }

    private int checkMovement(int startingPoint) {
        if(startingPoint > this.boardAdvanced.getArchiList().size() - 1) {
            startingPoint = startingPoint % this.boardAdvanced.getArchiList().size() - 1;
        }

        return startingPoint;
    }
}
