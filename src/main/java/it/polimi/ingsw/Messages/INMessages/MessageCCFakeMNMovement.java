package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_FAKE_MN_MOVEMENT;

public class MessageCCFakeMNMovement extends MessageCC{
    private final int fakeMNPosition;

    public MessageCCFakeMNMovement(int indexCard, String nicknamePlayer, int fakeMNPosition){
        super(CC_FAKE_MN_MOVEMENT, nicknamePlayer, indexCard);
        this.fakeMNPosition = fakeMNPosition;
    }

    public int getFakeMNPosition() {
        return fakeMNPosition;
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkIndexCard(this.indexCard) &&
                controller.checkNickname(this.nickname) &&
                controller.checkDestinationArchipelagoIndex(this.fakeMNPosition));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCFakeMNMovement(this);
    }
}
