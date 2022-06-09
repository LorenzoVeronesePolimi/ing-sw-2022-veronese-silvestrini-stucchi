package it.polimi.ingsw.Model.Exceptions;

public class ExceededNumberForbidFlagException extends Exception {
    public ExceededNumberForbidFlagException() {
        System.out.println("[Exception]: There are already 4 No Entry tiles on the island.");
        printStackTrace();
    }
}
