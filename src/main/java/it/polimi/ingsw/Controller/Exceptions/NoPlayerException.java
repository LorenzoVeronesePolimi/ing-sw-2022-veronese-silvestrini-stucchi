package it.polimi.ingsw.Controller.Exceptions;

/**
 * exception: non-existent player with that nickname
 */
public class NoPlayerException extends Exception {
    /**
     * constructor of controller exception that outputs the exception message.
     */
    public NoPlayerException(){
        System.out.println("[Exception]: No player with that nickname");
    }
}
