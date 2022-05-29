package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoaderController implements GUIController {
    private GUIViewFX guiViewFX;
    private Client client;

    @FXML Label labelMessage;
    @FXML Label labelLoading;

    @Override
    public void setGUIFX(GUIViewFX gui) {
        this.guiViewFX = gui;
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    public void setMessage(String message) {
        this.labelMessage.setText(message);
    }
}
