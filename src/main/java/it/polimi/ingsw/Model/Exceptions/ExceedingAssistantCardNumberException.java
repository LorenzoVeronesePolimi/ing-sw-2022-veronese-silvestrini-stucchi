package it.polimi.ingsw.Model.Exceptions;

public class ExceedingAssistantCardNumberException extends Exception {
    public ExceedingAssistantCardNumberException(){
        System.out.println("Reached max number of assistant card added.");
    }
}
