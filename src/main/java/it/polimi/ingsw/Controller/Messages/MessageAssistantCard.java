package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Enumerations.MessageType;

public class MessageAssistantCard extends Message{
    private int motherNatureMovement;
    private int turnPriority;

    public MessageAssistantCard(int motherNatureMovement, int turnPriority){
        super(MessageType.ASSISTANT_CARD);
        this.motherNatureMovement = motherNatureMovement;
        this.turnPriority = turnPriority;
    }

    public int getMotherNatureMovement() {
        return motherNatureMovement;
    }

    public int getTurnPriority() {
        return turnPriority;
    }
}
