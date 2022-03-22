package it.polimi.ingsw.Model.Exceptions;

public class NoStudentMatchColourException extends Exception{
    public NoStudentMatchColourException(){
        System.out.println("You're trying to remove a student but there's no one here");
    }
}
