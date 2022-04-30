package it.polimi.ingsw.Messages.INMessage;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;

public class MessageAssistantCard extends Message{
    private final String nicknamePlayer;
    private final int motherNatureMovement;
    private final int turnPriority;

    public MessageAssistantCard(String nicknamePlayer, int motherNatureMovement, int turnPriority){
        super(INMessageType.ASSISTANT_CARD);
        this.nicknamePlayer = nicknamePlayer;
        this.motherNatureMovement = motherNatureMovement;
        this.turnPriority = turnPriority;
    }

    public String getNicknamePlayer(){
        return this.nicknamePlayer;
    }

    /*public int getMotherNatureMovement() {
        return motherNatureMovement;
    } unused*/

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
