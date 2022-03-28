package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Enumerations.MessageType;

public class MessageAddPlayer extends Message{
    private String nickname;
    private String colour;

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
}
