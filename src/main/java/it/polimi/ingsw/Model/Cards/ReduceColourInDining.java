package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Places.School.School;

import java.io.Serializable;

/**
 * This class represents the card with this effect:
 * a colour of students is chosen. Every player has to put in the bag 3 students of that colour from the dining room.
 * If a player has 2 or fewer students, he will put in the bag the ones he has.
 */
public class ReduceColourInDining extends AbstractCharacterCard implements Serializable {
    private transient final Bag bag;

    /**
     * Constructor of the card. It sets the price and the bag.
     * @param boardAdvanced The object modified by the card.
     */
    public ReduceColourInDining(BoardAdvanced boardAdvanced){
        super(CharacterCardEnumeration.REDUCE_COLOUR_IN_DINING, boardAdvanced,3);
        bag = boardAdvanced.getBag();
    }

    /**
     * This method activates the effect of the card.
     * @param colour The chosen colour of students.
     * @throws StudentNotFoundException When there are no students in the dining room.
     */
    public void useEffect(SPColour colour) throws StudentNotFoundException {
        for(School s: boardAdvanced.getSchools()){
            for(int i=0; i<3; i++){
                if(s.getNumStudentColour(colour)>0) {
                    bag.putStudent(s.removeStudentDiningRoom(colour));
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Reduce Colour In Dining";
    }
}
