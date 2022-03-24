package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.School;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardAdvanced implements Board{
    private BoardAbstract board;
    private boolean twoExtraPointsFlag = false;
    private SPColour colourToExclude=null;

    public BoardAdvanced(BoardAbstract boardToExtend) {
        this.board = boardToExtend;
    }

    public Bag getBag(){return this.board.bag;}

    public List<Archipelago> getArchiList(){
        return new ArrayList<Archipelago>(this.board.archipelagos);
    }

    public List<School> getSchools(){return new ArrayList<School>(this.board.schools);}

    public Archipelago getArchipelago(int archipelagoIndex) {
        return this.board.getArchipelago(archipelagoIndex);
    }

    public void initializeBoard() {
        this.board.initializeBoard();
    }

    public void moveStudentSchoolToArchipelagos(Player player, SPColour colour, int archipelagoIndex) {
        this.board.moveStudentSchoolToArchipelagos(player, colour, archipelagoIndex);
    }

    public void moveStudentCloudToSchool(Player player, int cloudIndex) {
        this.board.moveStudentCloudToSchool(player, cloudIndex);
    }

    public void moveStudentHallToDiningRoom(Player player, SPColour colour) {
        this.board.moveStudentHallToDiningRoom(player, colour);
    }

    public void moveStudentBagToSchool(int numStudents) {
        this.board.moveStudentBagToSchool(numStudents);
    }

    public void moveMotherNature(int archipelagoIndex) {
        this.board.moveMotherNature(archipelagoIndex);
    }

    public void moveProfessor(Player destinationPlayer, SPColour colour) {
        this.board.moveProfessor(destinationPlayer, colour);
    }

    public boolean isProfessorInSchool(SPColour colour) {
        return this.board.isProfessorInSchool(colour);
    }

    public School whereIsProfessor(SPColour colour) {
        return this.board.whereIsProfessor(colour);
    }

    public void conquerProfessor(SPColour colour) {
        this.board.conquerProfessor(colour);
    }

    public int whereIsMotherNature() {
        return this.board.whereIsMotherNature();
    }

    public School getPlayerSchool(Player player) {
        return this.board.getPlayerSchool(player);
    }

    public void makeTurn() {
        this.board.makeTurn();
    }

    public void tryToConquer(){
        int currPosMotherNature = this.board.whereIsMotherNature();
        boolean archipelagoConquerable = this.checkIfConquerable();
        if(archipelagoConquerable){
            this.board.conquerArchipelago(this.board.players.get(this.board.currentPlayer), this.board.archipelagos.get(currPosMotherNature));

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
        if(currentArchipelago.getOwner() == this.board.players.get(this.board.currentPlayer)){
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
            Player winner = this.computeWinner(currentArchipelago.getOwner(), this.board.players.get(this.board.currentPlayer), currentArchipelago, twoExtraPointsFlag, colourToExclude);
            twoExtraPointsFlag = false;

            if(winner == this.board.players.get(this.board.currentPlayer)){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }

    @Override
    public Player computeWinner(Player owner, Player challenger, Archipelago archipelago) {
        return null;
    }

    @Override
    public int computeInfluenceOfPlayer(Player player, Archipelago archipelago) {
        return 0;
    }

    protected Player computeWinner(Player owner, Player challenger, Archipelago archipelago, boolean twoExtraPointsFlag, SPColour colourToExclude){
        int ownerInfluence = this.computeInfluenceOfPlayer(owner, archipelago, colourToExclude);
        int challengerInfluence = this.computeInfluenceOfPlayer(challenger, archipelago, colourToExclude);

        if(twoExtraPointsFlag) {
            challengerInfluence += 2;
        }

        if(ownerInfluence > challengerInfluence){
            return owner;
        }
        else{
            return challenger;
        }
    }


    // Returns the influence of a Player on a Archipelago
    private int computeInfluenceOfPlayer(Player player, Archipelago archipelago, SPColour colourToExclude){
        int influence = 0;

        //if the player owns the Archipelago, the number of Towers (= number of Islands) counts
        if(player == archipelago.getOwner()){
            influence += archipelago.getNumIslands();
        }

        Map<SPColour, Integer> archipelagoStudentsData = archipelago.howManyStudents(); //data about Students on the Archipelago
        List<Professor> playerProfessors = this.board.playerSchool.get(player).getProfessors(); //Professors of the player
        for(Professor p : playerProfessors){
            if(colourToExclude==null || !p.getColour().equals(colourToExclude))
                influence += archipelagoStudentsData.get(p.getColour());
        }

        return influence;
    }

    public void setColourToExclude(SPColour colourToExclude){
        this.colourToExclude= colourToExclude;
    }

    public void setTwoExtraPointsFlag(boolean twoExtraPointsFlag) {
        this.twoExtraPointsFlag = twoExtraPointsFlag;
    }

}
