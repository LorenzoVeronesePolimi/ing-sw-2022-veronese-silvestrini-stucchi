package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * controller of the ShowWinner scene
 */
public class ShowWinnerController implements GUIController, Initializable {
    @FXML Label label_intro;
    @FXML Label label_winner;
    @FXML Label label_phrase;

    private GUIViewFX guiViewFX;
    private Client client;


    /**
     * mandatory method to show personalized information in the scene. the initialization of the scene must be done here, than can be
     * modified where we want
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * setter of parameters of the scene
     * @param numPlayers number of players of the game
     * @param winner nickname of the winner
     * @param isWinner parameter that says if the client who uses this view won or not
     */
    public void setVisualization(int numPlayers, String winner, boolean isWinner){
        if(numPlayers == 4){
            label_intro.setText("Winners are:");
            String[] winners = winner.split("\\*");
            label_winner.setText(winners[0] + " and " + winners[1]);
        }
        else{
            label_intro.setText("The winner is:");
            label_winner.setText(winner);
        }


        if(isWinner){
            label_phrase.setText("Congratulations: you've won!");
        }
        else{
            label_phrase.setText("You've lost!");
        }
    }

    /**
     * setter of the GUI view fx
     * @param gui GUI view fx to be setted
     */
    @Override
    public void setGUIFX(GUIViewFX gui) {
        this.guiViewFX = gui;
    }

    /**
     * setter of the client
     * @param client client to be set
     */
    @Override
    public void setClient(Client client) {
        this.client = client;
    }
}
