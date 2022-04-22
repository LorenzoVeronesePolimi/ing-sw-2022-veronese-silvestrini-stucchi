package it.polimi.ingsw.Messages.INMessage;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_FORBID_ISLAND;

public class MessageCCForbidIsland extends MessageCC{
    private final String nicknamePlayer;
    private final int archipelagoIndexToForbid;

    public MessageCCForbidIsland(int indexCard, String nicknamePlayer, int archipelagoIndexToForbid){
        super(CC_FORBID_ISLAND, indexCard);
        this.nicknamePlayer = nicknamePlayer;
        this.archipelagoIndexToForbid = archipelagoIndexToForbid;
    }

    public String getNicknamePlayer() {
        return nicknamePlayer;
    }

    public int getArchipelagoIndexToForbid() {
        return archipelagoIndexToForbid;
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkIndexCard(this.indexCard) &&
                controller.checkNickname(this.nicknamePlayer) &&
                controller.checkDestinationArchipelagoIndex(this.archipelagoIndexToForbid));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCForbidIsland(this);
    }
}
