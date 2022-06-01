package it.polimi.ingsw.View.GUI.Controllers.DataStructures;

import javafx.scene.image.ImageView;

import java.util.Map;

public class AssistantCardFxml {
    private final Map<Integer, ImageView> priorityImage;

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

    public void setAssistantCardVisualization(int turnPriority){
        for(int i = 1; i <= 10; i++){
            if(i == turnPriority){
                this.priorityImage.get(i).setVisible(true);
            }
            else{
                this.priorityImage.get(i).setVisible(false);
            }
        }
    }
}
