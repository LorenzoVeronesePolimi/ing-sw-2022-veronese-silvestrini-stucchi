package it.polimi.ingsw.Model.Exceptions;

public class EmptyCaveauException extends Exception{
    public EmptyCaveauException(){
        System.out.println("[Exception]: Caveau is empty!");
        printStackTrace();
    }
}
