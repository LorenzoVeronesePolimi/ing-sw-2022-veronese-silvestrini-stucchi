package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.MOVE_MOTHER_NATURE;

public class MessageMoveMotherNature extends Message{
    private final int moves;

    public MessageMoveMotherNature(String nicknamePlayer, int moves){
        super(MOVE_MOTHER_NATURE, nicknamePlayer);
        this.moves = moves;
    }

    public int getMoves() {
        return moves;
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname) &&
                controller.checkMotherNatureMovement(this.moves));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageMoveMotherNature(this);
    }
}
