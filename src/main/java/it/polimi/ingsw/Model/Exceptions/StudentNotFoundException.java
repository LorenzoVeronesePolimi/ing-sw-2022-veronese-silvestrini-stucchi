package it.polimi.ingsw.Model.Exceptions;

public class StudentNotFoundException extends  Exception{
    public StudentNotFoundException(){
        System.out.println("[Exception]: Student not found");
        printStackTrace();
    }
}
