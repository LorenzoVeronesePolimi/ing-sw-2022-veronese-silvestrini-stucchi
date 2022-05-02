package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Controller.Enumerations.ControllerErrorType;
import it.polimi.ingsw.Controller.Exceptions.ControllerException;
import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;
import it.polimi.ingsw.View.ClientView;


public class MessageControllerError extends OUTMessage{

    public MessageControllerError() {
       super(OUTMessageType.CONTROLLER_ERROR);
    }

    @Override
    public void manageMessage(ClientView view) {
        view.setErrorStatus(true);
        //view.printErrorMessage();
    }
}
