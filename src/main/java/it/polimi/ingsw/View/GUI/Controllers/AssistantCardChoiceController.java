package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.View.GUI.Controllers.DataStructures.AssistantCardChoiceFxml;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AssistantCardChoiceController implements GUIController, Initializable {
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


    private List<AssistantCardChoiceFxml> assistantCardsChoiceFxml;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.assistantCardsChoiceFxml = new ArrayList<>();
        assistantCardsChoiceFxml.add(new AssistantCardChoiceFxml(1, imageC1, buttonC1));
        assistantCardsChoiceFxml.add(new AssistantCardChoiceFxml(2, imageC2, buttonC2));
        assistantCardsChoiceFxml.add(new AssistantCardChoiceFxml(3, imageC3, buttonC3));
        assistantCardsChoiceFxml.add(new AssistantCardChoiceFxml(4, imageC4, buttonC4));
        assistantCardsChoiceFxml.add(new AssistantCardChoiceFxml(5, imageC5, buttonC5));
        assistantCardsChoiceFxml.add(new AssistantCardChoiceFxml(6, imageC6, buttonC6));
        assistantCardsChoiceFxml.add(new AssistantCardChoiceFxml(7, imageC7, buttonC7));
        assistantCardsChoiceFxml.add(new AssistantCardChoiceFxml(8, imageC8, buttonC8));
        assistantCardsChoiceFxml.add(new AssistantCardChoiceFxml(9, imageC9, buttonC9));
        assistantCardsChoiceFxml.add(new AssistantCardChoiceFxml(10, imageC10, buttonC10));
    }

    public void setDataStructures(){
        for(AssistantCardChoiceFxml c : this.assistantCardsChoiceFxml){
            c.setController(this);
            c.setClient(this.client);
            c.setBoard(this.serializedBoardAbstract);
        }
    }

    public void setSerializedBoardAbstract(SerializedBoardAbstract serializedBoardAbstract) {
        this.serializedBoardAbstract = serializedBoardAbstract;
        Player player = null;
        // retrieve the corresponding Player class
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
        List<Integer> present = new ArrayList<>();

        // searching for cards to remove from visualization
        int i = 1;
        for(int k = 0; k < playerHand.size(); k++) {
            if(playerHand.get(k).getTurnPriority() != i) {
                System.out.println("[AssistantCardChoiceController, setSerializedBoardAbstract]: removing #" + i);
                removed.add(i); // card with turn priority i has already been played
                k--;
            } else {
                present.add(i);
            }

            i++;
        }

        // removing cards from visualization
        for(int w = 1; w <= this.assistantCardsChoiceFxml.size(); w++) {
            if(removed.contains(w) || !present.contains(w)) {
                this.assistantCardsChoiceFxml.get(w-1).getButton().setOpacity(0.7);
                this.assistantCardsChoiceFxml.get(w-1).getButton().setOnAction(null);
                this.assistantCardsChoiceFxml.get(w-1).getButton().setOnMouseEntered(null);
                this.assistantCardsChoiceFxml.get(w-1).getButton().setOnMouseExited(null);
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
