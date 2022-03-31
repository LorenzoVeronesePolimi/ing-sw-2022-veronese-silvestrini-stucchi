package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxStudentsDiningRoomException;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxStudentsHallException;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Exceptions.WrongNumberOfStudentsTransferExcpetion;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;

public class ExchangeTwoHallDining extends AbstractCharacterCard{
    private final BoardAdvanced boardAdvanced;

    public ExchangeTwoHallDining(BoardAdvanced boardAdvanced){
        super(1);
        this.boardAdvanced=boardAdvanced;
    }

    public void useEffect(Player player, List<SPColour> hallStudents, List<SPColour> diningStudents) throws
            WrongNumberOfStudentsTransferExcpetion, StudentNotFoundException, ExceededMaxStudentsHallException,
            ExceededMaxStudentsDiningRoomException {

        if(hallStudents.size()<=2 || diningStudents.size()<=2) {
            throw new WrongNumberOfStudentsTransferExcpetion();
        }

        List<Student> hallToDining = new ArrayList<>();
        for(SPColour colour: hallStudents) {
            hallToDining.add(boardAdvanced.getPlayerSchool(player).removeStudentHall(colour));
        }

        List<Student> diningToHall = new ArrayList<>();
        for(SPColour colour: diningStudents) {
            diningToHall.add(boardAdvanced.getPlayerSchool(player).removeStudentDiningRoom(colour));
        }

        if((hallStudents.size()<=2) || (diningStudents.size()<=2)) {
            throw new WrongNumberOfStudentsTransferExcpetion();
        }

        School currentPlayerSchool = boardAdvanced.getPlayerSchool(player);

        for(Student s: diningToHall) {
            currentPlayerSchool.addStudentHall(s);
        }

        for(Student s: hallToDining) {
            currentPlayerSchool.addStudentDiningRoom(s);
        }
    }
}