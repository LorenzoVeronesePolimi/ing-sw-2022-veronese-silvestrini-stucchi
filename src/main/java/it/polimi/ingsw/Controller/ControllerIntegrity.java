package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Board.Board;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Player;

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

    public boolean checkStudentToArchipelago(Player player, SPColour studentColour, int destArchipelagoIndex){
        if(!this.board.isStudentInSchoolHall(player, studentColour)){return false;}
        if(this.board.getArchipelago(destArchipelagoIndex) != null){return false;}
        return true;
    }
}
