package it.polimi.ingsw.View.GUI.Controllers.DataStructures;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.View.GUI.Controllers.BoardFourAdvancedController;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * data structure for schools
 */
public class SchoolFxml {
    @FXML private final Label nick;
    @FXML private final GridPane hall;
    @FXML private final GridPane dining;
    @FXML private final GridPane professors;
    @FXML private final GridPane towers;
    @FXML private final Label coins;

    private Client client;  // Client class
    private SerializedBoardAbstract board;  // Board
    private BoardFourAdvancedController controller; // BoardFourAdvancedController (passed in the setSchoolsFxmlVisualization method)
    private ImageView movedStudent = null; // when a student from the hall il clicked this value is updated
    private List<ImageView> studentImages;

    private Map<ImageView, SPColour> imageColour;

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


    /**
     * constructor of the school data structure
     * @param nick label for nickname
     * @param hall grid pane for hall
     * @param dining grid pane for dining room
     * @param professors grid pane for professors
     * @param towers grid pane for towers
     * @param coins label for coins
     */
    public SchoolFxml(Label nick, GridPane hall, GridPane dining, GridPane professors, GridPane towers, Label coins) {
        this.nick = nick;
        this.hall = hall;
        this.dining = dining;
        this.professors = professors;
        this.towers = towers;
        this.coins = coins;

        this.imageColour = new HashMap<>();

        ColumnConstraints col = new ColumnConstraints();
        col.setHalignment(HPos.CENTER);
        RowConstraints row = new RowConstraints();
        row.setValignment(VPos.CENTER);
        this.hall.getColumnConstraints().add(col);
        this.hall.getRowConstraints().add(row);
        this.dining.getColumnConstraints().add(col);
        this.dining.getRowConstraints().add(row);
        this.dining.setOnMouseClicked(this::diningClicked);
        this.professors.getColumnConstraints().add(col);
        this.professors.getRowConstraints().add(row);
        this.towers.getColumnConstraints().add(col);
        this.towers.getRowConstraints().add(row);

        this.studentImages = new ArrayList<>();
    }

    /**
     * setter of serialized board notified by model
     * @param board serialized board notified by model
     */
    public void setBoard(SerializedBoardAbstract board) {
        this.board = board;
    }

    /**
     * setter of client
     * @param client client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * setter of BoardFourAdvancedController
     * @param controller BoardFourAdvancedController controller
     */
    public void setController(BoardFourAdvancedController controller) {
        this.controller = controller;
    }

    // called by the controller when a student is placed on an archipelago

    /**
     * setter of the student that has been moved
     * @param student student that has been moved
     */
    public void setMovedStudent(ImageView student) {
        this.movedStudent = student;
    }

    /**
     *getter of nickname
     * @return nick
     */
    public Label getNick() {
        return nick;
    }

    /**
     * getter of hall grid pane
     * @return  hall grid pane
     */
    public GridPane getHall() {
        return hall;
    }

    /**
     * getter of dining grid pane
     * @return  dining grid pane
     */
    public GridPane getDining() {
        return dining;
    }

    /**
     * getter of professors grid pane
     * @return  professors grid pane
     */
    public GridPane getProfessors() {
        return professors;
    }

    /**
     * getter of towers grid pane
     * @return  towers grid pane
     */
    public GridPane getTowers() {
        return towers;
    }

    /**
     * getter of coins label
     * @return  coins label
     */
    public Label getCoins() {
        return coins;
    }

    /**
     * method that reset the visualization
     */
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

