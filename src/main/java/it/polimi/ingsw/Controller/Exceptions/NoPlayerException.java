package it.polimi.ingsw.Controller.Exceptions;

public class NoPlayerException extends Exception{
    public NoPlayerException(){
        System.out.println("[Exception]: No player with that nickname");
    }
}
