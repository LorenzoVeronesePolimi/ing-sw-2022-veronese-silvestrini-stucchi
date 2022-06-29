package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_EXTRA_STUDENT_IN_DINING;

/**
 * input message that asks to manage the use of the ExtraStudentInDining character card
 */
public class MessageCCExtraStudentInDining extends Message{
    private final String colourToMove;

    /**
     * constructor of the input message
     * @param nicknamePlayer nick of the player who buys the usage of the card
     * @param colourToMove colour of the student to pick from the card
     */
    public MessageCCExtraStudentInDining(String nicknamePlayer, String colourToMove){
        super(CC_EXTRA_STUDENT_IN_DINING, nicknamePlayer);
        this.colourToMove = colourToMove;
    }

    /**
     * getter of the colour to pick from the card
     * @return colour to pick from the card
     */
    public String getColourToMove() {
        return colourToMove;
    }

    /**
     * method that verifies the input of the message
     * @param controller controller
     * @return true if input is acceptable, else otherwise
     */
    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname)) &&
                controller.checkStudentColour(this.colourToMove);
    }

    /**
     * method that does what the message requires
     * @param controller controller
     * @return true if the message has been successfully managed, false otherwise
     */
    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCExtraStudentInDining(this);
    }
}
