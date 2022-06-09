package it.polimi.ingsw.Model.Exceptions;

public class WrongNumberOfStudentsTransferException extends Exception {
    public WrongNumberOfStudentsTransferException() {
        System.out.println("[Exception]: Wrong colour!");
        printStackTrace();
    }
}
