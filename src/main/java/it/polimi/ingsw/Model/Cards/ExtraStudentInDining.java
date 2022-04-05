package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxStudentsDiningRoomException;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExtraStudentInDining extends AbstractCharacterCard{
    final BoardAdvanced boardAdvanced;
    final Bag bag;
    private List<Student> students;

    public ExtraStudentInDining(BoardAdvanced boardAdvanced) throws StudentNotFoundException {
        super(2);
        bag=boardAdvanced.getBag();
        students = new ArrayList<>(bag.extractStudents(4));
        this.boardAdvanced = boardAdvanced;
    }

    public List<Student> getStudentOnCard(){
        return students;
    }

    public void useEffect(Player currentPlayer, SPColour cardToDining) throws StudentNotFoundException, ExceededMaxStudentsDiningRoomException {
        School school=boardAdvanced.getPlayerSchool(currentPlayer);
        List<Student> s= students.stream().filter(x -> x.getColour().equals(cardToDining)).collect(Collectors.toList());
        List<Student> student;
        if(!s.isEmpty()){
            school.addStudentDiningRoom(students.remove(students.indexOf(s.get(0))));
        }else {
            throw new StudentNotFoundException();
        }
        student=bag.extractStudents(1);
        students.add(student.get(0));
    }
}
