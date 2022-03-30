package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Exceptions.ImpossibleMNMove;
import it.polimi.ingsw.Model.Player;

public class TwoExtraIslands extends AbstractCharacterCard{
    private BoardAdvanced boardAdvanced;

    public TwoExtraIslands(BoardAdvanced boardAdvanced){
        super(1);

        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect(Player currentPlayer, int archipelago) throws ImpossibleMNMove {
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
            boardAdvanced.tryToConquer(currentPlayer); //TODO: added Player parameter
        else
            throw new ImpossibleMNMove();
    }

    private int checkMovement(int startingPoint) {
        if(startingPoint > 11) {
            startingPoint = startingPoint % this.boardAdvanced.getArchiList().size() - 1;
        }

        return startingPoint;
    }
}
