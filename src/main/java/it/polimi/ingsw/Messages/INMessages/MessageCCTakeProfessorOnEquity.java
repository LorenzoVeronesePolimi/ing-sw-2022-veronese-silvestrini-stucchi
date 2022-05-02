package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_TAKE_PROFESSOR_ON_EQUITY;

public class MessageCCTakeProfessorOnEquity extends MessageCC{

    public MessageCCTakeProfessorOnEquity(int indexCard, String nicknamePlayer){
        super(CC_TAKE_PROFESSOR_ON_EQUITY, nicknamePlayer, indexCard);
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkIndexCard(this.indexCard) &&
                controller.checkNickname(this.nickname));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCTakeProfessorOnEquity(this);
    }
}
