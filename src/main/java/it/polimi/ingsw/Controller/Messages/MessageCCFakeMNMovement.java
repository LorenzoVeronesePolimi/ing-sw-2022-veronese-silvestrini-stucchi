package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Controller;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.CC_FAKE_MN_MOVEMENT;

public class MessageCCFakeMNMovement extends MessageCC{
    private final String nicknamePlayer;
    private final int fakeMNPosition;

    public MessageCCFakeMNMovement(int indexCard, String nicknamePlayer, int fakeMNPosition){
        super(CC_FAKE_MN_MOVEMENT, indexCard);
        this.nicknamePlayer = nicknamePlayer;
        this.fakeMNPosition = fakeMNPosition;
    }

    public String getNicknamePlayer() {
        return nicknamePlayer;
    }

    public int getFakeMNPosition() {
        return fakeMNPosition;
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCFakeMNMovement(this);
    }
}
