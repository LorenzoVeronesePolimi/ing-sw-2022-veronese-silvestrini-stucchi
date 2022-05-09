package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxStudentsDiningRoomException;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxStudentsHallException;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Exceptions.WrongNumberOfStudentsTransferException;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the card that has this effect:
 * the player can exchange up to two students between the one that he has in the dining
 * room and the one that are in the hall.
 */
public class ExchangeTwoHallDining extends AbstractCharacterCard implements Serializable {

    /**
     * Constructor of the card.
     * @param boardAdvanced The object modified by the card.
     */
    public ExchangeTwoHallDining(BoardAdvanced boardAdvanced){
        super(CharacterCardEnumeration.EXCHANGE_TWO_HALL_DINING, boardAdvanced,1);
    }

    /**
     * This method activates the effect of the card.
     * @param player is the player that has activated the card.
     * @param hallStudents is the list of students in the hall that must be moved in the
     *                    dining room
     * @param diningStudents is the list of students in the  dining room that must be
     *                      moved in the hall
     * @throws WrongNumberOfStudentsTransferException if the two lists of students are not of the
     * same length, or if they have more than two students
     * @throws StudentNotFoundException if one student is not present in dining room or hall
     * @throws ExceededMaxStudentsHallException if is not possible to add a student in
     * the hall because it is full
     * @throws ExceededMaxStudentsDiningRoomException if is not possible to add a student in
     *      * the dining room because it is full for that colour
     */
    public void useEffect(Player player, List<SPColour> hallStudents, List<SPColour> diningStudents) throws
            WrongNumberOfStudentsTransferException, StudentNotFoundException, ExceededMaxStudentsHallException,
            ExceededMaxStudentsDiningRoomException {

        if(hallStudents.size()>2 || diningStudents.size()>2) {
            throw new WrongNumberOfStudentsTransferException();
        }

        List<Student> hallToDining = new ArrayList<>();
        for(SPColour colour: hallStudents) {
            hallToDining.add(boardAdvanced.getPlayerSchool(player).removeStudentHall(colour));
        }

        List<Student> diningToHall = new ArrayList<>();
        for(SPColour colour: diningStudents) {
            diningToHall.add(boardAdvanced.getPlayerSchool(player).removeStudentDiningRoom(colour));
        }

        if((hallStudents.size()>2) || (diningStudents.size()>2)) {
            throw new WrongNumberOfStudentsTransferException();
        }

        School currentPlayerSchool = boardAdvanced.getPlayerSchool(player);

        for(Student s: diningToHall) {
            currentPlayerSchool.addStudentHall(s);
        }

        for(Student s: hallToDining) {
            currentPlayerSchool.addStudentDiningRoom(s);
        }
    }

    @Override
    public String toString() {
        return "Exchange Two Hall Dining";
    }
}