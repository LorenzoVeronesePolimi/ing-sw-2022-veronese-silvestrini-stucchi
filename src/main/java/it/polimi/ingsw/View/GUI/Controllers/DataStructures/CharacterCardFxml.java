package it.polimi.ingsw.View.GUI.Controllers.DataStructures;

import com.sun.prism.Image;
import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Board.SerializedBoardAdvanced;
import it.polimi.ingsw.Model.Cards.AbstractCharacterCard;
import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import it.polimi.ingsw.View.GUI.Controllers.BoardFourAdvancedController;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CharacterCardFxml {
    private final GridPane cards;
    private List<Label> costs;

    private Client client;  // Client class
    private SerializedBoardAdvanced board;  // Board

    private static final Map<CharacterCardEnumeration, String> cardPath = Map.ofEntries(
            Map.entry(CharacterCardEnumeration.EXCHANGE_THREE_STUDENTS, "/images/Characters/ExchangeThreeStudents.jpg"),
            Map.entry(CharacterCardEnumeration.EXCHANGE_TWO_HALL_DINING, "/images/Characters/ExchangeTwoHallDining.jpg"),
            Map.entry(CharacterCardEnumeration.EXCLUDE_COLOUR_FROM_COUNTING, "/images/Characters/ExcludeColourFromCounting.jpg"),
            Map.entry(CharacterCardEnumeration.EXTRA_STUDENT_IN_DINING, "/images/Characters/ExtraStudentInDining.jpg"),
            Map.entry(CharacterCardEnumeration.FAKE_MN_MOVEMENT, "/images/Characters/FakeMNMovement.jpg"),
            Map.entry(CharacterCardEnumeration.FORBID_ISLAND, "/images/Characters/ForbidIsland.jpg"),
            Map.entry(CharacterCardEnumeration.PLACE_ONE_STUDENT, "/images/Characters/PlaceOneStudent.jpg"),
            Map.entry(CharacterCardEnumeration.REDUCE_COLOUR_IN_DINING, "/images/Characters/ReduceColourInDining.jpg"),
            Map.entry(CharacterCardEnumeration.TAKE_PROFESSOR_ON_EQUITY, "/images/Characters/TakeProfessorOnEquity.jpg"),
            Map.entry(CharacterCardEnumeration.TOWER_NO_VALUE, "/images/Characters/TowerNoValue.jpg"),
            Map.entry(CharacterCardEnumeration.TWO_EXTRA_ISLANDS, "/images/Characters/TwoExtraIslands.jpg"),
            Map.entry(CharacterCardEnumeration.TWO_EXTRA_POINTS, "/images/Characters/TwoExtraPoints.jpg")
    );

    private static final Map<CharacterCardEnumeration, String> cardEffect = Map.ofEntries(
            Map.entry(CharacterCardEnumeration.EXCHANGE_THREE_STUDENTS, "You can exchange up to three students between the ones that you have in the hall and the ones that are in placed on this card."),
            Map.entry(CharacterCardEnumeration.EXCHANGE_TWO_HALL_DINING, "You can exchange up to two students between the ones in your dining room and the ones in your hall."),
            Map.entry(CharacterCardEnumeration.EXCLUDE_COLOUR_FROM_COUNTING, "Chooses a colour: for that turn, during the influence computation, that colour won't be considered"),
            Map.entry(CharacterCardEnumeration.EXTRA_STUDENT_IN_DINING, "Choose one student from the four that are on this card: put it in your dining Room, then a student is extracted from the bag and added to this card."),
            Map.entry(CharacterCardEnumeration.FAKE_MN_MOVEMENT, "Chooses an archipelago: you can try to conquer it as if mother nature has ended her movement on that archipelago. After that, the round continues normally."),
            Map.entry(CharacterCardEnumeration.FORBID_ISLAND, "Take one No Entry tile from this card and put it on an archipelago. The next time Mother Nature goes to that archipelago, she won't conquer it and the tile will be dropped."),
            Map.entry(CharacterCardEnumeration.PLACE_ONE_STUDENT, "You can choose a student on this card and place it on an archipelago. Then, a new student is extracted from the bag and put on this card."),
            Map.entry(CharacterCardEnumeration.REDUCE_COLOUR_IN_DINING, "Chose a colour. Each player puts in the bag 2 students (or less, if he has less) from his own dining room."),
            Map.entry(CharacterCardEnumeration.TAKE_PROFESSOR_ON_EQUITY, "During this turn, you take control of the professors even if you have the same number of students in the school as the current owner of the professor."),
            Map.entry(CharacterCardEnumeration.TOWER_NO_VALUE, "For this turn, when resolving a conquering on an archipelago, towers do not count towards influence."),
            Map.entry(CharacterCardEnumeration.TWO_EXTRA_ISLANDS, "You can move Mother Nature up to two additional archipelagos than is indicated in the assistant card you played."),
            Map.entry(CharacterCardEnumeration.TWO_EXTRA_POINTS, "For this turn, during the influence calculation, you will have two additional point.")
    );

    private static final Map<CharacterCardEnumeration, String> cardName = Map.ofEntries(
            Map.entry(CharacterCardEnumeration.EXCHANGE_THREE_STUDENTS, "Exchange Three Students"),
            Map.entry(CharacterCardEnumeration.EXCHANGE_TWO_HALL_DINING, "Exchange Two Hall-Dining Room"),
            Map.entry(CharacterCardEnumeration.EXCLUDE_COLOUR_FROM_COUNTING, "Exclude Colour from Counting"),
            Map.entry(CharacterCardEnumeration.EXTRA_STUDENT_IN_DINING, "Extra Student in Dining Room"),
            Map.entry(CharacterCardEnumeration.FAKE_MN_MOVEMENT, "Fake Mother Nature Movement"),
            Map.entry(CharacterCardEnumeration.FORBID_ISLAND, "Forbid Island"),
            Map.entry(CharacterCardEnumeration.PLACE_ONE_STUDENT, "Place One Student"),
            Map.entry(CharacterCardEnumeration.REDUCE_COLOUR_IN_DINING, "Reduce Colour in Dining Room"),
            Map.entry(CharacterCardEnumeration.TAKE_PROFESSOR_ON_EQUITY, "Take Professor on Equity"),
            Map.entry(CharacterCardEnumeration.TOWER_NO_VALUE, "Tower No Value"),
            Map.entry(CharacterCardEnumeration.TWO_EXTRA_ISLANDS, "Two Extra Islands"),
            Map.entry(CharacterCardEnumeration.TWO_EXTRA_POINTS, "Two Extra Points")
    );

    public CharacterCardFxml(GridPane cards, Label cost1, Label cost2, Label cost3) {
        this.cards = cards;
        this.costs = new ArrayList<>();
        this.costs.add(cost1);
        this.costs.add(cost2);
        this.costs.add(cost3);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setBoard(SerializedBoardAdvanced board) {
        this.board = board;
    }


    public void setVisible(boolean isVisible){
        for(Node c : this.cards.getChildren()){
            c.setVisible(isVisible);
        }
    }

    private void onMouseClicked(ImageView image, CharacterCardEnumeration type, GUIViewFX guiFX){
        image.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            /*
            guiFX.characterCardAlert(cardEffect.get(type));
            event.consume();*/
            //guiFX.characterCardAlert(type, cardName.get(type), cardEffect.get(type), cardPath.get(type));
            guiFX.characterCardAlert(type, this.board);
        });
    }

    public void setCharacterCardsVisualization(List<AbstractCharacterCard> cards, double scale, GUIViewFX guiViewFX){
        int i = 0;
        for(AbstractCharacterCard c : cards){
            // set cost
            this.costs.get(i).setText(Integer.toString(c.getCurrentPrice()));

            // set visualization
            ImageView image = new ImageView(getClass().getResource(cardPath.get(c.getType())).toExternalForm());
            image.setFitWidth(100 * scale);
            image.setPreserveRatio(true);
            this.onMouseClicked(image, c.getType(), guiViewFX);
            this.cards.add(image, i, 0);

            i++;
        }
    }


}
