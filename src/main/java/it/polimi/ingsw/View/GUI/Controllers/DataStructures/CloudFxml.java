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

public class CloudFxml {
    private int index;
    private final ImageView cloudImage;
    private final GridPane cloudGrid;

    private Client client;  // Client class
    private SerializedBoardAbstract board;  // Board
    private BoardFourAdvancedController controller; // BoardFourAdvancedController (passed in the setCloudsFxmlVisualization method)

    private static final Map<SPColour, String> studentColourPath = Map.of(
            SPColour.BLUE, "/images/pawns/stud_blue.png",
            SPColour.PINK, "/images/pawns/stud_pink.png",
            SPColour.RED, "/images/pawns/stud_red.png",
            SPColour.YELLOW, "/images/pawns/stud_yellow.png",
            SPColour.GREEN, "/images/pawns/stud_green.png"
    ); // relates the SPColour to the image of the student of that colour

    public CloudFxml(int index, GridPane cloud, ImageView cloudImage) {
        this.index = index;
        this.cloudImage = cloudImage;
        this.cloudGrid = cloud;

        this.cloudGrid.setOnMouseClicked(this::onMouseClicked);
    }


    public void setBoard(SerializedBoardAbstract board) {
        this.board = board;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setController(BoardFourAdvancedController controller) {
        this.controller = controller;
    }

    public void setVisible(boolean isVisible){
        this.cloudImage.setVisible(isVisible);
        this.cloudGrid.setVisible(isVisible);
    }

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

    private void onMouseClicked(MouseEvent event) {
        if(this.controller.isCurrentPlayer(this.client.getNickname())) {
            if(this.board.getCurrentState().equals(State.ACTION3)) {
                this.client.asyncWriteToSocket("studentCloudToSchool " + this.index);
            }
        }
    }
}
