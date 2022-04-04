package it.polimi.ingsw.Controller.Messages;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.CC_TWO_EXTRA_POINTS;

public class MessageCCTwoExtraPoints extends MessageCC{
    private final String nicknamePlayer;

    public MessageCCTwoExtraPoints(int indexCard, String nicknamePlayer) {
        super(CC_TWO_EXTRA_POINTS, indexCard);
        this.nicknamePlayer = nicknamePlayer;
    }

    public String getNicknamePlayer() {
        return nicknamePlayer;
    }
}
