package it.polimi.ingsw.Model.Pawns;

import it.polimi.ingsw.Model.Enumerations.SPColour;

import java.io.Serializable;

/**
 * This class represents a student in the game.
 * It has a specific colour.
 */
public class Student implements Serializable {
    private final SPColour colour;

    /**
     * This method creates a student assigning a colour.
     * @param colour Colour of the student created.
     */
    public Student(SPColour colour) {
        this.colour = colour;
    }

    /**
     * @return The colour of the current student.
     */
    public SPColour getColour() {
        return colour;
    }

    /**
     * Method toString of the structure of the class.
     * @return The description of the class.
     */
    @Override
    public String toString() {
        return "Student{" +
                "colour=" + colour +
                '}';
    }
}
