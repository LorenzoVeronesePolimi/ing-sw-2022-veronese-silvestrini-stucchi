package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DisconnectController implements GUIController {
    private GUIViewFX guiViewFX;
    private Client client;

    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;
    @FXML private Button endGame;

    public void onButtonClicked(ActionEvent e) {
        Platform.exit();
        //System.exit(0);
    }

    public void setMessage(String msg) {
        if(msg != null) {
            if (msg.length() > 15) {
                String[] split = msg.split("\\.");
                if(split[0] != null && split[1] != null) {
                    titleLabel.setText(split[0]);
                    subtitleLabel.setText(split[1]);
                } else {
                    titleLabel.setText(msg);
                }
            } else {
                titleLabel.setText(msg);
            }
        }
    }

    @Override
    public void setGUIFX(GUIViewFX gui) {
        this.guiViewFX = gui;
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }
}
