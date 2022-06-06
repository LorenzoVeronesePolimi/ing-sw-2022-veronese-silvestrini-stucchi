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

public class ArchipelagoFxml {
    private static final SPColour[] availableSPColours = {SPColour.BLUE,SPColour.PINK, SPColour.RED, SPColour.YELLOW, SPColour.GREEN};
    private static final PlayerColour[] availablePlayerColours = {PlayerColour.WHITE, PlayerColour.BLACK, PlayerColour.GRAY};

    private int index;
    private AnchorPane archiAnchor;
    private final ImageView archi_mother_nature;

    private final Map<PlayerColour, ImageView> towerColourImage; //useful to avoid switches and to streamline the code
    private final ImageView archi_white_tower;
    private final ImageView archi_black_tower;
    private final ImageView archi_gray_tower;
    private final Label archi_num_towers;

    private final Map<SPColour, Label> studentColourNumber; //useful to avoid switches and to streamline the code

    private Client client;  // Client class
    private SerializedBoardAbstract board;  // Board
    private BoardFourAdvancedController controller; // BoardFourAdvancedController (passed in the setArchipelagosFxmlVisualization method)

    public ArchipelagoFxml(int index, AnchorPane archi, ImageView archi_mother_nature, ImageView archi_white_tower, ImageView archi_black_tower, ImageView archi_gray_tower, Label archi_num_towers, Label archi_num_blue, Label archi_num_pink, Label archi_num_red, Label archi_num_yellow, Label archi_num_green){
        this.index = index;
        this.archiAnchor = archi;
        this.archi_mother_nature = archi_mother_nature;
        this.archi_white_tower = archi_white_tower;
        this.archi_black_tower = archi_black_tower;
        this.archi_gray_tower = archi_gray_tower;
        this.archi_num_towers = archi_num_towers;

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
        archi.setOnMouseClicked(this::onMouseClicked);
    }

    public AnchorPane getArchiAnchor(){
        return archiAnchor;
    }

    public ImageView getArchi_mother_nature() {
        return archi_mother_nature;
    }

    public ImageView getArchi_white_tower() {
        return archi_white_tower;
    }

    public ImageView getArchi_black_tower() {
        return archi_black_tower;
    }

    public ImageView getArchi_gray_tower() {
        return archi_gray_tower;
    }

    public Label getArchi_num_towers() {
        return archi_num_towers;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setBoard(SerializedBoardAbstract board) {
        this.board = board;
    }

    public void setController(BoardFourAdvancedController controller) {
        this.controller = controller;
    }

    //set only one (or zero) of three towers visible
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

    public void setTextNumStudents(Map<SPColour, Integer> studentsData){
        for(SPColour c : availableSPColours){
            this.studentColourNumber.get(c).setText(Integer.toString(studentsData.get(c)));
        }
    }

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
    }

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
