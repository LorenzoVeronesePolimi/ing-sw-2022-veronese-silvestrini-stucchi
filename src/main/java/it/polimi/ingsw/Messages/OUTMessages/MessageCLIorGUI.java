package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;
import it.polimi.ingsw.View.ClientView;

import java.io.Serializable;

public class MessageCLIorGUI extends OUTMessage implements Serializable {
    public MessageCLIorGUI() {
        super(OUTMessageType.ASK_CLI_GUI);
    }

    @Override
    public void manageMessage(ClientView view) {
        view.askCLIorGUI();
    }
}
