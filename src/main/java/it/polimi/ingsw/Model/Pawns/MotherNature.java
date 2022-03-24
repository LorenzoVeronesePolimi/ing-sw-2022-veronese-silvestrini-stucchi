package it.polimi.ingsw.Model.Pawns;

import it.polimi.ingsw.Model.Places.Archipelago;

public class MotherNature { //Singleton
    private Archipelago currentPosition;

    public MotherNature() { }

    //this method is to set the position on the board of mother nature (called in Board constructor)
    public void putInPosition(Archipelago dest) {
        this.currentPosition = dest;
    }

    public Archipelago getCurrentPosition() {
        return this.currentPosition;
    }
}
