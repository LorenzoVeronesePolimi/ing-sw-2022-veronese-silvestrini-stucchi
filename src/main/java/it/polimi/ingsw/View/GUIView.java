package it.polimi.ingsw.View;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.List;

/**
 * class that extends the abstract class ClientView for GUI
 */
public class GUIView extends ClientView {
    private GUIViewFX guiViewFX;

    /**
     * constructor of the class
     * @param client owner of this GUI view
     */
    public GUIView(Client client) {
        super(client);
    }

    /**
     * setter of the stage FX
     * @param guiViewFX FX to be set
     */
    public void setGuiViewFX(GUIViewFX guiViewFX) {
        this.guiViewFX = guiViewFX;
    }

    /**
     * method that shows that an error has occurred
     * @param err error to be printed
     */
    @Override
    public void printErrorMessage(String err) {
        if(err.equals("Controller error")) {
            err = "You made an error, retry!";
            String finalErr = err;
            Platform.runLater(() -> {
                this.guiViewFX.sceneAlert(finalErr, Alert.AlertType.ERROR);
            });
        } else {
            this.clientDisconnectionEnd();
        }
    }

    /**
     * method that ends the view
     */
    @Override
    public void endView() {

    }

    /**
     * method that disconnect a client
     */
    @Override
    public void clientDisconnectionEnd() {
        Platform.runLater(() -> {
                this.guiViewFX.sceneClientDisconnect(null);
        });
    }

    /**
     * method that asks the client if he wants to play through CLI or GUI (doesn't do anything. the choice is made in CLI: GUI
     * accessible only via CLI)
     */
    @Override
    public void askCLIorGUI() {

    }

    /**
     * method that asks the first player who connects to the server his nickname and colour, how many players we want the game to contain,
     * and the game mode (advanced or standard)
     */
    @Override
    public void askFirstPlayerInfo() {
        Platform.runLater(() ->{
            this.guiViewFX.sceneAskFirstPlayerInfo();
        });

    }

    /**
     * method that asks a client which nickname and colour (among the available one) he wants to use
     * @param list list of available colours
     * @param numPlayer number of players of the game
     */
    @Override
    public void askNickName(List<PlayerColour> list, int numPlayer) {
        Platform.runLater(() -> {
            this.guiViewFX.sceneAskNickname(list, numPlayer);
        });
    }

    /**
     * method that prints the board information every time something is updated in the model board
     * @param serializedBoardAbstract serialized board that is notified by the model
     */
    @Override
    public void showBoard(SerializedBoardAbstract serializedBoardAbstract) {
        System.out.println("[GUIVIew, showBoard]: current player " + serializedBoardAbstract.getCurrentPlayer());
        System.out.println("[GUIVIew, showBoard]: current state " + serializedBoardAbstract.getCurrentState());

        if(serializedBoardAbstract.getCurrentState().equals(State.END)) {
            this.client.setEndGame(true);
        }

        Platform.runLater(() -> {
            this.guiViewFX.manageScene(serializedBoardAbstract);
        });
    }

    /**
     * method that shows the winner
     * @param serializedBoardAbstract serialized board that is notified by the model
     */
    public void showWinner(SerializedBoardAbstract serializedBoardAbstract){
        Platform.runLater(() -> {
            this.guiViewFX.manageScene(serializedBoardAbstract);
        });
    }


    //TODO: just for test
    /**
     * printer of errors. just for tests
     * @param err error to be printed
     */
    @Override
    public void printCustom(String err) {

    }
}
