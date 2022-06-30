package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;

/**
 * input message that asks to manage the use of an assistant card
 */
public class MessageAssistantCard extends Message{
    private final int motherNatureMovement;
    private final int turnPriority;

    /**
     * constructor of the assistant card input message
     * @param nicknamePlayer nick of the player that sends the message
     * @param motherNatureMovement number of moves allowed for mother nature by the chosen assistant card
     * @param turnPriority turn priority of the chosen assistant card
     */
    public MessageAssistantCard(String nicknamePlayer, int motherNatureMovement, int turnPriority){
        super(INMessageType.ASSISTANT_CARD, nicknamePlayer);
        this.motherNatureMovement = motherNatureMovement;
        this.turnPriority = turnPriority;
    }

    /*public int getMotherNatureMovement() {
        return motherNatureMovement;
    } unused*/

    /**
     * getter of turn priority of the chosen assistant card
     * @return turn priority of the chosen assistant card
     */
    public int getTurnPriority() {
        return turnPriority;
    }

    /**
     * method that verifies the input of the message
     * @param controller controller
     * @return true if input is acceptable, else otherwise
     */
    @Override
    public boolean checkInput(ControllerInput controller) {
        return(controller.checkNickname(this.nickname) &&
                controller.checkMotherNatureMovement(this.motherNatureMovement) &&
                controller.checkTurnPriority(this.turnPriority));
    }

    /**
     * method that does what the message requires
     * @param controller controller
     * @return true if the message has been successfully managed, false otherwise
     */
    public boolean manageMessage(Controller controller) {
        return controller.manageAssistantCard(this);
    }
}
