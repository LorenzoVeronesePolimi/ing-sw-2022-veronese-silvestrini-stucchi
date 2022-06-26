package it.polimi.ingsw.View.Exceptions;

/**
 * exception: the chosen card is non-existent
 */
public class NoCharacterCardException extends Exception{
    public NoCharacterCardException(){
        System.out.println("No character card existing");
    }
}
