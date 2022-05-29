package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.View.GUI.Controllers.GUIController;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements GUIController, Initializable {
    private GUIViewFX guiViewFX;
    private Client client;
    private List<String> availableColours = Arrays.asList("Black", "White", "Gray");
    private List<Integer> availableNumPlayers = Arrays.asList(2, 3, 4);
    private List<String> availableModes = Arrays.asList("Normal", "Advanced");
    private boolean firstPlayer = true;
    private int numPlayers;

    private String chosenNick = null;
    private String chosenColour = null;
    private Integer chosenNumPlayers = null;
    private String chosenMode = null;

    @FXML private Label titleLogin;
    @FXML private Label nicknameLabel;
    @FXML private Label colourLabel;
    @FXML private Label numPlayersLabel;
    @FXML private Label gameModeLabel;
    @FXML private TextField nicknameChoice;
    @FXML private ChoiceBox<String> colourChoice;
    @FXML private ChoiceBox<Integer> numPlayersChoice;
    @FXML private ChoiceBox<String> modeChoice;
    @FXML private Button startButton;

    //This method is mandatory to show personalized info
    // The initialization of the scene must be done here (the modified where we want)
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.titleLogin.setText("Login information");
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
        this.chosenMode = modeChoice.getValue().equals("Normal") ? "false" : "true";
    }

    public void onButtonClicked(ActionEvent event) {
        this.chosenNick = nicknameChoice.getText();

        if(this.chosenNick == null) {
            //TODO: add an alert message, not only a cli message
            System.out.println("NickName not selected!");
            return;
        }
        if(this.chosenColour == null) {
            //TODO: add an alert message, not only a cli message
            System.out.println("Colour not selected!");
            return;
        }

        if(this.firstPlayer) {
            if(this.chosenNumPlayers == null) {
                //TODO: add an alert message, not only a cli message
                System.out.println("Number of players not selected!");
                return;
            }
            if(this.chosenMode == null) {
                //TODO: add an alert message, not only a cli message
                System.out.println("Game mode not selected!");
                return;
            }
            this.client.asyncWriteToSocket("createMatch " + this.chosenNick + " " + this.chosenColour + " " + this.chosenNumPlayers + " " + this.chosenMode);

        } else {
            this.client.asyncWriteToSocket("addPlayer " + this.chosenNick + " " + this.chosenColour);
        }

        this.guiViewFX.sceneLoading("LoadingPage.fxml", "Wait for other players to connect!");
    }

    @Override
    public void setGUIFX(GUIViewFX gui) {
        this.guiViewFX = gui;
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        this.firstPlayer = firstPlayer;
        if(!this.firstPlayer) {
            this.numPlayersLabel.setVisible(false);
            this.gameModeLabel.setVisible(false);
            this.numPlayersChoice.setVisible(false);
            this.modeChoice.setVisible(false);
            this.startButton.setTranslateY(-200);
        }
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public void setAvailableColours(List<PlayerColour> chosenColour) {
        System.out.println(chosenColour);
        colourChoice.getItems().clear();
        colourChoice.getItems().addAll(mapColourToStringList(chosenColour));
    }

    private List<String> mapColourToStringList(List<PlayerColour> colourList) {
        ArrayList<String> colour = new ArrayList<>();
        //TODO: manage colour choice based on the number of players
        if(colourList.contains(PlayerColour.BLACK)) {
            if(colourList.contains(PlayerColour.GRAY)) {
                colour.add("White");
            } else if(colourList.contains(PlayerColour.WHITE)) {
                colour.add("Gray");
            } else {
                colour.add("White");
                colour.add("Gray");
            }

        } else if(colourList.contains(PlayerColour.GRAY)) {
            if(colourList.contains(PlayerColour.BLACK)) {
                colour.add("White");
            } else if(colourList.contains(PlayerColour.WHITE)) {
                colour.add("Black");
            } else {
                colour.add("White");
                colour.add("Black");
            }
        }
        if(colourList.contains(PlayerColour.WHITE)) {
            if(colourList.contains(PlayerColour.BLACK)) {
                colour.add("Gray");
            } else if(colourList.contains(PlayerColour.GRAY)) {
                colour.add("Black");
            } else {
                colour.add("Gray");
                colour.add("Black");
            }
        }
        return colour;
    }
}
