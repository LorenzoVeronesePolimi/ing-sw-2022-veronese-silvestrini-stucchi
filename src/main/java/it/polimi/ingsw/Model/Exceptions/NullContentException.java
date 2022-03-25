package it.polimi.ingsw.Model.Exceptions;

public class NullContentException extends Exception {
    public NullContentException(){
        System.out.println("Null content in list");
    }
}
