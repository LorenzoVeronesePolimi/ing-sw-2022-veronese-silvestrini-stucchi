package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Board.SerializedBoardAdvanced;
import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Places.School.SchoolAdvanced;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * controller of the Character card dialog scene
 */
public class CharacterCardDialogController {
    @FXML private AnchorPane general_anchor;
    @FXML private Label card_name;
    @FXML private Label card_effect;
    @FXML private ImageView card_image;
    @FXML private GridPane students;
    @FXML private Label choice_left_label;
    @FXML private AnchorPane anchor_choices;
    private List<ChoiceBox<String>> choicesLeft;
    @FXML private ChoiceBox<String> choice1_left;
    @FXML private ChoiceBox<String> choice2_left;
    @FXML private ChoiceBox<String> choice3_left;
    @FXML private Label choice_right_label;
    private List<ChoiceBox<String>> choicesRight;
    @FXML private ChoiceBox<String> choice1_right;
    @FXML private ChoiceBox<String> choice2_right;
    @FXML private ChoiceBox<String> choice3_right;
    @FXML private Button use_yes;

    @FXML private Label forbid_label;
    @FXML private ImageView forbid_icon;


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
            Map.entry(CharacterCardEnumeration.PLACE_ONE_STUDENT, "You can take a student from this card and place it on an archipelago. Then, a new student is extracted from the bag and put on this card."),
            Map.entry(CharacterCardEnumeration.REDUCE_COLOUR_IN_DINING, "Chose a colour. Each player puts in the bag 3 students (or less, if he has less) from his own dining room."),
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

    private static final Map<SPColour, String> SPColourString = Map.of(
            SPColour.GREEN, "green",
            SPColour.RED, "red",
            SPColour.YELLOW, "yellow",
            SPColour.PINK, "pink",
            SPColour.BLUE, "blue"
    );

    private static final Map<SPColour, String> studentColourPath = Map.of(
            SPColour.BLUE, "/images/pawns/stud_blue.png",
            SPColour.PINK, "/images/pawns/stud_pink.png",
            SPColour.RED, "/images/pawns/stud_red.png",
            SPColour.YELLOW, "/images/pawns/stud_yellow.png",
            SPColour.GREEN, "/images/pawns/stud_green.png"
    ); // relates the SPColour to the image of the student of that colour

    private CharacterCardEnumeration cardType;
    private SerializedBoardAdvanced board;
    private Client client;
    private GUIViewFX guiViewFX;
    private AbstractCharacterCard card;
    private int playerIndex; // index of the player who wants to use the Character card inside schools list and sitPlayers

    /**
     * mandatory method to show personalized information in the scene. the initialization of the scene must be done here, than can be
     * modified where we want
     */
    public void initialize(){
        this.choicesLeft = new ArrayList<>();
        this.choicesLeft.add(this.choice1_left);
        this.choicesLeft.add(this.choice2_left);
        this.choicesLeft.add(this.choice3_left);
        this.choicesRight = new ArrayList<>();
        this.choicesRight.add(this.choice1_right);
        this.choicesRight.add(this.choice2_right);
        this.choicesRight.add(this.choice3_right);

        this.forbid_label.setVisible(false);
        this.forbid_icon.setVisible(false);
    }

    /**
     * setter of card type
     * @param cardType card type
     */
    public void setCardType(CharacterCardEnumeration cardType) {
        this.cardType = cardType;
    }

    /**
     * setter of the board
     * @param board board
     */
    public void setBoard(SerializedBoardAdvanced board) {
        this.board = board;
    }

    /**
     * setter of the client
     * @param client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * setter of gui view fx
     * @param guiViewFX gui view fx
     */
    public void setGuiViewFX(GUIViewFX guiViewFX) {
        this.guiViewFX = guiViewFX;
    }

