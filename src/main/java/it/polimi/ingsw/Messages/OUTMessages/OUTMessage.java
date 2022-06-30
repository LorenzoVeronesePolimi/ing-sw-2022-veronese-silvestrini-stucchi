package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Messages.ActiveMessageView;
import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;
import it.polimi.ingsw.View.ClientView;

import java.io.Serializable;

/**
 * Abstract class that is used to define the type of messages from the Server
 */
public abstract class OUTMessage implements Serializable, ActiveMessageView {
    private static final long serialVersionUID = 1L;
    private final OUTMessageType type;  // It describes correctly what needs to be done by assigning an enumeration

    /**
     * Constructor of the class that sets the type of the message.
     */
    public OUTMessage(OUTMessageType type) {
        this.type = type;
    }

    /**
     * Method to get the type of specific message.
     * @return the type of the message.
     */
    public OUTMessageType getType() {
        return type;
    }

    /**
     * Abstract method to be implemented in subclasses.
     * @param view type of view of the client. (it can be CLIView of GUIView)
     */
    @Override
    public abstract void manageMessage(ClientView view);
}
