package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.Enumerations.MessageType;

public class MessageStudentToArchipelago extends Message{
    private final String nicknamePlayer;
    private final String colour;
    private final int destArchipelagoIndex;

    public MessageStudentToArchipelago(String nicknamePlayer, String colour, int destArchipelagoIndex){
        super(MessageType.STUDENT_TO_ARCHIPELAGO);
        this.nicknamePlayer = nicknamePlayer;
        this.colour = colour;
        this.destArchipelagoIndex = destArchipelagoIndex;
    }

    public String getNicknamePlayer(){return this.nicknamePlayer;}

    public String getColour() {
        return colour;
    }

    public int getDestArchipelagoIndex() {
        return destArchipelagoIndex;
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageStudentToArchipelago(this);
    }
}
