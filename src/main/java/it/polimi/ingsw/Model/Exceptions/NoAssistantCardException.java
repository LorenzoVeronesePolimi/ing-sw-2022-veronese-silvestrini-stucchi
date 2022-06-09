package it.polimi.ingsw.Model.Exceptions;

public class NoAssistantCardException extends Exception {
    public NoAssistantCardException(){
        System.out.println("[Exception]: You don't have that AssistantCard");
        printStackTrace();
    }
}
