package it.polimi.ingsw.Model.Exceptions;

public class ExceededMaxStudentsCloudException extends Exception{
    public ExceededMaxStudentsCloudException(){
        System.out.println("Your Cloud has too much Students");
    }
}
