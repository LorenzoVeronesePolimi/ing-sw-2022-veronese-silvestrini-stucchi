package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Places.Archipelago;

import java.util.List;
import java.util.Optional;

public class PlaceOneStudent extends AbstractCharacterCard{
    private List<Student> fourStudents;
    List<Archipelago> archi;

    public PlaceOneStudent(List<Archipelago> archipelagos) {
        super(1);
        Bag bag = Bag.instance();
        fourStudents = bag.extractStudents(4);
        archi=archipelagos;
    }
    public void useEffect(SPColour chosen, int archipelago) throws StudentNotFoundException{
        /*Optional s= fourStudents.stream().filter(x -> x.getColour().equals(chosen)).findAny();
        if(!s.isEmpty()){
            fourStudents.remove(s);
            archi.get(archipelago).addStudent(s);
        }else {
            throw new StudentNotFoundException();
        }
        }
*/
        Bag bag = Bag.instance();
        List<Student> s = bag.extractStudents(1);
        fourStudents.add(s.get(0));
    }
}
