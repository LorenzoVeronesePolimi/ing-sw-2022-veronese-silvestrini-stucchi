package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_REDUCE_COLOUR_IN_DINING;

/**
 * input message that asks to manage the use of the ReduceColourInDining character card
 */
public class MessageCCReduceColourInDining extends Message{
    private final String colourToReduce;

    /**
     * constructor of the input message
     * @param nicknamePlayer nick of the player who buys the usage of the card
     * @param colourToReduce colour of the students to reduce in every dining room
     */
    public MessageCCReduceColourInDining(String nicknamePlayer, String colourToReduce){
        super(CC_REDUCE_COLOUR_IN_DINING, nicknamePlayer);
        this.colourToReduce = colourToReduce;
    }

    /**
     * getter of the colour to reduce
     * @return colour to reduce
     */
    public String getColourToReduce() {
        return colourToReduce;
    }

    /**
     * method that verifies the input of the message
     * @param controller controller
     * @return true if input is acceptable, else otherwise
     */
    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname) &&
                controller.checkStudentColour(this.colourToReduce));
    }

    /**
     * method that does what the message requires
     * @param controller controller
     * @return true if the message has been successfully managed, false otherwise
     */
    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCReduceColourInDining(this);
    }
}
