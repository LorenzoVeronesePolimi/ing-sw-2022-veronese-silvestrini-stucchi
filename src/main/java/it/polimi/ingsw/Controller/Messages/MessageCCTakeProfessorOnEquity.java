package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Controller;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.CC_TAKE_PROFESSOR_ON_EQUITY;

public class MessageCCTakeProfessorOnEquity extends MessageCC{
    private final String nicknamePlayer;

    public MessageCCTakeProfessorOnEquity(int indexCard, String nicknamePlayer){
        super(CC_TAKE_PROFESSOR_ON_EQUITY, indexCard);
        this.nicknamePlayer = nicknamePlayer;
    }

    public String getNicknamePlayer() {
        return nicknamePlayer;
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCTakeProfessorOnEquity(this);
    }
}
