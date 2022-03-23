package it.polimi.ingsw.Model.Exceptions;

public class AssistantCardAlreadyPlayedTurnException extends Exception {
    public AssistantCardAlreadyPlayedTurnException(){
        System.out.println("Previous Player used that AssistantCard");
    }
}
