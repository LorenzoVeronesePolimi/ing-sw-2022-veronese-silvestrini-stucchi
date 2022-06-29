package it.polimi.ingsw.Controller.Exceptions;

/**
 * exception: non-existent player with that nickname
 */
public class NoPlayerException extends Exception{
    public NoPlayerException(){
        System.out.println("[Exception]: No player with that nickname");
    }
}
