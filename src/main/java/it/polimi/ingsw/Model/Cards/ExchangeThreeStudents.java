package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Board.Board;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;

public class ExchangeThreeStudents extends AbstractCharacterCard{
    private BoardAdvanced boardAdvanced;
    Bag bag = Bag.instance();
    private List<Student> students;

    public ExchangeThreeStudents(BoardAdvanced boardAdvanced){
        super(1);

        students.addAll(bag.extractStudents(6));
        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect(Player player, List<SPColour> hallStudents, List<SPColour> exchangeStudents) {
        List<Student> park = new ArrayList<>();
        for(SPColour colour: hallStudents) {
            try {
                park.add(boardAdvanced.getPlayerSchool(player).removeStudentHall(colour));
            } catch (StudentNotFoundException e) {
                e.printStackTrace();
            }
        }

        //students.addAll(hallStudents);
    }

    @Override
    public void update(BoardAdvanced boardAdvanced) {

    }
}
