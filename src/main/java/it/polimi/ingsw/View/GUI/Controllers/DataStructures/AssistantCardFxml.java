package it.polimi.ingsw.View.GUI.Controllers.DataStructures;

import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

import java.util.Map;

/**
 * data structure for assistant cards
 */
public class AssistantCardFxml {
    private final Map<Integer, ImageView> priorityImage;

    /**
     * constructor of character card data structure
     * @param op_a1 assistant 1 image
     * @param op_a2 assistant 2 image
     * @param op_a3 assistant 3 image
     * @param op_a4 assistant 4 image
     * @param op_a5 assistant 5 image
     * @param op_a6 assistant 6 image
     * @param op_a7 assistant 7 image
     * @param op_a8 assistant 8 image
     * @param op_a9 assistant 9 image
     * @param op_a10 assistant 10 image
     */
    public AssistantCardFxml(ImageView op_a1, ImageView op_a2, ImageView op_a3, ImageView op_a4, ImageView op_a5, ImageView op_a6, ImageView op_a7, ImageView op_a8, ImageView op_a9, ImageView op_a10) {
        this.priorityImage = Map.of(
                1, op_a1,
                2, op_a2,
                3, op_a3,
                4, op_a4,
                5, op_a5,
                6, op_a6,
                7, op_a7,
                8, op_a8,
                9, op_a9,
                10, op_a10
        );
    }

    /**
     * setter of assistant card visualization
     * @param turnPriority turn priority of the chosen card
     */
    public void setAssistantCardVisualization(int turnPriority){
        for(int i = 1; i <= 10; i++){
            if(i == turnPriority){
                //ImageView image = this.priorityImage.get(i);
                this.priorityImage.get(i).setVisible(true);
                //this.priorityImage.get(i).setOnMouseDragOver(event -> ACDragged(event, image));
            }
            else{
                this.priorityImage.get(i).setVisible(false);
            }
        }
    }

    public void ACDragged(MouseEvent e, ImageView image){
        /*Dragboard db = image.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        // Store node ID in order to know what is dragged.
        content.putString(image.getId());
        db.setContent(content);
        //e.consume()*/

        double distanceX = image.getX() - e.getX();
        double distanceY = image.getY() - e.getY();

        double x = image.getLayoutX() + distanceX;
        double y = image.getLayoutY() + distanceY;
        image.setTranslateX(x);
        image.setTranslateX(y);
        //After calculating X and y, relocate the node to the specified coordinate point (x, y)
        //image.relocate(x, y);
        //e.consume();
        /*
        image.setX(image.getX() + e.getX());
        image.setY(image.getY() + e.getY());
        image.relocate();*/

        /*double offsetX = e.getSceneX() - image.getX();
        double offsetY = e.getSceneY() - image.getY();
        double newTranslateX = image.getX() + offsetX;
        double newTranslateY = image.getY() + offsetY;
        image.setTranslateX(newTranslateX);
        image.setTranslateY(newTranslateY);
        e.consume();*/
    }
}
