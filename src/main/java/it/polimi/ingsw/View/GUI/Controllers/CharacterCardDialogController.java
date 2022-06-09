package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Messages.Enumerations.INMessageType;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Board.SerializedBoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.Map;

public class CharacterCardDialogController {
    @FXML private AnchorPane general_anchor;
    @FXML private Label card_name;
    @FXML private Label card_effect;
    @FXML private ImageView card_image;
    @FXML private AnchorPane anchor_choices;
    @FXML private ChoiceBox<String> choice1_left;
    @FXML private ChoiceBox<String> choice2_left;
    @FXML private ChoiceBox<String> choice3_left;
    @FXML private ChoiceBox<String> choice1_right;
    @FXML private ChoiceBox<String> choice2_right;
    @FXML private ChoiceBox<String> choice3_right;
    @FXML private Button use_yes;
    @FXML private Button use_no;

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

    private CharacterCardEnumeration cardType;
    private SerializedBoardAdvanced board;

    public void initialize(){

    }

    public void setCardType(CharacterCardEnumeration cardType) {
        this.cardType = cardType;
    }

    public void setBoard(SerializedBoardAdvanced board) {
        this.board = board;
    }

    public void setVisualization(){
        this.card_name.setText(cardName.get(this.cardType));
        this.card_effect.setText(cardEffect.get(this.cardType));
        this.card_image.setImage(new Image(getClass().getResource(cardPath.get(this.cardType)).toExternalForm()));

        switch(this.cardType){
            case EXCHANGE_THREE_STUDENTS:
                this.visualizeExchangeThreeStudents();
                break;
            case EXCHANGE_TWO_HALL_DINING:
                this.visualizeExchangeTwoHallDining();
                break;
            case EXCLUDE_COLOUR_FROM_COUNTING:
                this.visualizeExcludeColourFromCounting();
                break;
            case EXTRA_STUDENT_IN_DINING:
                this.visualizeExtraStudentInDining();
                break;
            case FAKE_MN_MOVEMENT:
                this.visualizeFakeMNMovement();
                break;
            case FORBID_ISLAND:
                this.visualizeForbidIsland();
                break;
            case PLACE_ONE_STUDENT:
                this.visualizePlaceOneStudent();
                break;
            case REDUCE_COLOUR_IN_DINING:
                this.visualizeReduceColourInDining();
                break;
            case TAKE_PROFESSOR_ON_EQUITY:
                this.visualizeTakeProfessorOnEquity();
                break;
            case TOWER_NO_VALUE:
                this.visualizeTowerNoValue();
                break;
            case TWO_EXTRA_ISLANDS:
                this.visualizeTowExtraIslands();
                break;
            case TWO_EXTRA_POINTS:
                this.visualizeTwoExtraPoints();
                break;
        }
    }

    public void visualizeExchangeThreeStudents(){

    }

    public void visualizeExchangeTwoHallDining(){

    }

    public void visualizeExcludeColourFromCounting(){

    }

    public void visualizeExtraStudentInDining(){

    }

    public void visualizeFakeMNMovement(){

    }

    public void visualizeForbidIsland(){

    }

    public void visualizePlaceOneStudent(){

    }

    public void visualizeReduceColourInDining(){

    }

    public void visualizeTakeProfessorOnEquity(){

    }

    public void visualizeTowerNoValue(){

    }

    public void visualizeTowExtraIslands(){

    }

    public void visualizeTwoExtraPoints(){

    }




}
