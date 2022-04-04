package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Enumerations.MessageType;

public class MessageAssistantCard extends Message{
    private final String nicknamePlayer;
    private final int motherNatureMovement;
    private final int turnPriority;

    public MessageAssistantCard(String nicknamePlayer, int motherNatureMovement, int turnPriority){
        super(MessageType.ASSISTANT_CARD);
        this.nicknamePlayer = nicknamePlayer;
        this.motherNatureMovement = motherNatureMovement;
        this.turnPriority = turnPriority;
    }

    public String getNicknamePlayer(){
        return this.nicknamePlayer;
    }

    public int getMotherNatureMovement() {
        return motherNatureMovement;
    }

    public int getTurnPriority() {
        return turnPriority;
    }
}
