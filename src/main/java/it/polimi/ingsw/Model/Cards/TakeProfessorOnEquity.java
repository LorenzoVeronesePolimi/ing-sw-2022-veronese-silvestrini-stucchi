package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

import java.io.Serializable;

/**
 * This class represents the card with this effect:
 * during this turn, the player takes control of the professors even if he has the same number of students in the
 * school as the current owner of the professor.
 */
public class TakeProfessorOnEquity extends AbstractCharacterCard implements Serializable {

    /**
     * Constructor of the card. It sets the price.
     * @param boardAdvanced The object modified by the card.
     */
    public  TakeProfessorOnEquity(BoardAdvanced boardAdvanced){
        super(CharacterCardEnumeration.TAKE_PROFESSOR_ON_EQUITY, boardAdvanced,2);
    }

    /**
     * This method activates the effect of the card.
     * @param currentPlayer The current player that has the turn and has played the card.
     * @throws ProfessorNotFoundException When moving a professor, it is not found in the school nor in the bag.
     * @throws NoProfessorBagException When moving a professor, it is not found in the bag nor in the school.
     * @throws InvalidTowerNumberException When changing towers on an archipelago, if there are no towers.
     * @throws AnotherTowerException When changing towers on an archipelago, if there is a tower that has not been removed.
     * @throws ExceededMaxTowersException When putting back in the school the towers, but the school is already full.
     * @throws TowerNotFoundException When conquering but there are no towers in the school.
     */
    public void useEffect(Player currentPlayer) throws ProfessorNotFoundException, NoProfessorBagException,
            InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException, TowerNotFoundException {

        var availableColours = new SPColour[]{SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};
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
    }
}
