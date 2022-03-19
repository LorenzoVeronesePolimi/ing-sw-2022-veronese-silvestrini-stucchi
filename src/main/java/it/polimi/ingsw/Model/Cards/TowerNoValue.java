package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Player;

public class TowerNoValue extends AbstractCharacterCard{
    private BoardAdvanced boardAdvanced;

    public TowerNoValue(BoardAdvanced boardAdvanced){
        super(3);

        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect(Player player) {
        boardAdvanced.getArchiList().get(boardAdvanced.getMotherNaturePosition()).setTowerNoValueFlag(true);

        boardAdvanced.checkIfConquerable(player);

        updatePrice();
    }

    @Override
    public void update(BoardAdvanced boardAdvanced) {
        this.boardAdvanced = boardAdvanced;
    }
}
