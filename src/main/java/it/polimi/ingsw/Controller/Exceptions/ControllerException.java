package it.polimi.ingsw.Controller.Exceptions;

import it.polimi.ingsw.Controller.Enumerations.ControllerErrorType;

/**
 * class that represents a controller exception: type can be format error, state error, integrity error
 */
public class ControllerException extends Exception {
    private ControllerErrorType type;

    /**
     * constructor of controller exception with a given type
     * @param type type of controller exception
     */
    public ControllerException(ControllerErrorType type) {
        this.type = type;
    }

    /**
     * getter of the type of the controller exception
     * @return type of controller exception
     */
    public ControllerErrorType getType() {
        return type;
    }
}
