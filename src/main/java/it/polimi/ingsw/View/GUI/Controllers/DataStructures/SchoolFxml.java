package it.polimi.ingsw.View.GUI.Controllers.DataStructures;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;
import it.polimi.ingsw.Model.Places.School.School;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SchoolFxml {
    @FXML private final Label nick;
    @FXML private final GridPane hall;
    @FXML private final GridPane dining;
    @FXML private final GridPane professors;
    @FXML private final GridPane towers;
    @FXML private final Label coins;

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

        ColumnConstraints col = new ColumnConstraints();
        col.setHalignment(HPos.CENTER);
        RowConstraints row = new RowConstraints();
        row.setValignment(VPos.CENTER);
        this.hall.getColumnConstraints().add(col);
        this.hall.getRowConstraints().add(row);
        this.dining.getColumnConstraints().add(col);
        this.dining.getRowConstraints().add(row);
        this.professors.getColumnConstraints().add(col);
        this.professors.getRowConstraints().add(row);
        this.towers.getColumnConstraints().add(col);
        this.towers.getRowConstraints().add(row);
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

    public void resetVisualization(){
        GridPane[] gridsToReset = {
                this.hall,
                this.dining,
                this.professors,
                this.towers,
        };

        for(GridPane grid : gridsToReset){
            removeAllNodesFromGrid(grid);
        }
    }

    private void removeAllNodesFromGrid(GridPane grid){
        List<Node> children = new ArrayList<>(grid.getChildren()); //clone to avoid ConcurrentModificationException
        for(Node n : children){
            this.hall.getChildren().remove(n);
        }
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
            image.setFitHeight(30 * scale);
            image.setFitWidth(30 * scale);
            //image.setOnMouseDragged(event -> studentHallDragged(event, image));
            //image.setFitHeight(image.getFitHeight() * scale);
            //image.setFitWidth(image.getFitWidth() * scale);
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
                this.dining.add(image, j, i);
            }
        }
    }

    public void setProfessorsVisualization(List<Professor> professors, double scale){
        for(Professor p : professors){
            ImageView image = new ImageView(getClass().getResource(professorColourPath.get(p.getColour())).toExternalForm());
            image.setFitHeight(30 * scale);
            image.setFitWidth(30 * scale);
            this.professors.add(image, 0, SPColourRow.get(p.getColour()));
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

    public void setCoinsVisualization(int numCoins){
        this.coins.setText(Integer.toString(numCoins));
    }

    public void studentHallDragged(MouseEvent e, ImageView image){
        image.setX(image.getX() + e.getX());
        image.setY(image.getY() + e.getY());
    }
}
