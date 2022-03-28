package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.WrongColourException;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

public class TakeProfessorOnEquity extends AbstractCharacterCard{
    SPColour[] availableColours = {SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};
    private BoardAdvanced boardAdvanced;

    public  TakeProfessorOnEquity(BoardAdvanced boardAdvanced){
        super(2);

        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect(Player currentPlayer){
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
                        boardAdvanced.moveProfessor(currentPlayer, colour);
                    }
                }
            } else {
                boardAdvanced.moveProfessor(currentPlayer, colour);
            }
        }

        //flow after professor movement
        boardAdvanced.tryToConquer();
    }
}
