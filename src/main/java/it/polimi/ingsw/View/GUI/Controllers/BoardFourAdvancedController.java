package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardFourAdvancedController implements GUIController{
    private GUIViewFX guiViewFX;
    private Client client;

    // School my
    @FXML private GridPane my_hall;
    @FXML private GridPane my_dining;
    @FXML private GridPane my_professors;
    @FXML private GridPane my_towers;
    // School 1
    @FXML private Label opponent1_nick;
    @FXML private GridPane opponent1_hall;
    @FXML private GridPane opponent1_dining;
    @FXML private GridPane opponent1_professors;
    @FXML private GridPane opponent1_towers;
    // School 2
    @FXML private Label opponent2_nick;
    @FXML private GridPane opponent2_hall;
    @FXML private GridPane opponent2_dining;
    @FXML private GridPane opponent2_professors;
    @FXML private GridPane opponent2_towers;
    // School 3
    @FXML private Label opponent3_nick;
    @FXML private GridPane opponent3_hall;
    @FXML private GridPane opponent3_dining;
    @FXML private GridPane opponent3_professors;
    @FXML private GridPane opponent3_towers;

    // Archipelago 0
    @FXML private ImageView white_tower0;
    @FXML private ImageView black_tower0;
    @FXML private ImageView gray_tower0;
    @FXML private Label num_blue0;
    @FXML private Label num_pink0;
    @FXML private Label num_red0;
    @FXML private Label num_yellow0;
    @FXML private Label num_green0;
    // Archipelago 1
    @FXML private ImageView white_tower1;
    @FXML private ImageView black_tower1;
    @FXML private ImageView gray_tower1;
    @FXML private Label num_blue1;
    @FXML private Label num_pink1;
    @FXML private Label num_red1;
    @FXML private Label num_yellow1;
    @FXML private Label num_green1;
    // Archipelago 2
    @FXML private ImageView white_tower2;
    @FXML private ImageView black_tower2;
    @FXML private ImageView gray_tower2;
    @FXML private Label num_blue2;
    @FXML private Label num_pink2;
    @FXML private Label num_red2;
    @FXML private Label num_yellow2;
    @FXML private Label num_green2;
    // Archipelago 3
    @FXML private ImageView white_tower3;
    @FXML private ImageView black_tower3;
    @FXML private ImageView gray_tower3;
    @FXML private Label num_blue3;
    @FXML private Label num_pink3;
    @FXML private Label num_red3;
    @FXML private Label num_yellow3;
    @FXML private Label num_green3;
    // Archipelago 4
    @FXML private ImageView white_tower4;
    @FXML private ImageView black_tower4;
    @FXML private ImageView gray_tower4;
    @FXML private Label num_blue4;
    @FXML private Label num_pink4;
    @FXML private Label num_red4;
    @FXML private Label num_yellow4;
    @FXML private Label num_green4;
    // Archipelago 5
    @FXML private ImageView white_tower5;
    @FXML private ImageView black_tower5;
    @FXML private ImageView gray_tower5;
    @FXML private Label num_blue5;
    @FXML private Label num_pink5;
    @FXML private Label num_red5;
    @FXML private Label num_yellow5;
    @FXML private Label num_green5;
    // Archipelago 6
    @FXML private ImageView white_tower6;
    @FXML private ImageView black_tower6;
    @FXML private ImageView gray_tower6;
    @FXML private Label num_blue6;
    @FXML private Label num_pink6;
    @FXML private Label num_red6;
    @FXML private Label num_yellow6;
    @FXML private Label num_green6;
    // Archipelago 7
    @FXML private ImageView white_tower7;
    @FXML private ImageView black_tower7;
    @FXML private ImageView gray_tower7;
    @FXML private Label num_blue7;
    @FXML private Label num_pink7;
    @FXML private Label num_red7;
    @FXML private Label num_yellow7;
    @FXML private Label num_green7;
    // Archipelago 8
    @FXML private ImageView white_tower8;
    @FXML private ImageView black_tower8;
    @FXML private ImageView gray_tower8;
    @FXML private Label num_blue8;
    @FXML private Label num_pink8;
    @FXML private Label num_red8;
    @FXML private Label num_yellow8;
    @FXML private Label num_green8;
    // Archipelago 9
    @FXML private ImageView white_tower9;
    @FXML private ImageView black_tower9;
    @FXML private ImageView gray_tower9;
    @FXML private Label num_blue9;
    @FXML private Label num_pink9;
    @FXML private Label num_red9;
    @FXML private Label num_yellow9;
    @FXML private Label num_green9;
    // Archipelago 10
    @FXML private ImageView white_tower10;
    @FXML private ImageView black_tower10;
    @FXML private ImageView gray_tower10;
    @FXML private Label num_blue10;
    @FXML private Label num_pink10;
    @FXML private Label num_red10;
    @FXML private Label num_yellow10;
    @FXML private Label num_green10;
    // Archipelago 11
    @FXML private ImageView white_tower11;
    @FXML private ImageView black_tower11;
    @FXML private ImageView gray_tower11;
    @FXML private Label num_blue11;
    @FXML private Label num_pink11;
    @FXML private Label num_red11;
    @FXML private Label num_yellow11;
    @FXML private Label num_green11;

    // Clouds
    @FXML private GridPane cloud1;
    @FXML private GridPane cloud2;
    @FXML private GridPane cloud3;
    @FXML private GridPane cloud4;

    int numPlayers;


    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setNumPlayers(int numPlayers){
        this.numPlayers = numPlayers;
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