    /**
     * setter of visualization of the scene, according to the specific character card selected
     */
    public void setVisualization(boolean useYes){
        this.use_yes.setVisible(useYes);
        this.card_name.setText(cardName.get(this.cardType));
        this.card_effect.setText(cardEffect.get(this.cardType));
        this.card_image.setImage(new Image(getClass().getResource(cardPath.get(this.cardType)).toExternalForm()));

        // set player index
        this.playerIndex = computeMyIndex(this.board);

        // set character card chosen
        for(AbstractCharacterCard c : this.board.getExtractedCards()){
            if(c.getType() == this.cardType){
                this.card = c;
            }
        }
        if(this.card == null){
            System.out.println("Problem with visualization");
        }

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
                this.visualizeTwoExtraIslands();
                break;
            case TWO_EXTRA_POINTS:
                this.visualizeTwoExtraPoints();
                break;
        }
    }

    /**
     * setter of the visualization of the students of the hall in the grid
     * @param students list of students
     */
    private void setStudentsVisualization(List<Student> students){
        int i = 0; // always 0 or 1
        int j = 0;
        for(Student s : students){
            ImageView image = new ImageView(getClass().getResource(studentColourPath.get(s.getColour())).toExternalForm());
            image.setFitHeight(30);
            image.setFitWidth(30);

            this.students.add(image, i, j);

            i++;
            if(i == 2) {
                i = 0;
                j++;
            }
        }
    }

    /**
     * This method is used to generate the out message to the server, in order to add the relevant parameters
     * @param outMessage message that needs to be modified and sent
     * @param content new content of the message
     * @param maxElemNum maximum number of elements that the message can contain
     */
    private void generateOutMessage(StringBuilder outMessage, List<String> content, int maxElemNum) {
        int i = maxElemNum;
        for(String s : content){
            outMessage.append(s);
            outMessage.append(" ");
            i--;
        }
        while(i > 0){ //add "-" if no choice
            outMessage.append("- ");
            i--;
        }
    }

    /**
     * This method is used to set the different left choice box of the Character card Alert
     * @param visible1 relative to choice1_left
     * @param visible2 relative to choice2_left
     * @param visible3 relative to choice3_left
     * @param choices content of the choice boxes
     */
    private void setLeftChoices(boolean visible1, boolean visible2, boolean visible3,List<String> choices) {
        if(visible1) {
            this.choice1_left.getItems().addAll(choices);
            this.choice1_left.setOnAction((ActionEvent e) -> {
                if (this.choice1_left.getValue() == null) {
                    return;
                }

                // removing items if already chosen
                setNewValue(this.choice1_left, this.choice2_left, this.choice3_left, choices);
            });
        } else {
            this.choice1_left.setVisible(false);
        }

        if(visible2) {
            this.choice2_left.getItems().addAll(choices);
            this.choice2_left.setOnAction((ActionEvent e) -> {
                if (this.choice2_left.getValue() == null) {
                    return;
                }

                // removing items if already chosen
                setNewValue(this.choice1_left, this.choice2_left, this.choice3_left, choices);
            });
        } else {
            this.choice2_left.setVisible(false);
        }

        if(visible3) {
            this.choice3_left.getItems().addAll(choices);
            this.choice3_left.setOnAction((ActionEvent e) -> {
                if (this.choice3_left.getValue() == null) {
                    return;
                }

                // removing items if already chosen
                setNewValue(this.choice1_left, this.choice2_left, this.choice3_left, choices);
            });
        } else {
            this.choice3_left.setVisible(false);
        }
    }

    /**
     * This method is used to set the different right choice box of the Character card Alert
     * @param visible1 relative to choice1_right
     * @param visible2 relative to choice2_right
     * @param visible3 relative to choice3_right
     * @param choices content of the choice boxes
     */
    private void setRigthChoices(boolean visible1, boolean visible2, boolean visible3, List<String> choices) {
        if(visible1) {
            this.choice1_right.getItems().addAll(choices);
            this.choice1_right.setOnAction((ActionEvent e) -> {
                if (this.choice1_right.getValue() == null) {
                    return;
                }

                // removing items if already chosen
                setNewValue(this.choice1_right, this.choice2_right, this.choice3_right, choices);
            });
        } else {
            this.choice1_right.setVisible(false);
        }

        if(visible2) {
            this.choice2_right.getItems().addAll(choices);
            this.choice2_right.setOnAction((ActionEvent e) -> {
                if (this.choice2_right.getValue() == null) {
                    return;
                }

                // removing items if already chosen
                setNewValue(this.choice1_right, this.choice2_right, this.choice3_right, choices);
            });
        } else {
            this.choice2_right.setVisible(false);
        }

        if(visible3) {
            this.choice3_right.getItems().addAll(choices);
            this.choice2_right.setOnAction((ActionEvent e) -> {
                if (this.choice3_right.getValue() == null) {
                    return;
                }

                // removing items if already chosen
                setNewValue(this.choice1_right, this.choice2_right, this.choice3_right, choices);
            });
        } else {
            this.choice3_right.setVisible(false);
        }
    }

    /**
     * This method is used to update the value of all the choice boxes when another choice box sets it's value (the user sets it)
     * @param choice1 first choice box
     * @param choice2 second choice box
     * @param choice3 third choice box
     * @param students original content of all choice boxes
     */
    private void setNewValue(ChoiceBox<String> choice1, ChoiceBox<String> choice2, ChoiceBox<String> choice3, List<String> students) {
        List<String> newStudents = new ArrayList<>(students);
        String chosen1 = null;
        String chosen2 = null;
        String chosen3 = null;

        if(choice1.isVisible()) {
            chosen1 = choice1.getValue();
            if(chosen1 != null) newStudents.remove(chosen1);
        }

        if(choice2.isVisible()) {
            chosen2 = choice2.getValue();
            if(chosen2 != null) newStudents.remove(chosen2);
        }

        if(choice3.isVisible()) {
            chosen3 = choice3.getValue();
            if(chosen3 != null) newStudents.remove(chosen3);
        }

        if(choice1.isVisible()) {
            choice1.getItems().clear();
            choice1.getItems().addAll(newStudents);
            if (chosen1 != null) choice1.setValue(chosen1);
        }

        if(choice2.isVisible()) {
            choice2.getItems().clear();
            choice2.getItems().addAll(newStudents);
            if (chosen2 != null) choice2.setValue(chosen2);
        }

        if(choice3.isVisible()) {
            choice3.getItems().clear();
            choice3.getItems().addAll(newStudents);
            if (chosen3 != null) choice3.setValue(chosen3);
        }
    }

    /**
     * This method is used to get a list of String of elements selected in the different choice boxes
     * @param choices list of choice boxes
     * @return
     */
    private ArrayList<String> getSelected(List<ChoiceBox<String>> choices) {
        ArrayList<String> selected = new ArrayList<>();
        for(ChoiceBox<String> c : choices){
            if(c.getValue() != null){
                selected.add(c.getValue());
            }
        }

        return selected;
    }

    /**
     * method that manages the visualization of the exchangeThreeStudents character card
     */
    public void visualizeExchangeThreeStudents(){
        this.choice_left_label.setText("Card:");
        this.choice_right_label.setText("Hall:");

        List<String> cardStudents = new ArrayList();
        for(Student s : ((ExchangeThreeStudents)this.card).getStudentsOnCard()){
            cardStudents.add(SPColourString.get(s.getColour()));
        }
        this.setStudentsVisualization(((ExchangeThreeStudents)this.card).getStudentsOnCard());

        List<String> hallStudents = new ArrayList();
        for(Student s : this.board.getSchools().get(this.playerIndex).getStudentsHall()){
            hallStudents.add(SPColourString.get(s.getColour()));
        }

        // left choices
        setLeftChoices(true, true, true, cardStudents);

        // right choices
        setRigthChoices(true, true, true, hallStudents);


        this.use_yes.setOnAction(actionEvent -> {
            if(!this.canIBuyCard(((SchoolAdvanced)this.board.getSchools().get(this.playerIndex)).getNumCoins(), this.card.getCurrentPrice())){
                guiViewFX.sceneAlert("You have not enough coins!", Alert.AlertType.ERROR);
            }
            else{
                StringBuilder outMessage = new StringBuilder("exchangeThreeStudents ");
                List<String> cardStudentsSelected = getSelected(choicesLeft);
                List<String> hallStudentsSelected = getSelected(choicesRight);

                if(cardStudentsSelected.size() != hallStudentsSelected.size()){
                    guiViewFX.sceneAlert("Incorrect number of colours selected", Alert.AlertType.ERROR);
                }
                else{
                    int initialCardSize = cardStudents.size();
                    for(String s : cardStudentsSelected){
                        cardStudents.remove(s);
                    }
                    if(cardStudents.size() != initialCardSize - cardStudentsSelected.size()){
                        guiViewFX.sceneAlert("You've chosen more students than the card has!", Alert.AlertType.ERROR);
                    }
                    else{
                        int initialHallSize = hallStudents.size();
                        for(String s : hallStudentsSelected){
                            hallStudents.remove(s);
                        }
                        if(hallStudents.size() != initialHallSize - hallStudentsSelected.size()){
                            guiViewFX.sceneAlert("You've chosen more students than you have!", Alert.AlertType.ERROR);
                        }
                        else{
                            generateOutMessage(outMessage, cardStudentsSelected, 3);
                            generateOutMessage(outMessage, hallStudentsSelected, 3);

                            this.client.asyncWriteToSocket(String.valueOf(outMessage));
                        }
                    }
                }
            }

        });
    }

    /**
     * method that manages the visualization of the exchangeTwoHallDining character card
     */
    public void visualizeExchangeTwoHallDining(){
        this.choice_left_label.setText("Hall");
        this.choice_right_label.setText("Dining Room");

        List<String> hallStudents = new ArrayList();
        for(Student s : this.board.getSchools().get(this.playerIndex).getStudentsHall()){
            hallStudents.add(SPColourString.get(s.getColour()));
        }

        List<String> diningStudents = new ArrayList();

        for(SPColour c : SPColourString.keySet()){
            if(this.board.getSchools().get(this.playerIndex).getNumStudentColour(c) > 1){ // add up to two strings of that name
                diningStudents.add(SPColourString.get(c));
                diningStudents.add(SPColourString.get(c));
            }
            else if(this.board.getSchools().get(this.playerIndex).getNumStudentColour(c) > 0){
                diningStudents.add(SPColourString.get(c));
            }
        }

        setLeftChoices(true, true, false, hallStudents);
        setRigthChoices(true, true, false, diningStudents);

        this.use_yes.setOnAction(actionEvent -> {
            if(!this.canIBuyCard(((SchoolAdvanced)this.board.getSchools().get(this.playerIndex)).getNumCoins(), this.card.getCurrentPrice())){
                guiViewFX.sceneAlert("You have not enough coins!", Alert.AlertType.ERROR);
            }
            else{
                StringBuilder outMessage = new StringBuilder("exchangeTwoHallDining ");
                List<String> hallStudentsSelected = getSelected(choicesLeft);
                List<String> diningStudentsSelected = getSelected(choicesRight);

                if(hallStudentsSelected.size() != diningStudentsSelected.size()){
                    guiViewFX.sceneAlert("Incorrect number of colours selected", Alert.AlertType.ERROR);
                }
                else{
                    int initialHallSize = hallStudents.size();
                    for(String s : hallStudentsSelected){
                        hallStudents.remove(s);
                    }
                    if(hallStudents.size() != initialHallSize - hallStudentsSelected.size()){
                        guiViewFX.sceneAlert("You've chosen more students than the card has!", Alert.AlertType.ERROR);
                    }
                    else{
                        int initialDiningSize = diningStudents.size();
                        for(String s : diningStudentsSelected){
                            diningStudents.remove(s);
                        }
                        if(diningStudents.size() != initialDiningSize - diningStudentsSelected.size()){
                            guiViewFX.sceneAlert("You've chosen more students than you have!", Alert.AlertType.ERROR);
                        }
                        else{
                            generateOutMessage(outMessage, hallStudentsSelected, 2);
                            generateOutMessage(outMessage, diningStudentsSelected, 2);
                            this.client.asyncWriteToSocket(String.valueOf(outMessage));
                        }
                    }
                }
            }

        });
    }

    /**
     * method that manages the visualization of the excludeColourFromCounting character card
     */
    public void visualizeExcludeColourFromCounting(){
        this.choice_left_label.setText("Colour to exclude");
        this.choice_right_label.setVisible(false);

        List<String> colours = Arrays.asList("blue", "pink", "red", "yellow", "green");
        setLeftChoices(true, false, false, colours);
        setRigthChoices(false, false, false, null);

        this.use_yes.setOnAction(actionEvent -> {
            if(!this.canIBuyCard(((SchoolAdvanced)this.board.getSchools().get(this.playerIndex)).getNumCoins(), this.card.getCurrentPrice())){
                guiViewFX.sceneAlert("You have not enough coins!", Alert.AlertType.ERROR);
            }
            else{
                this.client.asyncWriteToSocket("excludeColourFromCounting " + choice1_left.getValue());
                //TODO: add lable to indicate colour excluded
            }
        });
    }

    /**
     * method that manages the visualization of the extraStudentInDining character card
     */
    public void visualizeExtraStudentInDining(){
        this.choice_left_label.setText("Extra colour:");

        List<String> cardColours = new ArrayList<>();
        for(Student s : ((ExtraStudentInDining)this.card).getStudentsOnCard()){
            if(!cardColours.contains(s.getColour())){
                cardColours.add(SPColourString.get(s.getColour()));
            }
        }

        this.choice_right_label.setVisible(false);
        setLeftChoices(true, false, false, cardColours);
        setRigthChoices(false, false, false, null);

        this.setStudentsVisualization(((ExtraStudentInDining)this.card).getStudentsOnCard());

        this.use_yes.setOnAction(actionEvent -> {
            if(!this.canIBuyCard(((SchoolAdvanced)this.board.getSchools().get(this.playerIndex)).getNumCoins(), this.card.getCurrentPrice())){
                guiViewFX.sceneAlert("You have not enough coins!", Alert.AlertType.ERROR);
            }
            else{
                this.client.asyncWriteToSocket("extraStudentInDining " + choice1_left.getValue());
            }

        });
    }

    /**
     * method that manages the visualization of the fakeMNMovement character card
     */
    public void visualizeFakeMNMovement(){
        this.choice_left_label.setText("Archipelago:");

        for(int i = 0; i < this.board.getArchipelagos().size(); i++){
            this.choice1_left.getItems().add(Integer.toString(i));
        }
        oneChoiceVisualization();

        this.use_yes.setOnAction(actionEvent -> {
            if(!this.canIBuyCard(((SchoolAdvanced)this.board.getSchools().get(this.playerIndex)).getNumCoins(), this.card.getCurrentPrice())){
                guiViewFX.sceneAlert("You have not enough coins!", Alert.AlertType.ERROR);
            }
            else{
                this.client.asyncWriteToSocket("fakeMNMovement " + choice1_left.getValue());
            }

        });
    }

    /**
     * method that manages the visualization of the forbidIsland character card
     */
    public void visualizeForbidIsland(){
        this.choice_left_label.setText("Archipelago to forbid:");

        for(int i = 0; i < this.board.getArchipelagos().size(); i++){
            this.choice1_left.getItems().add(Integer.toString(i));
        }
        oneChoiceVisualization();

        this.forbid_label.setVisible(true);
        this.forbid_label.setText(Integer.toString(((ForbidIsland)this.card).getForbidIconsRemained()));
        this.forbid_icon.setVisible(true);

        this.use_yes.setOnAction(actionEvent -> {
            if(!this.canIBuyCard(((SchoolAdvanced)this.board.getSchools().get(this.playerIndex)).getNumCoins(), this.card.getCurrentPrice())){
                guiViewFX.sceneAlert("You have not enough coins!", Alert.AlertType.ERROR);
            }
            else{
                this.client.asyncWriteToSocket("forbidIsland " + choice1_left.getValue());
            }

        });
    }

    /**
     * method that manages the visualization of the placeOneStudent character card
     */
    public void visualizePlaceOneStudent(){
        this.choice_left_label.setText("Student to take:");
        this.choice_right_label.setText("Destination:");

        this.setStudentsVisualization(((PlaceOneStudent)this.card).getStudentsOnCard());
        List<String> cardStudents = new ArrayList();
        for(Student s : ((PlaceOneStudent)this.card).getStudentsOnCard()){
            cardStudents.add(SPColourString.get(s.getColour()));
        }
        setLeftChoices(true, false, false, cardStudents);

        this.choice2_right.setVisible(false);
        this.choice3_right.setVisible(false);
        for(int i = 0; i < this.board.getArchipelagos().size(); i++){
            this.choice1_right.getItems().add(Integer.toString(i));
        }
        this.choice1_right.setOnAction((ActionEvent e) -> {
            if(this.choice1_right.getValue() == null) {
                return;
            }

            String chosen = this.choice1_right.getValue();
            List<String> newIndexes = new ArrayList<>();
            for(int i = 0; i < this.board.getArchipelagos().size(); i++){
                newIndexes.add(Integer.toString(i));
            }
            newIndexes.remove(chosen);
            this.choice1_right.getItems().clear();
            this.choice1_right.getItems().addAll(newIndexes);
            this.choice1_right.setValue(chosen);

        });

        this.use_yes.setOnAction(actionEvent -> {
            if(!this.canIBuyCard(((SchoolAdvanced)this.board.getSchools().get(this.playerIndex)).getNumCoins(), this.card.getCurrentPrice())){
                guiViewFX.sceneAlert("You have not enough coins!", Alert.AlertType.ERROR);
            }
            else{
                this.client.asyncWriteToSocket("placeOneStudent " + choice1_left.getValue() + " " + choice1_right.getValue());
            }

        });
    }

    /**
     * method that manages the visualization of the reduceColourInDining character card
     */
    public void visualizeReduceColourInDining(){
        this.choice_left_label.setText("Colour:");
        this.choice_right_label.setVisible(false);

        List<String> colours = Arrays.asList("blue", "pink", "red", "yellow", "green");
        setLeftChoices(true, false, false, colours);
        setRigthChoices(false, false, false, null);

        this.use_yes.setOnAction(actionEvent -> {
            if(!this.canIBuyCard(((SchoolAdvanced)this.board.getSchools().get(this.playerIndex)).getNumCoins(), this.card.getCurrentPrice())){
                guiViewFX.sceneAlert("You have not enough coins!", Alert.AlertType.ERROR);
            }
            else{
                this.client.asyncWriteToSocket("reduceColourInDining " + choice1_left.getValue());
            }
        });
    }

    /**
     * method that manages the visualization of the takeProfessorOnEquity character card
     */
    public void visualizeTakeProfessorOnEquity(){
        noChoiceVisualization();

        this.use_yes.setOnAction(actionEvent -> {
            if(!this.canIBuyCard(((SchoolAdvanced)this.board.getSchools().get(this.playerIndex)).getNumCoins(), this.card.getCurrentPrice())){
                guiViewFX.sceneAlert("You have not enough coins!", Alert.AlertType.ERROR);
            }
            else{
                this.client.asyncWriteToSocket("takeProfessorOnEquity ");
            }
        });
    }

    /**
     * method that manages the visualization of the towerNoValue character card
     */
    public void visualizeTowerNoValue(){
        noChoiceVisualization();

        this.use_yes.setOnAction(actionEvent -> {
            if(!this.canIBuyCard(((SchoolAdvanced)this.board.getSchools().get(this.playerIndex)).getNumCoins(), this.card.getCurrentPrice())){
                guiViewFX.sceneAlert("You have not enough coins!", Alert.AlertType.ERROR);
            }
            else {
                this.client.asyncWriteToSocket("towerNoValue ");
            }
        });
    }

    /**
     * method that manages the visualization of the twoExtraIslands character card
     */
    public void visualizeTwoExtraIslands(){
        noChoiceVisualization();

        this.use_yes.setOnAction(actionEvent -> {
            if(!this.canIBuyCard(((SchoolAdvanced)this.board.getSchools().get(this.playerIndex)).getNumCoins(), this.card.getCurrentPrice())){
                guiViewFX.sceneAlert("You have not enough coins!", Alert.AlertType.ERROR);
            }
            else {
                this.client.asyncWriteToSocket("twoExtraIslands ");
            }
        });
    }

    /**
     * method that manages the visualization of the twoExtraPoints character card
     */
    public void visualizeTwoExtraPoints(){
        noChoiceVisualization();

        this.use_yes.setOnAction(actionEvent -> {
            if(!this.canIBuyCard(((SchoolAdvanced)this.board.getSchools().get(this.playerIndex)).getNumCoins(), this.card.getCurrentPrice())){
                guiViewFX.sceneAlert("You have not enough coins!", Alert.AlertType.ERROR);
            }
            else{
                this.client.asyncWriteToSocket("twoExtraPoints ");
            }
        });
    }


    private void noChoiceVisualization() {
        this.choice_left_label.setVisible(false);
        this.choice1_left.setVisible(false);
        oneChoiceVisualization();
    }

    private void oneChoiceVisualization() {
        this.choice_right_label.setVisible(false);

        this.choice2_left.setVisible(false);
        this.choice3_left.setVisible(false);
        this.choice1_right.setVisible(false);
        this.choice2_right.setVisible(false);
        this.choice3_right.setVisible(false);
    }

    /**
     * method that says if it is possible for the client to buy a card
     * @param coinPlayer number of coins of the player
     * @param costCard cost of the card in coins
     * @return true if is possible for the client to buy a card, false otherwise
     */
    private boolean canIBuyCard(int coinPlayer, int costCard){
        if(coinPlayer < costCard){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * method that computes the index of the player
     * @param boardAbstract serialized board notified by the model
     * @return the index
     */
    private int computeMyIndex(SerializedBoardAbstract boardAbstract) {
        int i = 0;
        for(Player p: boardAbstract.getSitPlayers()) {
            if(p.getNickname().equals(this.client.getNickname())) {
                return i;
            }
            i++;
        }
        return -1;
    }
}
