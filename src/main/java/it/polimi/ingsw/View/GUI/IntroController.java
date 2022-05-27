package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Client.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class IntroController implements GUIController{
    private Client client;
    private GUIViewFX guiViewFX;
    public static Stage firstStage;

    @FXML private Label titleLable;
    @FXML private Label subtitleLable;
    @FXML private Button startGame;

    public void onButtonClicked(ActionEvent e) {
        System.out.println("Button pressed");
        this.client.setPlatformReady(true);
        /*
        System.out.println("ciao2");
        this.firstStage = (Stage)((Node)e.getSource()).getScene().getWindow();
        this.firstEvent = e;
        System.out.println("ciao3");
        Platform.runLater(() ->{
            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            Parent root = null;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/try.fxml")));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Scene scene = new Scene(root);

            // css
            String css = getClass().getResource("/css/try2.css").toExternalForm();
            scene.getStylesheets().add(css);

            stage.setScene(scene);
            stage.show();
        });

         */
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void setGUIFX(GUIViewFX gui) {
        this.guiViewFX = gui;
    }
}
