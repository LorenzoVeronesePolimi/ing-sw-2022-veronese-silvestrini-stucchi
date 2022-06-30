package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_PLACE_ONE_STUDENT;

/**
 * input message that asks to manage the use of the PlaceOneStudent character card
 */
public class MessageCCPlaceOneStudent extends Message{
    private final String colourToMove;
    private final int archipelagoIndexDest;

    /**
     * constructor of the input message
     * @param nicknamePlayer nick of the player who buys the usage of the card
     * @param archipelagoIndexDest index of the archipelago where the student must be put
     * @param colourToMove colour of the student to move from the card to the chosen archipelago
     */
    public MessageCCPlaceOneStudent(String nicknamePlayer, String colourToMove, int archipelagoIndexDest) {
        super(CC_PLACE_ONE_STUDENT, nicknamePlayer);
        this.colourToMove = colourToMove;
        this.archipelagoIndexDest = archipelagoIndexDest;
    }

    /**
     * getter of the colour of the student to move from the card to the archipelago
     * @return colour of the student to move from the card to the archipelago
     */
    public String getColourToMove() {
        return colourToMove;
    }

    /**
     * getter of the index of the archipelago of destination
     * @return
     */
    public int getArchipelagoIndexDest() {
        return archipelagoIndexDest;
    }

    /**
     * method that verifies the input of the message
     * @param controller controller
     * @return true if input is acceptable, else otherwise
     */
    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname) &&
                controller.checkStudentColour(this.colourToMove) &&
                controller.checkDestinationArchipelagoIndex(this.archipelagoIndexDest));
    }

    /**
     * method that does what the message requires
     * @param controller controller
     * @return true if the message has been successfully managed, false otherwise
     */
    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCPlaceOneStudent(this);
    }
}
