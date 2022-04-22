package it.polimi.ingsw.Messages.INMessage;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;

public class MessageStudentToArchipelago extends Message{
    private final String nicknamePlayer;
    private final String colour;
    private final int destArchipelagoIndex;

    public MessageStudentToArchipelago(String nicknamePlayer, String colour, int destArchipelagoIndex){
        super(INMessageType.STUDENT_TO_ARCHIPELAGO);
        this.nicknamePlayer = nicknamePlayer;
        this.colour = colour;
        this.destArchipelagoIndex = destArchipelagoIndex;
    }

    public String getNicknamePlayer(){return this.nicknamePlayer;}

    public String getColour() {
        return colour;
    }

    public int getDestArchipelagoIndex() {
        return destArchipelagoIndex;
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nicknamePlayer) &&
                controller.checkStudentColour(this.colour) &&
                controller.checkDestinationArchipelagoIndex(this.destArchipelagoIndex));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageStudentToArchipelago(this);
    }
}
