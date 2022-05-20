package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;
import it.polimi.ingsw.Messages.INMessages.Message;
import it.polimi.ingsw.View.ClientView;

public class Ping extends Message {

    public Ping(String nick) {
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
