package it.polimi.ingsw.View.GUI.Controllers.DataStructures;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class SchoolFxml {
    @FXML private Label nick;
    @FXML private GridPane hall;
    @FXML private GridPane dining;
    @FXML private GridPane professors;
    @FXML private GridPane towers;
    @FXML private Label coins;

    public SchoolFxml(Label nick, GridPane hall, GridPane dining, GridPane professors, GridPane towers, Label coins) {
        this.nick = nick;
        this.hall = hall;
        this.dining = dining;
        this.professors = professors;
        this.towers = towers;
        this.coins = coins;
    }

    public Label getNick() {
        return nick;
    }

    public GridPane getHall() {
        return hall;
    }

    public GridPane getDining() {
        return dining;
    }

    public GridPane getProfessors() {
        return professors;
    }

    public GridPane getTowers() {
        return towers;
    }

    public Label getCoins() {
        return coins;
    }
}
