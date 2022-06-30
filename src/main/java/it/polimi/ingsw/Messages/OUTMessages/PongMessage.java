package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;
import it.polimi.ingsw.View.ClientView;

/**
 * Message that pings the client to check connection.
 */
public class PongMessage extends OUTMessage{

    /**
     * Constructor of the class that sets the type of the message.
     */
    public PongMessage() {
        super(OUTMessageType.PONG);
    }

    /**
     * Empty method because it does not need to do anything. It just respects the activeMessage pattern.
     * @param view type of view of the client. (it can be CLIView of GUIView)
     */
    @Override
    public void manageMessage(ClientView view) {}
}
