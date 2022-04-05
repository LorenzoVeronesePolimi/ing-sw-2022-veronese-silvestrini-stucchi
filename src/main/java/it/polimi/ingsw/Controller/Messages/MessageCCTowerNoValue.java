package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Controller;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.CC_TOWER_NO_VALUE;

public class MessageCCTowerNoValue extends MessageCC{
    private final String nicknamePlayer;

    public MessageCCTowerNoValue(int indexCard, String nicknamePlayer) {
        super(CC_TOWER_NO_VALUE, indexCard);
        this.nicknamePlayer = nicknamePlayer;
    }

    public String getNicknamePlayer() {
        return nicknamePlayer;
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCTowerNoValue(this);
    }
}
