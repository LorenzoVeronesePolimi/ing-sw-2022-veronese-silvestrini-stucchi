package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Pawns.Student;

import java.util.List;
import java.util.stream.Collectors;

public class PlaceOneStudent extends AbstractCharacterCard{
    private final BoardAdvanced boardAdvanced;
    private List<Student> fourStudents;
    private final Bag bag;

    public List<Student> getCardStudents(){
        return fourStudents;
    }

    public PlaceOneStudent(BoardAdvanced boardAdvanced) {
        super(1);
        this.boardAdvanced = boardAdvanced;
        bag = boardAdvanced.getBag();
        try {
            fourStudents = bag.extractStudents(4);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void useEffect(SPColour chosen, int archipelago) throws StudentNotFoundException{
        List<Student> s = fourStudents.stream().filter(x -> x.getColour().equals(chosen)).collect(Collectors.toList());
        Student student;
        if(!s.isEmpty()){
            student = fourStudents.remove(fourStudents.indexOf(s.get(0)));
            this.boardAdvanced.getArchiList().get(archipelago).addStudent(student);
        }else {
            throw new StudentNotFoundException();
        }
        s = bag.extractStudents(1);
        fourStudents.add(s.get(0));
    }
}
