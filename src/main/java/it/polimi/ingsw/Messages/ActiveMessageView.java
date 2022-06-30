package it.polimi.ingsw.Messages;

import it.polimi.ingsw.View.ClientView;

/**
 * Interface that defines the structure of the ActiveMessage pattern to be implemented in subclasses.
 * It defines the type of message from the server to the client (OUTMessages)
 */
public interface ActiveMessageView {

    /**
     * Method to be implemented in subclasses.
     * @param view type of view of the client. (it can be CLIView of GUIView)
     */
    void manageMessage(ClientView view);
}
