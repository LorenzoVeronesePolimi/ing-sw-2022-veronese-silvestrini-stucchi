package it.polimi.ingsw.Controller;

import java.io.Serializable;

public class SerializedController implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Controller controller;

    public SerializedController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }
}
