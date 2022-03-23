package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Places.Archipelago;

import java.util.List;
import java.util.stream.Collectors;

public class PlaceOneStudent extends AbstractCharacterCard{
    private List<Student> fourStudents;
    List<Archipelago> archi;

    public PlaceOneStudent(BoardAdvanced boardAdvanced) {
        super(1);
        Bag bag = Bag.instance();
        fourStudents = bag.extractStudents(4);
        archi.addAll(boardAdvanced.getArchiList()); // o assegnamento archi=archipelagos?????
    }
    public void useEffect(SPColour chosen, int archipelago) throws StudentNotFoundException{
        List<Student> s= fourStudents.stream().filter(x -> x.getColour().equals(chosen)).collect(Collectors.toList());
        Student student;
        if(!s.isEmpty()){
            student= fourStudents.remove(fourStudents.indexOf(s.get(0)));
            archi.get(archipelago).addStudent(student);
        }else {
            throw new StudentNotFoundException();
        }
        Bag bag = Bag.instance();
        s = bag.extractStudents(1);
        fourStudents.add(s.get(0));
        updatePrice();
    }

    @Override
    public void update(BoardAdvanced boardAdvanced) {
        archi.addAll(boardAdvanced.getArchiList());
    }
}
