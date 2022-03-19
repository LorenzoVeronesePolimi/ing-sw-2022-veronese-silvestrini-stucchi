package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;

public class ForbidIsland extends AbstractCharacterCard{
    private BoardAdvanced boardAdvanced;

    public ForbidIsland(BoardAdvanced boardAdvanced){
        super(2);
        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect(int archipelago) {
        boardAdvanced.getArchiList().get(archipelago).setForbidFlag(true);

        updatePrice();
    }

    @Override
    public void update(BoardAdvanced boardAdvanced) {
        this.boardAdvanced = boardAdvanced;
    }
}
