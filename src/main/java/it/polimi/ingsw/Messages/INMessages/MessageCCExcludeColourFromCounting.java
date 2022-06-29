package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CC_EXCLUDE_COLOUR_FROM_COUNTING;

/**
 * input message that asks to manage the use of the ExcludeColourFromCounting character card
 */
public class MessageCCExcludeColourFromCounting extends Message{
    private final String colourToExclude;

    /**
     * constructor of the input message
     * @param nicknamePlayer nick of the player who buys the usage of the card
     * @param colourToExclude colour to exclude from counting in the dominance computation
     */
    public MessageCCExcludeColourFromCounting(String nicknamePlayer, String colourToExclude){
        super(CC_EXCLUDE_COLOUR_FROM_COUNTING, nicknamePlayer);
        this.colourToExclude = colourToExclude;
    }

    /**
     * getter of the colour to exclude
     * @return colour to exclude
     */
    public String getColourToExclude() {
        return colourToExclude;
    }

    /**
     * method that verifies the input of the message
     * @param controller controller
     * @return true if input is acceptable, else otherwise
     */
    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname)) &&
                controller.checkStudentColour(this.colourToExclude);
    }

    /**
     * method that does what the message requires
     * @param controller controller
     * @return true if the message has been successfully managed, false otherwise
     */
    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCCExcludeColourFromCounting(this);
    }
}
