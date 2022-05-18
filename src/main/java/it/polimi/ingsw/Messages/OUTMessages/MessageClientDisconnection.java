package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;
import it.polimi.ingsw.View.ClientView;

import java.io.Serializable;

public class MessageClientDisconnection extends OUTMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    public MessageClientDisconnection() {
        super(OUTMessageType.CLIENT_DISCONNECTION);
    }



    @Override
    public void manageMessage(ClientView view) {
        view.clientDisconnectionEnd();
        view.setClientActive(false);
        view.setClientError(true);
    }
}
