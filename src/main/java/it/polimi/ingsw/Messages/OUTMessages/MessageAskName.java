package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;
import it.polimi.ingsw.View.ClientView;

import java.io.Serializable;

public class MessageAskName extends OUTMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    public MessageAskName() {
        super(OUTMessageType.ASK_NICKNAME);
    }

    @Override
    public void manageMessage(ClientView view) {
        view.askNickName();
    }
}
