package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_TWO_EXTRA_POINTS;

public class MessageCCTwoExtraPoints extends Message{

    public MessageCCTwoExtraPoints(String nicknamePlayer) {
        super(CC_TWO_EXTRA_POINTS, nicknamePlayer);
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCTwoExtraPoints(this);
    }
}
