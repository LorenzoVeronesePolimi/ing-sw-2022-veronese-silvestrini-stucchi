package it.polimi.ingsw.Controller.Messages;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.STUDENT_HALL_TO_DINING_ROOM;

public class MessageStudentHallToDiningRoom extends Message{
    private String nicknamePlayer;
    private String colour;

    public MessageStudentHallToDiningRoom(String nicknamePlayer, String colour){
        super(STUDENT_HALL_TO_DINING_ROOM);
        this.nicknamePlayer = nicknamePlayer;
        this.colour = colour;
    }

    public String getNicknamePlayer() {
        return nicknamePlayer;
    }

    public String getColour() {
        return colour;
    }
}
