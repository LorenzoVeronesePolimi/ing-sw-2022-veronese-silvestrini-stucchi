package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;

public abstract class OUTMessage {
    private final String content; // An example of message for the user
    private final OUTMessageType type;  // It describes correctly what needs to be done by assigning an enumeration

    public OUTMessage(String content, OUTMessageType type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() { return this.content; }

    public OUTMessageType getType() {
        return type;
    }
}
