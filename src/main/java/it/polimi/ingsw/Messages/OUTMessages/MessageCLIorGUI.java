package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;
import it.polimi.ingsw.View.ClientView;

import java.io.Serializable;

public class MessageCLIorGUI extends OUTMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    public MessageCLIorGUI() {
        super(OUTMessageType.ASK_CLI_GUI);
    }

    @Override
    public void manageMessage(ClientView view) {
        view.askCLIorGUI();
    }
}
