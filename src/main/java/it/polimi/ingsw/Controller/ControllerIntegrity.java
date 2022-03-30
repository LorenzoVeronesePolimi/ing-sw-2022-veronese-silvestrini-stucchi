package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Board.Board;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.AssistantCardAlreadyPlayedTurnException;
import it.polimi.ingsw.Model.Exceptions.WrongColourException;
import it.polimi.ingsw.Model.Player;

import java.util.List;

public class ControllerIntegrity {
    private Board board;
    private boolean advanced; //0: simple, 1: advanced

    public ControllerIntegrity(){
    }

    public void setBoard(Board board) {
        this.board = board;
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
        try{
            if(this.board.getPlayerSchool(player).getNumStudentColour(colour) == 10){return false;}
        }catch(WrongColourException ex){ex.printStackTrace(); return false;}
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
    /*
    public boolean checkStudentCloudToSchool(Player player, int indexCloud){

    }*/
}
