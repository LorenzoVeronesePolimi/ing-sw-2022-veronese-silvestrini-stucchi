package it.polimi.ingsw.View.GUI.Controllers.DataStructures;

import it.polimi.ingsw.Model.Cards.AbstractCharacterCard;
import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Map;

public class CharacterCardFxml {
    private final GridPane cards;

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

    public CharacterCardFxml(GridPane cards) {
        this.cards = cards;
    }

    public void setVisible(boolean isVisible){
        for(Node c : this.cards.getChildren()){
            c.setVisible(isVisible);
        }
    }

    public void setCharacterCardsVisualization(List<AbstractCharacterCard> cards, double scale){
        int i = 0;
        for(AbstractCharacterCard c : cards){
            ImageView image = new ImageView(getClass().getResource(cardPath.get(c.getType())).toExternalForm());
            image.setFitWidth(105 * scale);
            image.setPreserveRatio(true);
            this.cards.add(image, i, 0);

            i++;
        }
    }
}
