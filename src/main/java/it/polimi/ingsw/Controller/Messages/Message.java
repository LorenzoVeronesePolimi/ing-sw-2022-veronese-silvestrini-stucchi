package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Enumerations.MessageType;

public abstract class Message {
    private MessageType type;

    public Message(MessageType type){
        this.type = type;
    }

    public MessageType getType(){
        return this.type;
    }
}
