package it.polimi.ingsw.Model.Pawns;

import it.polimi.ingsw.Model.Places.Archipelago;

public class MotherNature { //Singleton
    private static MotherNature instance;
    private Archipelago currentPosition;

    private MotherNature() { }

    public static MotherNature instance() {
        if (instance == null) {
            instance = new MotherNature();
        }

         return instance;
    }

    //this method is to set the position on the board of mother nature (called in Board constructor)
    public void putInPosition(Archipelago archi) {
        this.currentPosition = archi;
    }

    public Archipelago getCurrentPosition() {
        return this.currentPosition;
    }
}
