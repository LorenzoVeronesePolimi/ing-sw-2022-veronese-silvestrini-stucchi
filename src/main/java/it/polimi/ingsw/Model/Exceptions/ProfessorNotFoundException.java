package it.polimi.ingsw.Model.Exceptions;

public class ProfessorNotFoundException extends Exception{
    public ProfessorNotFoundException(){
        System.out.println("[Exception]: Professor not found");
        printStackTrace();
    }
}
