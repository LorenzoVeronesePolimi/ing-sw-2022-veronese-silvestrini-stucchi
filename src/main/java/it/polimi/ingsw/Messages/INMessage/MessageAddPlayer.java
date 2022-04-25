package it.polimi.ingsw.Messages.INMessage;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;
import it.polimi.ingsw.View.ServerView;

public class MessageAddPlayer extends Message {
    private final String nickname;
    private final String colour;
    //private final ServerView serverView;

    public MessageAddPlayer(String nickname, String colour){//, ServerView serverView
        super(INMessageType.ADD_PLAYER);
        this.nickname = nickname;
        this.colour = colour;
        //this.serverView = serverView;
    }

    public String getNickname() {
        return nickname;
    }

    public String getColour() {
        return colour;
    }

    /*public ServerView getServerView() {
        return serverView;
    }*/

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname) &&
                controller.checkPlayerColour(this.colour));
    }

    public boolean manageMessage(Controller controller){
        return controller.manageAddPlayer(this);
    }
}
