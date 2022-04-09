package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
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

    @Override
    public boolean checkInput(ControllerInput controller) {
        return(controller.checkNickname(this.nicknamePlayer) &&
                controller.checkMotherNatureMovement(this.motherNatureMovement) &&
                controller.checkTurnPriority(this.turnPriority));
    }

    public boolean manageMessage(Controller controller) {
        return controller.manageAssistantCard(this);
    }
}
