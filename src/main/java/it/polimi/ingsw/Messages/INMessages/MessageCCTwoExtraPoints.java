package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_TWO_EXTRA_POINTS;

public class MessageCCTwoExtraPoints extends MessageCC{

    public MessageCCTwoExtraPoints(int indexCard, String nicknamePlayer) {
        super(CC_TWO_EXTRA_POINTS, nicknamePlayer, indexCard);
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkIndexCard(this.indexCard) &&
                controller.checkNickname(this.nickname));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCTwoExtraPoints(this);
    }
}
