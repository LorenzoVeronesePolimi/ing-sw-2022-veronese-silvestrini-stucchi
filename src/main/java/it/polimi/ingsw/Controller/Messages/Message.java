package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Controller.Enumerations.MessageType;

public abstract class Message {
    private final MessageType type;

    public Message(MessageType type){
        this.type = type;
    }

    public MessageType getType(){
        return this.type;
    }

    public abstract boolean checkInput(ControllerInput controller);

    public abstract boolean manageMessage(Controller controller);
}
