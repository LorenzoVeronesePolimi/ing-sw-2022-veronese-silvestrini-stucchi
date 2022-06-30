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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that performs input checking. It checks if the message received from the client is allowed by the rules of the game.
 */
public class ControllerIntegrity implements Serializable {
    private static final long serialVersionUID = 1L;
    private BoardAbstract board;
    private BoardAdvanced boardAdvanced;
    private boolean advanced; //0: simple, 1: advanced

    /**
     * default constructor
     */
    public ControllerIntegrity(){
    }

    /**
     * setter of the board
     * @param board abstract board
     */
    public void setBoard(BoardAbstract board) {
        this.board = board;
    }

    /**
     * getter of the board
     * @return abstract board
     */
    public BoardAbstract getBoard(){return this.board;}

    /**
     * setter of board advanced
     * @param boardAdvanced board advanced to be set
     */
    public void setBoardAdvanced(BoardAdvanced boardAdvanced){
        this.boardAdvanced = boardAdvanced;
    }

    /**
     * getter of board advanced
     * @return board advanced
     */
    public BoardAdvanced getBoardAdvanced(){return this.boardAdvanced;}

    /**
     * setter of parameter that says if a game is advanced or not
     * @param advanced boolean value: true if advanced, false otherwise
     */
    public void setAdvanced(boolean advanced){
        this.advanced = advanced;
    }

    /**
     * method that says if a game is advanced
     * @return true if advanced, false otherwise
     */
    public boolean isAdvanced(){return this.advanced;}

    /**
     * method that says if in a list there are at least one student for each required colour
     * @param coloursToHave list of colour to have
     * @param availableIn list of available students
     * @return true if there are at least one student for each required colour, false otherwise
     */
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

    /**
     * method that verifies that a game has at least 2 players, and 4 as maximum
     * @param numPlayers
     * @return
     */
    public boolean checkCreateMatch(int numPlayers){
        return (numPlayers >= 2 && numPlayers <= 4);
    }

    /**
     * method that verifies that the chosen card has not been played before by the player
     * @param usedCards list of already used cards
     * @param currPlayer current player
     * @param currPriority chosen priority
     * @return true if the chosen card has not been played before by the player, false otherwise
     */
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

    /**
     * method that verifies that the chosen card has not been played by another player in che same turn
     * @param sitPlayers list of player in the order of entrance
     * @param currentPlayerIndex index of current player
     * @param player player that chooses the card
     * @param turnPriority chosen turn priority
     * @return true if the chosen card has not been played by another player, false otherwise
     */
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

    /**
     * method that verifies that in a given player's dining room there are less than 10 students of the given color
     * @param player owner of the school to check
     * @param colour colour to check
     * @return true if the student can be moved to the dining room, else otherwise
     */
    public boolean checkStudentHallToDiningRoom(Player player, SPColour colour){
        return this.board.getPlayerSchool(player).getNumStudentColour(colour) < 10;
    }

    /**
     * method that verifies that in a player's room exists a student of a given colour and that the given archipelago index
     * is not out of bound
     * @param player owner of the school to check
     * @param studentColour colour of the student to move
     * @param destinationArchipelagoIndex index where the student must be moved
     * @return true if the movement is allowed, false otherwise
     */
    public boolean checkStudentToArchipelago(Player player, SPColour studentColour, int destinationArchipelagoIndex){
        if(!this.board.isStudentInSchoolHall(player, studentColour)){return false;}

        try{this.board.getArchipelago(destinationArchipelagoIndex);}
        catch(IndexOutOfBoundsException ex){return false;}

        return true;
    }

    /**
     * method that verifies that the number of moves required for mother nature is equal or less than the number of moves that it is
     * allowed to do (according to the last assistant card played)
     * @param player current player
     * @param moves number of moves to perform
     * @return true if the movement can be done, false otherwise
     */
    public boolean checkMoveMotherNature(Player player, int moves){
        return (player.getLastCard().getMotherNatureMovement() >= moves);
    }

