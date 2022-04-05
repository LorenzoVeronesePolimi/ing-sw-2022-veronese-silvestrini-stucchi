package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Controller;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.CC_TWO_EXTRA_ISLANDS;

public class MessageCCTwoExtraIslands extends MessageCC{
    private final String nicknamePlayer;

    public MessageCCTwoExtraIslands(int indexCard, String nicknamePlayer){
        super(CC_TWO_EXTRA_ISLANDS, indexCard);
        this.nicknamePlayer = nicknamePlayer;
    }

    public String getNicknamePlayer() {
        return nicknamePlayer;
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCTwoExtraIslands(this);
    }
}
