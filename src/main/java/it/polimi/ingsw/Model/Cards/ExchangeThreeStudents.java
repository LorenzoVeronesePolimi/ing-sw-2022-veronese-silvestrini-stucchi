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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExchangeThreeStudents extends AbstractCharacterCard{
    private final BoardAdvanced boardAdvanced;
    private final List<Student> students;

    public ExchangeThreeStudents(BoardAdvanced boardAdvanced) throws StudentNotFoundException {
        super(1);

        Bag bag = boardAdvanced.getBag();
        students = new ArrayList<>(bag.extractStudents(6));
        this.boardAdvanced = boardAdvanced;
    }

    public List<Student> getStudents(){
        return new ArrayList<>(this.students);
    }

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
