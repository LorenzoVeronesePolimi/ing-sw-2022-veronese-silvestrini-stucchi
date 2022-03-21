package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Board.Board;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.ProfessorNotFoundException;
import it.polimi.ingsw.Model.Exceptions.WrongColourException;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Places.School;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TakeProfessorOnEquity extends AbstractCharacterCard{
    SPColour[] availableColours = {SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};
    private BoardAdvanced boardAdvanced;

    public  TakeProfessorOnEquity(BoardAdvanced boardAdvanced){
        super(2);

        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect(Player currentPlayer){
        List<SPColour> changedColour = new ArrayList<>();
        List<SPColour> bagColour = new ArrayList<>();
        List<Player> originPlayer = new ArrayList<>();
        Bag bag = Bag.instance();
        School currentSchoolPlayer = boardAdvanced.getPlayerSchool(currentPlayer);

        for(SPColour colour: availableColours) {
            if(boardAdvanced.isProfessorInSchool(colour)) {
                School professorSchool = boardAdvanced.whereIsProfessor(colour);

                if(!professorSchool.getPlayer().equals(currentPlayer)) {
                    //number of currentPlayer's students of that particular colour
                    int numStudentsColorPlayer = 0;
                    try {
                        numStudentsColorPlayer = currentSchoolPlayer.getNumStudentColour(colour);
                    } catch (WrongColourException e) {
                        e.printStackTrace();
                    }

                    //number of enemyPlayer's students of that particular colour
                    int numStudentColourEnemy = 0;
                    try {
                        numStudentColourEnemy = professorSchool.getNumStudentColour(colour);
                    } catch (WrongColourException e) {
                        e.printStackTrace();
                    }

                    if(numStudentsColorPlayer == numStudentColourEnemy) {
                        changedColour.add(colour);
                        originPlayer.add(professorSchool.getPlayer());
                        boardAdvanced.moveProfessor(currentPlayer, colour);
                    }
                }
            } else {
                bagColour.add(colour);
                boardAdvanced.moveProfessor(currentPlayer, colour);
            }
        }

        //flow after professor movement
        boardAdvanced.tryToConquer();

        for(int i = 0; i < changedColour.size(); i++) {
            boardAdvanced.moveProfessor(originPlayer.get(i), changedColour.get(i));
        }

        for(int i = 0; i < bagColour.size(); i++) {
            Professor toBeMoved = null;

            try {
                toBeMoved = currentSchoolPlayer.removeProfessor(bagColour.get(i));
            } catch (ProfessorNotFoundException e) {
                e.printStackTrace();
            }

            bag.putProfessor(toBeMoved);
        }


        updatePrice();
    }

    @Override
    public void update(BoardAdvanced boardAdvanced) {
        this.boardAdvanced = boardAdvanced;
    }
}
