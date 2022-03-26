package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxStudentsHallException;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Exceptions.WrongNumberOfStudentsTransferExcpetion;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Places.School;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExchangeThreeStudents extends AbstractCharacterCard{
    private BoardAdvanced boardAdvanced;
    Bag bag;
    private List<Student> students;

    public ExchangeThreeStudents(BoardAdvanced boardAdvanced){
        super(1);
        bag=boardAdvanced.getBag();
        try {
            students = new ArrayList<>(bag.extractStudents(6));
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect(Player player, List<SPColour> hallStudents, List<SPColour> exchangeStudents) throws WrongNumberOfStudentsTransferExcpetion {
        if(hallStudents.size() != 3 || exchangeStudents.size() != 3) {
            throw new WrongNumberOfStudentsTransferExcpetion();
        }

        List<Student> hallToCard = new ArrayList<>();
        for(SPColour colour: hallStudents) {
            try {
                hallToCard.add(boardAdvanced.getPlayerSchool(player).removeStudentHall(colour));
            } catch (StudentNotFoundException e) {
                e.printStackTrace();
            }
        }

        List<Student> cardToHall = new ArrayList<>();
        for(SPColour colour: exchangeStudents) {
            List<Student> temp = students.stream().filter(x -> x.getColour().equals(colour)).collect(Collectors.toList());

            if(!temp.isEmpty()) {
                cardToHall.add(temp.get(0));
            }
        }

        if(hallToCard.size() != 3 || cardToHall.size() != 3) {
            throw new WrongNumberOfStudentsTransferExcpetion();
        }

        School currentPlayerSchool = boardAdvanced.getPlayerSchool(player);

        for(Student s: cardToHall) {
            try {
                currentPlayerSchool.addStudentHall(s);
            } catch (ExceededMaxStudentsHallException e) {
                e.printStackTrace();
            }
        }

        students.addAll(hallToCard);

        updatePrice();
    }
}
