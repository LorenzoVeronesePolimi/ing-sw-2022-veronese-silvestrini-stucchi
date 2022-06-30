package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;
import it.polimi.ingsw.View.ClientView;

import java.io.Serializable;

/**
 * Message that asks the client if he wants to play with CLI of GUI interface.
 */
public class MessageCLIorGUI extends OUTMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor of the class that sets the type of the message.
     */
    public MessageCLIorGUI() {
        super(OUTMessageType.ASK_CLI_GUI);
    }

    /**
     * Method that calls the function askCLIorGUI to execute the message.
     * @param view type of view of the client. (it can be CLIView of GUIView)
     */
    @Override
    public void manageMessage(ClientView view) {
        view.askCLIorGUI();
    }
}
