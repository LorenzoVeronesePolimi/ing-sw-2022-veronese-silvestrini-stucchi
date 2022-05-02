package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;
import it.polimi.ingsw.View.ClientView;

import java.io.Serializable;

public class MessageFirstPlayer extends OUTMessage implements Serializable {
    public MessageFirstPlayer() {
        super(OUTMessageType.ASK_FIRST_PLAYER);
    }

    @Override
    public void manageMessage(ClientView view) {
        view.askFirstPlayerInfo();
    }
}
