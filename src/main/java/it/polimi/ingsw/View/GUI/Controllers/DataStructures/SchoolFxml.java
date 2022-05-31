package it.polimi.ingsw.View.GUI.Controllers.DataStructures;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;
import it.polimi.ingsw.Model.Places.School.School;
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

    private static final Map<SPColour, String> studentColourPath = Map.of(
            SPColour.BLUE, "/images/pawns/stud_blue.png",
            SPColour.PINK, "/images/pawns/stud_pink.png",
            SPColour.RED, "/images/pawns/stud_red.png",
            SPColour.YELLOW, "/images/pawns/stud_yellow.png",
            SPColour.GREEN, "/images/pawns/stud_green.png"
    ); // relates the SPColour to the image of the student of that colour

    private static final Map<Integer, SPColour> rowSPColour = Map.of(
            0, SPColour.GREEN,
            1, SPColour.RED,
            2, SPColour.YELLOW,
            3, SPColour.PINK,
            4, SPColour.BLUE
    ); // relates index of the grid to the SPColour

    private static final Map<SPColour, String> professorColourPath = Map.of(
            SPColour.BLUE, "/images/pawns/prof_blue.png",
            SPColour.PINK, "/images/pawns/prof_pink.png",
            SPColour.RED, "/images/pawns/prof_red.png",
            SPColour.YELLOW, "/images/pawns/prof_yellow.png",
            SPColour.GREEN, "/images/pawns/prof_green.png"
    ); // relates the SPColour to the image of the professor of that colour

    private static final Map<SPColour, Integer> SPColourRow = Map.of(
            SPColour.GREEN, 0,
            SPColour.RED, 1,
            SPColour.YELLOW, 2,
            SPColour.PINK, 3,
            SPColour.BLUE, 4
    ); // relates index of the grid to the SPColour

    private static final Map<PlayerColour, String> playerColourPath = Map.of(
            PlayerColour.WHITE, "/images/pawns/WhiteTower.png",
            PlayerColour.BLACK, "/images/pawns/BlackTower.png",
            PlayerColour.GRAY, "/images/pawns/GrayTower.png"
    );


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
        int i = 0; // always 0 or 1
        int j = 0;
        for(Student s : students){
            ImageView image = new ImageView(getClass().getResource(studentColourPath.get(s.getColour())).toExternalForm());
            //image.setFitHeight(image.getFitHeight() * scale);
            //image.setFitWidth(image.getFitWidth() * scale);
            image.setFitHeight(30 * scale);
            image.setFitWidth(30 * scale);
            //image.setPreserveRatio(true);
            this.hall.add(image, i, j);
            i++;
            if(i == 2) {
                i = 0;
                j++;
            }
        }
    }

    public void setDiningVisualization(School school, double scale){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < school.getNumStudentColour(rowSPColour.get(i)); j++){
                ImageView image = new ImageView(getClass().getResource(studentColourPath.get(rowSPColour.get(i))).toExternalForm());
                image.setFitHeight(30 * scale);
                image.setFitWidth(30 * scale);
                this.professors.add(image, i, j);
            }
        }
    }

    public void setProfessorsVisualization(List<Professor> professors, double scale){
        Professor[] ps= {new Professor(SPColour.RED)};
        for(Professor p : ps){
            ImageView image = new ImageView(getClass().getResource(professorColourPath.get(p.getColour())).toExternalForm());
            image.setFitHeight(30 * scale);
            image.setFitWidth(30 * scale);
            this.dining.add(image, SPColourRow.get(p.getColour()), 0);
        }
    }

    public void setTowersVisualization(List<Tower> towers, double scale){
        int i = 0; // always 0 or 1
        int j = 0;
        for(Tower t : towers){
            ImageView image = new ImageView(getClass().getResource(playerColourPath.get(t.getPlayer().getColour())).toExternalForm());
            image.setFitHeight(30 * scale);
            image.setFitWidth(30 * scale);
            this.towers.add(image, i, j);
            i++;
            if(i == 2) {
                i = 0;
                j++;
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
