package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Exceptions.AnotherTowerException;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxTowersException;
import it.polimi.ingsw.Model.Exceptions.InvalidTowerNumberException;
import it.polimi.ingsw.Model.Exceptions.TowerNotFoundException;
import it.polimi.ingsw.Model.Player;

public class TowerNoValue extends AbstractCharacterCard {
    private final BoardAdvanced boardAdvanced;

    public TowerNoValue(BoardAdvanced boardAdvanced) {
        super(3);

        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect(Player currentPlayer) throws InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException, TowerNotFoundException {
        boardAdvanced.getArchiList().get(boardAdvanced.whereIsMotherNature()).setTowerNoValueFlag(true);

        boardAdvanced.tryToConquer(currentPlayer);
    }
}
