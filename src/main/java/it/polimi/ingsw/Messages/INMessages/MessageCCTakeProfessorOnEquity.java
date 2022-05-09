package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_TAKE_PROFESSOR_ON_EQUITY;

public class MessageCCTakeProfessorOnEquity extends Message{

    public MessageCCTakeProfessorOnEquity(String nicknamePlayer){
        super(CC_TAKE_PROFESSOR_ON_EQUITY, nicknamePlayer);
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCTakeProfessorOnEquity(this);
    }
}
