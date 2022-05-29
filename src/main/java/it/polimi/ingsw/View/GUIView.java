package it.polimi.ingsw.View;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import it.polimi.ingsw.View.GUI.IntroController;
import it.polimi.ingsw.View.GUI.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static it.polimi.ingsw.View.CLIColours.ANSI_GREEN;
import static it.polimi.ingsw.View.CLIColours.ANSI_RESET;

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
