package it.polimi.ingsw.Messages.INMessage;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_PLACE_ONE_STUDENT;

public class MessageCCPlaceOneStudent extends MessageCC{
    private final String nicknamePlayer;
    private final String colourToMove;
    private final int archipelagoIndexDest;

    public MessageCCPlaceOneStudent(int indexCard, String nicknamePlayer, String colourToMove, int archipelagoIndexDest) {
        super(CC_PLACE_ONE_STUDENT, indexCard);
        this.nicknamePlayer = nicknamePlayer;
        this.colourToMove = colourToMove;
        this.archipelagoIndexDest = archipelagoIndexDest;
    }

    public String getNicknamePlayer() {
        return nicknamePlayer;
    }

    public String getColourToMove() {
        return colourToMove;
    }

    public int getArchipelagoIndexDest() {
        return archipelagoIndexDest;
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkIndexCard(this.indexCard) &&
                controller.checkNickname(this.nicknamePlayer) &&
                controller.checkStudentColour(this.colourToMove) &&
                controller.checkDestinationArchipelagoIndex(this.archipelagoIndexDest));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCPlaceOneStudent(this);
    }
}
