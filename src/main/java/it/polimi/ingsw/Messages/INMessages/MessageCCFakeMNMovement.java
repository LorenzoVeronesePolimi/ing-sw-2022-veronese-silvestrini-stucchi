package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_FAKE_MN_MOVEMENT;

public class MessageCCFakeMNMovement extends Message{
    private final int fakeMNPosition;

    public MessageCCFakeMNMovement(String nicknamePlayer, int fakeMNPosition){
        super(CC_FAKE_MN_MOVEMENT, nicknamePlayer);
        this.fakeMNPosition = fakeMNPosition;
    }

    public int getFakeMNPosition() {
        return fakeMNPosition;
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname) &&
                controller.checkDestinationArchipelagoIndex(this.fakeMNPosition));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCFakeMNMovement(this);
    }
}
