package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Messages.ActiveMessageController;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;

public abstract class Message implements ActiveMessageController {
    private final INMessageType type;
    protected final String nickname;

    public Message(INMessageType type, String nickname){
        this.type = type;
        this.nickname = nickname;
    }

    public INMessageType getType(){
        return this.type;
    }
    public String getNickname() {
        return nickname;
    }

    public abstract boolean checkInput(ControllerInput controller);

    @Override
    public abstract boolean manageMessage(Controller controller);
}
