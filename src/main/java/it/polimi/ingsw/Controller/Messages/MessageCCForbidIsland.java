package it.polimi.ingsw.Controller.Messages;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.CC_FORBID_ISLAND;

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
}
