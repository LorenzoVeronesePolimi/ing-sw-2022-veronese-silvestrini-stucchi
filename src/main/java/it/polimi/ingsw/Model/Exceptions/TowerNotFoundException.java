package it.polimi.ingsw.Model.Exceptions;

public class TowerNotFoundException extends Exception {
    public TowerNotFoundException() {
        System.out.println("[Exception]: No tower remaining.");
        printStackTrace();
    }
}
