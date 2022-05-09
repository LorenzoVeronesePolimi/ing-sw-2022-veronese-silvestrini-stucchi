package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_REDUCE_COLOUR_IN_DINING;

public class MessageCCReduceColourInDining extends Message{
    private final String colourToReduce;

    public MessageCCReduceColourInDining(String nicknamePlayer, String colourToReduce){
        super(CC_REDUCE_COLOUR_IN_DINING, nicknamePlayer);
        this.colourToReduce = colourToReduce;
    }

    public String getColourToReduce() {
        return colourToReduce;
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname) &&
                controller.checkStudentColour(this.colourToReduce));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCReduceColourInDining(this);
    }
}
