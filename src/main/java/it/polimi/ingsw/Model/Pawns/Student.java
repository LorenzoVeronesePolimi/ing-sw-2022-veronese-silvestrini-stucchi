package it.polimi.ingsw.Model.Pawns;

import it.polimi.ingsw.Model.Enumerations.SPColour;

public class Student {
    private final SPColour colour;

    public Student(SPColour colour) {
        this.colour = colour;
    }

    public SPColour getColour() {
        return colour;
    }

    @Override
    public String toString() {
        return "Student{" +
                "colour=" + colour +
                '}';
    }
}
