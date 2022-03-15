package it.polimi.ingsw.Model.Exceptions;

public class InvalidTowerNumberException extends Exception{
    public InvalidTowerNumberException(){
        System.out.println("The number of Towers doesn't match the number of Towers");
    }
}
