package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

public class TakeProfessorOnEquity extends AbstractCharacterCard{
    final SPColour[] availableColours = {SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};
    private final BoardAdvanced boardAdvanced;

    public  TakeProfessorOnEquity(BoardAdvanced boardAdvanced){
        super(2);

        this.boardAdvanced = boardAdvanced;
    }

    public void useEffect(Player currentPlayer) throws ProfessorNotFoundException, NoProfessorBagException, InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException, TowerNotFoundException {
        School currentSchoolPlayer = boardAdvanced.getPlayerSchool(currentPlayer);

        for(SPColour colour: availableColours) {
            if(boardAdvanced.isProfessorInSchool(colour)) {
                School professorSchool = boardAdvanced.whereIsProfessor(colour);

                if(!professorSchool.getPlayer().equals(currentPlayer)) {
                    //number of currentPlayer's students of that particular colour
                    int numStudentsColorPlayer;
                    numStudentsColorPlayer = currentSchoolPlayer.getNumStudentColour(colour);

                    //number of enemyPlayer's students of that particular colour
                    int numStudentColourEnemy;
                    numStudentColourEnemy = professorSchool.getNumStudentColour(colour);

                    if(numStudentsColorPlayer == numStudentColourEnemy) {
                        boardAdvanced.moveProfessor(currentPlayer, colour);
                    }
                }
            } else {
                boardAdvanced.moveProfessor(currentPlayer, colour);
            }
        }

        //flow after professor movement
        boardAdvanced.tryToConquer(currentPlayer);
    }
}
