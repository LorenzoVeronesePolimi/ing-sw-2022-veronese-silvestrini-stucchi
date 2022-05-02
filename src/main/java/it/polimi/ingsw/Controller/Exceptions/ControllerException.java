package it.polimi.ingsw.Controller.Exceptions;

import it.polimi.ingsw.Controller.Enumerations.ControllerErrorType;

public class ControllerException extends Exception {
    private ControllerErrorType type;
    public ControllerException(ControllerErrorType type) {
        this.type = type;
    }

    public ControllerErrorType getType() {
        return type;
    }
}
