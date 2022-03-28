package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Controller.Messages.Message;
import it.polimi.ingsw.Controller.Messages.MessageAddPlayer;
import it.polimi.ingsw.Controller.Messages.MessageCreateMatch;
import it.polimi.ingsw.Controller.Messages.MessageStudentToArchipelago;
import it.polimi.ingsw.Model.Board.Board;
import it.polimi.ingsw.Model.Board.BoardAbstract;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Board.BoardFactory;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.View.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

// This is the main Controller: it coordinates all the others
public class Controller implements Observer {
    private int numPlayers;
    private boolean advanced;
    private Board board;
    private BoardAdvanced boardAdvanced; //null if advanced=0
    private List<Player> players;

    private Player currentPlayer;

    private ControllerInput controllerInput;
    private ControllerState controllerState;
    private ControllerIntegrity controllerIntegrity;


    public Controller(){
        this.players = new ArrayList<>();
        this.controllerInput = new ControllerInput();
        this.controllerState = new ControllerState();
        this.controllerIntegrity = new ControllerIntegrity();
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

            case STUDENT_TO_ARCHIPELAGO:
                if(!this.manageStudentToArchipelago((MessageStudentToArchipelago)message)){
                    System.out.println("You can't move a Student in that way");
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

    private void initMatch(){
        BoardFactory factory = new BoardFactory(this.players);
        this.board = factory.createBoard();

        if(this.advanced){
            this.boardAdvanced = new BoardAdvanced((BoardAbstract) this.board);
        }

        this.board.initializeBoard();
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

        // no integrity to check

        Player player = new Player(nickname, colour);
        this.players.add(player);

        if(this.players.size() == numPlayers){ // The requested number of players has been reached: let's go on
            controllerState.setState(State.PIANIFICATION2);
            //TODO: start game
        }

        return true;
    }

    private boolean manageStudentToArchipelago(MessageStudentToArchipelago message){
        SPColour studentColour = mapStringToSPColour(message.getColour());
        int destArchipelagoIndex = message.getDestArchipelagoIndex();

        if(controllerIntegrity.checkStudentToArchipelago(this.currentPlayer, studentColour, destArchipelagoIndex)){
            board.moveStudentSchoolToArchipelagos(this.currentPlayer, studentColour, destArchipelagoIndex);
            return true;
        }
        return false;
    }
}
