package it.polimi.ingsw.View;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    }

    @Override
    public void endView() {

    }

    @Override
    public void clientDisconnectionEnd() {

    }

    @Override
    public void askReconnect() {

    }

    @Override
    public void askCLIorGUI() {

    }

    @Override
    public void askFirstPlayerInfo() {
        System.out.println("askFirstPlayer");
        Platform.runLater(() ->{
            System.out.println("askFirstPlayer2");
            this.guiViewFX.sceneAskFirstPlayerInfo("Login.fxml");
        });

    }

    @Override
    public void askNickName(List<PlayerColour> list, int numPlayer) {
        System.out.println("askNickname");
        Platform.runLater(() -> {
            System.out.println("askNickname2");
            //TODO: change this scene to "askNick" or model the Login one to manage first player and not first player
            this.guiViewFX.sceneAskNickname("Login.fxml", list, numPlayer);
        });
    }

    @Override
    public void showBoard(SerializedBoardAbstract serializedBoardAbstract) {
        System.out.println("show board (outside)");
        Platform.runLater(() -> {
            System.out.println("show board (inside)");
            this.guiViewFX.sceneShowBoard("BoardGrid.fxml", serializedBoardAbstract);
        });
    }

    //TODO: just for test
    @Override
    public void printCustom(String err) {

    }

    public void up(ActionEvent e) {
        //System.out.println("UP");
        this.myCircle.setCenterY(this.y -= 10);
    }

    public void down(ActionEvent e) {
        //System.out.println("DOWN");
        this.myCircle.setCenterY(this.y += 10);
    }

    public void right(ActionEvent e) {
        //System.out.println("RIGHT");
        this.myCircle.setCenterX(this.x += 10);
    }

    public void left(ActionEvent e) {
        //System.out.println("LEFT");
        this.myCircle.setCenterX(this.x -= 10);
    }

}
