package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Board.SerializedBoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Places.School.SchoolAdvanced;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.View.GUI.Controllers.DataStructures.*;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoardFourAdvancedController implements GUIController, Initializable {
    private GUIViewFX guiViewFX;
    private Client client;
    private SerializedBoardAbstract board;

    // AnchorPanes
    @FXML public AnchorPane general_anchor;
    @FXML private AnchorPane my_anchor;
    @FXML private AnchorPane op1_anchor;
    @FXML private AnchorPane op2_anchor;
    @FXML private AnchorPane op3_anchor;

    // Label on top of my school
    @FXML private Label actionLabel;
    @FXML private Label turnLabel;

    // School of mine
    @FXML private GridPane my_hall;
    @FXML private GridPane my_dining;
    @FXML private GridPane my_professors;
    @FXML private GridPane my_towers;
    @FXML private Label my_coins;
    // School 1
    @FXML private Label opponent1_nick;
    @FXML private GridPane opponent1_hall;
    @FXML private GridPane opponent1_dining;
    @FXML private GridPane opponent1_professors;
    @FXML private GridPane opponent1_towers;
    @FXML private Label opponent1_coins;
    // School 2
    @FXML private Label opponent2_nick;
    @FXML private GridPane opponent2_hall;
    @FXML private GridPane opponent2_dining;
    @FXML private GridPane opponent2_professors;
    @FXML private GridPane opponent2_towers;
    @FXML private Label opponent2_coins;
    // School 3
    @FXML private Label opponent3_nick;
    @FXML private GridPane opponent3_hall;
    @FXML private GridPane opponent3_dining;
    @FXML private GridPane opponent3_professors;
    @FXML private GridPane opponent3_towers;
    @FXML private Label opponent3_coins;

    // Archipelago 0
    @FXML private AnchorPane archi0;
    @FXML private ImageView archi0_mother_nature;
    @FXML private ImageView archi0_white_tower;
    @FXML private ImageView archi0_black_tower;
    @FXML private ImageView archi0_gray_tower;
    @FXML private Label archi0_num_towers;
    @FXML private Label archi0_num_blue;
    @FXML private Label archi0_num_pink;
    @FXML private Label archi0_num_red;
    @FXML private Label archi0_num_yellow;
    @FXML private Label archi0_num_green;
    // Archipelago 1
    @FXML private AnchorPane archi1;
    @FXML private ImageView archi1_mother_nature;
    @FXML private ImageView archi1_white_tower;
    @FXML private ImageView archi1_black_tower;
    @FXML private ImageView archi1_gray_tower;
    @FXML private Label archi1_num_towers;
    @FXML private Label archi1_num_blue;
    @FXML private Label archi1_num_pink;
    @FXML private Label archi1_num_red;
    @FXML private Label archi1_num_yellow;
    @FXML private Label archi1_num_green;
    // Archipelago 2
    @FXML private AnchorPane archi2;
    @FXML private ImageView archi2_mother_nature;
    @FXML private ImageView archi2_white_tower;
    @FXML private ImageView archi2_black_tower;
    @FXML private ImageView archi2_gray_tower;
    @FXML private Label archi2_num_towers;
    @FXML private Label archi2_num_blue;
    @FXML private Label archi2_num_pink;
    @FXML private Label archi2_num_red;
    @FXML private Label archi2_num_yellow;
    @FXML private Label archi2_num_green;
    // Archipelago 3
    @FXML private AnchorPane archi3;
    @FXML private ImageView archi3_mother_nature;
    @FXML private ImageView archi3_white_tower;
    @FXML private ImageView archi3_black_tower;
    @FXML private ImageView archi3_gray_tower;
    @FXML private Label archi3_num_towers;
    @FXML private Label archi3_num_blue;
    @FXML private Label archi3_num_pink;
    @FXML private Label archi3_num_red;
    @FXML private Label archi3_num_yellow;
    @FXML private Label archi3_num_green;
    // Archipelago 4
    @FXML private AnchorPane archi4;
    @FXML private ImageView archi4_mother_nature;
    @FXML private ImageView archi4_white_tower;
    @FXML private ImageView archi4_black_tower;
    @FXML private ImageView archi4_gray_tower;
    @FXML private Label archi4_num_towers;
    @FXML private Label archi4_num_blue;
    @FXML private Label archi4_num_pink;
    @FXML private Label archi4_num_red;
    @FXML private Label archi4_num_yellow;
    @FXML private Label archi4_num_green;
    // Archipelago 5
    @FXML private AnchorPane archi5;
    @FXML private ImageView archi5_mother_nature;
    @FXML private ImageView archi5_white_tower;
    @FXML private ImageView archi5_black_tower;
    @FXML private ImageView archi5_gray_tower;
    @FXML private Label archi5_num_towers;
    @FXML private Label archi5_num_blue;
    @FXML private Label archi5_num_pink;
    @FXML private Label archi5_num_red;
    @FXML private Label archi5_num_yellow;
    @FXML private Label archi5_num_green;
    // Archipelago 6
    @FXML private AnchorPane archi6;
    @FXML private ImageView archi6_mother_nature;
    @FXML private ImageView archi6_white_tower;
    @FXML private ImageView archi6_black_tower;
    @FXML private ImageView archi6_gray_tower;
    @FXML private Label archi6_num_towers;
    @FXML private Label archi6_num_blue;
    @FXML private Label archi6_num_pink;
    @FXML private Label archi6_num_red;
    @FXML private Label archi6_num_yellow;
    @FXML private Label archi6_num_green;
    // Archipelago 7
    @FXML private AnchorPane archi7;
    @FXML private ImageView archi7_mother_nature;
    @FXML private ImageView archi7_white_tower;
    @FXML private ImageView archi7_black_tower;
    @FXML private ImageView archi7_gray_tower;
    @FXML private Label archi7_num_towers;
    @FXML private Label archi7_num_blue;
    @FXML private Label archi7_num_pink;
    @FXML private Label archi7_num_red;
    @FXML private Label archi7_num_yellow;
    @FXML private Label archi7_num_green;
    // Archipelago 8
    @FXML private AnchorPane archi8;
    @FXML private ImageView archi8_mother_nature;
    @FXML private ImageView archi8_white_tower;
    @FXML private ImageView archi8_black_tower;
    @FXML private ImageView archi8_gray_tower;
    @FXML private Label archi8_num_towers;
    @FXML private Label archi8_num_blue;
    @FXML private Label archi8_num_pink;
    @FXML private Label archi8_num_red;
    @FXML private Label archi8_num_yellow;
    @FXML private Label archi8_num_green;
    // Archipelago 9
    @FXML private AnchorPane archi9;
    @FXML private ImageView archi9_mother_nature;
    @FXML private ImageView archi9_white_tower;
    @FXML private ImageView archi9_black_tower;
    @FXML private ImageView archi9_gray_tower;
    @FXML private Label archi9_num_towers;
    @FXML private Label archi9_num_blue;
    @FXML private Label archi9_num_pink;
    @FXML private Label archi9_num_red;
    @FXML private Label archi9_num_yellow;
    @FXML private Label archi9_num_green;
    // Archipelago 10
    @FXML private AnchorPane archi10;
    @FXML private ImageView archi10_mother_nature;
    @FXML private ImageView archi10_white_tower;
    @FXML private ImageView archi10_black_tower;
    @FXML private ImageView archi10_gray_tower;
    @FXML private Label archi10_num_towers;
    @FXML private Label archi10_num_blue;
    @FXML private Label archi10_num_pink;
    @FXML private Label archi10_num_red;
    @FXML private Label archi10_num_yellow;
    @FXML private Label archi10_num_green;
    // Archipelago 11
    @FXML private AnchorPane archi11;
    @FXML private ImageView archi11_mother_nature;
    @FXML private ImageView archi11_white_tower;
    @FXML private ImageView archi11_black_tower;
    @FXML private ImageView archi11_gray_tower;
    @FXML private Label archi11_num_towers;
    @FXML private Label archi11_num_blue;
    @FXML private Label archi11_num_pink;
    @FXML private Label archi11_num_red;
    @FXML private Label archi11_num_yellow;
    @FXML private Label archi11_num_green;

    // Clouds
    @FXML private ImageView cloud1_image;
    @FXML private GridPane cloud1;
    @FXML private ImageView cloud2_image;
    @FXML private GridPane cloud2;
    @FXML private ImageView cloud3_image;
    @FXML private GridPane cloud3;
    @FXML private ImageView cloud4_image;
    @FXML private GridPane cloud4;

    // AssistantCard of mine
    @FXML private ImageView my_a1;
    @FXML private ImageView my_a2;
    @FXML private ImageView my_a3;
    @FXML private ImageView my_a4;
    @FXML private ImageView my_a5;
    @FXML private ImageView my_a6;
    @FXML private ImageView my_a7;
    @FXML private ImageView my_a8;
    @FXML private ImageView my_a9;
    @FXML private ImageView my_a10;
    // AssistantCard 1
    @FXML private ImageView op1_a1;
    @FXML private ImageView op1_a2;
    @FXML private ImageView op1_a3;
    @FXML private ImageView op1_a4;
    @FXML private ImageView op1_a5;
    @FXML private ImageView op1_a6;
    @FXML private ImageView op1_a7;
    @FXML private ImageView op1_a8;
    @FXML private ImageView op1_a9;
    @FXML private ImageView op1_a10;
    // AssistantCard 2
    @FXML private ImageView op2_a1;
    @FXML private ImageView op2_a2;
    @FXML private ImageView op2_a3;
    @FXML private ImageView op2_a4;
    @FXML private ImageView op2_a5;
    @FXML private ImageView op2_a6;
    @FXML private ImageView op2_a7;
    @FXML private ImageView op2_a8;
    @FXML private ImageView op2_a9;
    @FXML private ImageView op2_a10;
    // AssistantCard 3
    @FXML private ImageView op3_a1;
    @FXML private ImageView op3_a2;
    @FXML private ImageView op3_a3;
    @FXML private ImageView op3_a4;
    @FXML private ImageView op3_a5;
    @FXML private ImageView op3_a6;
    @FXML private ImageView op3_a7;
    @FXML private ImageView op3_a8;
    @FXML private ImageView op3_a9;
    @FXML private ImageView op3_a10;

    // CharacterCards
    @FXML private GridPane character_card_grid;
    @FXML private Label card_1_cost_label;
    @FXML private Label card_2_cost_label;
    @FXML private Label card_3_cost_label;

    private SPColour movedStudent = null;

    private List<ArchipelagoFxml> archipelagosFxml;
    private List<SchoolFxml> schoolsFxml;
    private List<AssistantCardFxml> assistantCardsFxml;
    private List<CloudFxml> cloudsFxml;
    private CharacterCardFxml characterCardsFxml;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Create Archipelagos data structure
        archipelagosFxml = new ArrayList<>();
        /* substituted by onMouseClicked in ArchipelagoFxml
        archi0.setOnMouseClicked(this::archiZeroClicked);   // When a student is moved from dining room to archipelago
        */
        archipelagosFxml.add(new ArchipelagoFxml(0, archi0, archi0_mother_nature, archi0_white_tower, archi0_black_tower, archi0_gray_tower, archi0_num_towers, archi0_num_blue, archi0_num_pink, archi0_num_red, archi0_num_yellow, archi0_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(1, archi1, archi1_mother_nature, archi1_white_tower, archi1_black_tower, archi1_gray_tower, archi1_num_towers, archi1_num_blue, archi1_num_pink, archi1_num_red, archi1_num_yellow, archi1_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(2, archi2, archi2_mother_nature, archi2_white_tower, archi2_black_tower, archi2_gray_tower, archi2_num_towers, archi2_num_blue, archi2_num_pink, archi2_num_red, archi2_num_yellow, archi2_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(3, archi3, archi3_mother_nature, archi3_white_tower, archi3_black_tower, archi3_gray_tower, archi3_num_towers, archi3_num_blue, archi3_num_pink, archi3_num_red, archi3_num_yellow, archi3_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(4, archi4, archi4_mother_nature, archi4_white_tower, archi4_black_tower, archi4_gray_tower, archi4_num_towers, archi4_num_blue, archi4_num_pink, archi4_num_red, archi4_num_yellow, archi4_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(5, archi5, archi5_mother_nature, archi5_white_tower, archi5_black_tower, archi5_gray_tower, archi5_num_towers, archi5_num_blue, archi5_num_pink, archi5_num_red, archi5_num_yellow, archi5_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(6, archi6, archi6_mother_nature, archi6_white_tower, archi6_black_tower, archi6_gray_tower, archi6_num_towers, archi6_num_blue, archi6_num_pink, archi6_num_red, archi6_num_yellow, archi6_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(7, archi7, archi7_mother_nature, archi7_white_tower, archi7_black_tower, archi7_gray_tower, archi7_num_towers, archi7_num_blue, archi7_num_pink, archi7_num_red, archi7_num_yellow, archi7_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(8, archi8, archi8_mother_nature, archi8_white_tower, archi8_black_tower, archi8_gray_tower, archi8_num_towers, archi8_num_blue, archi8_num_pink, archi8_num_red, archi8_num_yellow, archi8_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(9, archi9, archi9_mother_nature, archi9_white_tower, archi9_black_tower, archi9_gray_tower, archi9_num_towers, archi9_num_blue, archi9_num_pink, archi9_num_red, archi9_num_yellow, archi9_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(10, archi10, archi10_mother_nature, archi10_white_tower, archi10_black_tower, archi10_gray_tower, archi10_num_towers, archi10_num_blue, archi10_num_pink, archi10_num_red, archi10_num_yellow, archi10_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(11, archi11, archi11_mother_nature, archi11_white_tower, archi11_black_tower, archi11_gray_tower, archi11_num_towers, archi11_num_blue, archi11_num_pink, archi11_num_red, archi11_num_yellow, archi11_num_green));

        // Create Schools data structure
        schoolsFxml = new ArrayList<>();
        schoolsFxml.add(new SchoolFxml(null, my_hall, my_dining, my_professors, my_towers, my_coins));
        schoolsFxml.add(new SchoolFxml(opponent1_nick, opponent1_hall, opponent1_dining, opponent1_professors, opponent1_towers, opponent1_coins));
        schoolsFxml.add(new SchoolFxml(opponent2_nick, opponent2_hall, opponent2_dining, opponent2_professors, opponent2_towers, opponent2_coins));
        schoolsFxml.add(new SchoolFxml(opponent3_nick, opponent3_hall, opponent3_dining, opponent3_professors, opponent3_towers, opponent3_coins));

        // Create AC data structure
        assistantCardsFxml = new ArrayList<>();
        assistantCardsFxml.add(new AssistantCardFxml(my_a1, my_a2, my_a3, my_a4, my_a5, my_a6, my_a7, my_a8, my_a9, my_a10));
        assistantCardsFxml.add(new AssistantCardFxml(op1_a1, op1_a2, op1_a3, op1_a4, op1_a5, op1_a6, op1_a7, op1_a8, op1_a9, op1_a10));
        assistantCardsFxml.add(new AssistantCardFxml(op2_a1, op2_a2, op2_a3, op2_a4, op2_a5, op2_a6, op2_a7, op2_a8, op2_a9, op2_a10));
        assistantCardsFxml.add(new AssistantCardFxml(op3_a1, op3_a2, op3_a3, op3_a4, op3_a5, op3_a6, op3_a7, op3_a8, op3_a9, op3_a10));

        // Create Clouds data structure
        cloudsFxml = new ArrayList<>();
        /* substituted by onMouseClicked in CloudFxml
        cloud1.setOnMouseClicked(this::cloudOneClicked);
        */
        cloudsFxml.add(new CloudFxml(0, cloud1, cloud1_image));
        cloudsFxml.add(new CloudFxml(1, cloud2, cloud2_image));
        cloudsFxml.add(new CloudFxml(2, cloud3, cloud3_image));
        cloudsFxml.add(new CloudFxml(3, cloud4, cloud4_image));

        // Character Cards data structure
        characterCardsFxml = new CharacterCardFxml(character_card_grid, card_1_cost_label, card_2_cost_label, card_3_cost_label);
    }

    public SPColour getMovedStudent() {
        return movedStudent;
    }

    public List<SchoolFxml> getSchoolsFxml() {
        return schoolsFxml;
    }

    public void setBoard(SerializedBoardAbstract board) {
        this.board = board;
    }

    public void setMovedStudent(SPColour colour) {
        this.movedStudent = colour;
    }

    public void setStandardSetup(){
        for(SchoolFxml s : this.schoolsFxml){
            s.getCoins().setVisible(false);
        }
    }

    public void setArchipelagosFxmlVisualization(){
        for(ArchipelagoFxml a : this.archipelagosFxml) {
            a.setBoard(board);
            a.setClient(this.client);
            a.setController(this);
        }
        int i = 0;
        for(Archipelago a : board.getArchipelagos()){
            // MotherNature: visible or not?
            if(board.getArchipelagos().indexOf(board.getMn().getCurrentPosition()) == i){
                archipelagosFxml.get(i).getArchi_mother_nature().setVisible(true);
            }
            else{
                archipelagosFxml.get(i).getArchi_mother_nature().setVisible(false);
            }

            // Towers: if present of which colour? And how many of them?
            if(board.getArchipelagos().get(i).getOwner() == null){
                archipelagosFxml.get(i).setVisibleTower(null);
                archipelagosFxml.get(i).getArchi_num_towers().setVisible(false);
            }
            else{
                archipelagosFxml.get(i).setVisibleTower(board.getArchipelagos().get(i).getOwner().getColour());
                archipelagosFxml.get(i).getArchi_num_towers().setVisible(true);
                archipelagosFxml.get(i).getArchi_num_towers().setText(Integer.toString(a.getIslands().size()));
            }

            // Students: How many of them?
            archipelagosFxml.get(i).setTextNumStudents(board.getArchipelagos().get(i).getStudentsData());

            i++;
        }

        // set invisible all archipelagos outside the archipelagos' list
        for(i = (12 - board.getArchipelagos().size()); i > 0; i--){
            this.archipelagosFxml.get(12 - i).setVisible(false);
        }
    }

    public void setSchoolsFxmlVisualization(){
        for(SchoolFxml s : schoolsFxml) {
            s.setBoard(board);
            s.setClient(this.client);
            s.setController(this);
        }

        //int onWorkingPlayerIndex = board.getOrderedPlayers().indexOf(board.getCurrentPlayer());
        int onWorkingPlayerIndex = computeMyIndex(board);
        if(onWorkingPlayerIndex == -1) {
            System.out.println("Error in setSchoolFxmlVisualization");
            return;
        }

        for(int i = 0; i < board.getSitPlayers().size(); i++){
            School onWorkingSchool = board.getSchools().get(onWorkingPlayerIndex);
            // Reset
            this.schoolsFxml.get(i).resetVisualization(); // necessary since otherwise new images would overlap the older one (which would not be present)
            // Nicknames
            this.schoolsFxml.get(i).setNickVisualization(board.getSitPlayers().get(onWorkingPlayerIndex).getNickname());

            //if(this.client.getNickname().equals(board.getOrderedPlayers().get(onWorkingPlayerIndex).getNickname())){
            if(i == 0){
                // Hall
                this.schoolsFxml.get(i).setHallVisualization(onWorkingSchool.getStudentsHall(), 1);
                // Dining room
                this.schoolsFxml.get(i).setDiningVisualization(onWorkingSchool, 1);
                // Professors
                this.schoolsFxml.get(i).setProfessorsVisualization(onWorkingSchool.getProfessors(), 1);
                // Towers
                this.schoolsFxml.get(i).setTowersVisualization(onWorkingSchool.getTowers(),1);
            }
            else{
                // Hall
                this.schoolsFxml.get(i).setHallVisualization(onWorkingSchool.getStudentsHall(), 0.78);
                // Dining room
                this.schoolsFxml.get(i).setDiningVisualization(onWorkingSchool, 0.78);
                // Professors
                this.schoolsFxml.get(i).setProfessorsVisualization(onWorkingSchool.getProfessors(), 0.78);
                // Towers
                this.schoolsFxml.get(i).setTowersVisualization(onWorkingSchool.getTowers(), 0.78);
            }

            if(board.getType().equals("advanced")){
                this.schoolsFxml.get(i).setCoinsVisualization(((SchoolAdvanced)board.getSchools().get(onWorkingPlayerIndex)).getNumCoins());
            }

            onWorkingPlayerIndex = (onWorkingPlayerIndex + 1) % board.getSitPlayers().size();
        }

        hydeSchools(board);
    }

    private void hydeSchools(SerializedBoardAbstract board) {
        if(board.getSchools().size() == 2) {
            op1_anchor.setTranslateY(+200);
            op2_anchor.setVisible(false);
            op3_anchor.setVisible(false);
        }
        if(board.getSchools().size() == 3) {
            op1_anchor.setTranslateY(+100);
            op2_anchor.setTranslateY(+100);
            op3_anchor.setVisible(false);
        }
    }

    public void setAssistantCardsVisualization(){
        int onWorkingPlayerIndex = computeMyIndex(board);

        for(int i = 0; i < board.getSitPlayers().size(); i++){
            School onWorkingSchool = board.getSchools().get(onWorkingPlayerIndex);

            this.assistantCardsFxml.get(i).setAssistantCardVisualization(onWorkingSchool.getPlayer().getLastCard().getTurnPriority());

            onWorkingPlayerIndex = (onWorkingPlayerIndex + 1) % board.getSitPlayers().size();
        }
    }

    public void setCloudsVisualization(){
        for(CloudFxml c : cloudsFxml) {
            c.setBoard(board);
            c.setClient(this.client);
            c.setController(this);
        }
        if(board.getClouds().size() == 2){
            removeAllNodesFromGrid(this.cloud1);
            removeAllNodesFromGrid(this.cloud2);
            this.cloudsFxml.get(2).setVisible(false);
            this.cloudsFxml.get(3).setVisible(false);
        }
        else if(board.getClouds().size() == 3){
            removeAllNodesFromGrid(this.cloud1);
            removeAllNodesFromGrid(this.cloud2);
            removeAllNodesFromGrid(this.cloud3);
            this.cloudsFxml.get(3).setVisible(false);
        } else if(board.getClouds().size() == 4) {
            removeAllNodesFromGrid(this.cloud1);
            removeAllNodesFromGrid(this.cloud2);
            removeAllNodesFromGrid(this.cloud3);
            removeAllNodesFromGrid(this.cloud4);
        }

        for(int i = 0; i < board.getClouds().size(); i++){
            this.cloudsFxml.get(i).setStudentsVisualization(board.getClouds().get(i).getStudents(), 0.78);
        }
    }

    public void setCharacterCardsVisualization(){ //assumes that the baord is advanced
        this.characterCardsFxml.setBoard((SerializedBoardAdvanced) this.board);
        this.characterCardsFxml.setClient(this.client);

        if(board.getType().equals("advanced")){
            this.characterCardsFxml.setVisible(true);
            this.characterCardsFxml.setCharacterCardsVisualization(((SerializedBoardAdvanced)board).getExtractedCards(), 1, this.guiViewFX);
        }
        else{
            this.characterCardsFxml.setVisible(false);
        }
    }

    public void setInstructionLabels() {
        if(board.getCurrentPlayer().getNickname().equals(this.client.getNickname())) {
            this.turnLabel.setText("It's your turn!");

            switch (board.getCurrentState()) {
                case ACTION1:
                    if(board.getType().equals("advanced")) {
                        this.actionLabel.setText("Select a student from your hall\n or buy a card.");
                    } else {
                        this.actionLabel.setText("Select a student from your hall.");
                    }
                    break;

                case ACTION2:
                    if(board.getType().equals("advanced")) {
                        this.actionLabel.setText("Select an island where you want\n to put Mother Nature or buy a card.");
                    } else {
                        this.actionLabel.setText("Select an island where you want\n to put Mother Nature.");
                    }
                    break;

                case ACTION3:
                    if(board.getType().equals("advanced")) {
                        this.actionLabel.setText("Select a cloud or buy a card.");
                    } else {
                        this.actionLabel.setText("Select a cloud.");
                    }
                    break;
            }
        } else {
            this.turnLabel.setText("It's " + board.getCurrentPlayer().getNickname() + " turn!");

            switch (board.getCurrentState()) {
                case ACTION1:
                    if (board.getType().equals("advanced")) {
                        this.actionLabel.setText("Choosing a student\n or buying a card...");
                    } else {
                        this.actionLabel.setText("Choosing a student...");
                    }
                    break;

                case ACTION2:
                    if (board.getType().equals("advanced")) {
                        this.actionLabel.setText("Selecting an island\n or buying a card...");
                    } else {
                        this.actionLabel.setText("Selecting an island...");
                    }
                    break;

                case ACTION3:
                    if (board.getType().equals("advanced")) {
                        this.actionLabel.setText("Selecting a cloud\n or buying a card...");
                    } else {
                        this.actionLabel.setText("Selecting a cloud...");
                    }
                    break;
            }
        }
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

    private void removeAllNodesFromGrid(GridPane grid){
        List<Node> children = new ArrayList<>(grid.getChildren()); //clone to avoid ConcurrentModificationException
        for(Node n : children){
            grid.getChildren().remove(n);
        }
    }

    public boolean isCurrentPlayer(String name) {
        return name.equals(board.getCurrentPlayer().getNickname());
    }

    private int computeMNMoves(int mnArchi, int clickedArchi) {
        int moves = 0;

        for(int i = mnArchi; i < board.getArchipelagos().size(); i++) {
            if(i != clickedArchi) { // if archipelago not found
                moves++;
            } else { // if archielago found
                return moves;
            }

            // if we need to restart the cicle. Example:
            /*
                mnArchi = 10, clickedArchi = 2
                1: i = 10 != 2 -> moves = 1
                2: i = 11 != 2 -> moves = 2 (i restarts from 0)
                3: i = 0 != 2 -> moves = 3
                4: i = 1 != 2 -> moves = 4
                5: i = 2 = 2 -> return moves 4
             */
            if(i == board.getArchipelagos().size() - 1) {
                i = -1;
            }
        }
        System.out.println("Error in computeMNMoves");
        return 0;
    }

    private void archiClicked(MouseEvent event){
        //TODO: modify this method in order to consider merged archipelagos (both for student move and for mother nature move)

        if(isCurrentPlayer(this.client.getNickname())) {
            if(this.movedStudent != null && board.getCurrentState().equals(State.ACTION1)) {

                this.client.asyncWriteToSocket("studentToArchipelago " + this.movedStudent + " 0");
                this.movedStudent = null;

                for(SchoolFxml s : schoolsFxml) {
                    s.setMovedStudent(null);
                }
            } else if(board.getCurrentState().equals(State.ACTION2)) {
                Archipelago mnArchi = board.getMn().getCurrentPosition();
                int i = 0;
                boolean found = false;
                for(Archipelago a : board.getArchipelagos()) {
                    if(!found) {
                        if(!a.equals(mnArchi)) {
                            i++;
                        } else {
                            found = true;
                            //TODO: break
                        }
                    }
                }

                this.client.asyncWriteToSocket("moveMotherNature " + computeMNMoves(i, 0));
            }
        }
    }

    /* substituted by onMouseClicked in ArchipelagoFxml
    private void archiOneClicked(MouseEvent event) {
        //TODO: modify this method in order to consider merged archipelagos (both for student move and for mother nature move)

        if(isCurrentPlayer(this.client.getNickname())) {
            if(this.movedStudent != null) {
                this.client.asyncWriteToSocket("studentToArchipelago " + this.movedStudent + " 1");
                this.movedStudent = null;

                for(SchoolFxml s : schoolsFxml) {
                    s.setMovedStudent(null);
                }
            } else if(board.getCurrentState().equals(State.ACTION2)) {
                Archipelago mnArchi = board.getMn().getCurrentPosition();
                int i = 0;
                boolean found = false;
                for(Archipelago a : board.getArchipelagos()) {
                    if(!found) {
                        if(!a.equals(mnArchi)) {
                            i++;
                        } else {
                            found = true;
                        }
                    }
                }

                this.client.asyncWriteToSocket("moveMotherNature " + computeMNMoves(i, 1));
            }
        }
    }*/

    /* replaced in CloudFxml
    private void cloudOneClicked(MouseEvent event) {
        if(isCurrentPlayer(this.client.getNickname())) {
            if(board.getCurrentState().equals(State.ACTION3)) {
                this.client.asyncWriteToSocket("studentCloudToSchool " + 0);
            }
        }
    }*/


    public void setCursor(String path){
        this.general_anchor.setCursor(new ImageCursor(new Image(path),
                40,
                40));
    }

    public void setCursorToDefault(){
        this.general_anchor.setCursor(Cursor.DEFAULT);
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
