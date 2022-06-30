package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;
import it.polimi.ingsw.View.ClientView;

import java.io.Serializable;

/**
 * Message that informs the player that he made an error in the move just performed.
 */
public class MessageControllerError extends OUTMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor of the class that sets the type of the message.
     */
    public MessageControllerError() {
       super(OUTMessageType.CONTROLLER_ERROR);
    }

    /**
     * Method that calls the function to set the error status in the client and prints the error message.
     * @param view type of view of the client. (it can be CLIView of GUIView)
     */
    @Override
    public void manageMessage(ClientView view) {
        view.setErrorStatus(true);
        view.printErrorMessage("Controller error");
    }
}
