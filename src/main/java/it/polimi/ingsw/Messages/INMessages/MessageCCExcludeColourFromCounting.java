package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_EXCLUDE_COLOUR_FROM_COUNTING;

public class MessageCCExcludeColourFromCounting extends MessageCC{
    private final String colourToExclude;

    public MessageCCExcludeColourFromCounting(int indexCard, String nicknamePlayer, String colourToExclude){
        super(CC_EXCLUDE_COLOUR_FROM_COUNTING, nicknamePlayer, indexCard);
        this.colourToExclude = colourToExclude;
    }

    public String getColourToExclude() {
        return colourToExclude;
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkIndexCard(this.indexCard) &&
                controller.checkNickname(this.nickname)) &&
                controller.checkStudentColour(this.colourToExclude);
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCExcludeColourFromCounting(this);
    }
}