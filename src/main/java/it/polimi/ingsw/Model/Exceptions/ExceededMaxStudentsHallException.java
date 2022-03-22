package it.polimi.ingsw.Model.Exceptions;

public class ExceededMaxStudentsHallException extends Exception{
    public ExceededMaxStudentsHallException(){
        System.out.println("Number of students in hall exceeded");
    }
}
