package it.polimi.ingsw.Messages;

import it.polimi.ingsw.Controller.Controller;

/**
 * Interface that defines the structure of the ActiveMessage pattern to be implemented in subclasses.
 * It defines the type of message from the client to the server (INMessages)
 */
public interface ActiveMessageController {

    /**
     * Method to be implemented in subclasses.
     * @param controller controller of the game that needs to perform the message actions.
     */
    boolean manageMessage(Controller controller);
}
