package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;

/**
 * input message that asks to manage the use of a character card
 */
public abstract class MessageCC extends Message{
    /**
     * constructor of the message
     * @param type type of chosen character card
     * @param nickname nick of the player who buys the usage of the card
     */
    public MessageCC(INMessageType type, String nickname){
        super(type, nickname);
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
    public abstract boolean manageMessage(Controller controller);
}
