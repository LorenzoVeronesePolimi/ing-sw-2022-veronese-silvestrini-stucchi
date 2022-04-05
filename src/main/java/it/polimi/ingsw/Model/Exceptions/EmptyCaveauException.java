package it.polimi.ingsw.Model.Exceptions;

public class EmptyCaveauException extends Exception{
    public EmptyCaveauException(){
        System.out.println("Caveau is empty!");
    }
}
