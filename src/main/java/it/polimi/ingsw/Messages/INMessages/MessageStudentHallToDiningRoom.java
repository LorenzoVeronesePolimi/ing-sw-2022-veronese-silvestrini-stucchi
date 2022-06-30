package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.STUDENT_HALL_TO_DINING_ROOM;

/**
 * input message that asks to move a student from the hall to the dining room
 */
public class MessageStudentHallToDiningRoom extends Message{
    private final String colour;

    /**
     * constructor of the message
     * @param nicknamePlayer nick of the current player
     * @param colour colour of the student that is wanted to be moved
     */
    public MessageStudentHallToDiningRoom(String nicknamePlayer, String colour){
        super(STUDENT_HALL_TO_DINING_ROOM, nicknamePlayer);
        this.colour = colour;
    }

    /**
     * getter of the colour of the student that is wanted to be moved
     * @return colour of the student that is wanted to be moved
     */
    public String getColour() {
        return colour;
    }

    /**
     * method that verifies the input of the message
     * @param controller controller
     * @return true if input is acceptable, else otherwise
     */
    @Override
    public boolean checkInput(ControllerInput controller) {
        return(controller.checkNickname(this.nickname) &&
                controller.checkStudentColour(this.colour));
    }

    /**
     * method that does what the message requires
     * @param controller controller
     * @return true if the message has been successfully managed, false otherwise
     */
    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageStudentHallToDiningRoom(this);
    }
}
