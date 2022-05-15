package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Board.BoardAbstract;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Cards.ExchangeThreeStudents;
import it.polimi.ingsw.Model.Cards.ExchangeTwoHallDining;
import it.polimi.ingsw.Model.Cards.PlaceOneStudent;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;

public class ControllerIntegrity {
    private BoardAbstract board;
    private BoardAdvanced boardAdvanced;
    private boolean advanced; //0: simple, 1: advanced

    public ControllerIntegrity(){
    }

    public void setBoard(BoardAbstract board) {
        this.board = board;
    }

    public BoardAbstract getBoard(){return this.board;}

    public void setBoardAdvanced(BoardAdvanced boardAdvanced){
        this.boardAdvanced = boardAdvanced;
    }

    public BoardAdvanced getBoardAdvanced(){return this.boardAdvanced;}

    public void setAdvanced(boolean advanced){
        this.advanced = advanced;
    }

    public boolean isAdvanced(){return this.advanced;}

    private boolean enoughColoursInListStudents(List<SPColour> coloursToHave, List<Student> availableIn){
        List<Student> available = new ArrayList<>();
        available.addAll(availableIn);

        int equal = 0;
        for(SPColour c : coloursToHave){
            for(int i = 0; i < available.size(); i++){
                if(available.get(i) != null){
                    if(available.get(i).getColour() == c){
                        available.set(i, null);
                        equal++;
                        break;
                    }
                }
            }
        }
        return (equal == coloursToHave.size());
    }

    public boolean checkCreateMatch(int numPlayers){
        return (numPlayers >= 2 && numPlayers <= 4);
    }

    public boolean checkAssistantCard(List<Integer> usedCards, Player currPlayer, int currPriority) {
        if(currPlayer.getHandLength() == 1){ // only one AssistantCard in hand: he has no alternative
            return true;
        } else {
            if(usedCards.size() > 0) {
                if (usedCards.contains(currPriority)) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean checkAssistantCard(List<Player> sitPlayers, int currentPlayerIndex, Player player, int turnPriority){
        //no other player used it or I have no choice
        if(player.getHandLength() == 1){ // only one AssistantCard in hand: he has no alternative
            return true;
        }
        else{
            if(currentPlayerIndex == 0){ // Base case: every Player controlled. No other Player used that AssistantCard
                return true;
            }
            if(sitPlayers.get(0).getLastCard().getTurnPriority() != turnPriority){ // the first Player didn't use that card: let's check the others
                return this.checkAssistantCard(sitPlayers.subList(1, sitPlayers.size()), currentPlayerIndex - 1, player, turnPriority);
            }
            else{ // another Player used that card
                return false;
            }
        }
    }

    public boolean checkStudentHallToDiningRoom(Player player, SPColour colour){
        return this.board.getPlayerSchool(player).getNumStudentColour(colour) < 10;
    }

    public boolean checkStudentToArchipelago(Player player, SPColour studentColour, int destinationArchipelagoIndex){
        if(!this.board.isStudentInSchoolHall(player, studentColour)){return false;}

        try{this.board.getArchipelago(destinationArchipelagoIndex);}
        catch(IndexOutOfBoundsException ex){return false;}

        return true;
    }

    public boolean checkMoveMotherNature(Player player, int moves){
        return (player.getLastCard().getMotherNatureMovement() >= moves);
    }

    public boolean checkStudentCloudToSchool(Player player, int indexCloud){
        List<Cloud> clouds;
        clouds = this.board.getClouds();

        if(clouds.size() <= indexCloud){return false;}

        if(clouds.get(indexCloud).getStudents().size() > 0){ //I can't choose a void Cloud
            School s = board.getPlayerSchool(player);
            // Is there enough space in the Hall?
            return s.getNumMaxStudentsHall() - s.getStudentsHall().size() <= clouds.get(0).getNumMaxStudents();
        }

        return false;
    }

    public boolean checkCCExchangeThreeStudents(Player player, List<SPColour> coloursCard, List<SPColour> coloursSchool, ExchangeThreeStudents chosenCard){
        if(!this.advanced){return false;}

        if(coloursCard.size() != coloursSchool.size()){return false;}

        // all Students in the Hall
        return enoughColoursInListStudents(coloursSchool, this.board.getPlayerSchool(player).getStudentsHall()) &&
                enoughColoursInListStudents(coloursCard, chosenCard.getStudentsOnCard());
    }

    public boolean checkCCExchangeTwoHallDining(Player player, List<SPColour> coloursHall, List<SPColour> coloursDiningRoom){
        if(!this.advanced){return false;}

        if(coloursHall.size() != coloursDiningRoom.size()){return false;}

        List<Student> studentsDiningRoom = new ArrayList<>(); // Students of the DiningRoom of the requested colours
        if(coloursDiningRoom.size() == 2){
            if(coloursDiningRoom.get(0) != coloursDiningRoom.get(1)){ //take interesting Students of the DiningRoom
                studentsDiningRoom.addAll(this.board.getPlayerSchool(player).getListStudentColour(coloursDiningRoom.get(0)));
                studentsDiningRoom.addAll(this.board.getPlayerSchool(player).getListStudentColour(coloursDiningRoom.get(1)));
            }
            else{
                studentsDiningRoom.addAll(this.board.getPlayerSchool(player).getListStudentColour(coloursDiningRoom.get(0)));
            }
        }
        else{
            studentsDiningRoom.addAll(this.board.getPlayerSchool(player).getListStudentColour(coloursDiningRoom.get(0)));
        }

        return (enoughColoursInListStudents(coloursDiningRoom, studentsDiningRoom) && // Are all Students required (in coloursDiningRoom) present in the DiningRoom?
                enoughColoursInListStudents(coloursHall, this.board.getPlayerSchool(player).getStudentsHall())); // Are all Students required (in coloursHall) present in the Hall?
    }

    public boolean checkCCGeneric(){
        return this.advanced;
    }

    public boolean checkCCFakeMNMovement(int fakeMNPosition){
        if(!this.advanced){return false;}

        return fakeMNPosition < this.boardAdvanced.getArchiList().size();
    }

    public boolean checkCCForbidIsland(int archipelagoIndexToForbid){
        if(!this.advanced){return false;}

        return archipelagoIndexToForbid < this.boardAdvanced.getArchiList().size();
    }

    public boolean checkCCPlaceOneStudent(SPColour colourToMove, int archipelagoIndexDestination, PlaceOneStudent chosenCard){
        if(!this.advanced){return false;}

        if(!(archipelagoIndexDestination < this.boardAdvanced.getArchiList().size())){return false;}

        for(Student s: chosenCard.getStudentsOnCard()){
            if(s.getColour() == colourToMove){return true;}
        }

        return false;
    }
}
