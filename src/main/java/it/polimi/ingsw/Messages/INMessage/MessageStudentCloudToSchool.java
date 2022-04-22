package it.polimi.ingsw.Messages.INMessage;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.STUDENT_CLOUD_TO_SCHOOL;

public class MessageStudentCloudToSchool extends Message{
    private final String nicknamePlayer;
    private final int indexCloud;

    public MessageStudentCloudToSchool(String nicknamePlayer, int indexCloud){
        super(STUDENT_CLOUD_TO_SCHOOL);
        this.nicknamePlayer = nicknamePlayer;
        this.indexCloud = indexCloud - 1; //player choose 1, I put 0
    }

    public String getNicknamePlayer() {
        return nicknamePlayer;
    }

    public int getIndexCloud() {
        return indexCloud;
    }

    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nicknamePlayer) &&
                controller.checkCloudIndex(this.indexCloud));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageStudentCloudToSchool(this);
    }
}
