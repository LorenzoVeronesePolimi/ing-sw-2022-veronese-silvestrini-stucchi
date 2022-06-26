package it.polimi.ingsw.View.GUI.Controllers.DataStructures;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.View.GUI.Controllers.BoardFourAdvancedController;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.Map;

/**
 * data structures for archipelagos
 */
public class ArchipelagoFxml {
    private static final SPColour[] availableSPColours = {SPColour.BLUE,SPColour.PINK, SPColour.RED, SPColour.YELLOW, SPColour.GREEN};
    private static final PlayerColour[] availablePlayerColours = {PlayerColour.WHITE, PlayerColour.BLACK, PlayerColour.GRAY};

    private final int index;
    private final AnchorPane archiAnchor;
    private final ImageView archi_mother_nature;

    private final Map<PlayerColour, ImageView> towerColourImage; //useful to avoid switches and to streamline the code
    private final ImageView archi_white_tower;
    private final ImageView archi_black_tower;
    private final ImageView archi_gray_tower;
    private final Label archi_num_towers;

    private final ImageView forbid_icon;

    private final Map<SPColour, Label> studentColourNumber; //useful to avoid switches and to streamline the code

    private Client client;  // Client class
    private SerializedBoardAbstract board;  // Board
    private BoardFourAdvancedController controller; // BoardFourAdvancedController (passed in the setArchipelagosFxmlVisualization method)

    /**
     * constructor of archipelago data structure
     * @param index index of archipelago
     * @param archi anchor pane of archipelago
     * @param archi_mother_nature image of mother nature
     * @param archi_white_tower image white tower
     * @param archi_black_tower image black tower
     * @param archi_gray_tower image gray tower
     * @param archi_num_towers label number of towers
     * @param archi_num_blue label number of blue students
     * @param archi_num_pink label number of pink students
     * @param archi_num_red label number of red students
     * @param archi_num_yellow label number of yellow students
     * @param archi_num_green label number of green students
     * @param forbid_icon image of forbid island tile
     */
    public ArchipelagoFxml(int index, AnchorPane archi, ImageView archi_mother_nature, ImageView archi_white_tower, ImageView archi_black_tower, ImageView archi_gray_tower, Label archi_num_towers, Label archi_num_blue, Label archi_num_pink, Label archi_num_red, Label archi_num_yellow, Label archi_num_green, ImageView forbid_icon){
        this.index = index;
        this.archiAnchor = archi;
        this.archi_mother_nature = archi_mother_nature;
        this.archi_white_tower = archi_white_tower;
        this.archi_black_tower = archi_black_tower;
        this.archi_gray_tower = archi_gray_tower;
        this.archi_num_towers = archi_num_towers;
        this.forbid_icon = forbid_icon;

        // setup data structures
        this.towerColourImage = Map.of(
                PlayerColour.WHITE, this.archi_white_tower,
                PlayerColour.BLACK, this.archi_black_tower,
                PlayerColour.GRAY, this.archi_gray_tower
        );

        this.studentColourNumber = Map.of(
                SPColour.BLUE, archi_num_blue,
                SPColour.PINK, archi_num_pink,
                SPColour.RED, archi_num_red,
                SPColour.YELLOW, archi_num_yellow,
                SPColour.GREEN, archi_num_green
        );

        // set actions
        this.archiAnchor.setOnMouseClicked(this::onMouseClicked);
    }

    /**
     * This method sets the clickable part of the scene.
     * @param enable true if the content is clickable, false otherwise.
     */
    public void enableClick(boolean enable) {
        if(enable) {
            this.archiAnchor.setOnMouseClicked(this::onMouseClicked);
        } else {
            this.archiAnchor.setOnMouseClicked(null);
        }
    }

    /**
     * getter of archipelago anchor pane
     * @return archipelago anchor pane
     */
    public AnchorPane getArchiAnchor(){
        return archiAnchor;
    }

    /**
     * getter of archipelago's image of mother nature
     * @return archipelago's image of mother nature
     */
    public ImageView getArchi_mother_nature() {
        return archi_mother_nature;
    }

