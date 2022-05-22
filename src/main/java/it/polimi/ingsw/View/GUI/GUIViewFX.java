package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static it.polimi.ingsw.View.CLIColours.ANSI_GREEN;
import static it.polimi.ingsw.View.CLIColours.ANSI_RESET;

public class GUIViewFX extends Application {
    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        // Erianrys logo
        Image icon = new Image("/images/eriantys.jpg");
        this.stage = stage;
        stage.getIcons().add(icon);

        // Windows title
        stage.setTitle("Stage demo w00t w00t");
        stage.setFullScreen(true);

        //------------------------------ Moving circle -------------------------------------------------
        // The circle can be moved by clicking on the buttons on the screen
        // for the moving circle uncomment this:
        //Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/try.fxml")));
        //----------------------------------------------------------------------------------------------

        //----------------------------- Eriantys Welcome -----------------------------------------------
        // for the eriantys welcome uncomment this:
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/try2.fxml")));
        //----------------------------------------------------------------------------------------------

        Scene scene = new Scene(root);

        // css
        String css = getClass().getResource("/css/try2.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Stage getStage(){
        return this.stage;
    }
}

