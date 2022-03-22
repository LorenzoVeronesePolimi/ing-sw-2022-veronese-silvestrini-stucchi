package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;

public class BoardAdvanced extends Board {
    private Board board;
    private boolean twoExtraPointsFlag = false;

    public BoardAdvanced(Board boardToExtend) {
        this.board = boardToExtend;
    }

    public List<Archipelago> getArchiList(){
        return new ArrayList<Archipelago>(archipelagos);
    }


    public void tryToConquer(){
        int currPosMotherNature = this.whereIsMotherNature();
        boolean archipelagoConquerable = this.checkIfConquerable();
        if(archipelagoConquerable){
            this.board.conquerArchipelago(this.players.get(this.currentPlayer), this.archipelagos.get(currPosMotherNature));

            //let's merge Archipelagos
            this.board.mergeArchipelagos();
        }
        else { //the Archipelago remains to the owner
        }
    }


    public boolean checkIfConquerable(){
        int currPosMotherNature = this.board.whereIsMotherNature();
        Archipelago currentArchipelago = this.board.archipelagos.get(currPosMotherNature);
        //if the owner of the Archipelago is the current Player, he conquers nothing
        if(currentArchipelago.getOwner() == this.board.players.get(currentPlayer)){
            return false;
        }
        else if(currentArchipelago.getOwner() == null){ //archipelago never conquered before
            return true;
        }
        else if(currentArchipelago.getForbidFlag()){ //This is an advanced function => see comment above(*)
            currentArchipelago.setForbidFlag(false);
        }
        else if(currentArchipelago.getTowerNoValueFlag()){ //This is an advanced function => see comment above(*)
            currentArchipelago.setTowerNoValueFlag(false);
        }
        //the current Player is not the owner: can he conquer the Archipelago?
        else{
            //who has higher influence according to rules?
            Player winner = this.board.computeWinner(currentArchipelago.getOwner(), this.board.players.get(currentPlayer), currentArchipelago, twoExtraPointsFlag);
            twoExtraPointsFlag = false;

            if(winner == this.board.players.get(currentPlayer)){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }

    public void setTwoExtraPointsFlag(boolean twoExtraPointsFlag) {
        this.twoExtraPointsFlag = twoExtraPointsFlag;
    }
}
