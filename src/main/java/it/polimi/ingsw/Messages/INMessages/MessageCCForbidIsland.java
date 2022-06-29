package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_FORBID_ISLAND;

/**
 * input message that asks to manage the use of the ForbidIsland character card
 */
public class MessageCCForbidIsland extends Message{
    private final int archipelagoIndexToForbid;

    /**
     * constructor of the input message
     * @param nicknamePlayer nick of the player who buys the usage of the card
     * @param archipelagoIndexToForbid index of the archipelago to forbid
     */
    public MessageCCForbidIsland(String nicknamePlayer, int archipelagoIndexToForbid){
        super(CC_FORBID_ISLAND, nicknamePlayer);
        this.archipelagoIndexToForbid = archipelagoIndexToForbid;
    }

    /**
     * getter of the index of the archipelago to forbid
     * @return index of the archipelago to forbid
     */
    public int getArchipelagoIndexToForbid() {
        return archipelagoIndexToForbid;
    }

    /**
     * method that verifies the input of the message
     * @param controller controller
     * @return true if input is acceptable, else otherwise
     */
    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname) &&
                controller.checkDestinationArchipelagoIndex(this.archipelagoIndexToForbid));
    }

    /**
     * method that does what the message requires
     * @param controller controller
     * @return true if the message has been successfully managed, false otherwise
     */
    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCForbidIsland(this);
    }
}
