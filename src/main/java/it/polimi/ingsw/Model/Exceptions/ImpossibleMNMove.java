package it.polimi.ingsw.Model.Exceptions;

public class ImpossibleMNMove extends Exception {
    public ImpossibleMNMove() {
        System.out.println("Movement not allowed");
    }
}
