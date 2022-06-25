package it.polimi.ingsw.View;

import it.polimi.ingsw.Client.Client;
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
    private Stage currentStage;

    @FXML
    private Circle myCircle;
    private double x;
    private double y;

    /**
     * constructor of the class
     * @param client owner of this GUI view
     */
    public GUIView(Client client) {
        super(client);
    }

    /**
     * default constructor of the class
     */
    public GUIView() {
        super();
    }

    /**
     * setter of the stage FX
     * @param guiViewFX FX to be set
     */
    public void setGuiViewFX(GUIViewFX guiViewFX) {
        this.guiViewFX = guiViewFX;
    }

    /**
     * setter of the current stage
     * @param currentStage stage to be set
     */
    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
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
     * method that asks a client if he wants to reconnect to the sever, after the disconnection of another client of the game
     */
    @Override
    public void askReconnect() {
        // we will not implement this
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
        //System.out.println("askFirstPlayer");
        Platform.runLater(() ->{
            //System.out.println("askFirstPlayer2");
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
        //System.out.println("askNickname");
        Platform.runLater(() -> {
            //System.out.println("askNickname2");
            this.guiViewFX.sceneAskNickname(list, numPlayer);
        });
    }

    /**
     * method that prints the board information every time something is updated in the model board
     * @param serializedBoardAbstract serialized board that is notified by the model
     */
    @Override
    public void showBoard(SerializedBoardAbstract serializedBoardAbstract) {
        //System.out.println("show board (outside)");
        Platform.runLater(() -> {
            //System.out.println("show board (inside)");
            this.guiViewFX.manageScene(serializedBoardAbstract);
        });
    }

    /**
     * method that shows the winner
     * @param serializedBoardAbstract serialized board that is notified by the model
     */
    public void showWinner(SerializedBoardAbstract serializedBoardAbstract){
        Platform.runLater(() -> {
            //System.out.println("show board (inside)");
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
