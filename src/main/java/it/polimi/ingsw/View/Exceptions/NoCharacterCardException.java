package it.polimi.ingsw.View.Exceptions;

public class NoCharacterCardException extends Exception{
    public NoCharacterCardException(){
        System.out.println("No character card existing");
    }
}
