package it.polimi.ingsw.Model.Exceptions;

public class AssistantCardAlreadyPlayedTurnException extends Exception {
    public AssistantCardAlreadyPlayedTurnException(){
        System.out.println("[Exception]: Previous Player used that AssistantCard");
        printStackTrace();
    }
}
