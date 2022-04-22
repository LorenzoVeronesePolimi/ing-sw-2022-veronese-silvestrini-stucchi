package it.polimi.ingsw.Messages.INMessage;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;

public abstract class Message {
    private final INMessageType type;

    public Message(INMessageType type){
        this.type = type;
    }

    public INMessageType getType(){
        return this.type;
    }

    public abstract boolean checkInput(ControllerInput controller);

    public abstract boolean manageMessage(Controller controller);
}
