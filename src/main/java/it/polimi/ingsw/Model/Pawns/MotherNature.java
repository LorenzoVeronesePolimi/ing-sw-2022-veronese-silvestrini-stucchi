package it.polimi.ingsw.Model.Pawns;

import it.polimi.ingsw.Model.Places.Archipelago;

/**
 * This class represents Mother Nature and it's position in the board.
 */
public class MotherNature {
    private Archipelago currentPosition;

    /**
     * This method sets the archipelago in which mother nature is currently placed
     * @param destination The archipelago in which mother nature is palced.
     */
    //this method is to set the position on the board of mother nature (called in Board constructor)
    public void putInPosition(Archipelago destination) {
        this.currentPosition = destination;
    }

    /**
     * @return The archipelago in which mother nature is placed.
     */
    public Archipelago getCurrentPosition() {
        return this.currentPosition;
    }
}
