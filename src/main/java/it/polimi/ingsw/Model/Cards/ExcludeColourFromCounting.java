package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;

public class ExcludeColourFromCounting extends AbstractCharacterCard{
    private BoardAdvanced boardAdvanced;

    public ExcludeColourFromCounting(BoardAdvanced boardAdvanced){
        super(3);
        this.boardAdvanced = boardAdvanced;
    }

    public void update(BoardAdvanced boardAdvanced){};
}
