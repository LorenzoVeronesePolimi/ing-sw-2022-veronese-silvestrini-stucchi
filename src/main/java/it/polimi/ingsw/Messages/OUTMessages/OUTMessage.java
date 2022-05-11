package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Messages.ActiveMessageView;
import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;
import it.polimi.ingsw.View.ClientView;

import java.io.Serializable;

public abstract class OUTMessage implements Serializable, ActiveMessageView {
    private static final long serialVersionUID = 1L;
    private final OUTMessageType type;  // It describes correctly what needs to be done by assigning an enumeration

    public OUTMessage(OUTMessageType type) {
        this.type = type;
    }

    public OUTMessageType getType() {
        return type;
    }

    @Override
    public abstract void manageMessage(ClientView view);
}
