package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_TOWER_NO_VALUE;

public class MessageCCTowerNoValue extends MessageCC{

    public MessageCCTowerNoValue(int indexCard, String nicknamePlayer) {
        super(CC_TOWER_NO_VALUE, nicknamePlayer, indexCard);
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkIndexCard(this.indexCard) &&
                controller.checkNickname(this.nickname));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCTowerNoValue(this);
    }
}
