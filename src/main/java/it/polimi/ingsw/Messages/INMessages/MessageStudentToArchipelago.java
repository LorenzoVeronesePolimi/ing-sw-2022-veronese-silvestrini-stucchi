package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;

/**
 * input message that asks to move a student from the hall to an archipelago
 */
public class MessageStudentToArchipelago extends Message{
    private final String colour;
    private final int destArchipelagoIndex;

    /**
     * constructor of the input message
     * @param nicknamePlayer nick of the current player
     * @param colour colour of the student that is wanted to be moved
     * @param destArchipelagoIndex index of the destination archipelago
     */
    public MessageStudentToArchipelago(String nicknamePlayer, String colour, int destArchipelagoIndex){
        super(INMessageType.STUDENT_TO_ARCHIPELAGO, nicknamePlayer);
        this.colour = colour;
        this.destArchipelagoIndex = destArchipelagoIndex;
    }

    /**
     * getter of the colour of the student that is wanted to be moved
     * @return colour of the student that is wanted to be moved
     */
    public String getColour() {
        return colour;
    }

    /**
     * getter of the index of the destination archipelago
     * @return index of the destination archipelago
     */
    public int getDestArchipelagoIndex() {
        return destArchipelagoIndex;
    }

    /**
     * method that verifies the input of the message
     * @param controller controller
     * @return true if input is acceptable, else otherwise
     */
    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname) &&
                controller.checkStudentColour(this.colour) &&
                controller.checkDestinationArchipelagoIndex(this.destArchipelagoIndex));
    }

    /**
     * method that does what the message requires
     * @param controller controller
     * @return true if the message has been successfully managed, false otherwise
     */
    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageStudentToArchipelago(this);
    }
}
