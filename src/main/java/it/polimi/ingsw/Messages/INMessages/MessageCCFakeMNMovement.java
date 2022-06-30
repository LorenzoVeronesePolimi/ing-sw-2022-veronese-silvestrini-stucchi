package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_FAKE_MN_MOVEMENT;

/**
 * input message that asks to manage the use of the FakeMNMovement character card
 */
public class MessageCCFakeMNMovement extends Message{
    private final int fakeMNPosition;

    /**
     * constructor of the input message
     * @param nicknamePlayer nick of the player who buys the usage of the card
     * @param fakeMNPosition index of the archipelago where mother nature has to fake move
     */
    public MessageCCFakeMNMovement(String nicknamePlayer, int fakeMNPosition){
        super(CC_FAKE_MN_MOVEMENT, nicknamePlayer);
        this.fakeMNPosition = fakeMNPosition;
    }

    /**
     * getter of the archipelago where mother nature has to fake move
     * @return archipelago where mother nature has to fake move
     */
    public int getFakeMNPosition() {
        return fakeMNPosition;
    }

    /**
     * method that verifies the input of the message
     * @param controller controller
     * @return true if input is acceptable, else otherwise
     */
    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname) &&
                controller.checkDestinationArchipelagoIndex(this.fakeMNPosition));
    }

    /**
     * method that does what the message requires
     * @param controller controller
     * @return true if the message has been successfully managed, false otherwise
     */
    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCFakeMNMovement(this);
    }
}
