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

public class GUIView extends ClientView {
    private GUIViewFX guiViewFX;
    private Stage currentStage;

    @FXML
    private Circle myCircle;
    private double x;
    private double y;

    public GUIView(Client client) {
        super(client);
    }

    public GUIView() {
        super();
    }

    public void setGuiViewFX(GUIViewFX guiViewFX) {
        this.guiViewFX = guiViewFX;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

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

    @Override
    public void endView() {

    }

    @Override
    public void clientDisconnectionEnd() {
        Platform.runLater(() -> {
                this.guiViewFX.sceneClientDisconnect(null);
        });
    }

    @Override
    public void askReconnect() {

    }

    @Override
    public void askCLIorGUI() {

    }

    @Override
    public void askFirstPlayerInfo() {
        //System.out.println("askFirstPlayer");
        Platform.runLater(() ->{
            //System.out.println("askFirstPlayer2");
            this.guiViewFX.sceneAskFirstPlayerInfo();
        });

    }

    @Override
    public void askNickName(List<PlayerColour> list, int numPlayer) {
        //System.out.println("askNickname");
        Platform.runLater(() -> {
            //System.out.println("askNickname2");
            this.guiViewFX.sceneAskNickname(list, numPlayer);
        });
    }

    @Override
    public void showBoard(SerializedBoardAbstract serializedBoardAbstract) {
        //System.out.println("show board (outside)");
        Platform.runLater(() -> {
            //System.out.println("show board (inside)");
            this.guiViewFX.manageScene(serializedBoardAbstract);
        });
    }

    public void showWinner(SerializedBoardAbstract serializedBoardAbstract){
        Platform.runLater(() -> {
            //System.out.println("show board (inside)");
            this.guiViewFX.manageScene(serializedBoardAbstract);
        });
    }

    //TODO: just for test
    @Override
    public void printCustom(String err) {

    }


}
