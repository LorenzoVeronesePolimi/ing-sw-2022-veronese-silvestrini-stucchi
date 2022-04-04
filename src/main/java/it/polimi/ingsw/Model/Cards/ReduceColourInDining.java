package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Places.School.School;

public class ReduceColourInDining extends AbstractCharacterCard{
    final BoardAdvanced boardAdvanced;
    final Bag bag;
    public ReduceColourInDining(BoardAdvanced boardAdvanced){
        super(3);
        bag=boardAdvanced.getBag();
        this.boardAdvanced=boardAdvanced;
    }
    public void useEffect(SPColour colour) throws StudentNotFoundException {
        for(School s: boardAdvanced.getSchools()){
            for(int i=0; i<3; i++){
                if(s.getNumStudentColour(colour)>0) {
                    bag.putStudent(s.removeStudentDiningRoom(colour));
                }
            }
        }
    }
}
