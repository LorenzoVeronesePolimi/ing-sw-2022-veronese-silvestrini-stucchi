package it.polimi.ingsw.Model.Exceptions;

public class AnotherTowerException extends Exception{
    public AnotherTowerException(){
        System.out.println("You can't add a Tower on an Island with another Tower!");
    }
}
