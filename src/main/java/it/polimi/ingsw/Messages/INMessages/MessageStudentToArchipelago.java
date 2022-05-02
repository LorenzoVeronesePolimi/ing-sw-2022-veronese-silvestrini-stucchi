package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;

public class MessageStudentToArchipelago extends Message{
    private final String colour;
    private final int destArchipelagoIndex;

    public MessageStudentToArchipelago(String nicknamePlayer, String colour, int destArchipelagoIndex){
        super(INMessageType.STUDENT_TO_ARCHIPELAGO, nicknamePlayer);
        this.colour = colour;
        this.destArchipelagoIndex = destArchipelagoIndex;
    }

    public String getColour() {
        return colour;
    }

    public int getDestArchipelagoIndex() {
        return destArchipelagoIndex;
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname) &&
                controller.checkStudentColour(this.colour) &&
                controller.checkDestinationArchipelagoIndex(this.destArchipelagoIndex));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageStudentToArchipelago(this);
    }
}
