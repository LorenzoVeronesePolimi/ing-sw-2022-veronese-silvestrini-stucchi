package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class CharacterCardDialogController {
    @FXML private AnchorPane general_anchor;
    @FXML private Label card_name;
    @FXML private Label card_effect;
    @FXML private ImageView card_image;
    @FXML private Button use_yes;
    @FXML private Button use_no;

    public void initialize(){

    }

    public void setCardName(String name) {
        this.card_name.setText(name);
    }

    public void setCardEffect(String effect) {
        this.card_effect.setText(effect);
    }

    public void setCardImage(String imagePath) {
        this.card_image.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
    }

    public void setCharacterCardActions(CharacterCardEnumeration type){

    }

}
