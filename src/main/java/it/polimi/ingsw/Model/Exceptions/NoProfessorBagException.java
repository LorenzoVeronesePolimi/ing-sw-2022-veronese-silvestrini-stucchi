package it.polimi.ingsw.Model.Exceptions;

public class NoProfessorBagException extends Exception{
    public NoProfessorBagException(){
        System.out.println("No Professor of that colour in the Bag");
    }
}
