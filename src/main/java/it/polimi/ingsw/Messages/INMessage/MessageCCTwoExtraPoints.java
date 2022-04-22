package it.polimi.ingsw.Messages.INMessage;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_TWO_EXTRA_POINTS;

public class MessageCCTwoExtraPoints extends MessageCC{
    private final String nicknamePlayer;

    public MessageCCTwoExtraPoints(int indexCard, String nicknamePlayer) {
        super(CC_TWO_EXTRA_POINTS, indexCard);
        this.nicknamePlayer = nicknamePlayer;
    }

    public String getNicknamePlayer() {
        return nicknamePlayer;
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkIndexCard(this.indexCard) &&
                controller.checkNickname(this.nicknamePlayer));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCTwoExtraPoints(this);
    }
}
