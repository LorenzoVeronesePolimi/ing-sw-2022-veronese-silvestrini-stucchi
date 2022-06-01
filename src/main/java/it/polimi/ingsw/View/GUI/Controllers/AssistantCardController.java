package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AssistantCardController implements GUIController, Initializable {
    private GUIViewFX guiViewFX;
    private Client client;

    private SerializedBoardAbstract serializedBoardAbstract;
    private List<Button> cardButtons = new ArrayList<>();

    @FXML private ImageView imageC1;
    @FXML private ImageView imageC2;
    @FXML private ImageView imageC3;
    @FXML private ImageView imageC4;
    @FXML private ImageView imageC5;
    @FXML private ImageView imageC6;
    @FXML private ImageView imageC7;
    @FXML private ImageView imageC8;
    @FXML private ImageView imageC9;
    @FXML private ImageView imageC10;

    @FXML private Button buttonC1;
    @FXML private Button buttonC2;
    @FXML private Button buttonC3;
    @FXML private Button buttonC4;
    @FXML private Button buttonC5;
    @FXML private Button buttonC6;
    @FXML private Button buttonC7;
    @FXML private Button buttonC8;
    @FXML private Button buttonC9;
    @FXML private Button buttonC10;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Card1
        this.cardButtons.add(buttonC1);
        this.buttonC1.setOnAction(this::playCard1);
        this.buttonC1.setOnMouseEntered(this::hoverEnterCard1);
        this.buttonC1.setOnMouseExited(this::hoverExitCard1);

        // Card2
        this.cardButtons.add(buttonC2);
        this.buttonC2.setOnAction(this::playCard2);
        this.buttonC2.setOnMouseEntered(this::hoverEnterCard2);
        this.buttonC2.setOnMouseExited(this::hoverExitCard2);

        // Card3
        this.cardButtons.add(buttonC3);
        this.buttonC3.setOnAction(this::playCard3);
        this.buttonC3.setOnMouseEntered(this::hoverEnterCard3);
        this.buttonC3.setOnMouseExited(this::hoverExitCard3);

        // Card4
        this.cardButtons.add(buttonC4);
        this.buttonC4.setOnAction(this::playCard4);
        this.buttonC4.setOnMouseEntered(this::hoverEnterCard4);
        this.buttonC4.setOnMouseExited(this::hoverExitCard4);

        // Card5
        this.cardButtons.add(buttonC5);
        this.buttonC5.setOnAction(this::playCard5);
        this.buttonC5.setOnMouseEntered(this::hoverEnterCard5);
        this.buttonC5.setOnMouseExited(this::hoverExitCard5);

        // Card6
        this.cardButtons.add(buttonC6);
        this.buttonC6.setOnAction(this::playCard6);
        this.buttonC6.setOnMouseEntered(this::hoverEnterCard6);
        this.buttonC6.setOnMouseExited(this::hoverExitCard6);

        // Card7
        this.cardButtons.add(buttonC7);
        this.buttonC7.setOnAction(this::playCard7);
        this.buttonC7.setOnMouseEntered(this::hoverEnterCard7);
        this.buttonC7.setOnMouseExited(this::hoverExitCard7);

        // Card8
        this.cardButtons.add(buttonC8);
        this.buttonC8.setOnAction(this::playCard8);
        this.buttonC8.setOnMouseEntered(this::hoverEnterCard8);
        this.buttonC8.setOnMouseExited(this::hoverExitCard8);

        // Card9
        this.cardButtons.add(buttonC9);
        this.buttonC9.setOnAction(this::playCard9);
        this.buttonC9.setOnMouseEntered(this::hoverEnterCard9);
        this.buttonC9.setOnMouseExited(this::hoverExitCard9);


        // Card10
        this.cardButtons.add(buttonC10);
        this.buttonC10.setOnAction(this::playCard10);
        this.buttonC10.setOnMouseEntered(this::hoverEnterCard10);
        this.buttonC10.setOnMouseExited(this::hoverExitCard10);
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

    private void hoverEnterCard1(MouseEvent event) {
        this.buttonC1.setScaleX(1.2);
        this.buttonC1.setScaleY(1.2);
        this.imageC1.setScaleX(1.2);
        this.imageC1.setScaleY(1.2);
    }

    private void hoverExitCard1(MouseEvent event) {
        this.buttonC1.setScaleX(1);
        this.buttonC1.setScaleY(1);
        this.imageC1.setScaleX(1);
        this.imageC1.setScaleY(1);
    }

    private void hoverEnterCard2(MouseEvent event) {
        this.buttonC2.setScaleX(1.2);
        this.buttonC2.setScaleY(1.2);
        this.imageC2.setScaleX(1.2);
        this.imageC2.setScaleY(1.2);
    }

    private void hoverExitCard2(MouseEvent event) {
        this.buttonC2.setScaleX(1);
        this.buttonC2.setScaleY(1);
        this.imageC2.setScaleX(1);
        this.imageC2.setScaleY(1);
    }

    private void hoverEnterCard3(MouseEvent event) {
        this.buttonC3.setScaleX(1.2);
        this.buttonC3.setScaleY(1.2);
        this.imageC3.setScaleX(1.2);
        this.imageC3.setScaleY(1.2);
    }

    private void hoverExitCard3(MouseEvent event) {
        this.buttonC3.setScaleX(1);
        this.buttonC3.setScaleY(1);
        this.imageC3.setScaleX(1);
        this.imageC3.setScaleY(1);
    }

    private void hoverEnterCard4(MouseEvent event) {
        this.buttonC4.setScaleX(1.2);
        this.buttonC4.setScaleY(1.2);
        this.imageC4.setScaleX(1.2);
        this.imageC4.setScaleY(1.2);
    }

    private void hoverExitCard4(MouseEvent event) {
        this.buttonC4.setScaleX(1);
        this.buttonC4.setScaleY(1);
        this.imageC4.setScaleX(1);
        this.imageC4.setScaleY(1);
    }

    private void hoverEnterCard5(MouseEvent event) {
        this.buttonC5.setScaleX(1.2);
        this.buttonC5.setScaleY(1.2);
        this.imageC5.setScaleX(1.2);
        this.imageC5.setScaleY(1.2);
    }

    private void hoverExitCard5(MouseEvent event) {
        this.buttonC5.setScaleX(1);
        this.buttonC5.setScaleY(1);
        this.imageC5.setScaleX(1);
        this.imageC5.setScaleY(1);
    }

    private void hoverEnterCard6(MouseEvent event) {
        this.buttonC6.setScaleX(1.2);
        this.buttonC6.setScaleY(1.2);
        this.imageC6.setScaleX(1.2);
        this.imageC6.setScaleY(1.2);
    }

    private void hoverExitCard6(MouseEvent event) {
        this.buttonC6.setScaleX(1);
        this.buttonC6.setScaleY(1);
        this.imageC6.setScaleX(1);
        this.imageC6.setScaleY(1);
    }

    private void hoverEnterCard7(MouseEvent event) {
        this.buttonC7.setScaleX(1.2);
        this.buttonC7.setScaleY(1.2);
        this.imageC7.setScaleX(1.2);
        this.imageC7.setScaleY(1.2);
    }

    private void hoverExitCard7(MouseEvent event) {
        this.buttonC7.setScaleX(1);
        this.buttonC7.setScaleY(1);
        this.imageC7.setScaleX(1);
        this.imageC7.setScaleY(1);
    }

    private void hoverEnterCard8(MouseEvent event) {
        this.buttonC8.setScaleX(1.2);
        this.buttonC8.setScaleY(1.2);
        this.imageC8.setScaleX(1.2);
        this.imageC8.setScaleY(1.2);
    }

    private void hoverExitCard8(MouseEvent event) {
        this.buttonC8.setScaleX(1);
        this.buttonC8.setScaleY(1);
        this.imageC8.setScaleX(1);
        this.imageC8.setScaleY(1);
    }

    private void hoverEnterCard9(MouseEvent event) {
        this.buttonC9.setScaleX(1.2);
        this.buttonC9.setScaleY(1.2);
        this.imageC9.setScaleX(1.2);
        this.imageC9.setScaleY(1.2);
    }

    private void hoverExitCard9(MouseEvent event) {
        this.buttonC9.setScaleX(1);
        this.buttonC9.setScaleY(1);
        this.imageC9.setScaleX(1);
        this.imageC9.setScaleY(1);
    }

    private void hoverEnterCard10(MouseEvent event) {
        this.buttonC10.setScaleX(1.2);
        this.buttonC10.setScaleY(1.2);
        this.imageC10.setScaleX(1.2);
        this.imageC10.setScaleY(1.2);
    }

    private void hoverExitCard10(MouseEvent event) {
        this.buttonC10.setScaleX(1);
        this.buttonC10.setScaleY(1);
        this.imageC10.setScaleX(1);
        this.imageC10.setScaleY(1);
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
                this.cardButtons.get(w).setOnMouseEntered(null);
                this.cardButtons.get(w).setOnMouseExited(null);
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
