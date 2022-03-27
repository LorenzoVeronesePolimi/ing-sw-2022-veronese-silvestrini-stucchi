package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Controller.Messages.Message;
import it.polimi.ingsw.Controller.Messages.MessageStudentToArchipelago;
import it.polimi.ingsw.Model.Board.Board;
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
    private List<Player> players;

    private Player currentPlayer; //TODO: manage Players

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
            case STUDENT_TO_ARCHIPELAGO:
                this.manageStudentToArchipelago((MessageStudentToArchipelago)message);

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

    private void manageStudentToArchipelago(MessageStudentToArchipelago message){
        SPColour studentColour = mapStringToSPColour(message.getColour());
        int destArchipelagoIndex = message.getDestArchipelagoIndex();

        controllerIntegrity.checkStudentToArchipelago(currentPlayer, studentColour, destArchipelagoIndex);
    }
}
