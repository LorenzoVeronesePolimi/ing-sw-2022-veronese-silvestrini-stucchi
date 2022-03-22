package it.polimi.ingsw.Model.Exceptions;

public class StudentNotFoundException extends  Exception{
    public StudentNotFoundException(){
        System.out.println("Student not found");
    }
}
