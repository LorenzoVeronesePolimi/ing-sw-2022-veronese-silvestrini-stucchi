package it.polimi.ingsw.Model.Pawns;

import it.polimi.ingsw.Model.Enumerations.SPColour;

public class Professor {
    private final SPColour colour;

    public Professor(SPColour colour) {
        this.colour = colour;
    }

    public SPColour getColour() {
        return colour;
    }

    @Override
    public String toString() {
        return "Professor{" +
                "colour=" + colour +
                '}';
    }
}
