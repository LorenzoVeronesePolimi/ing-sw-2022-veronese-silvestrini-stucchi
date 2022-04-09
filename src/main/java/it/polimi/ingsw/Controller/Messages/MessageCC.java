package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Controller.Enumerations.MessageType;

public abstract class MessageCC extends Message{
    final int indexCard;

    public MessageCC(MessageType type, int indexCard){
        super(type);
        this.indexCard = indexCard - 1;
    }

    public int getIndexCard() {
        return indexCard;
    }

    public abstract boolean checkInput(ControllerInput controller);

    public abstract boolean manageMessage(Controller controller);
}
