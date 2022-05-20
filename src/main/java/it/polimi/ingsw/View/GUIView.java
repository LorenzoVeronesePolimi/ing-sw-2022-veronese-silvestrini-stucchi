package it.polimi.ingsw.View;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GUIView extends ClientView {
    @FXML
    private Circle myCircle;
    private double x;
    private double y;

    public GUIView(Client client) {
        super(client);
        Application.launch(it.polimi.ingsw.View.GUI.GUIViewFX.class);
    }

    public GUIView() {
        super();
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
    public void askNickName(List<PlayerColour> list, int numPlayer) {

    }

    @Override
    public void askFirstPlayerInfo() {

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
