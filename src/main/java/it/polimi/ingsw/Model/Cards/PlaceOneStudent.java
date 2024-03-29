package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Pawns.Student;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This calls represents the card with this effect:
 * in setup this card has 4 students on itself.
 * When a player activates this card, he can choose a student on this card and place it on an archipelago. Then,
 * a new student is extracted from the bag and put on the card.
 */
public class PlaceOneStudent extends AbstractCharacterCard implements Serializable {
    private final List<Student> students;
    private transient final Bag bag;

    /**
     * Constructor of the card. It sets the price and the bag. Also, it extracts 4 student from the bag and place them on
     * the card.
     * @param boardAdvanced The object modified by the card.
     */
    public PlaceOneStudent(BoardAdvanced boardAdvanced) throws StudentNotFoundException {
        super(CharacterCardEnumeration.PLACE_ONE_STUDENT, boardAdvanced,1);
        bag = boardAdvanced.getBag();
        students = bag.extractStudents(4);
    }

    /**
     * @return The list of student that are on the card.
     */
    public List<Student> getStudentsOnCard(){
        return students;
    }

    /**
     * This method activates the effect on the card.
     * @param chosenColour The colour of the student to take from the card and to place on the archipelago.
     * @param archipelago The index of the archipelago where the student needs to be placed.
     * @throws StudentNotFoundException when there are no students of the specified colour on the card.
     */
    public void useEffect(SPColour chosenColour, int archipelago) throws StudentNotFoundException {
        List<Student> s = students.stream().filter(x -> x.getColour().equals(chosenColour)).collect(Collectors.toList());
        Student student;

        if(!s.isEmpty()){
            student = students.remove(students.indexOf(s.get(0)));
            this.boardAdvanced.getArchiList().get(archipelago).addStudent(student);
        }else {
            throw new StudentNotFoundException();
        }

        s = bag.extractStudents(1);
        students.add(s.get(0));
    }

    /**
     * @return String representation of students on the card.
     */
    public String printStudents() {
        return this.students.toString();
    }

    @Override
    public String toString() {
        return "PlaceOneStudent " + this.printPrice();
    }
}
