package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxStudentsDiningRoomException;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the card that has this effect:
 * the player chooses one student from the four that are on the card, and places it in
 * his dining Room, then a student is extracted from the bag and added to the card .
 */
public class ExtraStudentInDining extends AbstractCharacterCard implements Serializable {
    final transient Bag bag;
    private final List<Student> students;

    /**
     * Constructor of the card.
     * @param boardAdvanced The object modified by the card.
     * @throws StudentNotFoundException when there are not enough students in the bag, so
     * it is not possible to extract four of them.
     */
    public ExtraStudentInDining(BoardAdvanced boardAdvanced) throws StudentNotFoundException {
        super(CharacterCardEnumeration.EXTRA_STUDENT_IN_DINING, boardAdvanced,2);
        bag=boardAdvanced.getBag();
        students = new ArrayList<>(bag.extractStudents(4));
    }

    /**
     *
     * @return the list of Students from which the player can choose.
     */
    public List<Student> getStudentsOnCard(){
        return students;
    }

    /**
     * This method activates the effect of the card.
     * @param currentPlayer the player that has activated the card.
     * @param cardToDining the colour that the player wants the student to put in his
     *                     dining Room to be.
     * @throws StudentNotFoundException when there are no students of the desired colour
     * on the card
     * @throws ExceededMaxStudentsDiningRoomException when is not possible to place a
     * student of that colour in the dining Room because there are already 10 other students
     * of that same colour
     */
    public void useEffect(Player currentPlayer, SPColour cardToDining) throws StudentNotFoundException, ExceededMaxStudentsDiningRoomException {
        School school = boardAdvanced.getPlayerSchool(currentPlayer);
        List<Student> s = students.stream().filter(x -> x.getColour().equals(cardToDining)).collect(Collectors.toList());
        List<Student> student;
        if(!s.isEmpty()){
            school.addStudentDiningRoom(students.remove(students.indexOf(s.get(0))));
        }else {
            throw new StudentNotFoundException();
        }
        student = bag.extractStudents(1);
        students.add(student.get(0));
    }

    @Override
    public String toString() {
        return "Extra Student In Dining";
    }
}
