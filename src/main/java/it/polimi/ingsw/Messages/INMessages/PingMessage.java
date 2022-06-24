package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;

public class PingMessage extends Message {
    public PingMessage(String nick) {
        super(INMessageType.PING, nick);
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return true;
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return true;
    }
}
