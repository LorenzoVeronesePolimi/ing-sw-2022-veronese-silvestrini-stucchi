package it.polimi.ingsw.View.GUI.Controllers.DataStructures;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Pawns.Student;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Map;

public class SchoolFxml {
    @FXML private Label nick;
    @FXML private GridPane hall;
    @FXML private GridPane dining;
    @FXML private GridPane professors;
    @FXML private GridPane towers;
    @FXML private Label coins;

    private static Map<SPColour, String> studentColourPath = Map.of(
            SPColour.BLUE, "/images/pawns/stud_blue.png",
            SPColour.PINK, "/images/pawns/stud_pink.png",
            SPColour.RED, "/images/pawns/stud_red.png",
            SPColour.YELLOW, "/images/pawns/stud_yellow.png",
            SPColour.GREEN, "/images/pawns/stud_green.png"
    ); // relates the SPColour to the image of the student of that colour

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

    public void setNickVisualization(String nick) {
        if(this.nick != null){
            this.nick.setText(nick);
        }
    }

    public void setHallVisualization(List<Student> students, double scale) {
        int x = 0; // always 0 or 1
        int y = 0;
        for(Student s : students){
            ImageView image = new ImageView(getClass().getResource(studentColourPath.get(s.getColour())).toExternalForm());
            image.setFitHeight(image.getFitHeight() * scale);
            image.setFitWidth(image.getFitWidth() * scale);
            image.setPreserveRatio(true);
            this.hall.add(image, x, y);
            x++;
            if(x == 2) {
                x = 0;
                y++;
            }
        }
    }

    public void setDining(GridPane dining) {
        this.dining = dining;
    }

    public void setProfessors(GridPane professors) {
        this.professors = professors;
    }

    public void setTowers(GridPane towers) {
        this.towers = towers;
    }

    public void setCoins(Label coins) {
        this.coins = coins;
    }


}
