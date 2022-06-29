package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.MOVE_MOTHER_NATURE;

/**
 * input message that asks to move mother nature
 */
public class MessageMoveMotherNature extends Message{
    private final int moves;

    /**
     * constructor of the input message
     * @param nicknamePlayer nick of the current player
     * @param moves number of archipelagos that mover nature moves through
     */
    public MessageMoveMotherNature(String nicknamePlayer, int moves){
        super(MOVE_MOTHER_NATURE, nicknamePlayer);
        this.moves = moves;
    }

    /**
     * getter of the number of archipelagos that mover nature moves through
     * @return number of archipelagos that mover nature moves through
     */
    public int getMoves() {
        return moves;
    }

    /**
     * method that verifies the input of the message
     * @param controller controller
     * @return true if input is acceptable, else otherwise
     */
    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname) &&
                controller.checkMotherNatureMovement(this.moves));
    }

    /**
     * method that does what the message requires
     * @param controller controller
     * @return true if the message has been successfully managed, false otherwise
     */
    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageMoveMotherNature(this);
    }
}
