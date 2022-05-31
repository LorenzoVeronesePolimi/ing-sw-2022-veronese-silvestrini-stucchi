package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AssistantCardController implements GUIController, Initializable {
    private GUIViewFX guiViewFX;
    private Client client;

    private SerializedBoardAbstract serializedBoardAbstract;
    private List<Button> cardButtons = new ArrayList<>();

    @FXML private Button card1;
    @FXML private Button card2;
    @FXML private Button card3;
    @FXML private Button card4;
    @FXML private Button card5;
    @FXML private Button card6;
    @FXML private Button card7;
    @FXML private Button card8;
    @FXML private Button card9;
    @FXML private Button card10;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.cardButtons.add(card1);
        this.cardButtons.add(card2);
        this.cardButtons.add(card3);
        this.cardButtons.add(card4);
        this.cardButtons.add(card5);
        this.cardButtons.add(card6);
        this.cardButtons.add(card7);
        this.cardButtons.add(card8);
        this.cardButtons.add(card9);
        this.cardButtons.add(card10);

        this.card1.setOnAction(this::playCard1);
        this.card2.setOnAction(this::playCard2);
        this.card3.setOnAction(this::playCard3);
        this.card4.setOnAction(this::playCard4);
        this.card5.setOnAction(this::playCard5);
        this.card6.setOnAction(this::playCard6);
        this.card7.setOnAction(this::playCard7);
        this.card8.setOnAction(this::playCard8);
        this.card9.setOnAction(this::playCard9);
        this.card10.setOnAction(this::playCard10);
    }

    private void playCard1(ActionEvent e) {
        this.client.asyncWriteToSocket("assistantCard 1 1");
    }

    private void playCard2(ActionEvent e) {
        this.client.asyncWriteToSocket("assistantCard 1 2");
    }

    private void playCard3(ActionEvent e) {
        this.client.asyncWriteToSocket("assistantCard 2 3");
    }

    private void playCard4(ActionEvent e) {
        this.client.asyncWriteToSocket("assistantCard 2 4");
    }

    private void playCard5(ActionEvent e) {
        this.client.asyncWriteToSocket("assistantCard 3 5");
    }

    private void playCard6(ActionEvent e) {
        this.client.asyncWriteToSocket("assistantCard 3 6");
    }

    private void playCard7(ActionEvent e) {
        this.client.asyncWriteToSocket("assistantCard 4 7");
    }

    private void playCard8(ActionEvent e) {
        this.client.asyncWriteToSocket("assistantCard 4 8");
    }

    private void playCard9(ActionEvent e) {
        this.client.asyncWriteToSocket("assistantCard 5 9");
    }

    private void playCard10(ActionEvent e) {
        this.client.asyncWriteToSocket("assistantCard 5 10");
    }

    public void setSerializedBoardAbstract(SerializedBoardAbstract serializedBoardAbstract) {
        this.serializedBoardAbstract = serializedBoardAbstract;
        Player player = null;
        for(School s : serializedBoardAbstract.getSchools()) {
            if(s.getPlayer().getNickname().equals(this.client.getNickname())) {
                player = s.getPlayer();
            }
        }

        if(!player.equals(serializedBoardAbstract.getCurrentPlayer())){
            System.err.println("Error in showing the AssistantCard scene. (In class AssistantCardController)");
        }

        List<AssistantCard> playerHand = new ArrayList<>(player.getPlayerHand());
        List<Integer> removed = new ArrayList<>();

        int i = 1;
        for(int k = 0; k < playerHand.size(); k++) {
            if(playerHand.get(k).getTurnPriority() != i) {
                removed.add(i);
                k--;
            }

            i++;
        }

        for(int w = 0; w < this.cardButtons.size(); w++) {
            if(removed.contains(w)) {
                this.cardButtons.get(w).setOpacity(0.7);
                this.cardButtons.get(w).setOnAction(null);
            }
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
