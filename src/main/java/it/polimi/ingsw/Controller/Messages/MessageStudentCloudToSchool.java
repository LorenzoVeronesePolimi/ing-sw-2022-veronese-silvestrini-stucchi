package it.polimi.ingsw.Controller.Messages;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.MOVE_MOTHER_NATURE;

public class MessageStudentCloudToSchool extends Message{
    private String nicknamePlayer;
    private int indexCloud;

    public MessageStudentCloudToSchool(String nicknamePlayer, int indexCloud){
        super(MOVE_MOTHER_NATURE);
        this.nicknamePlayer = nicknamePlayer;
        this.indexCloud = indexCloud - 1; //player choose 1, I put 0
    }

    public String getNicknamePlayer() {
        return nicknamePlayer;
    }

    public int getIndexCloud() {
        return indexCloud;
    }
}
