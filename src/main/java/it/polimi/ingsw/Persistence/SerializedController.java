package it.polimi.ingsw.Persistence;

import it.polimi.ingsw.Controller.Controller;

import java.io.Serializable;

/**
 * class for the serialization of the controller
 */
public class SerializedController implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Controller controller;

    /**
     * constructor of the serialized controller, given a controller
     * @param controller controller to serialize
     */
    public SerializedController(Controller controller) {
        this.controller = controller;
    }

    /**
     * getter of the controller
     * @return controller
     */
    public Controller getController() {
        return controller;
    }
}
