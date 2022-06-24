package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;
import it.polimi.ingsw.View.ClientView;

public class PongMessage extends OUTMessage{
    public PongMessage() {
        super(OUTMessageType.PONG);
    }

    @Override
    public void manageMessage(ClientView view) {

    }
}
