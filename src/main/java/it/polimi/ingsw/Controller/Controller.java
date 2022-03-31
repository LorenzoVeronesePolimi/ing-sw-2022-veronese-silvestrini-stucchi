package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Controller.Exceptions.NoPlayerException;
import it.polimi.ingsw.Controller.Messages.*;
import it.polimi.ingsw.Model.Board.Board;
import it.polimi.ingsw.Model.Board.BoardAbstract;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Board.BoardFactory;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.View.View;

import java.util.*;

// This is the main Controller: it coordinates all the others
public class Controller implements Observer {
    private int numPlayers;
    private boolean advanced;
    private Board board;
    private BoardAdvanced boardAdvanced; //null if advanced=0
    private List<Player> players; //ordered

    private int currentPlayerIndex = 0;

    private ControllerInput controllerInput;
    private ControllerState controllerState;
    private ControllerIntegrity controllerIntegrity;

    private int numStudentsToMove;
    private int numStudentsToMoveCurrent;
    private static final int NUM_STUDENTS_TO_MOVE_TWO = 3;
    private static final int NUM_STUDENTS_TO_MOVE_THREE = 3;


    public Controller(){
        this.players = new ArrayList<>();
        this.controllerInput = new ControllerInput();
        this.controllerState = new ControllerState();
        this.controllerIntegrity = new ControllerIntegrity();
    }

    public Player getCurrentPlayer(){
        return this.players.get(this.currentPlayerIndex);
    }

