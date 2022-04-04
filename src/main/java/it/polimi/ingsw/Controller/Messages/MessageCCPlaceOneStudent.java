package it.polimi.ingsw.Controller.Messages;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.CC_PLACE_ONE_STUDENT;

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
}
