package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxStudentsDiningRoomException;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxStudentsHallException;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Exceptions.WrongNumberOfStudentsTransferExcpetion;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Places.School;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExchangeTwoHallDining extends AbstractCharacterCard{
    private BoardAdvanced boardAdvanced;
    public ExchangeTwoHallDining(BoardAdvanced boardAdvanced){

        super(1);
        this.boardAdvanced=boardAdvanced;
    }

    public void useEffect(Player player, List<SPColour> hallStudents, List<SPColour> diningStudents) throws WrongNumberOfStudentsTransferExcpetion {
        if(hallStudents.size()<=2 && 0<=hallStudents.size() || diningStudents.size()<=2 && 0<=diningStudents.size()) {
            throw new WrongNumberOfStudentsTransferExcpetion();
        }

        List<Student> hallToDining = new ArrayList<>();
        for(SPColour colour: hallStudents) {
            try {
                hallToDining.add(boardAdvanced.getPlayerSchool(player).removeStudentHall(colour));
            } catch (StudentNotFoundException e) {
                e.printStackTrace();
            }
        }

        List<Student> diningToHall = new ArrayList<>();
        for(SPColour colour: diningStudents) {
            try {
                diningToHall.add(boardAdvanced.getPlayerSchool(player).removeStudentDiningRoom(colour));
            } catch (StudentNotFoundException e) {
                e.printStackTrace();
            }
        }

        if((hallStudents.size()<=2 && 0<=hallStudents.size()) || (diningStudents.size()<=2 && 0<=diningStudents.size())) {
            throw new WrongNumberOfStudentsTransferExcpetion();
        }

        School currentPlayerSchool = boardAdvanced.getPlayerSchool(player);

        for(Student s: diningToHall) {
            try {
                currentPlayerSchool.addStudentHall(s);
            } catch (ExceededMaxStudentsHallException e) {
                e.printStackTrace();
            }
        }

        for(Student s: hallToDining) {
            try {
                currentPlayerSchool.addStudentDiningRoom(s);
            } catch (ExceededMaxStudentsDiningRoomException e) {
                e.printStackTrace();
            }
        }
        updatePrice();

    }

    public void update(BoardAdvanced boardAdvanced){};
}