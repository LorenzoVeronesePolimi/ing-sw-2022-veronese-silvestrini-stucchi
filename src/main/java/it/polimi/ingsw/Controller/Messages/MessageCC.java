package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Enumerations.MessageType;

public class MessageCC extends Message{
    int indexCard;

    public MessageCC(MessageType type, int indexCard){
        super(type);
        this.indexCard = indexCard - 1;
    }

    public int getIndexCard() {
        return indexCard;
    }
}
