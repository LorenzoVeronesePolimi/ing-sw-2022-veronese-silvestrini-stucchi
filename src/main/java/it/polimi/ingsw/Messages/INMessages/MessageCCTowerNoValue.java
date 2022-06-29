package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_TOWER_NO_VALUE;

/**
 * input message that asks to manage the use of the towerNoValue character card
 */
public class MessageCCTowerNoValue extends Message{

    /**
     * constructor of the input message
     * @param nicknamePlayer nick of the player who buys the usage of the card
     */
    public MessageCCTowerNoValue(String nicknamePlayer) {
        super(CC_TOWER_NO_VALUE, nicknamePlayer);
    }

    /**
     * method that verifies the input of the message
     * @param controller controller
     * @return true if input is acceptable, else otherwise
     */
    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname));
    }

    /**
     * method that does what the message requires
     * @param controller controller
     * @return true if the message has been successfully managed, false otherwise
     */
    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCTowerNoValue(this);
    }
}
