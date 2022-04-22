package it.polimi.ingsw.Messages.INMessage;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.STUDENT_HALL_TO_DINING_ROOM;

public class MessageStudentHallToDiningRoom extends Message{
    private final String nicknamePlayer;
    private final String colour;

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

    @Override
    public boolean checkInput(ControllerInput controller) {
        return(controller.checkNickname(this.nicknamePlayer) &&
                controller.checkStudentColour(this.colour));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageStudentHallToDiningRoom(this);
    }
}
