package it.polimi.ingsw.View.GUI.Controllers.DataStructures;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Player;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class ArchipelagoFxml {
    private static final SPColour[] availableSPColours = {SPColour.BLUE,SPColour.PINK, SPColour.RED, SPColour.YELLOW, SPColour.GREEN};
    private static final PlayerColour[] availablePlayerColours = {PlayerColour.WHITE, PlayerColour.BLACK, PlayerColour.GRAY};
    private ImageView archi_mother_nature;

    private Map<PlayerColour, ImageView> towerColourImage; //useful to avoid switches and to streamline the code
    private ImageView archi_white_tower;
    private ImageView archi_black_tower;
    private ImageView archi_gray_tower;
    private Label archi_num_towers;

    private Map<SPColour, Label> studentColourNumber; //useful to avoid switches and to streamline the code
    private Label archi_num_blue;
    private Label archi_num_pink;
    private Label archi_num_red;
    private Label archi_num_yellow;
    private Label archi_num_green;


    public ArchipelagoFxml(ImageView archi_mother_nature, ImageView archi_white_tower, ImageView archi_black_tower, ImageView archi_gray_tower, Label archi_num_towers, Label archi_num_blue, Label archi_num_pink, Label archi_num_red, Label archi_num_yellow, Label archi_num_green){
        this.archi_mother_nature = archi_mother_nature;
        this.archi_white_tower = archi_white_tower;
        this.archi_black_tower = archi_black_tower;
        this.archi_gray_tower = archi_gray_tower;
        this.archi_num_towers = archi_num_towers;
        this.archi_num_blue = archi_num_blue;
        this.archi_num_pink = archi_num_pink;
        this.archi_num_red = archi_num_red;
        this.archi_num_yellow = archi_num_yellow;
        this.archi_num_green = archi_num_green;

        // Set data structures
        this.towerColourImage = new HashMap<>();
        this.towerColourImage.put(PlayerColour.WHITE, this.archi_white_tower);
        this.towerColourImage.put(PlayerColour.BLACK, this.archi_black_tower);
        this.towerColourImage.put(PlayerColour.GRAY, this.archi_gray_tower);

        this.studentColourNumber = new HashMap<>();
        this.studentColourNumber.put(SPColour.BLUE, this.archi_num_blue);
        this.studentColourNumber.put(SPColour.PINK, this.archi_num_pink);
        this.studentColourNumber.put(SPColour.RED, this.archi_num_red);
        this.studentColourNumber.put(SPColour.YELLOW, this.archi_num_yellow);
        this.studentColourNumber.put(SPColour.GREEN, this.archi_num_green);
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
}
