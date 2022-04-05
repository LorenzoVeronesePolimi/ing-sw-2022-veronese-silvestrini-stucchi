package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Controller;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.CC_EXTRA_STUDENT_IN_DINING;

public class MessageCCExtraStudentInDining extends MessageCC{
    private final String nicknamePlayer;
    private final String colourToMove;

    public MessageCCExtraStudentInDining(int indexCard, String nicknamePlayer, String colourToMove){
        super(CC_EXTRA_STUDENT_IN_DINING, indexCard);
        this.nicknamePlayer = nicknamePlayer;
        this.colourToMove = colourToMove;
    }

    public String getNicknamePlayer() {
        return nicknamePlayer;
    }

    public String getColourToMove() {
        return colourToMove;
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCExtraStudentInDining(this);
    }
}
