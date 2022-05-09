package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_EXTRA_STUDENT_IN_DINING;

public class MessageCCExtraStudentInDining extends Message{
    private final String colourToMove;

    public MessageCCExtraStudentInDining(String nicknamePlayer, String colourToMove){
        super(CC_EXTRA_STUDENT_IN_DINING, nicknamePlayer);
        this.colourToMove = colourToMove;
    }


    public String getColourToMove() {
        return colourToMove;
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname)) &&
                controller.checkStudentColour(this.colourToMove);
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCExtraStudentInDining(this);
    }
}
