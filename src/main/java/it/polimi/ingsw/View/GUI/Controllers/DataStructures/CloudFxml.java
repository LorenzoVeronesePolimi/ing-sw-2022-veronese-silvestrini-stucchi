package it.polimi.ingsw.View.GUI.Controllers.DataStructures;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Pawns.Student;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Map;

public class CloudFxml {
    private final ImageView cloudImage;
    private final GridPane cloud;

    private static final Map<SPColour, String> studentColourPath = Map.of(
            SPColour.BLUE, "/images/pawns/stud_blue.png",
            SPColour.PINK, "/images/pawns/stud_pink.png",
            SPColour.RED, "/images/pawns/stud_red.png",
            SPColour.YELLOW, "/images/pawns/stud_yellow.png",
            SPColour.GREEN, "/images/pawns/stud_green.png"
    ); // relates the SPColour to the image of the student of that colour

    public CloudFxml(GridPane cloud, ImageView cloudImage) {
        this.cloud = cloud;
        this.cloudImage = cloudImage;
    }

    public void setVisible(boolean isVisible){
        this.cloudImage.setVisible(isVisible);
        this.cloud.setVisible(isVisible);
    }

    public void setStudentsVisualization(List<Student> students, double scale){
        int i = 0;
        int j = 0;
        for(Student s : students){
            ImageView image = new ImageView(getClass().getResource(studentColourPath.get(s.getColour())).toExternalForm());
            image.setFitHeight(30 * scale);
            image.setFitWidth(30 * scale);
            cloud.add(image, i, j);

            i++;
            if(i == 2) {
                i = 0;
                j++;
            }
        }
    }
}
