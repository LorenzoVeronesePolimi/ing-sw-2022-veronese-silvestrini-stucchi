package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Controller.Enumerations.MessageType;

public class MessageAddPlayer extends Message{
    private final String nickname;
    private final String colour;

    public MessageAddPlayer(String nickname, String colour){
        super(MessageType.CREATE_MATCH);
        this.nickname = nickname;
        this.colour = colour;
    }

    public String getNickname() {
        return nickname;
    }

    public String getColour() {
        return colour;
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname) &&
                controller.checkPlayerColour(this.colour));
    }

    public boolean manageMessage(Controller controller){
        return controller.manageAddPlayer(this);
    }
}
