package it.polimi.ingsw.Model.Pawns;

import it.polimi.ingsw.Model.Enumerations.SPColour;

public class Student {
    private SPColour colour;

    public Student(SPColour colour) {
        this.colour = colour;
    }

    public SPColour getColour() {
        return colour;
    }
}