    /*I RECEIVED A MESSAGE => I need to:
     * Know its format: is it a STUDENT_TO_ARCHIPELAGO or something else?
     *   if not a valid format: resend
     * Now I know what to do
     * Is it the right time to receive this message? Is it the right part of the game/turn?
     *   if no: resend
     * Does this message respect the rules (ex. I can't move MotherNature of 6 plates)?
     *   if no: resend
     * Call the Model and applicate the move requested
     * */
    public void update(Observable o, Object arg) {
        if(!controllerInput.checkFormat(arg)){
            System.out.println("Invalid format");
            return;
        };

        Message message = (Message)arg;
        if(!controllerState.checkState(message.getType())){
            System.out.println("You can't do that now");
            return;
        };

        switch(message.getType()){
            case CREATE_MATCH: //TODO: manage GameFour
                if(!this.manageCreateMatch((MessageCreateMatch)message)){
                    System.out.println("You can't create a match now");
                }
            case ADD_PLAYER:
                if(!this.manageAddPlayer((MessageAddPlayer)message)){
                    System.out.println("Impossible to add this player");
                }
            case ASSISTANT_CARD:
                if(!this.manageAssistantCard((MessageAssistantCard)message)){
                    System.out.println("Impossible to play this AssistantCard");
                }
            case STUDENT_HALL_TO_DINING_ROOM:
                if(!this.manageStudentHallToDiningRoom((MessageStudentHallToDiningRoom)message)){
                    System.out.println("You can't move a Student in that way");
                }
            case STUDENT_TO_ARCHIPELAGO:
                if(!this.manageStudentToArchipelago((MessageStudentToArchipelago)message)){
                    System.out.println("You can't move a Student in that way");
                }
            case MOVE_MOTHER_NATURE:
                if(!this.manageMoveMotherNature((MessageMoveMotherNature)message)){
                    System.out.println("You can't move the Mother Nature in that way");
                }
        }

        //check if I have to make some automatic action (=>PIANIFICATION1)
        if(controllerState.getState() == State.PLANNING1){
            //TODO: surrounded with try catch just to remove errors. It needs to be checked before leaving it like that
            try {
                this.board.moveStudentBagToCloud();
            } catch (ExceededMaxStudentsCloudException e) {
                e.printStackTrace();
            } catch (StudentNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //associate the String to its SPColour. Note that I'm sure this association exists, since I made a control
    // in ControllerInput (checkStudentColour())
    private SPColour mapStringToSPColour(String s){
        switch(s.toLowerCase()){
            case "red":
                return SPColour.RED;
            case "pink":
                return SPColour.PINK;
            case "blue":
                return SPColour.BLUE;
            case "yellow":
                return SPColour.YELLOW;
            case "green":
                return SPColour.GREEN;
        }
        return null; //impossible
    }

    private PlayerColour mapStringToPlayerColour(String s){
        switch(s.toLowerCase()){
            case "white":
                return PlayerColour.WHITE;
            case "black":
                return PlayerColour.BLACK;
            case "gray":
                return PlayerColour.GRAY;
        }
        return null; //impossible
    }

    private Player mapStringToPlayer(String s) throws NoPlayerException{
        for(Player p : this.players){
            if(p.getNickname() == s){
                return p;
            }
        }

        throw new NoPlayerException();
    }

    // checks if nickname is the current Player
    private boolean isCurrentPlayer(String nickname){
        try{
            Player player = this.mapStringToPlayer(nickname);
            if(!(this.players.get(currentPlayerIndex) == player)){return false;}
        } catch(NoPlayerException ex){return false;}
        return true;
    }

    private void initMatch(){
        BoardFactory factory = new BoardFactory(this.players);
        this.board = factory.createBoard();

        if(this.advanced){
            //TODO: surrounded with try catch just to remove errors. It needs to be checked before leaving it like that
            try {
                this.boardAdvanced = new BoardAdvanced((BoardAbstract) this.board);
            } catch (ExceededMaxStudentsHallException e) {
                e.printStackTrace();
            } catch (StudentNotFoundException e) {
                e.printStackTrace();
            } catch (TowerNotFoundException e) {
                e.printStackTrace();
            } catch (EmptyCaveauExcepion e) {
                e.printStackTrace();
            }
        }

        controllerIntegrity.setBoard(this.board);
        controllerIntegrity.setAdvanced(this.advanced);

        //set number of Students to move at each ACTION1
        if(this.players.size() == 3){
            this.numStudentsToMove = NUM_STUDENTS_TO_MOVE_THREE;
            this.numStudentsToMoveCurrent = NUM_STUDENTS_TO_MOVE_THREE;
        } else{
            this.numStudentsToMove = NUM_STUDENTS_TO_MOVE_TWO;
            this.numStudentsToMoveCurrent = NUM_STUDENTS_TO_MOVE_TWO;
        }
    }

    private void changeTurnOrder(){
        Map<Player, Integer> values = new HashMap<>();

        for(Player p : this.players){
            values.put(p, p.getLastCard().getTurnPriority());
        }

        List<Player> orderedPlayerList = new ArrayList<>();
        while (values.size() > 0) {
            Map.Entry<Player, Integer> min = null;
            for (Map.Entry<Player, Integer> e : values.entrySet()) {
                if (min == null || min.getValue() > e.getValue()) {
                    min = e;
                }
            }
            Player minPlayer = min.getKey();
            orderedPlayerList.add(minPlayer);
            values.remove(minPlayer);
        }

        this.players = orderedPlayerList;
    }

    private boolean manageCreateMatch(MessageCreateMatch message){ //TODO: manage GameFour
        int numPlayers = message.getNumPlayers();
        PlayerColour colourFirstPlayer = mapStringToPlayerColour(message.getColourFirstPlayer());
        // no need to control the boolean "advanced"

        if(!controllerIntegrity.checkCreateMatch(numPlayers)){
            return false;
        }

        Player player = new Player(message.getNicknameFirstPlayer(), colourFirstPlayer);
        this.players.add(player);

        controllerState.setState(State.WAITING_PLAYERS);

        return true;
    }

    private boolean manageAddPlayer(MessageAddPlayer message){ // TODO: manage GameFour
        String nickname = message.getNickname();
        PlayerColour colour = mapStringToPlayerColour(message.getColour());
        // He can't have the name of an existing Player
        for(Player p : this.players){
            if(p.getNickname() == nickname){return false;}
        }

        // No integrity to check

        Player player = new Player(nickname, colour);
        this.players.add(player);

        if(this.players.size() == numPlayers){ // The requested number of players has been reached: let's go on
            this.initMatch();
            controllerState.setState(State.PLANNING1);
        }

        return true;
    }

    private boolean manageAssistantCard(MessageAssistantCard message){
        String nicknamePlayer = message.getNicknamePlayer();
        int turnPriority = message.getTurnPriority();

        // Is him the currentPlayer? Can he use that AssistantCard?
        if(!isCurrentPlayer(nicknamePlayer)){return false;}
        controllerIntegrity.checkAssistantCard(this.players, this.currentPlayerIndex, this.players.get(this.currentPlayerIndex), turnPriority);

        // Remove the card from his hand
        //TODO: surrounded with try catch just to remove errors. It needs to be checked before leaving it like that
        try{
            board.useAssistantCard(this.players.get(this.currentPlayerIndex), turnPriority);
        } catch(AssistantCardAlreadyPlayedTurnException | NoAssistantCardException ex){return false;} // card already used

        // Go on within the turn
        this.currentPlayerIndex++;

        if(this.currentPlayerIndex == this.players.size()-1){ //last player: all players has played their AssistantCard. No I can set the order
            this.changeTurnOrder(); // reset the order of the Players according to the values of the AssistantCards
            this.currentPlayerIndex = 0; // the new turn will start
        }
        return true;
    }

    private boolean manageStudentHallToDiningRoom(MessageStudentHallToDiningRoom message){
        String nicknamePlayer = message.getNicknamePlayer();
        SPColour studentColour = mapStringToSPColour(message.getColour());

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(controllerIntegrity.checkStudentHallToDiningRoom(this.players.get(this.currentPlayerIndex), studentColour)){
            if(this.advanced){
                //TODO: surrounded with try catch just to remove errors. It needs to be checked before leaving it like that
                try {
                    boardAdvanced.moveStudentHallToDiningRoom(this.players.get(this.currentPlayerIndex), studentColour);
                } catch (StudentNotFoundException e) {
                    e.printStackTrace();
                } catch (ExceededMaxStudentsDiningRoomException e) {
                    e.printStackTrace();
                } catch (EmptyCaveauExcepion e) {
                    e.printStackTrace();
                } catch (ProfessorNotFoundException e) {
                    e.printStackTrace();
                } catch (NoProfessorBagException e) {
                    e.printStackTrace();
                }
            } else{
                //TODO: surrounded with try catch just to remove errors. It needs to be checked before leaving it like that
                try {
                    board.moveStudentHallToDiningRoom(this.players.get(this.currentPlayerIndex), studentColour);
                } catch (StudentNotFoundException e) {
                    e.printStackTrace();
                } catch (ExceededMaxStudentsDiningRoomException e) {
                    e.printStackTrace();
                } catch (EmptyCaveauExcepion e) {
                    e.printStackTrace();
                } catch (ProfessorNotFoundException e) {
                    e.printStackTrace();
                } catch (NoProfessorBagException e) {
                    e.printStackTrace();
                }
            }
            this.numStudentsToMoveCurrent--;
            if(this.numStudentsToMoveCurrent == 0 || // all possible Students moved
                    this.board.getPlayerSchool(this.players.get(this.currentPlayerIndex)).getStudentsHall().size() == 0){ // no Students remained
                this.numStudentsToMoveCurrent = this.numStudentsToMove;
                controllerState.setState(State.ACTION2);
            }
            return true;
        }
        return false;
    }

    private boolean manageStudentToArchipelago(MessageStudentToArchipelago message){
        String nicknamePlayer = message.getNicknamePlayer();
        SPColour studentColour = mapStringToSPColour(message.getColour());
        int destArchipelagoIndex = message.getDestArchipelagoIndex();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(controllerIntegrity.checkStudentToArchipelago(this.players.get(this.currentPlayerIndex), studentColour, destArchipelagoIndex)){
            //TODO: surrounded with try catch just to remove errors. It needs to be checked before leaving it like that
            try {
                board.moveStudentSchoolToArchipelagos(this.players.get(this.currentPlayerIndex), studentColour, destArchipelagoIndex);
            } catch (StudentNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private boolean manageMoveMotherNature(MessageMoveMotherNature message){
        String nicknamePlayer = message.getNicknamePlayer();
        int moves = message.getMoves();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(controllerIntegrity.checkMoveMotherNature(this.players.get(this.currentPlayerIndex), moves)){
            board.moveMotherNature(moves);
            //TODO: surrounded with try catch just to remove errors. It needs to be checked before leaving it like that
            try {
                board.tryToConquer(this.players.get(this.currentPlayerIndex));
            } catch (InvalidTowerNumberException e) {
                e.printStackTrace();
            } catch (AnotherTowerException e) {
                e.printStackTrace();
            } catch (ExceededMaxTowersException e) {
                e.printStackTrace();
            }
            controllerState.setState(State.ACTION3);
            return true;
        }
        return false;
    }
}
