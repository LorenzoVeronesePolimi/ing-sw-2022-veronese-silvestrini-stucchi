package it.polimi.ingsw.Model.Pawns;

import it.polimi.ingsw.Model.Enumerations.SPColour;

import java.awt.*;
import java.io.Serializable;

/**
 * This class represent the entity of the professor, which has a colour.
 */
public class Professor implements Serializable {
    private static final long serialVersionUID = 1L;
    private final SPColour colour;

    /**
     * The constructor builds a professor of the colour that is received as parameter.
     * @param colour colour of the professor to build.
     */
    public Professor(SPColour colour) {
        this.colour = colour;
    }

    /**
     *
     * @return the colour of the professor that calls the method.
     */
    public SPColour getColour() {
        return colour;
    }

    /**
     *
     * @return the conversion of a professor object to string.
     */
    @Override
    public String toString() {
        return "Professor{" +
                "colour=" + colour +
                '}';
    }
}
