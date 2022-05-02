package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.STUDENT_CLOUD_TO_SCHOOL;

public class MessageStudentCloudToSchool extends Message{
    private final int indexCloud;

    public MessageStudentCloudToSchool(String nicknamePlayer, int indexCloud){
        super(STUDENT_CLOUD_TO_SCHOOL, nicknamePlayer);
        this.indexCloud = indexCloud - 1; //player choose 1, I put 0
    }

    public int getIndexCloud() {
        return indexCloud;
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname) &&
                controller.checkCloudIndex(this.indexCloud));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageStudentCloudToSchool(this);
    }
}
