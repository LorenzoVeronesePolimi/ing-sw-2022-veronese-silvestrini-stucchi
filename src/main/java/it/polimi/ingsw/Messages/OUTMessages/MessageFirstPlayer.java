package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;
import it.polimi.ingsw.View.ClientView;

import java.io.Serializable;

/**
 * Message that asks the first player for the match info.
 */
public class MessageFirstPlayer extends OUTMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor of the class that sets the type of the message.
     */
    public MessageFirstPlayer() {
        super(OUTMessageType.ASK_FIRST_PLAYER);
    }

    /**
     * Method that calls the function to ask the client info about the match.
     * @param view type of view of the client. (it can be CLIView of GUIView)
     */
    @Override
    public void manageMessage(ClientView view) {
        view.askFirstPlayerInfo();
    }
}
