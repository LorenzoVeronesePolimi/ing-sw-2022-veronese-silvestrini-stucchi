package it.polimi.ingsw.Model.Exceptions;

public class ExceededMaxStudentsHallException extends Exception{
    public ExceededMaxStudentsHallException(){
        System.out.println("[Exception]: Number of students in hall exceeded");
        printStackTrace();
    }
}
