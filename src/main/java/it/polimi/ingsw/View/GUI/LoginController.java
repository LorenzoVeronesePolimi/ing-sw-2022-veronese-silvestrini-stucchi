package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements GUIController, Initializable {
    private GUIViewFX guiViewFX;
    private Client client;
    private List<String> availableColours = Arrays.asList("Black", "White", "Gray");
    private List<Integer> availableNumPlayers = Arrays.asList(2, 3, 4);
    private List<String> availableModes = Arrays.asList("Normal", "Advanced");

    private String chosenNick = null;
    private String chosenColour = null;
    private Integer chosenNumPlayers = null;
    private String chosenMode = null;

    @FXML private Label titleLogin;
    @FXML private TextField nicknameChoice;
    @FXML private ChoiceBox<String> colourChoice;
    @FXML private ChoiceBox<Integer> numPlayersChoice;
    @FXML private ChoiceBox<String> modeChoice;

    //This method is mandatory to show personalized info
    // The initialization of the scene must be done here (the modified where we want)
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colourChoice.getItems().addAll(availableColours);
        colourChoice.setOnAction(this::saveColour);
        numPlayersChoice.getItems().addAll(availableNumPlayers);
        numPlayersChoice.setOnAction(this::saveNumPlayers);
        modeChoice.getItems().addAll(availableModes);
        modeChoice.setOnAction(this::saveMode);
    }

    //TODO: insert check on the values (also after passing the board to this controller)
    public void saveColour(ActionEvent event) {
        this.chosenColour = colourChoice.getValue();
    }

    public void saveNumPlayers(ActionEvent event) {
        this.chosenNumPlayers = numPlayersChoice.getValue();
    }

    public void saveMode(ActionEvent event) {
        this.chosenMode = modeChoice.getValue();
    }

    public void onButtonClicked(ActionEvent event) {
        this.chosenNick = nicknameChoice.getText();
        if(this.chosenNick != null && this.chosenColour != null && this.chosenNumPlayers != null
                && this.chosenMode != null) {
            this.client.asyncWriteToSocket("createMatch " + chosenNick + " " + chosenColour + " " + chosenNumPlayers + " " + chosenMode);
        } else {
            System.out.println("Something wrong happend");
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
