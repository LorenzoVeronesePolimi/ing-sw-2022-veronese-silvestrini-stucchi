package it.polimi.ingsw.Model.Exceptions;

public class MergeDifferentOwnersException extends Exception{
    public MergeDifferentOwnersException(){
        System.out.println("[Exception]: You are trying to merge Islands with different owners");
        printStackTrace();
    }
}
