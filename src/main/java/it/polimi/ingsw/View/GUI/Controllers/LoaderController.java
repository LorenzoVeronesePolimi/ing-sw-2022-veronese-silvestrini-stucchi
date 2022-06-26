package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * controller of the Loader scene
 */
public class LoaderController implements GUIController {
    private GUIViewFX guiViewFX;
    private Client client;

    @FXML Label labelMessage;
    @FXML Label labelLoading;

    /**
     * setter of gui fx
     * @param gui gui fx to be set
     */
    @Override
    public void setGUIFX(GUIViewFX gui) {
        this.guiViewFX = gui;
    }

    /**
     * setter of the client
     * @param client client to be setted
     */
    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * setter of message that will be printed
     * @param message message that will be printed
     */
    public void setMessage(String message) {
        this.labelMessage.setText(message);
    }
}
