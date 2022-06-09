package it.polimi.ingsw.Model.Exceptions;

public class ExceededMaxStudentsCloudException extends Exception{
    public ExceededMaxStudentsCloudException(){
        System.out.println("[Exception]: This Cloud has too many Students");
        printStackTrace();
    }
}
