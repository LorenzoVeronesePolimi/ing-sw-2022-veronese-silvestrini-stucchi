package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;

public abstract class MessageCC extends Message{
    public MessageCC(INMessageType type, String nickname){
        super(type, nickname);
    }


    public abstract boolean checkInput(ControllerInput controller);

    public abstract boolean manageMessage(Controller controller);
}
