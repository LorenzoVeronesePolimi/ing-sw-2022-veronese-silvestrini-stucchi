package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Board.SerializedBoardAdvanced;
import it.polimi.ingsw.Model.Cards.AbstractCharacterCard;
import it.polimi.ingsw.Model.Cards.ExchangeThreeStudents;
import it.polimi.ingsw.Model.Cards.ForbidIsland;
import it.polimi.ingsw.Model.Cards.PlaceOneStudent;
import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public void setCardType(CharacterCardEnumeration cardType) {
        this.cardType = cardType;
    }

    public void setBoard(SerializedBoardAdvanced board) {
        this.board = board;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setGuiViewFX(GUIViewFX guiViewFX) {
        this.guiViewFX = guiViewFX;
    }

    public void setVisualization(){
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

        this.choice1_left.getItems().addAll(cardStudents);
        this.choice2_left.getItems().addAll(cardStudents);
        this.choice3_left.getItems().addAll(cardStudents);
        this.choice1_right.getItems().addAll(hallStudents);
        this.choice2_right.getItems().addAll(hallStudents);
        this.choice3_right.getItems().addAll(hallStudents);

        this.use_yes.setOnAction(actionEvent -> {
            StringBuilder outMessage = new StringBuilder("exchangeThreeStudents ");
            List<String> cardStudentsSelected = new ArrayList<>();
            List<String> hallStudentsSelected = new ArrayList<>();
            for(ChoiceBox<String> c : choicesLeft){
                System.out.println(c.getValue());
                if(c.getValue() != null){
                    //cardStudentsSelected.add(c.getSelectionModel().getSelectedItem());
                    cardStudentsSelected.add(c.getValue());
                }
            }
            for(ChoiceBox<String> c : choicesRight){
                if(c.getValue() != null){
                    hallStudentsSelected.add(c.getValue());
                }
            }
            if(cardStudentsSelected.size() != hallStudentsSelected.size()){
                guiViewFX.sceneAlert("Incorrect colours selected", Alert.AlertType.ERROR);
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
                    if(cardStudents.size() != initialCardSize - cardStudentsSelected.size()){
                        guiViewFX.sceneAlert("You've chosen more students than you have!", Alert.AlertType.ERROR);
                    }
                    else{
                        int i = 3;//cardStudentsSelected.size();
                        for(String s : cardStudentsSelected){
                            outMessage.append(s);
                            outMessage.append(" ");
                            i--;
                        }
                        while(i > 0){ //add "-" if no choice
                            outMessage.append("- ");
                            i--;
                        }
                        i = 3;//hallStudentsSelected.size();
                        for(String s : hallStudentsSelected){
                            outMessage.append(s);
                            outMessage.append(" ");
                            i--;
                        }
                        while(i > 0){ //add "-" if no choice
                            outMessage.append("- ");
                            i--;
                        }
                        this.client.asyncWriteToSocket(String.valueOf(outMessage));
                    }
                }
            }
        });
    }


    public void visualizeExchangeTwoHallDining(){
        this.choice_left_label.setText("Hall");
        this.choice_right_label.setText("Dining Room");

        List<String> hallStudents = new ArrayList();
        for(Student s : this.board.getSchools().get(this.playerIndex).getStudentsHall()){
            hallStudents.add(SPColourString.get(s.getColour()));
        }

        List<String> diningStudents = new ArrayList();
        for(SPColour c : SPColourString.keySet()){
            System.out.println(c);
        }
        System.out.println(this.playerIndex);

        for(SPColour c : SPColourString.keySet()){
            if(this.board.getSchools().get(this.playerIndex).getNumStudentColour(c) > 1){ // add up to two strings of that name
                diningStudents.add(SPColourString.get(c));
                diningStudents.add(SPColourString.get(c));
            }
            else if(this.board.getSchools().get(this.playerIndex).getNumStudentColour(c) > 0){
                diningStudents.add(SPColourString.get(c));
            }
        }

        for(String a : diningStudents){
            System.out.println(a);
        }

        this.choice1_left.getItems().addAll(hallStudents);
        this.choice2_left.getItems().addAll(hallStudents);
        this.choice3_left.setVisible(false);
        this.choice1_right.getItems().addAll(diningStudents);
        this.choice2_right.getItems().addAll(diningStudents);
        this.choice3_right.setVisible(false);

        this.use_yes.setOnAction(actionEvent -> {
            StringBuilder outMessage = new StringBuilder("exchangeTwoHallDining ");
            List<String> hallStudentsSelected = new ArrayList<>();
            List<String> diningStudentsSelected = new ArrayList<>();
            for(ChoiceBox<String> c : choicesLeft){
                if(c.getValue() != null){
                    //cardStudentsSelected.add(c.getSelectionModel().getSelectedItem());
                    hallStudentsSelected.add(c.getValue());
                }
            }
            for(ChoiceBox<String> c : choicesRight){
                if(c.getValue() != null){
                    diningStudentsSelected.add(c.getValue());
                }
            }
            if(hallStudentsSelected.size() != diningStudentsSelected.size()){
                guiViewFX.sceneAlert("Incorrect colours selected", Alert.AlertType.ERROR);
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
                        int i = 2;//hallStudentsSelected.size();
                        for(String s : hallStudentsSelected){
                            outMessage.append(s);
                            outMessage.append(" ");
                            i--;
                        }
                        while(i > 0){ //add "-" if no choice
                            outMessage.append("- ");
                            i--;
                        }
                        i = 2;//hallStudentsSelected.size();
                        for(String s : diningStudentsSelected){
                            outMessage.append(s);
                            outMessage.append(" ");
                            i--;
                        }
                        while(i > 0){ //add "-" if no choice
                            outMessage.append("- ");
                            i--;
                        }
                        this.client.asyncWriteToSocket(String.valueOf(outMessage));
                    }
                }
            }
        });
    }

    public void visualizeExcludeColourFromCounting(){
        this.choice_left_label.setText("Colour to exclude");

        String[] colours = {"blue", "pink", "red", "yellow", "green"};
        for(String c : colours){
            this.choice1_left.getItems().add(c);
        }
        oneChoiceVisualization();

        this.use_yes.setOnAction(actionEvent -> {
            this.client.asyncWriteToSocket("excludeColourFromCounting " + choice1_left.getValue());
        });
    }

    public void visualizeExtraStudentInDining(){
        this.choice_left_label.setText("Extra colour:");

        String[] colours = {"blue", "pink", "red", "yellow", "green"};
        for(String c : colours){
            this.choice1_left.getItems().add(c);
        }
        oneChoiceVisualization();

        this.use_yes.setOnAction(actionEvent -> {
            this.client.asyncWriteToSocket("extraStudentInDining " + choice1_left.getValue());
        });
    }

    public void visualizeFakeMNMovement(){
        this.choice_left_label.setText("Archipelago:");

        for(int i = 0; i < this.board.getArchipelagos().size(); i++){
            this.choice1_left.getItems().add(Integer.toString(i));
        }
        oneChoiceVisualization();

        this.use_yes.setOnAction(actionEvent -> {
            this.client.asyncWriteToSocket("fakeMNMovement " + choice1_left.getValue());
        });
    }

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
            this.client.asyncWriteToSocket("forbidIsland " + choice1_left.getValue());
        });
    }

    public void visualizePlaceOneStudent(){
        this.choice_left_label.setText("Student to take:");
        this.choice_right_label.setText("Destination:");

        this.choice2_left.setVisible(false);
        this.choice3_left.setVisible(false);
        this.choice2_right.setVisible(false);
        this.choice3_right.setVisible(false);

        List<String> cardStudents = new ArrayList();
        for(Student s : ((PlaceOneStudent)this.card).getStudentsOnCard()){
            cardStudents.add(SPColourString.get(s.getColour()));
        }
        this.setStudentsVisualization(((PlaceOneStudent)this.card).getStudentsOnCard());
        this.choice1_left.getItems().addAll(cardStudents);

        for(int i = 0; i < this.board.getArchipelagos().size(); i++){
            this.choice1_right.getItems().add(Integer.toString(i));
        }

        this.use_yes.setOnAction(actionEvent -> {
            this.client.asyncWriteToSocket("placeOneStudent " + choice1_left.getValue() + " " + choice1_right.getValue());
        });
    }

    public void visualizeReduceColourInDining(){
        this.choice_left_label.setText("Colour:");

        String[] colours = {"blue", "pink", "red", "yellow", "green"};
        for(String c : colours){
            this.choice1_left.getItems().add(c);
        }
        oneChoiceVisualization();

        this.use_yes.setOnAction(actionEvent -> {
            this.client.asyncWriteToSocket("reduceColourInDining " + choice1_left.getValue());
        });
    }

    public void visualizeTakeProfessorOnEquity(){
        noChoiceVisualization();

        this.use_yes.setOnAction(actionEvent -> {
            this.client.asyncWriteToSocket("takeProfessorOnEquity ");
        });
    }

    public void visualizeTowerNoValue(){
        noChoiceVisualization();

        this.use_yes.setOnAction(actionEvent -> {
            this.client.asyncWriteToSocket("towerNoValue ");
        });
    }

    public void visualizeTwoExtraIslands(){
        noChoiceVisualization();

        this.use_yes.setOnAction(actionEvent -> {
            this.client.asyncWriteToSocket("twoExtraIslands ");
        });
    }

    public void visualizeTwoExtraPoints(){
        noChoiceVisualization();

        this.use_yes.setOnAction(actionEvent -> {
            this.client.asyncWriteToSocket("twoExtraPoints ");
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
