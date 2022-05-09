package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_TOWER_NO_VALUE;

public class MessageCCTowerNoValue extends Message{

    public MessageCCTowerNoValue(String nicknamePlayer) {
        super(CC_TOWER_NO_VALUE, nicknamePlayer);
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCTowerNoValue(this);
    }
}
