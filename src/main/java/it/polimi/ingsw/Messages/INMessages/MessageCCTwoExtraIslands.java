package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_TWO_EXTRA_ISLANDS;

public class MessageCCTwoExtraIslands extends Message{

    public MessageCCTwoExtraIslands(String nicknamePlayer){
        super(CC_TWO_EXTRA_ISLANDS, nicknamePlayer);
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return(controller.checkNickname(this.nickname));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCTwoExtraIslands(this);
    }
}
