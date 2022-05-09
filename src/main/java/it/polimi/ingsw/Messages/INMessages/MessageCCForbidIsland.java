package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_FORBID_ISLAND;

public class MessageCCForbidIsland extends Message{
    private final int archipelagoIndexToForbid;

    public MessageCCForbidIsland(String nicknamePlayer, int archipelagoIndexToForbid){
        super(CC_FORBID_ISLAND, nicknamePlayer);
        this.archipelagoIndexToForbid = archipelagoIndexToForbid;
    }


    public int getArchipelagoIndexToForbid() {
        return archipelagoIndexToForbid;
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname) &&
                controller.checkDestinationArchipelagoIndex(this.archipelagoIndexToForbid));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCForbidIsland(this);
    }
}