    /**
     * getter of archipelago's image of white tower
     * @return archipelago's image of white tower
     */
    public ImageView getArchi_white_tower() {
        return archi_white_tower;
    }

    /**
     * getter of archipelago's image of black tower
     * @return archipelago's image of black tower
     */
    public ImageView getArchi_black_tower() {
        return archi_black_tower;
    }

    /**
     * getter of archipelago's image of gray tower
     * @return archipelago's image of gray tower
     */
    public ImageView getArchi_gray_tower() {
        return archi_gray_tower;
    }

    /**
     * getter of archipelago's label of number of towers
     * @return archipelago's label of number of towers
     */
    public Label getArchi_num_towers() {
        return archi_num_towers;
    }

    /**
     * setter of client
     * @param client client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * setter of serialized board
     * @param board serialized board notified by model
     */
    public void setBoard(SerializedBoardAbstract board) {
        this.board = board;
    }

    /**
     * setter of board four advanced controller
     * @param controller controller to be set
     */
    public void setController(BoardFourAdvancedController controller) {
        this.controller = controller;
    }

    //set only one (or zero) of three towers visible

    /**
     * setter of visibility of towers
     * @param colourToShow colour of the tower to set visible
     */
    public void setVisibleTower(PlayerColour colourToShow){
        if(colourToShow == null){
            for(PlayerColour c : availablePlayerColours){
                this.towerColourImage.get(c).setVisible(false);
            }
        }
        else{
            for(PlayerColour c : availablePlayerColours){
                if(c == colourToShow){
                    this.towerColourImage.get(c).setVisible(true);
                }
                else{
                    this.towerColourImage.get(c).setVisible(false);
                }
            }
        }
    }

    /**
     * setter of text of labels of the number of students for each colour
     * @param studentsData data about students on archipelago
     */
    public void setTextNumStudents(Map<SPColour, Integer> studentsData){
        for(SPColour c : availableSPColours){
            this.studentColourNumber.get(c).setText(Integer.toString(studentsData.get(c)));
        }
    }

    /**
     * setter of visibility of forbid island tile
     * @param numForbid
     */
    public void setVisibleForbidIcon(int numForbid){
        if(numForbid > 0){
            this.forbid_icon.setVisible(true);
        }
        else{
            this.forbid_icon.setVisible(false);
        }

    }

    /**
     * setter of visibility of the entire archipelago anchor pane
     * @param isVisible value of visibility
     */
    public void setVisible(boolean isVisible){
        this.archiAnchor.setVisible(isVisible);
    }

    /**
     * manager of mouse event
     * @param event mouse event
     */
    private void onMouseClicked(MouseEvent event){
        //TODO: modify this method in order to consider merged archipelagos (both for student move and for mother nature move)

        if(this.controller.isCurrentPlayer(this.client.getNickname())) {
            if(this.controller.getMovedStudent() != null && this.board.getCurrentState().equals(State.ACTION1)) {

                this.client.asyncWriteToSocket("studentToArchipelago " + this.controller.getMovedStudent() + " " + this.index);
                this.controller.setMovedStudent(null);

                for(SchoolFxml s : controller.getSchoolsFxml()) {
                    s.setMovedStudent(null);
                }
            } else if(this.board.getCurrentState().equals(State.ACTION2)) {
                this.client.asyncWriteToSocket("moveMotherNature " + computeMNMoves(this.board.getArchipelagos().indexOf(this.board.getMn().getCurrentPosition()), this.index));
            }
        }

        this.controller.setCursorToDefault();
    }

    /**
     * method that computes how many moves mother nature does if it moves from its current position to the clicked archipelago
     * @param mnArchi index of mother nature current position
     * @param clickedArchi index of clicked archipelago
     * @return number of moves required
     */
    private int computeMNMoves(int mnArchi, int clickedArchi) {
        int moves = 0;

        for(int i = mnArchi; i < board.getArchipelagos().size(); i++) {
            if(i != clickedArchi) { // if archipelago not found
                moves++;
            } else { // if archipelago found
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
}
