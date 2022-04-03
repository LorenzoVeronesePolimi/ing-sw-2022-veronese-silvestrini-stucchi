package it.polimi.ingsw.Controller.Exceptions;

public class NoCorrespondingCharacterCardException extends Exception{
    public NoCorrespondingCharacterCardException(){
        System.out.println("The CharacterCard chosen and the parameters doesn't corresponds");
    }
}
