package it.polimi.ingsw.View.GUI.Controllers.DataStructures;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ArchipelagoFxml {
    private ImageView archi_mother_nature;
    private ImageView archi_white_tower;
    private ImageView archi_black_tower;
    private ImageView archi_gray_tower;
    private Label archi_num_towers;
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

    public Label getArchi_num_blue() {
        return archi_num_blue;
    }

    public Label getArchi_num_pink() {
        return archi_num_pink;
    }

    public Label getArchi_num_red() {
        return archi_num_red;
    }

    public Label getArchi_num_yellow() {
        return archi_num_yellow;
    }

    public Label getArchi_num_green() {
        return archi_num_green;
    }

    public void setArchi_mother_nature(ImageView archi_mother_nature) {
        this.archi_mother_nature = archi_mother_nature;
    }

    public void setArchi_white_tower(ImageView archi_white_tower) {
        this.archi_white_tower = archi_white_tower;
    }

    public void setArchi_black_tower(ImageView archi_black_tower) {
        this.archi_black_tower = archi_black_tower;
    }

    public void setArchi_gray_tower(ImageView archi_gray_tower) {
        this.archi_gray_tower = archi_gray_tower;
    }

    public void setArchi_num_blue(Label archi_num_blue) {
        this.archi_num_blue = archi_num_blue;
    }

    public void setArchi_num_pink(Label archi_num_pink) {
        this.archi_num_pink = archi_num_pink;
    }

    public void setArchi_num_red(Label archi_num_red) {
        this.archi_num_red = archi_num_red;
    }

    public void setArchi_num_yellow(Label archi_num_yellow) {
        this.archi_num_yellow = archi_num_yellow;
    }

    public void setArchi_num_green(Label archi_num_green) {
        this.archi_num_green = archi_num_green;
    }

    //set only one (or zero) of three towers visible
    public void setVisibleTower(PlayerColour colourToShow){
        if(colourToShow == null){
            this.archi_white_tower.setVisible(false);
            this.archi_black_tower.setVisible(false);
            this.archi_gray_tower.setVisible(false);
        }
        else{
            switch(colourToShow){
                case WHITE:
                    this.archi_white_tower.setVisible(true);
                    this.archi_black_tower.setVisible(false);
                    this.archi_gray_tower.setVisible(false);
                    break;
                case BLACK:
                    this.archi_white_tower.setVisible(false);
                    this.archi_black_tower.setVisible(true);
                    this.archi_gray_tower.setVisible(false);
                    break;
                case GRAY:
                    this.archi_white_tower.setVisible(false);
                    this.archi_black_tower.setVisible(false);
                    this.archi_gray_tower.setVisible(true);
                    break;
            }
        }
    }
}
