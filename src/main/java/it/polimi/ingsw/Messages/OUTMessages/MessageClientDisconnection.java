package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;
import it.polimi.ingsw.View.ClientView;

import java.io.Serializable;

/**
 * Message that informs the client that an error occurred, and it needs to disconnect.
 */
public class MessageClientDisconnection extends OUTMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor of the class that sets the type of the message.
     */
    public MessageClientDisconnection() {
        super(OUTMessageType.CLIENT_DISCONNECTION);
    }

    /**
     * Method that calls the function clientDisconnectionEnd to execute the message, and sets some disconnection parameters
     * in the view class.
     * @param view type of view of the client. (it can be CLIView of GUIView)
     */
    @Override
    public void manageMessage(ClientView view) {
        view.clientDisconnectionEnd();
        view.setClientActive(false);
        view.enablePinger(false);
    }
}