        this.imageColour.clear();
    }

    /**
     * This method sets the clickable part of the scene.
     * @param enable true if the content is clickable, false otherwise.
     */
    public void enableClick(boolean enable) {
        if(enable) {
            this.dining.setOnMouseClicked(this::diningClicked);

            if (studentImages.size() > 0) {
                for (ImageView i : this.studentImages) {
                    i.setOnMouseClicked(event -> hallStudentClicked(event, i));
                }
            }
        } else {
            this.dining.setOnMouseClicked(null);

            if (studentImages.size() > 0) {
                for (ImageView i : this.studentImages) {
                    i.setOnMouseClicked(null);
                }
            }
        }
    }

    /**
     * method that removes everything from a grid pane
     * @param grid grid to clear
     */
    private void removeAllNodesFromGrid(GridPane grid){
        List<Node> children = new ArrayList<>(grid.getChildren()); //clone to avoid ConcurrentModificationException
        for(Node n : children){
            grid.getChildren().remove(n);
        }
    }

    /**
     * setter of nick visualization
     * @param nick nickname
     * @param colour colour of the player
     */
    public void setNickVisualization(String nick, PlayerColour colour) {
        if(this.nick != null){
            this.nick.setText(nick + " - Team: " + colour.toString());
        }
    }

    /**
     * setter of hall visualization
     * @param students list of hall students
     * @param scale scale of image
     */
    public void setHallVisualization(List<Student> students, double scale) {
        int i = 0; // always 0 or 1
        int j = 0;
        for(Student s : students){
            ImageView image = new ImageView(getClass().getResource(studentColourPath.get(s.getColour())).toExternalForm());
            image.setFitHeight(30 * scale);
            image.setFitWidth(30 * scale);
            //THIS COMMENTED PART NEEDS TO BE CANCELLED IF WE USE CLICK INSTEAD OF DRAG AND DROP

            /*image.setOnDragDetected((MouseEvent event) -> {
                System.out.println("dragga");
                Dragboard db = image.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                // Store node ID in order to know what is dragged.
                content.putString(image.getId());
                db.setContent(content);
                event.consume();
            });*/
            //image.setOnMouseClicked(event -> studentHallClicked(event, image));
            //image.setOnMouseDragged(event -> studentHallDragged(event, image));
            //image.setPreserveRatio(true);

            this.studentImages.add(image);
            image.setOnMouseClicked(event -> hallStudentClicked(event, image));
            this.imageColour.put(image, s.getColour());

            this.hall.add(image, i, j);

            i++;
            if(i == 2) {
                i = 0;
                j++;
            }
        }
    }

    /**
     * setter of dining room visualization
     * @param school school
     * @param scale image scale
     */
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

    /**
     * setter of professor visualization
     * @param professors list of professors
     * @param scale image scale
     */
    public void setProfessorsVisualization(List<Professor> professors, double scale){
        for(Professor p : professors){
            ImageView image = new ImageView(getClass().getResource(professorColourPath.get(p.getColour())).toExternalForm());
            image.setFitHeight(30 * scale);
            image.setFitWidth(30 * scale);
            this.professors.add(image, 0, SPColourRow.get(p.getColour()));
        }

    }

    /**
     * setter of towers visualization
     * @param towers list of towers
     * @param scale image scale
     */
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

    /**
     * setter of coin visualization
     * @param numCoins
     */
    public void setCoinsVisualization(int numCoins){
        this.coins.setText(Integer.toString(numCoins));
    }

    public void studentHallClicked(MouseEvent e, ImageView image) {
        /*for(Node n : this.hall.getChildren()){
            if(n == image){
                this.hall.getChildren().remove(image);
            }
        }*/
        /*System.out.println("MOUSE");
        double mouseX = e.getSceneX();
        double mouseY = e.getSceneY();
        System.out.println(mouseX);
        System.out.println(mouseY);

        System.out.println("IMMAGINE");
        Bounds boundsInScene = image.localToScene(image.getBoundsInLocal());
        double imageX = boundsInScene.getMinX();
        double imageY = boundsInScene.getMinY();
        System.out.println(imageX);
        System.out.println(imageY);*/
        //System.out.println("Student y: " + image.getLayoutY());

    }

    /**
     * manager of student click event
     * @param e event
     * @param image student image
     */
    private void hallStudentClicked(MouseEvent e, ImageView image) {
        if(this.movedStudent != null) {
            this.movedStudent.setOpacity(1);
            this.movedStudent = null;
            this.controller.setMovedStudent(null);
        }
        if(this.client.getNickname().equals(this.board.getCurrentPlayer().getNickname())) { // if it's the player turn
            for(Node n : this.hall.getChildren()) { // if he touched a hall student
                if (n == image) {
                    /* image blinking
                    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.1), image);
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0.0);
                    fadeTransition.setCycleCount(Animation.INDEFINITE);*/
                    image.setOpacity(0);    // I think it is fancier if the student "disappears" from the hall
                    this.controller.setCursor(studentColourPath.get(this.imageColour.get(image)));
                    this.movedStudent = image;
                    this.controller.setMovedStudent(this.imageColour.get(this.movedStudent)); // in case of studentToArchipelago

                    this.controller.setActionLabel("Select your DiningRoom \nor an Island.");
                }
            }
        }
    }

    /**
     * manager of dining room clicked (in case of studentHallToDiningRoom)
     * @param event event
     */
    private void diningClicked(MouseEvent event) {
        if(this.movedStudent != null) {
            this.client.asyncWriteToSocket("studentHallToDiningRoom " + this.imageColour.get(movedStudent));
            this.movedStudent = null;
            this.controller.setMovedStudent(null);
            this.controller.setCursorToDefault();
        }
    }

    //THIS METHOD NEEDS TO BE CANCELLED IF WE USE CLICK INSTEAD OF DRAG AND DROP
    public void studentHallDragged(MouseEvent e, ImageView image){
        /*
        Dragboard db = image.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        // Store node ID in order to know what is dragged.
        content.putString(image.getId());
        db.setContent(content);
        //e.consume();*/

        System.out.println("sto draggando");
        System.out.println("MOUSE");
        double mouseX = e.getSceneX();
        double mouseY = e.getSceneY();
        System.out.println(mouseX);
        System.out.println(mouseY);

        System.out.println("IMMAGINE");
        Bounds boundsInScene = image.localToScene(image.getBoundsInLocal());
        double imageX = boundsInScene.getMinX() + image.getFitWidth();
        double imageY = boundsInScene.getMinY() - image.getFitHeight();
        System.out.println(imageX);
        System.out.println(imageY);
        /*
        System.out.println("DELTA");
        System.out.println(image.getLayoutX() + e.getX());
        System.out.println(image.getLayoutY() + e.getY());

        double distanceX = image.getX() + e.getX();
        double distanceY = image.getY() + e.getY();

         */

        double distanceX = mouseX - imageX;
        double distanceY = mouseY - imageY;

        image.setTranslateX(distanceX);
        image.setTranslateY(distanceY);

        e.consume();
        /*
        image.setX(image.getX() + e.getX());
        image.setY(image.getY() + e.getY());
        e.consume();*/
    }
}