    /**
     * method that verifies that the movement of the students from a given cloud to a given player school is allowed
     * @param player current player
     * @param indexCloud index of the chosen cloud
     * @return true is movement allowed, false otherwise
     */
    public boolean checkStudentCloudToSchool(Player player, int indexCloud){
        List<Cloud> clouds;
        clouds = this.board.getClouds();

        if(clouds.size() <= indexCloud){return false;}

        if(clouds.get(indexCloud).getStudents().size() > 0){ //I can't choose a void Cloud...
            School s = board.getPlayerSchool(player);
            // Is there enough space in the Hall?
            return s.getNumMaxStudentsHall() - s.getStudentsHall().size() <= clouds.get(0).getNumMaxStudents();
        }
        else{ //...  unless I have no choice
            for(Cloud c : clouds){
                if(c.getStudents().size() > 0){
                    return false; // a cloud has students: I can't choose a void one
                }
            }
            return true; // no cloud has students: I can choose a void one
        }
    }

    /**
     * method that verifies that parameters for exchangeThreeStudents card are correct
     * @param player player that uses the card
     * @param coloursCard list of colour chosen from the card
     * @param coloursSchool list of chosen colour from the hall
     * @param chosenCard chosen card
     * @return true if usage allowed, false otherwise
     */
    public boolean checkCCExchangeThreeStudents(Player player, List<SPColour> coloursCard, List<SPColour> coloursSchool, ExchangeThreeStudents chosenCard){
        if(!this.advanced){return false;}

        if(coloursCard.size() != coloursSchool.size()){return false;}

        // all Students in the Hall
        return enoughColoursInListStudents(coloursSchool, this.board.getPlayerSchool(player).getStudentsHall()) &&
                enoughColoursInListStudents(coloursCard, chosenCard.getStudentsOnCard());
    }

    /**
     * method that verifies that parameters for exchangeTwoHallDining card are correct
     * @param player player that uses the card
     * @param coloursHall list of colour chosen from the hall
     * @param coloursDiningRoom list of chosen colour from the dining room
     * @return true if usage allowed, false otherwise
     */
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

    /**
     * method that verifies that a character card is used only if the game is advanced
     * @return true if character card is usable, false otherwise
     */
    public boolean checkCCGeneric(){
        return this.advanced;
    }

    /**
     * method that verifies that parameters for fakeMNMovement card are correct
     * @param fakeMNPosition index of archipelago to go for fake movement
     * @return true if character card is usable, false otherwise
     */
    public boolean checkCCFakeMNMovement(int fakeMNPosition){
        if(!this.advanced){return false;}

        return fakeMNPosition < this.boardAdvanced.getArchiList().size();
    }

    /**
     *  method that verifies that parameters for forbidIsland card are correct
     * @param archipelagoIndexToForbid index of archipelago to forbid
     * @return true if character card is usable, false otherwise
     */
    public boolean checkCCForbidIsland(int archipelagoIndexToForbid){
        if(!this.advanced){return false;}

        return archipelagoIndexToForbid < this.boardAdvanced.getArchiList().size();
    }

    /**
     * method that verifies that parameters for placeOneStudent card are correct
     * @param colourToMove colour of a student from the card
     * @param archipelagoIndexDestination index of archipelago where the student has to be put
     * @param chosenCard chosen character card
     * @return true if character card is usable, false otherwise
     */
    public boolean checkCCPlaceOneStudent(SPColour colourToMove, int archipelagoIndexDestination, PlaceOneStudent chosenCard){
        if(!this.advanced){return false;}

        if(!(archipelagoIndexDestination < this.boardAdvanced.getArchiList().size())){return false;}

        for(Student s: chosenCard.getStudentsOnCard()){
            if(s.getColour() == colourToMove){return true;}
        }

        return false;
    }
}
