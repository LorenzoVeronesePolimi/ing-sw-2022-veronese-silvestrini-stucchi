package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.View.GUI.Controllers.DataStructures.ArchipelagoFxml;
import it.polimi.ingsw.View.GUI.Controllers.DataStructures.SchoolFxml;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class BoardFourAdvancedController implements GUIController, Initializable {
    private GUIViewFX guiViewFX;
    private Client client;

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
    @FXML private GridPane cloud1;
    @FXML private GridPane cloud2;
    @FXML private GridPane cloud3;
    @FXML private GridPane cloud4;

    private static List<ArchipelagoFxml> archipelagosFxml;
    private static List<SchoolFxml> schoolsFxml;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Create Archipelagos data structure
        archipelagosFxml = new ArrayList<>();
        archipelagosFxml.add(new ArchipelagoFxml(archi0_mother_nature, archi0_white_tower, archi0_black_tower, archi0_gray_tower, archi0_num_towers, archi0_num_blue, archi0_num_pink, archi0_num_red, archi0_num_yellow, archi0_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(archi1_mother_nature, archi1_white_tower, archi1_black_tower, archi1_gray_tower, archi1_num_towers, archi1_num_blue, archi1_num_pink, archi1_num_red, archi1_num_yellow, archi1_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(archi2_mother_nature, archi2_white_tower, archi2_black_tower, archi2_gray_tower, archi2_num_towers, archi2_num_blue, archi2_num_pink, archi2_num_red, archi2_num_yellow, archi2_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(archi3_mother_nature, archi3_white_tower, archi3_black_tower, archi3_gray_tower, archi3_num_towers, archi3_num_blue, archi3_num_pink, archi3_num_red, archi3_num_yellow, archi3_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(archi4_mother_nature, archi4_white_tower, archi4_black_tower, archi4_gray_tower, archi4_num_towers, archi4_num_blue, archi4_num_pink, archi4_num_red, archi4_num_yellow, archi4_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(archi5_mother_nature, archi5_white_tower, archi5_black_tower, archi5_gray_tower, archi5_num_towers, archi5_num_blue, archi5_num_pink, archi5_num_red, archi5_num_yellow, archi5_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(archi6_mother_nature, archi6_white_tower, archi6_black_tower, archi6_gray_tower, archi6_num_towers, archi6_num_blue, archi6_num_pink, archi6_num_red, archi6_num_yellow, archi6_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(archi7_mother_nature, archi7_white_tower, archi7_black_tower, archi7_gray_tower, archi7_num_towers, archi7_num_blue, archi7_num_pink, archi7_num_red, archi7_num_yellow, archi7_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(archi8_mother_nature, archi8_white_tower, archi8_black_tower, archi8_gray_tower, archi8_num_towers, archi8_num_blue, archi8_num_pink, archi8_num_red, archi8_num_yellow, archi8_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(archi9_mother_nature, archi9_white_tower, archi9_black_tower, archi9_gray_tower, archi9_num_towers, archi9_num_blue, archi9_num_pink, archi9_num_red, archi9_num_yellow, archi9_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(archi10_mother_nature, archi10_white_tower, archi10_black_tower, archi10_gray_tower, archi10_num_towers, archi10_num_blue, archi10_num_pink, archi10_num_red, archi10_num_yellow, archi10_num_green));
        archipelagosFxml.add(new ArchipelagoFxml(archi11_mother_nature, archi11_white_tower, archi11_black_tower, archi11_gray_tower, archi11_num_towers, archi11_num_blue, archi11_num_pink, archi11_num_red, archi11_num_yellow, archi11_num_green));

        // Create Schools data structure
        schoolsFxml.add(new SchoolFxml(null, my_hall, my_dining, my_professors, my_towers, my_coins));
        schoolsFxml.add(new SchoolFxml(opponent1_nick, opponent1_hall, opponent1_dining, opponent1_professors, opponent1_towers, opponent1_coins));
        schoolsFxml.add(new SchoolFxml(opponent2_nick, opponent2_hall, opponent2_dining, opponent2_professors, opponent2_towers, opponent2_coins));
        schoolsFxml.add(new SchoolFxml(opponent3_nick, opponent3_hall, opponent3_dining, opponent3_professors, opponent3_towers, opponent3_coins));
    }

    public void setArchipelagosFxmlVisualization(SerializedBoardAbstract board){
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
    }

    public void setSchoolsFxmlVisualization(SerializedBoardAbstract board){
        int currentPlayerIndex = board.getOrderedPlayers().indexOf(board.getCurrentPlayer());
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
