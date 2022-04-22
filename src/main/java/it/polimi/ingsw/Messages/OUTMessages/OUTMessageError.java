package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;

public class OUTMessageError extends OUTMessage {
    public OUTMessageError(String content, OUTMessageType type) {
        super(content, type);
    }
}
