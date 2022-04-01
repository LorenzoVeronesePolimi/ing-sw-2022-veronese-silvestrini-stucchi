package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Board.Board;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;

public class ControllerIntegrity {
    private Board board;
    private BoardAdvanced boardAdvanced;
    private boolean advanced; //0: simple, 1: advanced

    public ControllerIntegrity(){
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setBoardAdvanced(BoardAdvanced boardAdvanced){
        this.boardAdvanced = boardAdvanced;
    }

    public void setAdvanced(boolean advanced){
        this.advanced = advanced;
    }

    public boolean checkCreateMatch(int numPlayers){
        return (numPlayers >= 2 && numPlayers <= 4);
    }

    public boolean checkAssistantCard(List<Player> players, int currentPlayerIndex, Player player, int turnPriority){
        //no other player used it or I have no choice
        if(player.getHandLength() == 1){ // only one AssistantCard in hand: he has no alternative
            return true;
        }
        else{
            if(currentPlayerIndex == 0){ // Base case: every Player controlled. No other Player used that AssistantCard
                return true;
            }
            if(players.get(0).getLastCard().getTurnPriority() != turnPriority){ // the first Player didn't use that card: let's check the others
                return this.checkAssistantCard(players.subList(1, players.size()), currentPlayerIndex - 1, player, turnPriority);
            }
            else{ // another Player used that card
                return false;
            }
        }
    }

    public boolean checkStudentHallToDiningRoom(Player player, SPColour colour){
        if(this.board.getPlayerSchool(player).getNumStudentColour(colour) == 10){return false;}

        return true;
    }

    public boolean checkStudentToArchipelago(Player player, SPColour studentColour, int destArchipelagoIndex){
        if(!this.board.isStudentInSchoolHall(player, studentColour)){return false;}
        if(this.board.getArchipelago(destArchipelagoIndex) != null){return false;}
        return true;
    }

    public boolean checkMoveMotherNature(Player player, int moves){
        return (player.getLastCard().getMotherNatureMovement() >= moves);
    }

    public boolean checkStudentCloudToSchool(Player player, int indexCloud){
        List<Cloud> clouds = new ArrayList<>();
        clouds = this.board.getClouds();
        if(clouds.get(indexCloud).getStudents().size() > 0){ //I can't choose a void Cloud
            School s = board.getPlayerSchool(player);
            if(s.getNumMaxStudentsHall() - s.getStudentsHall().size() <= clouds.get(0).getNumMaxStudents()){ // Is there enough space in the Hall?
                return true;
            }
            return false;
        } else{return false;}
    }

    public boolean checkCCExchangeThreeStudents(Player player, List<SPColour> coloursCard, List<SPColour> coloursSchool){
        if(!this.advanced){return false;}

        if(coloursCard.size() != coloursSchool.size()){return false;}

        // all Students in the Hall
        List<Student> availableSchool = this.board.getPlayerSchool(player).getStudentsHall();
        int equalsSchool = 0;
        for(SPColour c : coloursSchool){
            for(Student s : availableSchool){
                if(s.getColour() == c){
                    availableSchool.remove(s);
                    equalsSchool++;
                    break;
                }
            }
        }
        /*
        // all Students on the Card
        //TODO: List<Student> availableCard = this.boardAdvanced.get;
        int equalsCard = 0;
        for(SPColour c : coloursSchool){
            for(Student s : availableCard){
                if(s.getColour() == c){
                    availableCard.remove(s);
                    equalsCard++;
                    break;
                }
            }
        }*/
        return false;
    }
}
