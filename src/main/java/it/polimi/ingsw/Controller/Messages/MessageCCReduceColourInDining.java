package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Controller;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.CC_REDUCE_COLOUR_IN_DINING;

public class MessageCCReduceColourInDining extends MessageCC{
    private final String nicknamePlayer;
    private final String colourToReduce;

    public MessageCCReduceColourInDining(int indexCard, String nicknamePlayer, String colourToReduce){
        super(CC_REDUCE_COLOUR_IN_DINING, indexCard);
        this.nicknamePlayer = nicknamePlayer;
        this.colourToReduce = colourToReduce;
    }

    public String getNicknamePlayer() {
        return nicknamePlayer;
    }

    public String getColourToReduce() {
        return colourToReduce;
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCReduceColourInDining(this);
    }
}
