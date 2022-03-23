package it.polimi.ingsw.Model.Exceptions;

public class NoAssistantCardException extends Exception{
    public NoAssistantCardException(){
        System.out.println("You don't have that AssistantCard");
    }
}
