package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.STUDENT_HALL_TO_DINING_ROOM;

public class MessageStudentHallToDiningRoom extends Message{
    private final String colour;

    public MessageStudentHallToDiningRoom(String nicknamePlayer, String colour){
        super(STUDENT_HALL_TO_DINING_ROOM, nicknamePlayer);
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return(controller.checkNickname(this.nickname) &&
                controller.checkStudentColour(this.colour));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageStudentHallToDiningRoom(this);
    }
}
