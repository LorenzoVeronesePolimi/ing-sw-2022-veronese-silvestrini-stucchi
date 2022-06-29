package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.STUDENT_CLOUD_TO_SCHOOL;

/**
 * input message that asks to move all the students placed on a cloud in the current players hall
 */
public class MessageStudentCloudToSchool extends Message{
    private final int indexCloud;

    /**
     * constructor of the input message
     * @param nicknamePlayer nick of the current player
     * @param indexCloud index of the chosen cloud
     */
    public MessageStudentCloudToSchool(String nicknamePlayer, int indexCloud){
        super(STUDENT_CLOUD_TO_SCHOOL, nicknamePlayer);
        this.indexCloud = indexCloud; //player choose 1, I put 0
    }

    /**
     * getter of the index of the chosen cloud
     * @return index of the chosen cloud
     */
    public int getIndexCloud() {
        return indexCloud;
    }

    /**
     * method that verifies the input of the message
     * @param controller controller
     * @return true if input is acceptable, else otherwise
     */
    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname) &&
                controller.checkCloudIndex(this.indexCloud));
    }

    /**
     * method that does what the message requires
     * @param controller controller
     * @return true if the message has been successfully managed, false otherwise
     */
    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageStudentCloudToSchool(this);
    }
}
