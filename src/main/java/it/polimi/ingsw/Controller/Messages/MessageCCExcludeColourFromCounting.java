package it.polimi.ingsw.Controller.Messages;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.CC_EXCLUDE_COLOUR_FROM_COUNTING;

public class MessageCCExcludeColourFromCounting extends MessageCC{
    private String nicknamePlayer;
    private String colourToExclude;

    public MessageCCExcludeColourFromCounting(int indexCard, String nicknamePlayer, String colourToExclude){
        super(CC_EXCLUDE_COLOUR_FROM_COUNTING, indexCard);
        this.nicknamePlayer = nicknamePlayer;
        this.colourToExclude = colourToExclude;
    }

    public String getNicknamePlayer() {
        return nicknamePlayer;
    }

    public String getColourToExclude() {
        return colourToExclude;
    }
}
