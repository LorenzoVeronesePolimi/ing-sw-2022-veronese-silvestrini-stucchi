package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Enumerations.MessageType;

public class MessageStudentToArchipelago extends Message{
    private String colour;
    private int destArchipelagoIndex;

    public MessageStudentToArchipelago(String colour, int destArchipelagoIndex){
        super(MessageType.STUDENT_TO_ARCHIPELAGO);
        this.colour = colour;
        this.destArchipelagoIndex = destArchipelagoIndex;
    }

    public String getColour() {
        return colour;
    }

    public int getDestArchipelagoIndex() {
        return destArchipelagoIndex;
    }
}
