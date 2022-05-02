package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxStudentsHallException;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Exceptions.WrongNumberOfStudentsTransferException;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the card that has this effect:
 * the player can exchange up to three students between the one that he has in the hall and the one that
 * are in placed on the card.
 */
public class ExchangeThreeStudents extends AbstractCharacterCard implements Serializable {
    private final List<Student> students;

    /**
     * Constructor of the card.
     * @param boardAdvanced The object modified by the card.
     */
    public ExchangeThreeStudents(BoardAdvanced boardAdvanced) throws StudentNotFoundException {
        super(CharacterCardEnumeration.EXCHANGE_THREE_STUDENTS, boardAdvanced, 1);

        Bag bag = boardAdvanced.getBag();
        students = new ArrayList<>(bag.extractStudents(6));
    }

    /**
     *
     * @return a copy of the list of students that are on the card at the moment
     */
    public List<Student> getStudentsOnCard(){
        return new ArrayList<>(this.students);
    }

    /**
     * This method activates the effect of the card.
     * @param player is the player that activates the card
     * @param hallStudents is the list of students that are in the player's hall that he wants to exchange
     * @param exchangeStudents is the list of students that are on the card that the players wants to exchange
     * @throws WrongNumberOfStudentsTransferException if the players tries to exchange more than three students
     * or gives two lists of students of different size
     * @throws StudentNotFoundException if is there is not a student of the requested colour in the hall or
     * on the card
     * @throws ExceededMaxStudentsHallException if players tries to place in the hall more students
     * than what he is allowed
     */
    public void useEffect(Player player, List<SPColour> hallStudents, List<SPColour> exchangeStudents) throws
            WrongNumberOfStudentsTransferException, StudentNotFoundException, ExceededMaxStudentsHallException {

        if(hallStudents.size() > 3 || exchangeStudents.size() > 3 || hallStudents.size()!= exchangeStudents.size()) {
            throw new WrongNumberOfStudentsTransferException();
        }

        List<Student> hallToCard = new ArrayList<>();
        for(SPColour colour: hallStudents) {
            hallToCard.add(boardAdvanced.getPlayerSchool(player).removeStudentHall(colour));
        }

        List<Student> cardToHall = new ArrayList<>();
        for(SPColour colour: exchangeStudents) {
            List<Student> temp = students.stream().filter(x -> x.getColour().equals(colour)).collect(Collectors.toList());

            if(!temp.isEmpty()) {
                cardToHall.add(students.remove(students.indexOf(temp.get(0))));
            }
        }

        if(hallToCard.size() > 3 || cardToHall.size() > 3) {
            throw new WrongNumberOfStudentsTransferException();
        }

        School currentPlayerSchool = boardAdvanced.getPlayerSchool(player);

        for(Student s: cardToHall) {
            currentPlayerSchool.addStudentHall(s);
        }

        students.addAll(hallToCard);
    }
}
