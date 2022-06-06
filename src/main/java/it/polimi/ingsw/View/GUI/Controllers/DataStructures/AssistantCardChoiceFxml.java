package it.polimi.ingsw.View.GUI.Controllers.DataStructures;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.View.GUI.Controllers.AssistantCardChoiceController;
import it.polimi.ingsw.View.GUI.Controllers.BoardFourAdvancedController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.Map;


public class AssistantCardChoiceFxml {
    private int index;
    private ImageView image;
    private Button button;

    private Client client;  // Client class
    private SerializedBoardAbstract board;  // Board
    private AssistantCardChoiceController controller;

    private static final Map<Integer, Integer> priorityMNMovement = Map.of(
            1, 1,
            2, 1,
            3, 2,
            4, 2,
            5, 3,
            6, 3,
            7, 4,
            8, 4,
            9, 5,
            10, 5
    );

    public AssistantCardChoiceFxml(int index, ImageView image, Button button){
        this.index = index;
        this.image = image;
        this.button = button;

        this.button.setOnAction(this::playCard);
        this.button.setOnMouseEntered(this::hoverEnterCard);
        this.button.setOnMouseExited(this::hoverExitCard);
    }

    public Button getButton() {
        return button;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setBoard(SerializedBoardAbstract board) {
        this.board = board;
    }

    public void setController(AssistantCardChoiceController controller) {
        this.controller = controller;
    }

    private void playCard(ActionEvent e) {
        this.client.asyncWriteToSocket("assistantCard " + priorityMNMovement.get(this.index) + " " + this.index);
    }

    private void hoverEnterCard(MouseEvent event) {
        this.button.setScaleX(1.2);
        this.button.setScaleY(1.2);
        this.image.setScaleX(1.2);
        this.image.setScaleY(1.2);
    }

    private void hoverExitCard(MouseEvent event) {
        this.button.setScaleX(1);
        this.button.setScaleY(1);
        this.image.setScaleX(1);
        this.image.setScaleY(1);
    }
}
