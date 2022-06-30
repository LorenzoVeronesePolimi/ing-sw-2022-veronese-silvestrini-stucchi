package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Messages.ActiveMessageController;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;

/**
 * class that represents an input (controller POV) message
 */
public abstract class Message implements ActiveMessageController {
    private final INMessageType type;
    protected final String nickname;

    /**
     * constructor of the message of a given type, sent by the player with a given nickname
     * @param type type of input message
     * @param nickname nick of the player who sends the message
     */
    public Message(INMessageType type, String nickname){
        this.type = type;
        this.nickname = nickname;
    }

    /**
     * getter of input message type
     * @return input message type
     */
    public INMessageType getType(){
        return this.type;
    }

    /**
     * getter of player's nickname
     * @return player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * method that verifies the input of the message
     * @param controller controller
     * @return true if input is acceptable, else otherwise
     */
    public abstract boolean checkInput(ControllerInput controller);

    /**
     * method that does what the message requires
     * @param controller controller
     * @return true if the message has been successfully managed, false otherwise
     */
    @Override
    public abstract boolean manageMessage(Controller controller);
}
