package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.View.GUI.Controllers.GUIController;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * controller of the Intro scene
 */
public class IntroController implements GUIController {
    private Client client;
    private GUIViewFX guiViewFX;

    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;
    @FXML private Button startGame;

    /**
     * method that manages the button clicked event
     * @param e occurred event
     */
    public void onButtonClicked(ActionEvent e) {
        this.client.setPlatformReady(true);
        this.guiViewFX.sceneLoading("You will be connected soon!");

    }

    /**
     * setter of the client
     * @param client client to be set
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * setter of gui fx
     * @param gui gui fx to be set
     */
    @Override
    public void setGUIFX(GUIViewFX gui) {
        this.guiViewFX = gui;
    }
}
