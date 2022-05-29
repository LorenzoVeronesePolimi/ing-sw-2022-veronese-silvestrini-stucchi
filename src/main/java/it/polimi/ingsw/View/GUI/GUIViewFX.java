package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.View.ClientView;
import it.polimi.ingsw.View.GUIView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

//TODO: implement method changeScene<particolare> in order to pass to the controller the parameter needed (the ones passed in GUIView
//TODO: when calling an ask method
public class GUIViewFX extends Application {
    private Stage stage;
    private Client client;
    private GUIView clientView;
    private Scene currentScene;
    private static final String BOARD_ABSTRACT = "BoardAbstract.fxml";
    private static final String BOARD_ABSTRACT_CSS = "try2.css";
    private static final String INTRO = "Intro.fxml";
    private static final String LOGIN = "Login.fxml";
    private static final String INTRO_CSS = "try2.css";
    private final HashMap<String, Scene> sceneMap = new HashMap<>();
    private final HashMap<String, GUIController> controllerMap = new HashMap<>();

    public GUIViewFX() {

    }

    public GUIViewFX(Client client, GUIView view) {
        this.client = client;
        this.clientView = view;
        this.clientView.setGuiViewFX(this);
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.setupControllers();    // This method is called to setup all scene prior to the display
        this.stage = stage;
        this.run(); // running the real stage
    }

    private void setupControllers() {
        // creating an array of scenes (All the scenes of the application)
        List<String> sceneList = new ArrayList<>(Arrays.asList(INTRO, LOGIN));
        try {
            for(String path : sceneList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + path)); // prepare the scene
                Parent root = loader.load();    //load the scene
                GUIController controller = loader.getController();  // get the controller of the scene

                sceneMap.put(path, new Scene(root));    // creating a map of scenes based on their path
                controller.setGUIFX(this);  // set the GUIFX in the controller (don't know if it's necessary)
                controller.setClient(this.client);  // set the client in controller (mandatory to call asyncWriteToSocket)
                //this.client.printOut("client in gui");
                controllerMap.put(path, controller);    // create a map of controller and path (don't know if it's necessary)

                // applying css to the scenes
                String css;
                switch (path) {
                    case "Intro.fxml":
                        css = getClass().getResource("/css/" + INTRO_CSS).toExternalForm();
                        sceneMap.get(INTRO).getStylesheets().add(css);
                        break;

                    case "BoardAbstract.fxml":
                        css = getClass().getResource("/css/" + BOARD_ABSTRACT_CSS).toExternalForm();
                        sceneMap.get(BOARD_ABSTRACT).getStylesheets().add(css);
                        break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentScene = sceneMap.get(INTRO); //inintal scene

        /*
            The first scene shown in the intro page.
            After a player clicks on the play button the GUIView is set free (platformReady = true)
            and the GUI can execute the askFirstplayerInfo.
            When the second player clicks on the play button it waits until the prevous player has sent his login info (as in CLI)
            It necessary to implement a waiting scene (loading scene)
         */
    }

    public void run() {
        System.out.println("GUI running");

        // Eriantys logo
        Image icon = new Image("/images/eriantys.jpg");
        stage.getIcons().add(icon);

        // Windows title
        stage.setTitle("Stage demo w00t w00t");
        stage.setFullScreen(false);

        stage.setScene(currentScene);
        stage.show();

    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void sceneAskFirstPlayerInfo(String scene){
        this.currentScene = sceneMap.get(scene);
        this.stage.setScene(this.currentScene);
        this.stage.show();
    }

    public void sceneAskNickname(String scene, List<PlayerColour> colourList, int numPlayer){
        /*  Example of what should be done for every scene:
             - receiving the parameters from Message->GUIView
             - passing them to the scene controller in order to show personalized info
             - when showing a scene the controller SHOULD execute the initialize method first (try this, I'm not sure)

        GUIController currentController = controllerMap.get(scene);
        currentController.setColourList(colourList);
        currentController.setNumPlayers(numPlayer);
        */

        this.currentScene = sceneMap.get(scene);
        this.stage.setScene(this.currentScene);
        this.stage.show();
    }
}

