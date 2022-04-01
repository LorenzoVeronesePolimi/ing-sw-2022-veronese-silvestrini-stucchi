package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Exceptions.AnotherTowerException;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxTowersException;
import it.polimi.ingsw.Model.Exceptions.InvalidTowerNumberException;
import it.polimi.ingsw.Model.Exceptions.TowerNotFoundException;
import it.polimi.ingsw.Model.Player;

public class TwoExtraPoints extends AbstractCharacterCard{
    private final BoardAdvanced boardAdvanced;

    public TwoExtraPoints(BoardAdvanced boardAdvanced){
        super(2);
        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect(Player currentPlayer) throws InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException, TowerNotFoundException {
        boardAdvanced.setTwoExtraPointsFlag(true);

        boardAdvanced.tryToConquer(currentPlayer);
    }
}
