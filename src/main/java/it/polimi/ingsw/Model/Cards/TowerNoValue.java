package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;

public class TowerNoValue extends AbstractCharacterCard {
    private BoardAdvanced boardAdvanced;

    public TowerNoValue(BoardAdvanced boardAdvanced) {
        super(3);

        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect() {
        boardAdvanced.getArchiList().get(boardAdvanced.whereIsMotherNature()).setTowerNoValueFlag(true);

        boardAdvanced.tryToConquer();

        updatePrice();
    }
}
