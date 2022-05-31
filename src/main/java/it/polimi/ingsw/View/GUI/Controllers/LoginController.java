package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

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

    // Nickname
    @FXML private Label nicknameLabel;
    @FXML private TextField nicknameChoice;
    @FXML private Rectangle nicknameError;

    // Colour
    @FXML private Label colourLabel;
    @FXML private ChoiceBox<String> colourChoice;
    @FXML private Rectangle colourError;

    // Num players
    @FXML private Label numPlayersLabel;
    @FXML private ChoiceBox<Integer> numPlayersChoice;
    @FXML private Rectangle numPlayerError;


    // Game mode
    @FXML private Label gameModeLabel;
    @FXML private ChoiceBox<String> modeChoice;
    @FXML private Rectangle gameModeError;

    // Start button
    @FXML private Button startButton;

    //This method is mandatory to show personalized info
    // The initialization of the scene must be done here (the modified where we want)
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.titleLogin.setText("Login information");
        this.nicknameChoice.setOnMousePressed(this::saveNickname);
        this.colourChoice.getItems().addAll(this.availableColours);
        this.colourChoice.setOnAction(this::saveColour);
        this.numPlayersChoice.getItems().addAll(this.availableNumPlayers);
        this.numPlayersChoice.setOnAction(this::saveNumPlayers);
        this.modeChoice.getItems().addAll(this.availableModes);
        this.modeChoice.setOnAction(this::saveMode);
    }

    private void saveNickname(MouseEvent event) {
        this.nicknameError.setVisible(false);
    }

    private void saveColour(ActionEvent event) {
        this.chosenColour = colourChoice.getValue();

        if(this.colourError.isVisible()) {
            this.colourError.setVisible(false);
            this.colourError.setTranslateZ(+1);
        }
    }

    private void saveNumPlayers(ActionEvent event) {
        this.chosenNumPlayers = numPlayersChoice.getValue();

        if(this.numPlayerError.isVisible()) {
            this.numPlayerError.setVisible(false);
            this.numPlayerError.setTranslateZ(+1);
        }
    }

    private void saveMode(ActionEvent event) {
        this.chosenMode = modeChoice.getValue().equals("Normal") ? "false" : "true";

        if(this.gameModeError.isVisible()) {
            this.gameModeError.setVisible(false);
            this.gameModeError.setTranslateZ(+1);
        }
    }

    public void onButtonClicked(ActionEvent event) {
        this.chosenNick = nicknameChoice.getText();

        if(this.chosenNick == "") {
            var msg = "NickName not selected!";
            this.guiViewFX.sceneAlert(msg);
            this.nicknameError.setVisible(true);
            System.out.println("NickName not selected!");
            return;
        }
        if(this.chosenColour == null) {
            var msg = "Colour not selected!";
            this.guiViewFX.sceneAlert(msg);
            this.colourError.setVisible(true);
            this.colourError.setTranslateZ(-1);
            System.out.println("Colour not selected!");
            return;
        }

        if(this.firstPlayer) {
            if(this.chosenNumPlayers == null) {
                var msg = "Number of players not selected!";
                this.guiViewFX.sceneAlert(msg);
                this.numPlayerError.setVisible(true);
                this.numPlayerError.setTranslateZ(-1);
                System.out.println("Number of players not selected!");
                return;
            }
            if(this.chosenNumPlayers == 2 || this.chosenNumPlayers == 4) {
                if(this.chosenColour.equals("Gray")) {
                    var msg = "You cannot chose Gray as colour with this number of players.";
                    this.guiViewFX.sceneAlert(msg);
                    this.colourError.setVisible(true);
                    this.colourError.setTranslateZ(-1);
                    System.out.println("You cannot chose Gray as colour with this number of players.");
                    return;
                }
            }
            if(this.chosenMode == null) {
                var msg = "Game mode not selected!";
                this.guiViewFX.sceneAlert(msg);
                this.gameModeError.setVisible(true);
                this.gameModeError.setTranslateZ(-1);
                System.out.println("Game mode not selected!");
                return;
            }
            this.client.asyncWriteToSocket("createMatch " + this.chosenNick + " " + this.chosenColour + " " + this.chosenNumPlayers + " " + this.chosenMode);

        } else {
            this.client.asyncWriteToSocket("addPlayer " + this.chosenNick + " " + this.chosenColour);
        }
        this.client.setNickname(this.chosenNick);
        this.guiViewFX.sceneLoading("Wait for other players to connect!");
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
        colourChoice.setVisible(true);
    }

    private List<String> mapColourToStringList(List<PlayerColour> colourList) {
        ArrayList<String> colour = new ArrayList<>();

        if(this.numPlayers == 3) {
            if (colourList.contains(PlayerColour.BLACK)) {
                if (colourList.contains(PlayerColour.GRAY)) {
                    colour.add("White");
                } else if (colourList.contains(PlayerColour.WHITE)) {
                    colour.add("Gray");
                } else {
                    colour.add("White");
                    colour.add("Gray");
                }

            } else if (colourList.contains(PlayerColour.GRAY)) {
                if (colourList.contains(PlayerColour.BLACK)) {
                    colour.add("White");
                } else if (colourList.contains(PlayerColour.WHITE)) {
                    colour.add("Black");
                } else {
                    colour.add("White");
                    colour.add("Black");
                }
            }
            if (colourList.contains(PlayerColour.WHITE)) {
                if (colourList.contains(PlayerColour.BLACK)) {
                    colour.add("Gray");
                } else if (colourList.contains(PlayerColour.GRAY)) {
                    colour.add("Black");
                } else {
                    colour.add("Gray");
                    colour.add("Black");
                }
            }
        } else {
            if(this.numPlayers == 2) {
                if(colourList.contains(PlayerColour.BLACK)) {
                    colour.add("White");
                } else if(colourList.contains(PlayerColour.WHITE)) {
                    colour.add("Black");
                }
            }

            if(this.numPlayers == 4) {
                long black = colourList.stream().filter(x -> x.equals(PlayerColour.BLACK)).count();
                long white = colourList.stream().filter(x -> x.equals(PlayerColour.WHITE)).count();

                if(colourList.size() == 3){
                    if(black == 2) {
                        colour.add("White");
                    } else {
                        colour.add("Black");
                    }
                }
                if(colourList.size() == 2){
                    if(black == 1 || white == 1){
                        colour.add("Black");
                        colour.add("White");
                    }
                    else{
                        if(black == 2) {
                            colour.add("White");
                        } else {
                            colour.add("Black");
                        }
                    }
                }
                if(colourList.size() == 1){
                    colour.add("Black");
                    colour.add("White");
                }
            }
        }
        return colour;
    }
}
