package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.View.ClientView;
import it.polimi.ingsw.View.GUIView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class GUIViewFX extends Application {
    private Stage stage;
    private GUIView guiView;
    private Client client;

    @Override
    public void start(Stage stage) throws IOException {
        // Erianrys logo
        Image icon = new Image("/images/eriantys.jpg");
        this.stage = stage;
        CurrentStage.currentStage = this.stage;

        stage.getIcons().add(icon);

        // Windows title
        this.stage.setTitle("Stage demo w00t w00t");
        this.stage.setFullScreen(false);

        //------------------------------ Moving circle -------------------------------------------------
        // The circle can be moved by clicking on the buttons on the screen
        // for the moving circle uncomment this:
        //Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/try.fxml")));
        //----------------------------------------------------------------------------------------------

        //----------------------------- Eriantys Welcome -----------------------------------------------
        // for the eriantys welcome uncomment this:
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Intro.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------------------

        Scene scene = new Scene(root);

        // css
        String css = getClass().getResource("/css/try2.css").toExternalForm();
        scene.getStylesheets().add(css);

        this.stage.setScene(scene);
        this.stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /* It seems that this doesn't set the right pointers
    public void initializeView(Client client, ClientView guiViewInClient){
        this.client = client;
        launch(null);
    }*/

    public void setGuiView(GUIView guiView){
        this.guiView = guiView;
    }

    public Stage getStage(){
        return this.stage;
    }

    public void displayAskFirstPlayerInfo(){

    }
}

