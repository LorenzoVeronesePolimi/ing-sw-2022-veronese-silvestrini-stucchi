package it.polimi.ingsw.View.GUI.Controllers.DataStructures;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.View.GUI.Controllers.BoardFourAdvancedController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Map;

/**
 * data structure for clouds
 */
public class CloudFxml {
    private final int index;
    private final ImageView cloudImage;
    private final GridPane cloudGrid;

    private Client client;  // Client class
    private SerializedBoardAbstract board;  // Board
    private BoardFourAdvancedController controller; // BoardFourAdvancedController (passed in the setCloudsFxmlVisualization method)
    private boolean cloudClicked = false;

    private static final Map<SPColour, String> studentColourPath = Map.of(
            SPColour.BLUE, "/images/pawns/stud_blue.png",
            SPColour.PINK, "/images/pawns/stud_pink.png",
            SPColour.RED, "/images/pawns/stud_red.png",
            SPColour.YELLOW, "/images/pawns/stud_yellow.png",
            SPColour.GREEN, "/images/pawns/stud_green.png"
    ); // relates the SPColour to the image of the student of that colour

    /**
     * constructor of cloud data structure
     * @param index cloud index
     * @param cloud cloud grid pane
     * @param cloudImage cloud image
     */
    public CloudFxml(int index, GridPane cloud, ImageView cloudImage) {
        this.index = index;
        this.cloudImage = cloudImage;
        this.cloudGrid = cloud;

        this.cloudGrid.setOnMouseClicked(this::onMouseClicked);
    }

    /**
     * This method sets the clickable part of the scene.
     * @param enable true if the content is clickable, false otherwise.
     */
    public void enableClick(boolean enable) {
        if(enable) {
            this.cloudGrid.setOnMouseClicked(this::onMouseClicked);
        } else {
            this.cloudGrid.setOnMouseClicked(null);
        }
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
     * setter of board controller
     * @param controller BoardFourAdvancedController controller
     */
    public void setController(BoardFourAdvancedController controller) {
        this.controller = controller;
    }

    /**
     * method that sets the cloud visible or not
     * @param isVisible value to set
     */
    public void setVisible(boolean isVisible){
        this.cloudImage.setVisible(isVisible);
        this.cloudGrid.setVisible(isVisible);
    }

    /**
     * Method used to indicate if the cloud has been clicked.
     * @param clicked true if the cloud has been clicked.
     */
    public void setCloudClicked(boolean clicked) {
        this.cloudClicked = clicked;
    }

    /**
     * setter of students visualization on the cloud
     * @param students list of students
     * @param scale image scale
     */
    public void setStudentsVisualization(List<Student> students, double scale){
        int i = 0;
        int j = 0;
        for(Student s : students){
            ImageView image = new ImageView(getClass().getResource(studentColourPath.get(s.getColour())).toExternalForm());
            image.setFitHeight(30 * scale);
            image.setFitWidth(30 * scale);
            cloudGrid.add(image, i, j);

            i++;
            if(i == 2) {
                i = 0;
                j++;
            }
        }
    }

    /**
     * manager of mouse click event
     * @param event event
     */
    private void onMouseClicked(MouseEvent event) {
        if(this.controller.isCurrentPlayer(this.client.getNickname())) {
            if(this.board.getCurrentState().equals(State.ACTION3)) {
                if(!this.cloudClicked) {
                    this.cloudClicked = true;
                    this.client.asyncWriteToSocket("studentCloudToSchool " + this.index);
                }
            }
        }
    }
}
