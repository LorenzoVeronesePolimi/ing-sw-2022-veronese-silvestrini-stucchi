package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;

public class MessageAssistantCard extends Message{
    private final int motherNatureMovement;
    private final int turnPriority;

    public MessageAssistantCard(String nicknamePlayer, int motherNatureMovement, int turnPriority){
        super(INMessageType.ASSISTANT_CARD, nicknamePlayer);
        this.motherNatureMovement = motherNatureMovement;
        this.turnPriority = turnPriority;
    }

    /*public int getMotherNatureMovement() {
        return motherNatureMovement;
    } unused*/

    public int getTurnPriority() {
        return turnPriority;
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return(controller.checkNickname(this.nickname) &&
                controller.checkMotherNatureMovement(this.motherNatureMovement) &&
                controller.checkTurnPriority(this.turnPriority));
    }

    public boolean manageMessage(Controller controller) {
        return controller.manageAssistantCard(this);
    }
}
