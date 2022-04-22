package it.polimi.ingsw.Messages.INMessage;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;

public abstract class MessageCC extends Message{
    final int indexCard;

    public MessageCC(INMessageType type, int indexCard){
        super(type);
        this.indexCard = indexCard - 1;
    }

    public int getIndexCard() {
        return indexCard;
    }

    public abstract boolean checkInput(ControllerInput controller);

    public abstract boolean manageMessage(Controller controller);
}
