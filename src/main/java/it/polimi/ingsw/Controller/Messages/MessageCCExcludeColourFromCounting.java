package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Controller;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.CC_EXCLUDE_COLOUR_FROM_COUNTING;

public class MessageCCExcludeColourFromCounting extends MessageCC{
    private final String nicknamePlayer;
    private final String colourToExclude;

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

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCExcludeColourFromCounting(this);
    }
}
